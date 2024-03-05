package com.example.trananhthi.controller;

import com.example.trananhthi.entity.UserAccount;
import com.example.trananhthi.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HomeController {
    private UserAccountService UserAccountService;
    @Autowired
    public HomeController(UserAccountService UserAccountService) {
        this.UserAccountService = UserAccountService;
    }

    @GetMapping("/id/{id}")
    public Optional<UserAccount> getUserByEmail(@PathVariable Long id) {
        return UserAccountService.getUserById(id);
    }
}
