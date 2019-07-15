package nbp.zad.URL;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private final static String BASE_NBP_API_URL = "http://api.nbp.pl/api/exchangerates/rates/{table}/{code}/{startDate}/{endDate}/";
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

        //http://api.nbp.pl/api/exchangerates/rates/{table}/{code}/{startDate}/{endDate}/
        //zmiana podanego adresu URL na nowy, wg parametrów
        String requestURL = BASE_NBP_API_URL
                .replace("{table}", tableFromUser.toString())
                .replace("{code}", currencyCode.toString())
                .replace("{startDate}", dateStart.format(DATE_TIME_FORMATTER))
                .replace("{endDate}", dateEnd.format(DATE_TIME_FORMATTER));

        System.out.println("Your request url is: " + requestURL);
    }

    public static CurenncyCode loadCurrencyCodeFromUser(Scanner scanner) {
        CurenncyCode curenncyCodeEnum = null;
        String currency;

        do {
            try {
                System.out.println("Podaj kod waluty" + Arrays.toString(CurenncyCode.values()));
                currency = scanner.nextLine();
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
                table = scanner.nextLine();
                tableFromUserEnum = Tables.valueOf(table);
            } catch (IllegalArgumentException iae) {
                System.out.println("Niepoprawny rodzaj tabeli! Podaj go ponownie");
            }
        } while (tableFromUserEnum == null);

        return tableFromUserEnum;
    }

}

