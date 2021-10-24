package com.example.cryptomvvm.api;

import com.example.cryptomvvm.models.CoinModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinsLatestResponse {

    @SerializedName("data")
    private List<CoinModel> coinsList;

    public List<CoinModel> getCoinsList(){
        return coinsList;
    }
}
