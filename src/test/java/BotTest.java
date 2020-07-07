import Area.AreaParser;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BotTest {

    private Bot bot;

    @Before
    public void initTest() throws IOException {
        bot = new Bot();
    }
    @Test
    public void botLogic() {
        assertEquals("Валюта JPY в городе Мурманск не обменивается", bot.botLogic("1", "2", "курс йены в мурманске")[0]);
        assertEquals("Не только лишь все могут смотреть в завтрашний день. Повторите попытку", bot.botLogic("1", "2", "Курс доллара на 20.10.2021")[0]);
        assertEquals("Курс ЦБ на 02.03.2019 для USD равен 65.8145", bot.botLogic("1", "2", "курс доллара цб на 30.02.2019")[0]);
        assertEquals("Валюта не распознана. Перефразируйте запрос", bot.botLogic("1", "2", "курс цб на 30.02.2019")[0]);
        assertEquals("Город не распознан. Переформулируйте запрос", bot.botLogic("1", "2", "евро доллар")[0]);

    }
}