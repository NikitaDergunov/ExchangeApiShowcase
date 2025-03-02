package com.nikita.exchangeratesc.dto.ecb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

import java.math.BigDecimal;

//Represents the innermost Cube elements with currency and rate
@XmlAccessorType(XmlAccessType.FIELD)
public class ECBCurrencyCubeDto {
    @XmlAttribute(name = "currency")
    private String currency;

    @XmlAttribute(name = "rate")
    private BigDecimal rate;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}