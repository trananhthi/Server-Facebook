package com.example.trananhthi.controller;

import com.example.trananhthi.common.BaseController;
import com.example.trananhthi.dto.request.UpdatePrivacyDefaultDto;
import com.example.trananhthi.dto.UserAccountDto;
import com.example.trananhthi.service.UserAccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserAccountController extends BaseController {
    private final UserAccountService userAccountService;
    private static final String ROOT = "/user";

    @GetMapping(V1 + ROOT + "/infor")
    public ResponseEntity<UserAccountDto> getUserInfor(@RequestParam(name = "userId") String userId, HttpServletRequest request)
    {
        return new ResponseEntity<>(userAccountService.getUserInfor(userId, request), HttpStatus.OK);
    }

    @PatchMapping(V1 + ROOT + "/update/privacy-default")
    public  ResponseEntity<UserAccountDto> updatePrivacyDefaultByEmail(@RequestBody UpdatePrivacyDefaultDto dto, HttpServletRequest request)
    {
        return new ResponseEntity<>(userAccountService.updatePrivacyDefaultByEmail(dto.getPrivacyDefault(), request), HttpStatus.OK);
    }

    //TODO : viết lại api này theo search text
//    @GetMapping(V1 + ROOT + "/search")
//    public  ResponseEntity<List<UserAccountDto>> searchByName(@RequestParam String keyword)
//    {
//        List<UserAccount> userAccountList = userAccountService.searchUsersByName(keyword);
//        return ResponseEntity.ok(mapEntityToDTO.mapUserAccountListToDTOList(userAccountList));
//    }

}
