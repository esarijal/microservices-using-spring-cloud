package com.mitrais.microservices.currencyconversionservice.controller;

import com.mitrais.microservices.currencyconversionservice.bean.CurrencyConversionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyConversionController {

    @Autowired
    private Environment environment;

    @GetMapping("currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal quantity){
        CurrencyConversionBean bean = new CurrencyConversionBean(
                1L, from, to, BigDecimal.valueOf(50), quantity,
                quantity.multiply(BigDecimal.valueOf(50)),
                Integer.parseInt(environment.getProperty("local.server.port"))
        );
        return bean;
    }
}
