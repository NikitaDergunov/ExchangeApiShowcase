package com.nikita.exchangeratesc.dto.ecb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// Represents the innermost Cube elements with currency and rate
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ECBCurrencyCubeDto {
    @XmlAttribute(name = "currency")
    private String currency;

    @XmlAttribute(name = "rate")
    private BigDecimal rate;
}