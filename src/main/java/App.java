
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws TelegramApiRequestException, IOException {



        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(new EchoBot());



//        Document document = Jsoup.connect("https://www.banki.ru/products/currency/cb/03.07.2020/").get();
//        Element body = document.body();
//
//        Elements els = body.select("tr[data-currency-code$=USD]");
//
//       // Elements res = els.select("a[href]" + "td");
//        for (Element el : els) {
//            String s[] = els.text().split(" ");
//            System.out.println(s[4]);
//           // System.out.println(el.text());
////            Elements as = el.select("a.font-bold");
////            for (Element a : as) {
////                if (!findedBanks.contains(a.text())) {
////                    findedBanks.add(a.text());
////                    Elements lines = el.select("div[data-currencies-rate-buy], div[data-currencies-rate-sell]");
////                    Elements prices = lines.select("div[data-currencies-code=" + currency.currencyName + "]");
////                    result.add(new ParseResult(a.text(), prices.attr("data-currencies-rate-buy"), prices.attr("data-currencies-rate-sell")));
////                }
////            }
//        }


//        String text = "Цб 4.07.2020";
//        Pattern pattern = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])([\\.\\\\\\/-])(0?[1-9]|1[012])\\2(((19|20)\\d\\d)|(\\d\\d))");
//        Matcher matcher = pattern.matcher(text);
//        if(matcher.find()) {
//            System.out.println(text.substring(matcher.start(), matcher.end()));
//
//        }


    }
}