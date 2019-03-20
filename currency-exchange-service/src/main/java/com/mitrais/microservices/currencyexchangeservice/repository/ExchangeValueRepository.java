package com.mitrais.microservices.currencyexchangeservice.repository;

import com.mitrais.microservices.currencyexchangeservice.bean.ExchangeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long> {
    Optional<ExchangeValue> findFirstByFromAndTo(String from, String to);
}
