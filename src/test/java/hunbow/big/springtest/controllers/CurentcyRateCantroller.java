package hunbow.big.springtest.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class CurentcyRateCantroller {

    private static final String CBR_URL = "https://cbr.ru/scripts/XML_daily.asp";

    @GetMapping("/currency-rate/{charCode}{date}")
    public String getCurrencyRate(@PathVariable String charCode,
                                  @DateTimeFormat(pattern = "dd-MM-yyyy") @PathVariable LocalDate date) {
        return null;
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
//            System.out.println(ex.getMessage());
//            return ex.getMessage();
            throw new RuntimeException("Could't connect to CBR");
        }
    }

}
