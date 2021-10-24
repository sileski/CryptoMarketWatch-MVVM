package com.example.cryptomvvm.api;

import com.example.cryptomvvm.api.CoinsLatestResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinMarketCapService {

    @GET("v1/cryptocurrency/listings/latest")
    Call<CoinsLatestResponse> getCoins(@Query("CMC_PRO_API_KEY") String apiKey, @Query("start") int start,
                                       @Query("limit") int limit, @Query("convert") String convert, @Query("sort") String sort, @Query("cryptocurrency_type") String cryptoType);

    @GET("v1/fiat/map")
    Call<FiatResponse> getFiats(@Query("CMC_PRO_API_KEY") String apiKey);
}
