package nbp.zad.URL.model;

import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ToString

@XmlRootElement(name = "Rate")
public class Rate{
    @XmlElement(name = "No")
    private String No;

    @XmlElement(name = "EffectiveDate")
    private String EffectiveDate;

    @XmlElement(name = "Bid")
    private Double Bid;

    @XmlElement(name = "Ask")
    private Double Ask;
}
