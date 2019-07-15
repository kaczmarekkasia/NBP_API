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
        Tables tableFromUser = loadTableFromUSer(scanner);

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
//        jeśli coś pójdzi enie tak, zwróci null
//        String apiContent = loadContentFromURL(requestURL);
//        System.out.println(apiContent);



        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ExchangeRatesSeries.class);

            //Marshaller zamienia obiekty na tekst
            //Unmarshaller - odwrotnie

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            ExchangeRatesSeries exchangeRatesSeries =(ExchangeRatesSeries) unmarshaller.unmarshal(new URL(requestURL));

            System.out.println(exchangeRatesSeries);

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

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
            while ((liniaZTekstuZReadera = bufferedReader.readLine())!= null){
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

    public static Tables loadTableFromUSer(Scanner scanner) {
        String table;
        Tables tableFromUserEnum = null;
        do {
            try {
                System.out.println("Podaj rodzaj tabeli " + Arrays.toString(Tables.values()));
                table = scanner.nextLine().toUpperCase();
                tableFromUserEnum = Tables.valueOf(table);
            } catch (IllegalArgumentException iae) {
                System.out.println("Niepoprawny rodzaj tabeli! Podaj go ponownie");
            }
        } while (tableFromUserEnum == null);

        return tableFromUserEnum;
    }

}

