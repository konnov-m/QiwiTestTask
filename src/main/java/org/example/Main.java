package org.example;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

    public static String code;

    public static String date;

    // Program arguments: currency_rates --code=USD --date=2020-10-08
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Need program");
        } else {
            if (args[0].equals("currency_rates")) {

                if (args.length < 3) {
                    System.out.println("Need code and date");
                    return;
                }

                // Note that the input data may be in different order
                code = args[1].split("=")[0].equals("--code") ? args[1].split("=")[1] : args[2].split("=")[1];
                date = args[2].split("=")[0].equals("--date") ? args[2].split("=")[1] : args[1].split("=")[1];

                try {
                    String currencyRates = CentralBankApi.getCurrencyRates(code, date);

                    System.out.println(currencyRates);
                } catch (IOException | ParserConfigurationException | SAXException e) {
                    throw new RuntimeException(e);
                }

            } else {
                System.out.println("No such program");
            }
        }

    }
}