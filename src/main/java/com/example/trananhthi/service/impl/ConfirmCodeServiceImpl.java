package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.entity.ConfirmCode;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.enumtype.Status;
import com.example.trananhthi.repository.ConfirmCodeRepository;
import com.example.trananhthi.repository.UserAccountRepository;
import com.example.trananhthi.service.ConfirmCodeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmCodeServiceImpl extends BaseServiceImpl<ConfirmCode, ConfirmCodeRepository> implements ConfirmCodeService {
    private final ConfirmCodeRepository confirmCodeRepository;
    private final UserAccountRepository userAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger("FacebookSever");

    @Override
    public Optional<ConfirmCode> findByCode(String code)
    {
        return confirmCodeRepository.findByCode(code);
    }

    @Override
    @Transactional
    public void deleteByCode(String code)
    {
        confirmCodeRepository.deleteByCode(code);
    }

    @Override
    @Transactional
    public void deleteAllByUserAccount(UserAccount userAccount)
    {
        confirmCodeRepository.deleteAllByUserAccount(userAccount);
    }
    @Override
    public ConfirmCode createConfirmCode(String email) {
        ConfirmCode confirmCode = new ConfirmCode();
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // Tạo mã ngẫu nhiên từ 100000 đến 999999
        confirmCode.setUserAccount(userAccount.orElse(null));
        confirmCode.setExpiryDate(Instant.now().plusMillis(1000*60*5));
        confirmCode.setCode(String.valueOf(code));
        confirmCode = confirmCodeRepository.save(confirmCode);
        return confirmCode;
    }

    @Scheduled( initialDelay = 1000*60*30,fixedRate = 1000*60*60*2)
    @Transactional
    protected void cleanupExpiredAccount() {

        deleteExpiredCodes();
        deleteNotConfirmedAccount();
        logger.info("Đã clean up hết các account chưa xác nhận trong database");

    }

    private void deleteExpiredCodes() {
        List<ConfirmCode> confirmCodeList = (List<ConfirmCode>) confirmCodeRepository.findAll();
        confirmCodeList.forEach(confirmCode -> {
            if (isExpiredCode(confirmCode))
            {
                confirmCodeRepository.delete(confirmCode);
            }
        });
    }

    @Transactional
    protected void deleteNotConfirmedAccount()
    {
        // Lấy thời gian hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Tạo một đối tượng LocalDateTime đại diện cho ngày cutoff (cắt ngày) - 2 ngày trước
        LocalDateTime cutoffDateTime = currentDateTime.minusDays(2);
        userAccountRepository.deleteUserAccountsByStatusAndCreatedAtBefore(Status.TEM,cutoffDateTime);
    }

    @Override
    public boolean isExpiredCode(ConfirmCode code)
    {
        return code.getExpiryDate().compareTo(Instant.now()) < 0;
    }
}
