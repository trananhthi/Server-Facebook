package com.example.trananhthi.controller;

import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.dto.UpdatePrivacyDefaultDTO;
import com.example.trananhthi.dto.UserAccountDTO;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.service.JwtService;
import com.example.trananhthi.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/user")
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final JwtService jwtService;

    @Autowired
    public UserAccountController(UserAccountService userAccountService, JwtService jwtService) {
        this.userAccountService = userAccountService;
        this.jwtService = jwtService;
    }

    @GetMapping("/infor")
    public ResponseEntity<UserAccountDTO> getUserInfor(@RequestHeader(name = "Authorization") String token)
    {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                String email = jwtService.extractUsername(jwtToken);
                List<UserAccount> userAccount = userAccountService.getUserByEmail(email);
                return ResponseEntity.status(HttpStatus.OK).body(MapEntityToDTO.mapUserAccountToDTO(userAccount.get(0)));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping("/update/privacy-default")
    public  ResponseEntity<UserAccountDTO> updatePrivacyDefaultByEmail(@RequestHeader(name = "Authorization") String token,@RequestBody UpdatePrivacyDefaultDTO dto)
    {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String email = jwtService.extractUsername(jwtToken);
            UserAccount userAccount = userAccountService.updatePrivacyDefaultByEmail(email,dto.getPrivacyDefault());
            return ResponseEntity.ok().body(MapEntityToDTO.mapUserAccountToDTO(userAccount));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
