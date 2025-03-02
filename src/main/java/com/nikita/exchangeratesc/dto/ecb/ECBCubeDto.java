package com.nikita.exchangeratesc.dto.ecb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;


// Represents the outer Cube element
@XmlAccessorType(XmlAccessType.FIELD)
public class ECBCubeDto {
    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private ECBTimeCubeDto timeCube;

    public ECBTimeCubeDto getTimeCube() {
        return timeCube;
    }

    public void setTimeCube(ECBTimeCubeDto timeCube) {
        this.timeCube = timeCube;
    }
}