package com.example.cryptomvvm.api;

import com.example.cryptomvvm.Utils.Credentials;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(Credentials.COINMARKETCAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static CoinMarketCapService coinMarketCapService = retrofit.create(CoinMarketCapService.class);

    public static CoinMarketCapService getCoinMarketCapApi(){
        return coinMarketCapService;
    }

}
