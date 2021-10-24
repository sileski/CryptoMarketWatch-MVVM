package com.example.cryptomvvm.api;

import android.os.Debug;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cryptomvvm.AppExecutors;
import com.example.cryptomvvm.Utils.Credentials;
import com.example.cryptomvvm.models.CoinModel;
import com.example.cryptomvvm.models.FiatModel;
import com.example.cryptomvvm.repositories.CoinsRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class CoinMarketCapApiClient {

    MutableLiveData<List<CoinModel>> mCoins;
    MutableLiveData<List<FiatModel>> mFiat;
    private static CoinMarketCapApiClient instance;

    private RetriveCoinsRunnable retriveCoinsRunnable;

    private RetriveFiatRunnable retriveFiatRunnable;

    public static CoinMarketCapApiClient getInstance() {
        if (instance == null) {
            instance = new CoinMarketCapApiClient();
        }
        return instance;
    }

    private CoinMarketCapApiClient() {
        mCoins = new MutableLiveData<>();
        mFiat = new MutableLiveData<>();
    }

    public LiveData<List<CoinModel>> getCoins() {
        return mCoins;
    }

    public LiveData<List<FiatModel>> getFiat() {return mFiat;}

    public void getAllCoinsApi(int start, int limit, String convert, String sort, String cryptoType) {
        if(retriveCoinsRunnable  != null){
            retriveCoinsRunnable = null;
        }

        retriveCoinsRunnable = new RetriveCoinsRunnable(start, limit, convert, sort, cryptoType);

        final Future myHandler = AppExecutors.getInstance().networkIo().submit(retriveCoinsRunnable);
        AppExecutors.getInstance().networkIo().schedule(new Runnable() {
            @Override
            public void run() {
                //Canceling retrofit call
                myHandler.cancel(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);
    }

    public void getAllFiatApi(){
        if(retriveFiatRunnable != null)
        {
            retriveFiatRunnable = null;
        }

        retriveFiatRunnable = new RetriveFiatRunnable();
        final Future myHandler = AppExecutors.getInstance().networkIo().submit(retriveFiatRunnable);
        AppExecutors.getInstance().networkIo().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        }, 5000, TimeUnit.MILLISECONDS);

    }

    //Retrive data from RestApi by runnable class
    private class RetriveCoinsRunnable implements Runnable {

        private int start;
        private int limit;
        private String convert;
        private String sort;
        private String cryptoType;
        boolean cancelRequest;

        public RetriveCoinsRunnable(int start, int limit, String convert, String sort, String cryptoType) {
            this.start = start;
            this.limit = limit;
            this.convert = convert;
            this.sort = sort;
            this.cryptoType = cryptoType;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response  = getCoins(start, limit, convert, sort, cryptoType).execute();
                if (cancelRequest == true) {
                    return;
                }

                if (response.code() == 200){
                    List<CoinModel> coinModelList = new ArrayList<>(((CoinsLatestResponse)response.body()).getCoinsList());
                    if(start == 1){
                        //postValue background thread
                        //setValue not for bacgkroud thread
                        mCoins.postValue(coinModelList);
                    }
                    else {
                        List<CoinModel> currentCoins = mCoins.getValue();
                        currentCoins.addAll(coinModelList);
                        mCoins.postValue(currentCoins);
                    }
                }
                else {
                    String error = response.errorBody().toString();
                    Log.v("Tag", error);
                    mCoins.postValue(null);
                }
            }
            catch (IOException e){
                e.printStackTrace();
                mCoins.postValue(null);
            }
        }

        private Call<CoinsLatestResponse> getCoins(int start, int limit, String convert, String sort, String cryptoType) {
            return RetrofitHelper.getCoinMarketCapApi().getCoins(
                    Credentials.COINMARKETCAP_API_KEY, start, limit, convert, sort, cryptoType
            );
        }

        private void cancelRequest() {
            cancelRequest = true;
            Log.v("Tag", "Canceling request");
        }
    }

    private class RetriveFiatRunnable implements Runnable{

        @Override
        public void run() {
            try {
                Response response = getFiat().execute();

                if (response.code() == 200){
                    List<FiatModel> fiatModelList = new ArrayList<>(((FiatResponse)response.body()).getFiatList());
                    mFiat.postValue(fiatModelList);
                }
                else{
                    String error = response.errorBody().toString();
                    Log.v("Tag", error);
                    mFiat.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Call<FiatResponse> getFiat(){
            return RetrofitHelper.getCoinMarketCapApi().getFiats(Credentials.COINMARKETCAP_API_KEY);
        }
    }
}
