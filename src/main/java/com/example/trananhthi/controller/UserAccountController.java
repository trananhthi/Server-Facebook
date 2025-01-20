package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.dto.request.UpdatePrivacyDefaultDto;
import com.example.trananhthi.dto.UserAccountDto;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.service.JwtService;
import com.example.trananhthi.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class UserAccountController extends BaseController {
    private final UserAccountService userAccountService;
    private final JwtService jwtService;
    private final MapEntityToDTO mapEntityToDTO = MapEntityToDTO.getInstance();
    private static final String ROOT = "/user";

    @GetMapping(V1 + ROOT + "/infor")
    public ResponseEntity<UserAccountDto> getUserInfor(@RequestHeader(name = "Authorization") String token)
    {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                String email = jwtService.extractUsername(jwtToken);
                Optional<UserAccount> userAccount = userAccountService.getUserByEmail(email);
                return ResponseEntity.status(HttpStatus.OK).body(mapEntityToDTO.mapUserAccountToDTO(userAccount.orElse(null)));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PatchMapping(V1 + ROOT + "/update/privacy-default")
    public  ResponseEntity<UserAccountDto> updatePrivacyDefaultByEmail(@RequestHeader(name = "Authorization") String token, @RequestBody UpdatePrivacyDefaultDto dto)
    {
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            String email = jwtService.extractUsername(jwtToken);
            UserAccount userAccount = userAccountService.updatePrivacyDefaultByEmail(email,dto.getPrivacyDefault());
            return ResponseEntity.ok().body(mapEntityToDTO.mapUserAccountToDTO(userAccount));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping(V1 + ROOT + "/search")
    public  ResponseEntity<List<UserAccountDto>> searchByName(@RequestParam String keyword)
    {
        List<UserAccount> userAccountList = userAccountService.searchUsersByName(keyword);
        return ResponseEntity.ok(mapEntityToDTO.mapUserAccountListToDTOList(userAccountList));
    }

}
