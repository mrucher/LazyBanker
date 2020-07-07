package SiteParser;

import Area.Area;
import Config.Config;
import Currency.Currency;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SiteParser {


    public Document makeUrl(Area area, Currency currency) throws IOException {
        StringBuilder url = new StringBuilder(area.URL);
        url.insert(Config.defaultURLPart.length()-1, "/" + currency.currencyName);
        return Jsoup.connect(url.toString()).get();
    }

    public List<ParseResult> parseBanks(Document document, Currency currency) throws IOException {
        List<ParseResult> result = new ArrayList<>();
        Set<String> findedBanks = new HashSet<>();

        Element body = document.body();

        Elements els = body.select("tr[data-test$=bank-rates-row]");

        for (Element el : els) {
            Elements as = el.select("a.font-bold");
            for (Element a : as) {
                if (!findedBanks.contains(a.text())) {
                    findedBanks.add(a.text());
                    Elements lines = el.select("td[data-currencies-rate-buy], td[data-currencies-rate-sell]");
                    Elements prices = lines.select("td[data-currencies-code=" + currency.currencyName + "]");
                    result.add(new ParseResult(a.text(), prices.attr("data-currencies-rate-buy"), prices.attr("data-currencies-rate-sell")));
                }
            }
        }
        els = body.getElementsByClass("table-flex__row item calculator-hover-icon__container");
        for (Element el : els) {
            Elements as = el.select("a.font-bold");
            for (Element a : as) {
                if (!findedBanks.contains(a.text())) {
                    findedBanks.add(a.text());
                    Elements lines = el.select("div[data-currencies-rate-buy], div[data-currencies-rate-sell]");
                    Elements prices = lines.select("div[data-currencies-code=" + currency.currencyName + "]");
                    result.add(new ParseResult(a.text(), prices.attr("data-currencies-rate-buy"), prices.attr("data-currencies-rate-sell")));
                }
            }
        }

        return result;
    }
}
