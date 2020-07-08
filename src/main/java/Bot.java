import Area.Area;
import Area.AreaParser;
import CBParser.CBParser;
import Currency.Currency;
import Currency.CurrencyParser;
import SiteParser.SiteParser;
import SiteParser.ParseResult;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Класс-обработчик поступающих к боту сообщений.
 */
public class Bot extends TelegramLongPollingBot {
    CurrencyParser cp;
    AreaParser ap;
    SiteParser s = new SiteParser();

    List<ParseResult> findedBanks;

    public Bot() {

        try {
            cp = new CurrencyParser();
        } catch (IOException e) {
            Logger.logTest("ERROR: Currency.json not found");
        }
        try {
            ap = new AreaParser();
        } catch (IOException e) {
            Logger.logTest("ERROR: Area.json not found");
        }

    }

    @Override
    public String getBotToken() {
        return "1356820281:AAFFXgtb5R35Lz5iJqdSoBLRcSLZAQxA8LU";
    }

    @Override
    public String getBotUsername() {
        return "LazyBanker_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage()) {
                Message inMessage = update.getMessage();
                Chat inChat = inMessage.getChat();

                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId());

                if (!inMessage.hasText() && !inChat.isSuperGroupChat() && !inChat.isGroupChat()) {
                    outMessage.setText("Моя твоя не понимать");
                    execute(outMessage);
                    String logMsg = inMessage.getChatId().toString() + " "
                            + inMessage.getDate().toString() + " "
                            + inMessage.getText() + " "
                            + outMessage.getText() + "Моя твоя не понимать" + '\n';
                    Logger.logTest(logMsg);
                    return;
                } else if (!inMessage.hasText()) {
                    return;
                }

                if (inMessage.getText().toLowerCase().contains("/start")) {
                    outMessage.setText("Добро пожаловать. Я бот позволяющий узнать курс валюты в ЦБ на определнное число " +
                            "или курс обмена валют в банках различных городов." + '\n' + '\n'
                            + "Введите /areas чтобы узнать доступные города." + '\n'
                            + "Введите /currencys чтобы узнать доступные валюты" + '\n' + '\n'
                            + "Вот некоторые примеры запросов:" + '\n' + '\n'
                            + "Курс евро в Москве" + '\n'
                            + "Юань спб" + '\n'
                            + "курс цб доллар" + '\n'
                            + "Центробанк йена 20.06.2020");
                    execute(outMessage);
                    String logMsg = inMessage.getChatId().toString() + " "
                            + inMessage.getDate().toString() + " "
                            + inMessage.getText() + " "
                            + "/start" + '\n';
                    Logger.logTest(logMsg);
                    return;
                }
                if (inMessage.getText().toLowerCase().contains("/areas")) {
                    StringBuilder sb = new StringBuilder("");
                    for (Area area : ap.globalArea.areas) {
                        sb.append(area.areaName + '\n');
                    }
                    outMessage.setText(sb.toString());
                    execute(outMessage);
                    String logMsg = inMessage.getChatId().toString() + " "
                            + inMessage.getDate().toString() + " "
                            + inMessage.getText() + " "
                            + "/areas" + '\n';
                    Logger.logTest(logMsg);
                    return;
                }
                if (inMessage.getText().toLowerCase().contains("/currencys")) {
                    StringBuilder sb = new StringBuilder("");
                    for (Currency currency : cp.globalCurrency.currencies) {
                        sb.append(currency.currencyName + '\n');
                    }
                    outMessage.setText(sb.toString());
                    execute(outMessage);
                    String logMsg = inMessage.getChatId().toString() + " "
                            + inMessage.getDate().toString() + " "
                            + inMessage.getText() + " "
                            + "/currencys" + '\n';
                    Logger.logTest(logMsg);
                    return;
                }

                Currency findedCurrency = cp.parse(inMessage.getText());

                if (inChat.isGroupChat() || inChat.isSuperGroupChat()) {
                    if (findedCurrency.currencyName.equals("Empty")) {
                        return;
                    }
                }

                String[] result = botLogic(inMessage.getChatId().toString(), inMessage.getDate().toString(), inMessage.getText());

