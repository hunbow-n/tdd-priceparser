package hunbow.big.springtest.controller;

import hunbow.big.springtest.model.CurrencyRate;
import hunbow.big.springtest.parser.CurrencyRateParser;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CurentcyRateCantroller {

    private static final String CBR_URL = "https://cbr.ru/scripts/XML_daily.asp";
    private final CurrencyRateParser currencyRateParser;

    @GetMapping("/currency-rate/{charCode}/{date}")
    public CurrencyRate getCurrencyRate(@PathVariable String charCode,
                                  @DateTimeFormat(pattern = "dd-MM-yyyy") @PathVariable LocalDate date) {

        String urlWithDate = String.format("%s?date_req=%s", CBR_URL, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date));
        String rate = getRatesAsXml(urlWithDate);
        List<CurrencyRate> currencyRates = currencyRateParser.parse(rate);
        return currencyRates.stream().filter(r -> r.getCharCode().equals(charCode))
                .findFirst()
                .orElse(null);
    }


    private String getRatesAsXml(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception ex) {
            throw new RuntimeException("Could't connect to CBR", ex);
        }
    }

//    private List<CurrencyRate> parse(String ratesAsString)  {
//
//        currencyRateParser.parse(ratesAsString);
//    }

}
