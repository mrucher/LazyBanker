package Area;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class AreaParserTest {

    private AreaParser areaParser;

    @Before
    public void initTest() throws IOException {
        areaParser = new AreaParser();
    }

    @Test
    public void parse() {
        assertEquals("Empty", areaParser.parse("sdfxv thtd usd sdgn").areaName);
        assertEquals("Москва", areaParser.parse("Есть ли фунтики в москве?").areaName);
        assertEquals("Самара", areaParser.parse("Эх самара городок").areaName);
    }
}