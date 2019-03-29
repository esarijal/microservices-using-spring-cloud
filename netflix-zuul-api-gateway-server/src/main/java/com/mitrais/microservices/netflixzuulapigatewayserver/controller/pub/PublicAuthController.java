package com.mitrais.microservices.netflixzuulapigatewayserver.controller.pub;

import com.mitrais.microservices.netflixzuulapigatewayserver.controller.payload.AuthenticationRequest;
import com.mitrais.microservices.netflixzuulapigatewayserver.controller.payload.AuthenticationResponse;
import com.mitrais.microservices.netflixzuulapigatewayserver.model.User;
import com.mitrais.microservices.netflixzuulapigatewayserver.repository.UserRepository;
import com.mitrais.microservices.netflixzuulapigatewayserver.service.IUserAuthenticationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/public/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
final class PublicAuthController {
    @NonNull IUserAuthenticationService authenticationService;
    @NonNull UserRepository userRepository;

    @PostMapping("registration")
    ResponseEntity<?> register(@RequestBody @NotNull AuthenticationRequest request){
        final String username = request.getUsername();
        final String password = request.getPassword();
        try{
            userRepository.save(new User(username, BCrypt.hashpw(password, BCrypt.gensalt())));
        } catch (DataAccessException e){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error",
                    e.getMessage()));
        }
        return login(request);
    }

    @PostMapping("login")
    ResponseEntity<?> login(@RequestBody @NotNull AuthenticationRequest request){
        try{
            Optional<String> login = authenticationService.login(request.getUsername(),
                    request.getPassword());
            login.orElseThrow(() -> new UsernameNotFoundException("Login Failed"));
            return ResponseEntity.ok(new AuthenticationResponse(login.get()));

        } catch (UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}
