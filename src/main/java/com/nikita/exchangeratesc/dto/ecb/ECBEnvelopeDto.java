package com.nikita.exchangeratesc.dto.ecb;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

// Root element for the XML
@XmlRootElement(name = "Envelope", namespace = "http://www.gesmes.org/xml/2002-08-01")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ECBEnvelopeDto {
    @XmlElement(name = "Cube", namespace = "http://www.ecb.int/vocabulary/2002-08-01/eurofxref")
    private ECBCubeDto cube;
}
