package nbp.zad.URL.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

//POJO - Plain Old Java Object
@NoArgsConstructor
@Getter
@Setter


@XmlRootElement(name = "ExchangeRatesSeries")

public class ExchangeRatesSeries {
    @XmlElement(name = "Table")
    private String Table;

    @XmlElement(name = "Currency")
    private String Currency;

    @XmlElement(name = "Code")
    private String Code;

    @XmlElementWrapper(name = "Rates")
    @XmlElement(name = "Rate")
    private List<Rate> Rates;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("ExchangeRatesSeries{");
        stringBuilder.append("Table='" + Table);
        stringBuilder.append(", Currency='" + Currency);
        stringBuilder.append(", Code='" + Code);
        stringBuilder.append(", Rates=\n");
        for (Rate rate : Rates) {
            stringBuilder.append(rate.toString());
        }

        stringBuilder.append("\n}");

        return stringBuilder.toString();
    }
}