                outMessage.setText(result[0]);
                execute(outMessage);

                Logger.logTest(result[1]);

            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public String[] botLogic(String ID, String Date, String text) {
        try {
            String[] result = new String[2];
            Currency findedCurrency = cp.parse(text);
            Area findedArea = ap.parse(text);

            if (findedCurrency.currencyName.equals("Empty")) {
                result[0] = "Валюта не распознана. Перефразируйте запрос";
                result[1] = ID + " "
                        + Date + " "
                        + text + " "
                        + result[0] + '\n';
                return result;
            }

            if (CBParser.hasCB(text) && findedArea.areaName.equals("Empty")) {
                Date dateNow = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");
                String curDate = formatForDateNow.format(dateNow);
                String findDate = CBParser.findDate(text);
                if (!findDate.equals("")) {
                    findDate = CBParser.dateNormalize(findDate);
                    String[] s1 = findDate.split("\\.");
                    String[] s2 = curDate.split("\\.");
                    if (Integer.parseInt(s1[2]) > Integer.parseInt(s2[2])
                            || (Integer.parseInt(s1[2]) == Integer.parseInt(s2[2]) && Integer.parseInt(s1[1]) > Integer.parseInt(s2[1]))
                            || (Integer.parseInt(s1[2]) == Integer.parseInt(s2[2]) && Integer.parseInt(s1[1]) == Integer.parseInt(s2[1]) && Integer.parseInt(s1[0]) > Integer.parseInt(s2[0]))) {
                        result[0] = "Не только лишь все могут смотреть в завтрашний день. Повторите попытку";

                        result[1] = ID + " "
                                + Date + " "
                                + text + " невалидная дата" + '\n';

                        return result;
                    }
                    if (Integer.parseInt(s1[2]) < 2003) {
                        result[0] = "Тогда еще динозавры жили, а курса ЦБ и подавно не было. Повторите попытку";
                        result[1] = ID + " "
                                + Date + " "
                                + text + " невалидная дата" + '\n';
                        return result;
                    }


                    String findCB = CBParser.parseCB(CBParser.findDate(text), findedCurrency);

                    result[0] = "Курс ЦБ на " + CBParser.findDate(text) + " для " + findedCurrency.currencyName + " равен " + findCB;

                    result[1] = ID + " "
                            + Date + " "
                            + text + " "
                            + result[0] + '\n';

                    return result;
                } else {
                    String findCB = CBParser.parseCB(curDate, findedCurrency);
                    result[0] = "Курс ЦБ сегодня для " + findedCurrency.currencyName + " равен " + findCB;

                    result[1] = ID + " "
                            + Date + " "
                            + text + '\n';

                    return result;
                }
            }

            if (findedArea.areaName.equals("Empty")) {
                result[0] = "Город не распознан. Переформулируйте запрос";
                result[1] = ID + " "
                        + Date + " "
                        + text + " невалидный город" + '\n';

                return result;
            } else {
                findedBanks = s.parseBanks(s.makeUrl(findedArea, findedCurrency), findedCurrency);
            }


            if (findedBanks.size() == 0) {
                result[0] = "Валюта " + findedCurrency.currencyName
                        + " в городе " + findedArea.areaName
                        + " не обменивается";

                result[1] = ID + " "
                        + Date + " "
                        + text + " "
                        + result[0] + '\n';

                return result;
            }
            StringBuilder msg = new StringBuilder(findedCurrency.currencyName + " в городе " + findedArea.areaName + '\n' + '\n');
            for (int i = 0; i < Math.min(5, findedBanks.size()); i++) {
                msg.append(findedBanks.get(i).getBankName() + ":" + '\n'
                        + "покупка " + findedBanks.get(i).getBuy() + " рублей " + '\n'
                        + "продажа " + findedBanks.get(i).getSell() + " рублей " + '\n' + '\n');
            }
            result[0] = msg.toString();

            result[1] = ID + " "
                    + Date + " "
                    + text + " "
                    + result[0] + '\n';
            return result;
        } catch (IOException e) {
            String[] result = new String[2];
            result[0] = "Упс. Что-то пошло не так. Повторите попытку позже.";
            result[1] = e.toString();
            return result;
        }

    }


}