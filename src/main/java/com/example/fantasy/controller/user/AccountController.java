package com.example.fantasy.controller.user;

import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.dto.TokenDTO;
import com.example.fantasy.dto.UserDTO;
import com.example.fantasy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    @PostMapping("/v1/account/signup")
    public void signup(@Valid @RequestBody NewUserDTO newUserDTO) {
        userService.createUser(newUserDTO);
    }

    @PostMapping("/v1/account/login")
    public TokenDTO login(@Valid @RequestBody UserDTO userDTO) {
        return null;
    }

}
