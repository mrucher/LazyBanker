package CBParser;

import Currency.*;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CBParserTest {

    @Test
    public void CBParse() throws IOException {
        CurrencyParser cp = new CurrencyParser();

        assertEquals("66.4437", CBParser.CBParse("04.03.2020", cp.globalCurrency.currencies.get(0)));
        assertEquals("66.9909", CBParser.CBParse("30.02.2020", cp.globalCurrency.currencies.get(0)));
        assertEquals("33.7956", CBParser.CBParse("04.03.2007", cp.globalCurrency.currencies.get(3)));
    }

    @Test
    public void findDate() throws IOException {
        assertEquals("04.03.10", CBParser.findDate("04.03.1020"));
        assertEquals("04.03.20", CBParser.findDate("04.03.20"));
        assertEquals("", CBParser.findDate("asvlm ldvlvm 12.12"));
        assertEquals("04.03.2020", CBParser.findDate("s 04.03.2020 sa"));
        assertEquals("04-03-20", CBParser.findDate("04-03-20"));
        assertEquals("04-03-20", CBParser.findDate("04-03-20 dvxv"));
    }

    @Test
    public void hasCB() {
        assertFalse(CBParser.hasCB("04.03.2020"));
        assertTrue(CBParser.hasCB("04.03.2020 цб"));
        assertTrue(CBParser.hasCB("zdfbs центробанкцыа"));
    }
}