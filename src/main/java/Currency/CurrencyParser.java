package Currency;

import Config.Config;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CurrencyParser {
    private Map<String, String> currencyMap = new HashMap<>();
    public GlobalCurrency globalCurrency;

    public CurrencyParser() throws IOException {
        globalCurrency = initGlobalCurrency();
        for (Currency currency : globalCurrency.currencies) {
            for (Nickname nickname : currency.nicknames) {
                currencyMap.put(nickname.name.toLowerCase(), currency.currencyName);
            }
        }
    }

    private GlobalCurrency initGlobalCurrency() throws IOException {
        GlobalCurrency globalCurrency;
        Gson g = new Gson();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(Config.defaultCurrencyJsonPath), StandardCharsets.UTF_8);
        globalCurrency = g.fromJson(reader, GlobalCurrency.class);
        reader.close();
        return globalCurrency;
    }

    public Currency parse(String msg) {
        Currency result = new Currency();
        result.currencyName = "Empty";
        String ms = msg.toLowerCase();
        for (Map.Entry<String, String> cur : currencyMap.entrySet()) {
            if (ms.contains(cur.getKey().toLowerCase())) {
                result.currencyName = cur.getValue();
                return result;
            }
        }
        return result;
    }
}
