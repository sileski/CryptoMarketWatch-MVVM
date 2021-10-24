package com.example.cryptomvvm.models;

import java.util.List;
import java.util.Map;

public class CoinModel {

    private String id;

    private String name;

    private String symbol;

    private String slug;

    private double max_supply;

   private Map<String, Quote> quote;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSlug() {
        return slug;
    }

    public double getMax_supply() {
        return max_supply;
    }

    public Map<String, Quote> getQuote() {
        return quote;
    }

    public class Quote{
        private double price;

        private double percent_change_24h;

        public double getPrice() {
            return price;
        }

        public double getPercent_change_24h() {
            return percent_change_24h;
        }
    }
}
