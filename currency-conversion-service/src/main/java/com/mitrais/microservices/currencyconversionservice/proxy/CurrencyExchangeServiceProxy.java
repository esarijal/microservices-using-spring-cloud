package com.mitrais.microservices.currencyconversionservice.proxy;

import com.mitrais.microservices.currencyconversionservice.bean.CurrencyConversionBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "currency-exchange-service", url = "localhost:8000")
public interface CurrencyExchangeServiceProxy {
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyConversionBean findExchangeValue(@PathVariable("from") String from,
                                                 @PathVariable("to") String to);
}
