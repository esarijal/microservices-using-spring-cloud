package com.mitrais.microservices.netflixzuulapigatewayserver.repository;

import com.mitrais.microservices.netflixzuulapigatewayserver.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
