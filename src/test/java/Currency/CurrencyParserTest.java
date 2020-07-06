package Currency;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CurrencyParserTest {
    private CurrencyParser currencyParser;

    @Before
    public void initTest() throws IOException {
        currencyParser = new CurrencyParser();
    }

    @Test
    public void parse() {
        assertEquals("USD", currencyParser.parse("sdfxv thtd usd sdgn").currencyName);
        assertEquals("EUR", currencyParser.parse("А можно ли евро прикупить в москве?").currencyName);
        assertEquals("CNY", currencyParser.parse("купить китайца на черном рынке").currencyName);
    }
}