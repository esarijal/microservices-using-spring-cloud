package com.mitrais.microservices.currencyexchangeservice.controller;

import com.mitrais.microservices.currencyexchangeservice.bean.ExchangeValue;
import com.mitrais.microservices.currencyexchangeservice.exception.NotFoundException;
import com.mitrais.microservices.currencyexchangeservice.repository.ExchangeValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    private Environment environment;

    private final ExchangeValueRepository exchangeValueRepository;

    public CurrencyExchangeController(ExchangeValueRepository exchangeValueRepository) {
        this.exchangeValueRepository = exchangeValueRepository;
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from,
                                               @PathVariable String to){
        ExchangeValue exchangeValue =
                exchangeValueRepository.findFirstByFromAndTo(from, to)
                        .orElseThrow(() -> new NotFoundException("Currency exchange from " + from +
                                            " " +"to " + to + " is not found"));
        exchangeValue.setPort(Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port"))));

        logger.info("{}", exchangeValue);
        return exchangeValue;
    }
}
