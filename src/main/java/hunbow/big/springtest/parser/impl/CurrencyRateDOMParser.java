package hunbow.big.springtest.parser.impl;

import hunbow.big.springtest.model.CurrencyRate;
import hunbow.big.springtest.parser.CurrencyRateParser;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CurrencyRateDOMParser implements CurrencyRateParser {

    @Override
    public List<CurrencyRate> parse(String ratesAsString) {
        List<CurrencyRate> currencyRates = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            try (StringReader reader = new StringReader(ratesAsString)) {

                Document document = db.parse(new InputSource(reader));
                document.getDocumentElement().normalize();
                NodeList nodeList = document.getElementsByTagName("Valute");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        CurrencyRate currencyRate = CurrencyRate.builder()
                                .numCode(element.getElementsByTagName("NumCode").item(0).getTextContent())
                                .charCode(element.getElementsByTagName("CharCode").item(0).getTextContent())
                                .nominal(element.getElementsByTagName("Nominal").item(0).getTextContent())
                                .name(element.getElementsByTagName("Name").item(0).getTextContent())
                                .value(element.getElementsByTagName("Value").item(0).getTextContent())
                                .vunitRate(element.getElementsByTagName("VunitRate").item(0).getTextContent())
                                .build();

                        currencyRates.add(currencyRate);

                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Xml parsing error, xml: " + ex);
            throw new RuntimeException();
        }

        return currencyRates;
    }
}
