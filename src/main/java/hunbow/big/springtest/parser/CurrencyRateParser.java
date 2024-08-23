package hunbow.big.springtest.parser;

import hunbow.big.springtest.model.CurrencyRate;

import java.util.List;

public interface CurrencyRateParser {
    public List<CurrencyRate> parse(String ratesAsString);
}
