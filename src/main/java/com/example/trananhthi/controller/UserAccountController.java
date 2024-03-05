package com.example.trananhthi.controller;

import com.example.trananhthi.common.MapEntityToDTO;
import com.example.trananhthi.dto.UpdatePrivacyDefaultDTO;
import com.example.trananhthi.dto.UserAccountDTO;
import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.service.JwtService;
import com.example.trananhthi.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final JwtService jwtService;
    private final MapEntityToDTO mapEntityToDTO = MapEntityToDTO.getInstance();

    @GetMapping("/infor")
    public ResponseEntity<UserAccountDTO> getUserInfor(@RequestHeader(name = "Authorization") String token)
    {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                String email = jwtService.extractUsername(jwtToken);
                List<UserAccount> userAccount = userAccountService.getUserByEmail(email);
//                MapEntityToDTO mapEntityToDTO = MapEntityToDTO.getInstance();
                return ResponseEntity.status(HttpStatus.OK).body(mapEntityToDTO.mapUserAccountToDTO(userAccount.get(0)));
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
            return ResponseEntity.ok().body(mapEntityToDTO.mapUserAccountToDTO(userAccount));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @GetMapping("/search")
    public  ResponseEntity<List<UserAccountDTO>> searchByName(@RequestParam String keyword)
    {
        List<UserAccount> userAccountList = userAccountService.searchUsersByName(keyword);
        return ResponseEntity.ok(mapEntityToDTO.mapUserAccountListToDTOList(userAccountList));
    }

}
