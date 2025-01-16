package com.example.trananhthi.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Service
public abstract class BaseServiceImpl<E extends BaseEntity, R extends CrudRepository<E,String>> implements BaseService{
    @Autowired
    protected Validator validator;
    @Autowired
    protected MessageSource messageResource;
    @Autowired
    protected LocaleResolver localeResolver;


    public String getMessageCode(String code, HttpServletRequest request) {
        Locale locale = this.localeResolver.resolveLocale(request);
        Locale.setDefault(locale);

        return this.messageResource.getMessage(code, (Object[])null, locale);
    }

    public BaseServiceImpl() {}
}
