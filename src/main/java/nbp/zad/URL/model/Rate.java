package nbp.zad.URL.model;

import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@ToString
@Getter

@XmlRootElement(name = "Rate")
public class Rate {
    @XmlElement(name = "No")
    private String No;

    @XmlElement(name = "EffectiveDate")
    private String EffectiveDate;

    @XmlElement(name = "Bid")
    private Double Bid;

    @XmlElement(name = "Ask")
    private Double Ask;

    @XmlElement(name = "Mid")
    private Double Mid;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Rate{");
        builder.append("No='" + No);
        builder.append(", EffectiveDate='" + EffectiveDate);
        if (Bid != null) {
            builder.append(", Bid=" + Bid);
        }
        if (Ask != null) {
            builder.append(", Ask=" + Ask);
        }
        if (Mid != null) {
            builder.append(", Mid=" + Mid);
        }
        builder.append("\n");

        return builder.toString();
    }
}
