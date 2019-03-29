package com.mitrais.microservices.netflixzuulapigatewayserver.repository;

import com.mitrais.microservices.netflixzuulapigatewayserver.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
