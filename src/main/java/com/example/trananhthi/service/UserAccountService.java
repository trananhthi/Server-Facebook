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
public class UserAccountService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository,@Lazy PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserAccount> getUserByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    public Optional<UserAccount> getUserById(Long id)
    {
        return userAccountRepository.findById(id);
    }

    @Transactional
    public void updateStatusByEmail(String email,String status)
    {
        userAccountRepository.updateStatusByEmail(email,status);
    }

    public UserAccount signUpNewAccount(UserAccount userAccount)
    {
        String encodePassword = passwordEncoder.encode(userAccount.getPassword());
        userAccount.setPassword(encodePassword);
        userAccount.setRole("ROLE_USER");
        userAccount.setStatus("not_activated");
        userAccount.setTimeCreated(new Date());
        userAccount.setName(userAccount.getFirstName() + ' ' + userAccount.getLastName());
        userAccount.setAvatar("https://s3-hcm-r1.longvan.net/2502-facebook/default_avatar.png");
        return userAccountRepository.save(userAccount);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserAccount> userAccounts = userAccountRepository.findByEmail(email);
        String password;
        List<GrantedAuthority> authorities;
        if(userAccounts.isEmpty()) {
            throw new UsernameNotFoundException("Account does not exist with email = " + email);
        }
        email = userAccounts.get().getEmail();
        password = userAccounts.get().getPassword();
        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userAccounts.get().getRole()));

        return new User(email,password,authorities);
    }

    @Transactional
    public UserAccount updatePrivacyDefaultByEmail(String email,String privacyDefault)
    {
        Optional<UserAccount> userAccounts = userAccountRepository.findByEmail(email);
        List<String> validPrivacyValues = Arrays.asList("friend", "public", "custom","except_friend","specific_friend","only_me");
        if(userAccounts.isEmpty())
        {
            throw new UsernameNotFoundException("Account does not exist with email = " + email);
        }
        if(!validPrivacyValues.contains(privacyDefault) )
        {
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "PrivacyInvalid","Privacy không hợp lệ");
        }
        UserAccount userAccount =  userAccounts.get();
        userAccount.setPrivacyDefault(privacyDefault);
        return userAccountRepository.save(userAccount);
    }

    public List<UserAccount> searchUsersByName(String keyword)
    {
        return userAccountRepository.findByNameContaining(keyword);
    }

    public Map<Long, UserAccount> getUsersByIds(Set<Long> userIds) {
        List<UserAccount> users = userAccountRepository.findAllByIdIn(userIds);
        Map<Long, UserAccount> userMap = new HashMap<>();
        for (UserAccount user : users) {
            userMap.put(user.getId(), user);
        }
        return userMap;
    }
}

