package com.mitrais.microservices.currencyconversionservice.controller;

import com.mitrais.microservices.currencyconversionservice.bean.CurrencyConversionBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    @GetMapping("currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean convertCurrency(@PathVariable String from,
                                                  @PathVariable String to,
                                                  @PathVariable BigDecimal quantity){
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        CurrencyConversionBean bean = new RestTemplate()
                .getForObject("http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                        CurrencyConversionBean.class, uriVariables);

        bean.setQuantity(quantity);
        bean.setTotalCalculatedAmount(quantity.multiply(bean.getConversionMultiple()));
        return bean;
    }
}
