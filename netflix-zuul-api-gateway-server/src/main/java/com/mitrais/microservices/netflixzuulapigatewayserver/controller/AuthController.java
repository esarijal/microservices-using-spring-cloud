package com.mitrais.microservices.netflixzuulapigatewayserver.controller;

import com.mitrais.microservices.netflixzuulapigatewayserver.model.User;
import com.mitrais.microservices.netflixzuulapigatewayserver.service.IUserAuthenticationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class AuthController {
    @NonNull IUserAuthenticationService authenticationService;

    @GetMapping("logout")
    boolean logout(@AuthenticationPrincipal final User user){
        authenticationService.logout(user);
        return true;
    }
}
