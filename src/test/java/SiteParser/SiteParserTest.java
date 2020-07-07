package SiteParser;

import Area.AreaParser;
import Currency.CurrencyParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class SiteParserTest {
    CurrencyParser cp;
    AreaParser ap;
    SiteParser sp;

    @Before
    public void initTest() throws IOException {
        cp = new CurrencyParser();
        ap = new AreaParser();
        sp = new SiteParser();
    }

    @Test
    public void parseBanks() throws IOException {
        File htmlFile = new File("src/test/url/01.html");
        Document doc = Jsoup.parse(htmlFile, "UTF-8");
        assertEquals("Ак Барс", sp.parseBanks(doc, cp.globalCurrency.currencies.get(0)).get(0).getBankName());
        assertEquals("69.67", sp.parseBanks(doc, cp.globalCurrency.currencies.get(0)).get(0).getBuy());
        assertEquals("73.67", sp.parseBanks(doc, cp.globalCurrency.currencies.get(0)).get(0).getSell());

        htmlFile = new File("src/test/url/02.html");
        doc = Jsoup.parse(htmlFile, "UTF-8");
        assertEquals("Банк Открытие", sp.parseBanks(doc, cp.globalCurrency.currencies.get(4)).get(0).getBankName());
        assertEquals("0.6501", sp.parseBanks(doc, cp.globalCurrency.currencies.get(4)).get(0).getBuy());
        assertEquals("0.683", sp.parseBanks(doc, cp.globalCurrency.currencies.get(4)).get(0).getSell());

        htmlFile = new File("src/test/url/03.html");
        doc = Jsoup.parse(htmlFile, "UTF-8");
        assertEquals("СМП Банк", sp.parseBanks(doc, cp.globalCurrency.currencies.get(1)).get(1).getBankName());
        assertEquals("79.8", sp.parseBanks(doc, cp.globalCurrency.currencies.get(1)).get(1).getBuy());
        assertEquals("82.2", sp.parseBanks(doc, cp.globalCurrency.currencies.get(1)).get(1).getSell());
    }
}