package com.nikita.exchangeratesc.dto.ecb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Represents the Cube element with time attribute
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ECBTimeCubeDto {
    @XmlAttribute(name = "time")
    private String time;

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private List<ECBCurrencyCubeDto> ECBCurrencyCubeDtos;
}