package CBParser;

import Currency.Currency;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CBParser {
    public static String CBParse(String date, Currency currency) throws IOException {

        Map<String, Integer> indexes = new HashMap<>();
        indexes.put("USD", 4);
        indexes.put("EUR", 3);
        indexes.put("GBP", 6);
        indexes.put("CNY", 4);
        indexes.put("JPY", 4);


        String correctDate = dateNormalize(date);
        Document document = Jsoup.connect("https://www.banki.ru/products/currency/cb/" + correctDate + "/").get();
        Element body = document.body();

        Elements els = body.select("tr[data-currency-code$=" + currency.currencyName + "]");
        String[] result = els.text().split(" ");
        if (els.text().equals("")) {
            return "";
        }
        return result[indexes.get(currency.currencyName)];
    }

    public static String findDate(String text) throws IOException {
        String result = "";
        Pattern pattern = Pattern.compile("(0?[1-9]|[12][0-9]|3[01])([\\.\\\\\\/-])(0?[1-9]|1[012])\\2(((19|20)\\d\\d)|(\\d\\d))");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            result = text.substring(matcher.start(), matcher.end());
            String correctDate = dateNormalize(result);
            Document document = Jsoup.connect("https://www.banki.ru/products/currency/cb/" + correctDate + "/").get();
            Element body = document.body();
            Elements date = body.select("span[data-test$=date-picker]");
            for(Element el : date){
                if(!date.text().equals("сегодня сегодня")) {
                    result = date.text();
                    break;
                }
            }
            result = result.substring(0, 10);
           // System.out.println(result);
        }
        return result;
    }

    public static boolean hasCB(String msg) {
        String ms = msg.toLowerCase();
        return ms.contains("цб") || ms.contains("центробанк") || ms.contains("курс");
    }

    public static String dateNormalize(String date) {
        StringBuilder result = new StringBuilder("");
        String[] s = date.split("-|\\.");
        if (s[0].length() == 1) {
            result.append("0");
        }
        result.append(s[0]);
        result.append(".");
        if (s[1].length() == 1) {
            result.append("0");
        }
        result.append(s[1]);
        result.append(".");
        if (s[2].length() == 2) {
            result.append("20");
        }
        result.append(s[2]);
        return result.toString();
    }
}

