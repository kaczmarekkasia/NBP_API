package nbp.zad.URL;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj walutę");
        String currency = scanner.nextLine();
        System.out.println("Podaj datę początkową");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String sDate = scanner.nextLine();
        try {
            LocalDateTime startDate = LocalDateTime.parse(sDate, dateTimeFormatter);
        }catch (DateTimeParseException dtpe){
            System.out.println("Zły format daty");
        }
        System.out.println("Podaj datę końcowąą");
        String eDate = scanner.nextLine();
        try {
            LocalDateTime endDate = LocalDateTime.parse(sDate, dateTimeFormatter);
        }catch (DateTimeParseException dtpe){
            System.out.println("Zły format daty");
        }


    }
}
