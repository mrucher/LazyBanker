package SiteParser;

public class ParseResult {
    private String bankName;
    private String buy;
    private String sell;

    public ParseResult(String bankName, String buy, String sell) {
        this.bankName = bankName;
        this.buy = buy;
        this.sell = sell;
    }

    public String getBankName(){
        return bankName;
    }

    public String getBuy(){
        return buy;
    }

    public String getSell(){
        return sell;
    }
}
