package com.nikita.exchangeratesc.dto.ecb;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Getter;
import lombok.Setter;

// Represents the outer Cube element
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ECBCubeDto {
    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private ECBTimeCubeDto timeCube;
}