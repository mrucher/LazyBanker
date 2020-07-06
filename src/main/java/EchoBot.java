import Area.Area;
import Area.AreaParser;
import CBParser.CBParser;
import Config.Config;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс-обработчик поступающих к боту сообщений.
 */
public class EchoBot extends TelegramLongPollingBot {
    CurrencyParser cp;
    AreaParser ap;
    SiteParser s = new SiteParser();

    List<ParseResult> findedBanks;
    Area defaultArea = new Area();

    Map<Long, Area> defaultAreaMap;

    public EchoBot() {

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

        defaultArea.areaName = Config.defaultAreaName;
        defaultArea.URL = Config.defaultAreaURL;
        defaultAreaMap = new HashMap<>();

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

                if (!defaultAreaMap.containsKey(inChat.getId())) {
                    defaultAreaMap.put(inChat.getId(), defaultArea);
                }

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

                Currency findedCurrency = cp.parse(inMessage.getText());
                System.out.println(cp.globalCurrency.currencies.size());
                if (CBParser.hasCB(inMessage.getText())) {
                    if (CBParser.findDate(inMessage.getText()) != "") {
                        String findCB = CBParser.CBParse(CBParser.findDate(inMessage.getText()), findedCurrency);

                        outMessage.setText("Курс ЦБ на " + CBParser.findDate(inMessage.getText()) + " для " + findedCurrency.currencyName + " равен " + findCB);
                        execute(outMessage);
                        Logger.logTest(outMessage.getText());
                        return;
                    } else {
                        Date dateNow = new Date();
                        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd.MM.yyyy");

                        String findCB = CBParser.CBParse(formatForDateNow.format(dateNow), findedCurrency);
                        outMessage.setText("Курс ЦБ сегодня для " + findedCurrency.currencyName + " равен " + findCB);
                        execute(outMessage);
                        Logger.logTest(outMessage.getText());
                        return;
                    }
                }


                Area findedArea = ap.parse(inMessage.getText());

                if (inChat.isGroupChat() || inChat.isSuperGroupChat()) {
                    if (findedCurrency.currencyName.equals("Empty")) {
                        return;
                    }
                }
                if (findedCurrency.currencyName.equals("Empty")) {
                    outMessage.setText("Валюта не распознана. Перефразируйте запрос");
                    String logMsg = inMessage.getChatId().toString() + " "
                            + inMessage.getDate().toString() + " "
                            + inMessage.getText() + " "
                            + outMessage.getText() + '\n';
                    Logger.logTest(logMsg);
                    execute(outMessage);
                    return;
                }

                if (!findedArea.areaName.equals("Empty")) {
                    defaultAreaMap.put(inChat.getId(), findedArea);
                    findedBanks = s.parseBanks(findedArea, findedCurrency);
                } else {
                    findedBanks = s.parseBanks(defaultArea, findedCurrency);
                }

                if (findedBanks.size() == 0) {
                    outMessage.setText("Валюта" + findedCurrency.currencyName
                            + " в городе " + defaultAreaMap.get(inChat.getId()).areaName
                            + " не обменивается");
                    String logMsg = inMessage.getChatId().toString() + " "
                            + inMessage.getDate().toString() + " "
                            + inMessage.getText() + " "
                            + outMessage.getText() + '\n';
                    Logger.logTest(logMsg);
                    return;
                }
                StringBuilder msg = new StringBuilder(findedCurrency.currencyName + " в городе " + defaultAreaMap.get(inChat.getId()).areaName + '\n' + '\n');
                for (int i = 0; i < Math.min(5, findedBanks.size()); i++) {
                    msg.append(findedBanks.get(i).getBankName() + ":" + '\n'
                            + "покупка " + findedBanks.get(i).getBuy() + " рублей " + '\n'
                            + "продажа " + findedBanks.get(i).getSell() + " рублей " + '\n' + '\n');
                }
                outMessage.setText(msg.toString());

                String logMsg = inMessage.getChatId().toString() + " "
                        + inMessage.getDate().toString() + " "
                        + inMessage.getText() + " "
                        + outMessage.getText() + '\n';
                Logger.logTest(logMsg);
                execute(outMessage);
            }
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
    }


}