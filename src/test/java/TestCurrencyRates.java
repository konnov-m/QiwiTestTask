import org.example.CentralBankApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TestCurrencyRates {

    @Test
    public void test1() {
        String[] args = {"currency_rates", "--code=USD", "--date=2022-10-08"};

        String code = args[1].split("=")[1];
        String date = args[2].split("=")[1];

        try {
            Assertions.assertEquals(CentralBankApi.getCurrencyRates(code, date), "USD (Доллар США): 61,2475");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test2() {
        String[] args = {"currency_rates", "--code=USD", "--date=2022-10-08"};

        String code = args[1].split("=")[1];
        String date = args[2].split("=")[1];

        try {
            Assertions.assertNotEquals(CentralBankApi.getCurrencyRates(code, date), "USD (Доллар США): 61,24");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test3() {
        String[] args = {"currency_rates", "--code=USD", "--date=2002-03-02"};

        String code = args[1].split("=")[1];
        String date = args[2].split("=")[1];

        try {
            Assertions.assertEquals(CentralBankApi.getCurrencyRates(code, date), "USD (Доллар США): 30,9436");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test4() {
        String[] args = {"currency_rates", "--code=EUR", "--date=2002-03-02"};

        String code = args[1].split("=")[1];
        String date = args[2].split("=")[1];

        try {
            Assertions.assertEquals(CentralBankApi.getCurrencyRates(code, date), "EUR (Евро): 26,8343");
        } catch (IOException | SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testIncorrectCode() {
        String[] args = {"currency_rates", "--code=eur", "--date=2002-03-02"};

        String code = args[1].split("=")[1];
        String date = args[2].split("=")[1];

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> CentralBankApi.getCurrencyRates(code, date));

        String expectedMessage = "Incorrect code";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testIncorrectDate() {
        String[] args = {"currency_rates", "--code=EUR", "--date=2025-03-02"};

        String code = args[1].split("=")[1];
        String date = args[2].split("=")[1];

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> CentralBankApi.getCurrencyRates(code, date));

        String expectedMessage = "Incorrect date";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

}
