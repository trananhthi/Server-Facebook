package com.example.trananhthi.service;

import com.example.trananhthi.dto.UserAccountDto;
import com.example.trananhthi.entity.UserAccount;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface UserAccountService {
    UserAccountDto getUserInfor(String userId, HttpServletRequest request);

    UserDetails loadUserByUsername(String email);

    @Transactional
    UserAccountDto updatePrivacyDefaultByEmail(String privacyDefault, HttpServletRequest request);

    List<UserAccount> searchUsersByName(String keyword);

}

