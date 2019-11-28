package nbp.zad.URL;

import com.sun.jmx.remote.internal.Unmarshal;
import nbp.zad.URL.model.ExchangeRatesSeries;

import javax.xml.bind.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Scanner;

public class Main {

    private final static String BASE_NBP_API_URL = "http://api.nbp.pl/api/exchangerates/rates/{table}/{code}/{startDate}/{endDate}/?format={dataFormat}";
    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) throws WrongDateException {

        Scanner scanner = new Scanner(System.in);


        System.out.println("Witaj w konsolowej aplikacji do pobierania kursów walut z API NBP.");

        //ustalenie waluty
        CurenncyCode currencyCode = loadCurrencyCodeFromUser(scanner);
        //ustalenie daty początkowej
        LocalDate dateStart;
        //ustalenie daty końcowej
        LocalDate dateEnd;
        //sptrawdzenie warunku aby data początkowa była przed datą końcową
        do {
            dateStart = loadDateFromUser(scanner);
            dateEnd = loadDateFromUser(scanner);

            if (dateEnd.isBefore(dateStart)) {
                throw new WrongDateException();
            }
        } while (dateStart.isAfter(dateEnd));
        //ustalenie rodzaju tabeli
        String tableFromUser = loadTableFromUSer(scanner);

        DataFormat dataFormat = loadDataFormatFromUser(scanner);

        //http://api.nbp.pl/api/exchangerates/rates/{table}/{code}/{startDate}/{endDate}/
        //zmiana podanego adresu URL na nowy, wg parametrów
        String requestURL = BASE_NBP_API_URL
                .replace("{table}", tableFromUser.toString())
                .replace("{code}", currencyCode.toString())
                .replace("{startDate}", dateStart.format(DATE_TIME_FORMATTER))
                .replace("{endDate}", dateEnd.format(DATE_TIME_FORMATTER))
                .replace("{dataFormat}", dataFormat.toString());

        System.out.println("Twój żądany adres URL to: " + requestURL);

//        metoda lodContentFromURL ładuje zawartość strony do stringa i zwraca go w wyniku
//        jeśli coś pójdzie nie tak, zwróci null
//        String apiContent = loadContentFromURL(requestURL);
//        System.out.println(apiContent);


        przetwarzanieXML(scanner, tableFromUser, requestURL);

    }

    private static void przetwarzanieXML(Scanner scanner, String tableFromUser, String requestURL) {
        ExchangeRatesSeries exchangeRatesSeries = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeRatesSeries.class);

            //Marshaller zamienia obiekty na tekst
            //Unmarshaller - odwrotnie

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            exchangeRatesSeries = (ExchangeRatesSeries) unmarshaller.unmarshal(new URL(requestURL));

            String command = loadTypeOfCalculationFromUser(scanner, tableFromUser);

            switch (command) {
                case "A":
                    System.out.println("Średni kurs waluty to:");
                    System.out.println(calculateMediumCurrencyRateMid(exchangeRatesSeries));
                    break;
                case "B":
                    System.out.println("Odchylenia maksymalne średniego kursu to:");
                    differenceBetweenMaxAndMinMid(exchangeRatesSeries);
                    break;
                case "C":
                    System.out.println("Maksymalna wartość kursu to:" + returnMaxMid(exchangeRatesSeries));
                    System.out.println("Minimalna wartość kursu to:" + returnMinMid(exchangeRatesSeries));
                    break;
                case "D":
                    System.out.println("Średnia cena sprzedaży to:");
                    System.out.println(calculateMediumCurrencyRateBid(exchangeRatesSeries));
                    break;
                case "E":
                    System.out.println("Odchylenia maksymalne cen sprzedaży to:");
                    differenceBetweenMaxAndMinBid(exchangeRatesSeries);
                    break;
                case "F":
                    System.out.println("Maksymalna wartość sorzedaży to:" + returnMaxBid(exchangeRatesSeries));
                    System.out.println("Minimalna wartość sprzedaży to:" + returnMinBid(exchangeRatesSeries));
                    break;
                case "G":
                    System.out.println("Średnia cena zakupy to:");
                    System.out.println(calculateMediumCurrencyRateAsk(exchangeRatesSeries));
                    break;
                case "H":
                    System.out.println("Odchylenia maksymalne cen zakupu to:");
                    differenceBetweenMaxAndMinAsk(exchangeRatesSeries);
                    break;
                case "I":
                    System.out.println("Maksymalna wartość zakupu to:" + returnMaxAsk(exchangeRatesSeries));
                    System.out.println("Minimalna wartość zakupu to:" + returnMinAsk(exchangeRatesSeries));
                    break;
            }
        } catch (UnmarshalException e) {
            System.out.println("Brak danych z podanego okresu");
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    private static String loadTypeOfCalculationFromUser(Scanner scanner, String tableFromUser) {
        String command = null;

        do {

            System.out.println("Podaj co chcesz obliczyć");
            if (tableFromUser.equalsIgnoreCase("a")) {
                System.out.println("a - Kurs średni z danego okresu");
                System.out.println("b - Odchylenia maksymalne z danego okresu");
                System.out.println("c - Maksymalny i minimalny kurs średni z danego okresu");
            }
            if (tableFromUser.equalsIgnoreCase("c")) {
                System.out.println("d - Średią wartość sprzedaży z danego okresu");
                System.out.println("e - Odchylenia maksymalne dla ceny ceny sprzeadży z danego okresu");
                System.out.println("f - Maksymalna i minimalna cena sprzedaży z danego okresu");
                System.out.println("g - Średią wartość zakupu z danego okresu");
                System.out.println("h - Odchylenia maksymalne dla ceny zakupu z danego okresu");
                System.out.println("i - Maksymalna i minimalna cena zakupu z danego okresu");
            }

            command = scanner.nextLine().toUpperCase();


            if (!command.equalsIgnoreCase("a") && !command.equalsIgnoreCase("b") && !command.equalsIgnoreCase("c") &&
                    !command.equalsIgnoreCase("d") && !command.equalsIgnoreCase("e") && !command.equalsIgnoreCase("f") &&
                    !command.equalsIgnoreCase("g") && !command.equalsIgnoreCase("h") && !command.equalsIgnoreCase("i")) {
                command = null;
                System.err.println("Niepoprawny rodzaj obliczeń. Wpisz ponownie co chcesz obliczyć.");
            }
        } while (command == null);
        return command;
    }

    //dla Ask
    private static double returnMaxAsk(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getAsk())
                .max().getAsDouble();

    }

    private static double returnMinAsk(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getAsk())
                .min().getAsDouble();

    }

    private static double calculateMediumCurrencyRateAsk(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getAsk())
                .sum() / exchangeRatesSeries.getRates().size();

    }

    private static void differenceBetweenMaxAndMinAsk(ExchangeRatesSeries exchangeRatesSeries) {
        System.out.println(returnMaxAsk(exchangeRatesSeries) - returnMinAsk(exchangeRatesSeries));

    }

    //obliczenia dla Bid
    private static double returnMaxBid(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getBid())
                .max().getAsDouble();

    }

    private static double returnMinBid(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getBid())
                .min().getAsDouble();

    }


    private static double calculateMediumCurrencyRateBid(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getBid())
                .sum() / exchangeRatesSeries.getRates().size();

    }

    private static void differenceBetweenMaxAndMinBid(ExchangeRatesSeries exchangeRatesSeries) {
        System.out.println(returnMaxBid(exchangeRatesSeries) - returnMinBid(exchangeRatesSeries));
    }

    //obliczenia dla Mid
    private static double returnMaxMid(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getMid())
                .max().getAsDouble();

    }

    private static double returnMinMid(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getMid())
                .min().getAsDouble();

    }


    private static double calculateMediumCurrencyRateMid(ExchangeRatesSeries exchangeRatesSeries) {
        return exchangeRatesSeries.getRates().stream()
                .mapToDouble(r -> r.getMid())
                .sum() / exchangeRatesSeries.getRates().size();

    }

    private static void differenceBetweenMaxAndMinMid(ExchangeRatesSeries exchangeRatesSeries) {
        System.out.println(returnMaxMid(exchangeRatesSeries) - returnMinMid(exchangeRatesSeries));
    }

    //pozostałe metody

    private static String loadContentFromURL(String requestURL) {
        String apiContent = null;

        try {
            //tworzymy Uniform Resource Locator
            URL url = new URL(requestURL);

            // input (dla naszej aplikacji) to wejsćie, czyli do naszej aplikacji będzie ładowany zasób z zewnątrz
            InputStream inputStream = url.openStream();
            //buffered reader pozwala czytać zasób, ale wymaga klasy pośredniczącej( np:
            //plik = FileReader,
            //stream = InputStreamReader,
            //posiada metodę "readline()' - zwraca jedną linię, lub jeśli nie ma treści zwraca null
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            String liniaZTekstuZReadera;


            //dopóki jest jakaś linia tekstu do przeczytania, przeczytaj ją i przypisz do "liniaTekstuZReadera"
            while ((liniaZTekstuZReadera = bufferedReader.readLine()) != null) {
                builder.append(liniaZTekstuZReadera);
            }
            //pamiętajmy o zmykaniu otwartych zasobów
            bufferedReader.close();

            apiContent = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return apiContent;
    }

    private static DataFormat loadDataFormatFromUser(Scanner scanner) {
        DataFormat dataFormatEnum = null;
        String dataFormat;

        do {
            try {
                System.out.println("Podaj format danych" + Arrays.toString(DataFormat.values()));
                dataFormat = scanner.nextLine().toUpperCase();
                dataFormatEnum = DataFormat.valueOf(dataFormat);
            } catch (IllegalArgumentException iae) {
                System.err.println("Niepoprawny kod danych! podaj go ponownie!");
            }
        } while (dataFormatEnum == null);

        return dataFormatEnum;
    }

    public static CurenncyCode loadCurrencyCodeFromUser(Scanner scanner) {
        CurenncyCode curenncyCodeEnum = null;
        String currency;

        do {
            try {
                System.out.println("Podaj kod waluty" + Arrays.toString(CurenncyCode.values()));
                currency = scanner.nextLine().toUpperCase();
                curenncyCodeEnum = CurenncyCode.valueOf(currency);
            } catch (IllegalArgumentException iae) {
                System.err.println("Niepoprawny kurs waluty! podaj go ponownie!");
            }
        } while (curenncyCodeEnum == null);

        return curenncyCodeEnum;
    }


    public static LocalDate loadDateFromUser(Scanner scanner) {
        LocalDate dateFromUser = null;
        try {
            System.out.println("Podaj datę w formacie yyyy-MM-dd");

            String date = scanner.nextLine();

            dateFromUser = LocalDate.parse(date, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException dtpe) {
            System.out.println("Zły format daty");
        }
        return dateFromUser;

    }

    public static String loadTableFromUSer(Scanner scanner) {
        String table;
        do {
            System.out.println("Podaj typ tabeli: ASK/BID = C, MID - A/B");
            table = scanner.nextLine();

            if (!table.equalsIgnoreCase("C") && !table.equalsIgnoreCase("A") && !table.equalsIgnoreCase("B")) {
                table = null;
                System.err.println("Niepoprawny typ tabeli. Wpisz ponownie typ tabeli.");
            }
        } while (table == null);
        return table;
    }

}

