package com.example.trananhthi.common;

import com.example.trananhthi.util.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.LocaleResolver;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public abstract class BaseServiceImpl<E extends BaseEntity, R extends CrudRepository<E,String>> implements BaseService{
    @Autowired
    private Validator validator;
    @Autowired
    private MessageSource messageResource;
    @Autowired
    private LocaleResolver localeResolver;
    @Autowired
    private Environment env;
    @Autowired
    private S3Client s3Client;
    @Autowired
    private S3AsyncClient s3AsyncClient;

    private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    public String getMessageCode(String code, HttpServletRequest request) {
        Locale locale = this.localeResolver.resolveLocale(request);
        Locale.setDefault(locale);

        return this.messageResource.getMessage(code, null, locale);
    }

    public String getMessageCode(String code, HttpServletRequest request, Object... args) {
        Locale locale = this.localeResolver.resolveLocale(request);
        Locale.setDefault(locale);

        return this.messageResource.getMessage(code, args, locale);
    }

    public <T> Object getFieldValue(String fieldName, T object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Error accessing field '{}' on object of type '{}': {}",
                    fieldName, object.getClass().getName(), e.getMessage(), e);
            return null;
        }
    }

    public <T> List<T> sorting(List<T> list, List<Sort.Order> orders) {
        return list.stream().sorted((o1, o2) -> {
            CompareToBuilder compareToBuild = new CompareToBuilder();
            orders.forEach((order) -> Arrays.stream(list.get(0).getClass().getDeclaredFields()).filter((field) -> field.getName().equals(order.getProperty())).findAny().ifPresent((field) -> {
                Object fv1 = this.getFieldValue(order.getProperty(), o1);
                Object fv2 = this.getFieldValue(order.getProperty(), o2);
                if (order.getDirection().equals(Sort.Direction.ASC)) {
                    compareToBuild.append(fv1, fv2);
                } else {
                    compareToBuild.append(fv2, fv1);
                }

            }));
            return compareToBuild.toComparison();
        }).collect(Collectors.toList());
    }

    public <T> Page<T> createPageFromList(List<T> data, Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            List<Sort.Order> orders = pageable.getSort().toList();
            data = this.<T>sorting(data, orders);
        }

        if (pageable.isPaged()) {
            int start = (int)pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), data.size());
            return start > end ? new PageImpl(data.subList(0, 0), pageable, (long)data.size()) : new PageImpl(data.subList(start, end), pageable, (long)data.size());
        } else {
            return pageable.isUnpaged() ? new PageImpl(data, Pageable.unpaged(), (long)data.size()) : new PageImpl(data, Pageable.unpaged(), (long)data.size());
        }
    }

    @SneakyThrows
    public String uploadFileToS3(String bucketName, String folder, MultipartFile file) {
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalArgumentException("Bucket name must not be null or empty");
        }
        if (folder == null || folder.isEmpty()) {
            throw new IllegalArgumentException("Folder name must not be null or empty");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null");
        }

        String fileName = folder + "/" + Utils.generateFileName(file);
        String fileUrl = Objects.requireNonNull(env.getProperty("aws.s3.endpoint")) + "/" + bucketName + "/" + fileName;

        try {
            // Cấu hình request cho S3 v2
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // Upload file lên S3
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

        } catch (S3Exception | IOException e) {
            throw new IllegalStateException("Failed to upload the file to S3", e);
        }

        return fileUrl;
    }

    public CompletableFuture<String> uploadFileToS3Async(String bucketName, String folder, MultipartFile file) {
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalArgumentException("Bucket name must not be null or empty");
        }
        if (folder == null || folder.isEmpty()) {
            throw new IllegalArgumentException("Folder name must not be null or empty");
        }
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File must not be null");
        }

        String fileName = folder + "/" + Utils.generateFileName(file);
        String fileUrl = Objects.requireNonNull(env.getProperty("aws.s3.endpoint")) + "/" + bucketName + "/" + fileName;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            // Upload file bất đồng bộ
            return s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromBytes(file.getBytes()))
                    .thenApply(response -> fileUrl)
                    .exceptionally(e -> {
                        throw new IllegalStateException("Failed to upload the file to S3", e);
                    });

        } catch (Exception e) {
            throw new IllegalStateException("Error preparing file upload", e);
        }
    }

    public BaseServiceImpl() {}
}
