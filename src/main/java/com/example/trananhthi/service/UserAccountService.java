package com.example.trananhthi.service;

import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface UserAccountService {
    Optional<UserAccount> getUserByEmail(String email);

    Optional<UserAccount> getUserById(String id);

    @Transactional
    void updateStatusByEmail(String email,String status);

    UserAccount signUpNewAccount(UserAccount userAccount);

    UserDetails loadUserByUsername(String email);

    @Transactional
    UserAccount updatePrivacyDefaultByEmail(String email,String privacyDefault);

    List<UserAccount> searchUsersByName(String keyword);

    Map<String, UserAccount> getUsersByIds(Set<String> userIds);
}

