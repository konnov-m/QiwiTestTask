package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CentralBankApi {

    private static final String NAME = "Name";
    private static final String NUMCODE = "NumCode";
    private static final String CHARCODE = "CharCode";
    private static final String VALUE = "Value";
    private static final String URL = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=";

    private static Map<String, Integer> mapData = new HashMap<>();


    static {
        mapData.put(NAME, -1);
        mapData.put(NUMCODE, -1);
        mapData.put(CHARCODE, -1);
        mapData.put(VALUE, -1);
    }

    public static String parse(String date) {
        StringBuilder parsedDate = new StringBuilder();
        String[] arr = date.split("-");

        for (int i = 1; i < arr.length + 1; i++) {
            parsedDate.append(arr[arr.length - i]);
            parsedDate.append("/");
        }

        return parsedDate.toString();
    }

    public static String findByCode(String code, NodeList nodeList) {
        StringBuilder stringBuilder = new StringBuilder();
        initMap(nodeList.item(0));

        for (int i = 0; i < nodeList.getLength(); i++) {
            NodeList nodeValute = nodeList.item(i).getChildNodes();

            if (nodeValute.item(mapData.get(CHARCODE)).getTextContent().equals(code)) {
                stringBuilder.append(nodeValute.item(mapData.get(CHARCODE)).getTextContent());
                stringBuilder.append(" (");
                stringBuilder.append(nodeValute.item(mapData.get(NAME)).getTextContent());
                stringBuilder.append("): ");
                stringBuilder.append(nodeValute.item(mapData.get(VALUE)).getTextContent());
            }
        }

        return stringBuilder.toString();
    }

    public static void initMap(Node node) {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            String buff = node.getChildNodes().item(i).getNodeName();
            switch (buff) {
                case (NAME) -> mapData.put(NAME, i);
                case (NUMCODE) -> mapData.put(NUMCODE, i);
                case (CHARCODE) -> mapData.put(CHARCODE, i);
                case (VALUE) -> mapData.put(VALUE, i);
            }
        }
    }

    public static String getCurrencyRates(String code, String date) throws IOException, ParserConfigurationException, SAXException {
        URL obj = new URL(URL + parse(date));
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(con.getInputStream());

            NodeList currencies = document.getElementsByTagName("ValCurs");

            if (currencies.item(0).getChildNodes().getLength() < 1 ||
                    currencies.item(0).getTextContent().equals("\nError in parameters\n")) {
                throw new RuntimeException("Incorrect date");
            } else {
                String out = findByCode(code, currencies.item(0).getChildNodes());
                if (out.length() == 0) {
                    throw new RuntimeException("Incorrect code");
                } else {
                    return out;
                }
            }
        } else {
            throw new RuntimeException("GET request did not work.");
        }
    }

}
