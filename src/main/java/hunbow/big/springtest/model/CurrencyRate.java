package hunbow.big.springtest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyRate {
    private String numCode;
    private String charCode;
    private String nominal;
    private String name;
    private String value;
    private String vunitRate;

}
