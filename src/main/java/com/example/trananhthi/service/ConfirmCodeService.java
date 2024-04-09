package com.example.trananhthi.service;

import com.example.trananhthi.entity.ConfirmCode;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.repository.ConfirmCodeRepository;
import com.example.trananhthi.repository.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConfirmCodeService {
    private final ConfirmCodeRepository confirmCodeRepository;
    private final UserAccountRepository userAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger("FacebookSever");

    @Autowired
    public ConfirmCodeService(ConfirmCodeRepository confirmCodeRepository, UserAccountRepository userAccountRepository) {
        this.confirmCodeRepository = confirmCodeRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public Optional<ConfirmCode> findByCode(String code)
    {
        return confirmCodeRepository.findByCode(code);
    }

    @Transactional
    public void deleteByCode(String code)
    {
        confirmCodeRepository.deleteByCode(code);
    }

    @Transactional
    public void deleteAllByUserAccount(UserAccount userAccount)
    {
        confirmCodeRepository.deleteAllByUserAccount(userAccount);
    }
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
    public void cleanupExpiredAccount() {

        deleteExpiredCodes();
        deleteNotConfirmedAccount();
        logger.info("Đã clean up hết các account chưa xác nhận trong database");

    }

    private void deleteExpiredCodes() {
        List<ConfirmCode> confirmCodeList = confirmCodeRepository.findAll();
        confirmCodeList.forEach(confirmCode -> {
            if (isExpiredCode(confirmCode))
            {
                confirmCodeRepository.delete(confirmCode);
            }
        });
    }

    @Transactional
    public void deleteNotConfirmedAccount()
    {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        Date cutoffDate = calendar.getTime();
        userAccountRepository.deleteUserAccountsByStatusAndTimeCreatedBefore("not_activated",cutoffDate);
    }

    public boolean isExpiredCode(ConfirmCode code)
    {
        return code.getExpiryDate().compareTo(Instant.now()) < 0;
    }

}
