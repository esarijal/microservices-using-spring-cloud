package com.mitrais.microservices.currencyconversionservice.controller;

import com.mitrais.microservices.currencyconversionservice.bean.CurrencyConversionBean;
import com.mitrais.microservices.currencyconversionservice.exception.NotFoundException;
import com.mitrais.microservices.currencyconversionservice.proxy.CurrencyExchangeServiceProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeServiceProxy proxy;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @GetMapping("currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal quantity) {

        // Feign - Problem 1, invoking other services
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        try {
            ResponseEntity<CurrencyConversionBean> entity = new RestTemplate()
                    .getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                            CurrencyConversionBean.class, uriVariables);

            CurrencyConversionBean bean = entity.getBody();
            assert bean != null;
            bean.setQuantity(quantity);
            bean.setTotalCalculatedAmount(quantity.multiply(bean.getConversionMultiple()));
            return bean;


        } catch (HttpClientErrorException e) {
            throw new NotFoundException("Cannot found conversion rate between " + from + " and " + to);
        }
    }

    @GetMapping("currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    @HystrixCommand(fallbackMethod = "fallbackGetConversionRate", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from,
                                                       @PathVariable String to,
                                                       @PathVariable BigDecimal quantity) {

        try {
            CurrencyConversionBean bean = proxy.findExchangeValue(from, to);
            bean.setQuantity(quantity);
            bean.setTotalCalculatedAmount(quantity.multiply(bean.getConversionMultiple()));

            logger.info("{}", bean);
            return bean;
        } catch (FeignException e) {
            throw new NotFoundException("Cannot found conversion rate between " + from + " and " + to);
        }
    }

    public CurrencyConversionBean fallbackGetConversionRate(String from,
                                                       String to,
                                                       BigDecimal quantity){
        return new CurrencyConversionBean(0L, from, to, BigDecimal.ZERO, quantity,
                BigDecimal.ZERO, 0);

    }
}
