package com.mitrais.microservices.netflixzuulapigatewayserver.service;


import com.mitrais.microservices.netflixzuulapigatewayserver.model.User;

import java.util.Optional;

public interface IUserAuthenticationService {
    Optional<String> login(final String username, final String password);
    Optional<User> findByToken(final String token);
    void logout(User user);
}
