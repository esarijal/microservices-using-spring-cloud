package com.mitrais.microservices.netflixzuulapigatewayserver.controller.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    @NonNull String token;
}
