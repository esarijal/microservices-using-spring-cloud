package com.mitrais.microservices.netflixzuulapigatewayserver.controller.payload;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@NoArgsConstructor
public class UserPayload {
    @NonNull
    @Size(min = 5, max = 30) @NotNull
    String username;
    @NonNull
    @NotNull
    String password;
    @NonNull
    @NotNull
    String email;
}
