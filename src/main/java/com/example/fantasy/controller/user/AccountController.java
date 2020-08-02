package com.example.fantasy.controller.user;

import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.dto.TokenDTO;
import com.example.fantasy.dto.UserDTO;
import com.example.fantasy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("isAnonymous()")
public class AccountController {

    private final UserService userService;

    @PostMapping("/v1/user/account/signup")
    public void signup(@Valid @RequestBody NewUserDTO newUserDTO) {
        userService.createUser(newUserDTO);
    }

    @PostMapping("/v1/user/account/login")
    public TokenDTO login(@Valid @RequestBody UserDTO userDTO) {
        return userService.signIn(userDTO);
    }

}
