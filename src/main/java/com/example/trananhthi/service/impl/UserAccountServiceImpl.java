package com.example.trananhthi.service.impl;

import com.example.trananhthi.common.BaseServiceImpl;
import com.example.trananhthi.context.UserContext;
import com.example.trananhthi.dto.UserAccountDto;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.enumtype.Privacy;
import com.example.trananhthi.exception.CustomException;
import com.example.trananhthi.mapstruct.UserAccountMapper;
import com.example.trananhthi.message.MessageCodes;
import com.example.trananhthi.repository.UserAccountRepository;
import com.example.trananhthi.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccount,UserAccountRepository> implements UserAccountService, UserDetailsService {
    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper userAccountMapper;

    @Override
    public UserAccountDto getUserInfor(String userId, HttpServletRequest request) {
        Optional<UserAccount> userAccount = userAccountRepository.findById(userId);
        if(userAccount.isEmpty())
        {
            String message = getMessageCode(MessageCodes.USER_NOTFOUND,request, userId);
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.USER_NOTFOUND, message);
        }
        return userAccountMapper.toDto(userAccount.get());
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
    @Override
    public UserAccountDto updatePrivacyDefaultByEmail(String privacyDefault, HttpServletRequest request)
    {
        String userId = UserContext.getUserId();
        Optional<UserAccount> userAccounts = userAccountRepository.findById(userId);
        if(userAccounts.isEmpty())
        {
            String message = getMessageCode(MessageCodes.USER_NOTFOUND,request, userId);
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.USER_NOTFOUND, message);
        }
        try
        {
            UserAccount userAccount =  userAccounts.get();
            userAccount.setPrivacyDefault(Enum.valueOf(Privacy.class,privacyDefault));
            return userAccountMapper.toDto(userAccountRepository.save(userAccount));
        }
        catch (Exception e)
        {
            String message = getMessageCode(MessageCodes.PRIVACY_NOTVALID,request);
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), MessageCodes.PRIVACY_NOTVALID,message);
        }
    }

    @Override
    public List<UserAccount> searchUsersByName(String keyword)
    {
        return userAccountRepository.findByNameContaining(keyword);
    }
}
