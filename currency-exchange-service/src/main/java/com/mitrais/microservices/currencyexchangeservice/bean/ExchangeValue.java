package com.mitrais.microservices.currencyexchangeservice.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class ExchangeValue {
    @Id
    private Long id;
    @Column(name = "currency_to")
    private String to;
    @Column(name = "currency_from")
    private String from;
    private BigDecimal conversionMultiple;
    private int port;

    public ExchangeValue() {
    }

    public ExchangeValue(Long id, String to, String from, BigDecimal conversionMultiple) {
        this.id = id;
        this.to = to;
        this.from = from;
        this.conversionMultiple = conversionMultiple;
    }

    public Long getId() {
        return id;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public BigDecimal getConversionMultiple() {
        return conversionMultiple;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
