package com.nikita.exchangeratesc.dto.ecb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;


import java.util.List;

// Represents the Cube element with time attribute
@XmlAccessorType(XmlAccessType.FIELD)

public class ECBTimeCubeDto {
    @XmlAttribute(name = "time")
    private String time;

    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private List<ECBCurrencyCubeDto> ECBCurrencyCubeDtos;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ECBCurrencyCubeDto> getECBCurrencyCubeDtos() {
        return ECBCurrencyCubeDtos;
    }

    public void setECBCurrencyCubeDtos(List<ECBCurrencyCubeDto> ECBCurrencyCubeDtos) {
        this.ECBCurrencyCubeDtos = ECBCurrencyCubeDtos;
    }
}