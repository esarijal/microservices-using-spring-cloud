package com.mitrais.microservices.netflixzuulapigatewayserver.service;

import com.mitrais.microservices.netflixzuulapigatewayserver.model.User;
import com.mitrais.microservices.netflixzuulapigatewayserver.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@Primary
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class TokenAuthenticationService implements IUserAuthenticationService {
    @NonNull UserRepository userRepository;
    @NonNull ITokenService tokenService;

    public TokenAuthenticationService(@NonNull UserRepository userRepository, @NonNull ITokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<String> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> BCrypt.checkpw(password, user.getPassword()))
                .map(user -> tokenService.expiring(Collections.singletonMap("username", username)));
    }

    @Override
    public Optional<User> findByToken(String token) {
        return Optional.of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userRepository::findByUsername);
    }

    @Override
    public void logout(User user) {
        // NOTHING TO DO
    }
}
