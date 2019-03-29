package com.mitrais.microservices.netflixzuulapigatewayserver.service;

import java.util.Map;

public interface ITokenService {
    String permanent(Map<String, String> attributes);
    String expiring(Map<String, String> attributes);
    Map<String, String> untrusted(String token);
    Map<String, String> verify(String token);
}
