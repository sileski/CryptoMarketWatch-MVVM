package com.example.cryptomvvm.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cryptomvvm.api.CoinMarketCapApiClient;
import com.example.cryptomvvm.models.CoinModel;
import com.example.cryptomvvm.models.FiatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoinsRepository {

    private static CoinsRepository instance;

    private CoinMarketCapApiClient coinMarketCapApiClient;

    private int mStart;
    private int mLimit;
    private String mConvert;
    private String mSort;
    private String mCryptoType;

    private String selectedFiat = "";
    private String selectedSort = "market_cap";
    private String selectedCryptoType = "all";

    HashMap<String, String> sortOptions = new HashMap<>();
    List<String> cryptoTypeOptions = new ArrayList<>();

    public static CoinsRepository getInstance(){
        if(instance == null){
            instance = new CoinsRepository();
        }
        return  instance;
    }

    private CoinsRepository(){
        coinMarketCapApiClient = CoinMarketCapApiClient.getInstance();
    }

    public LiveData<List<CoinModel>> getCoins(){
        return coinMarketCapApiClient.getCoins();
    }

    public LiveData<List<FiatModel>> getFiat(){ return coinMarketCapApiClient.getFiat(); };

    public void getAllFiatApi(){
        coinMarketCapApiClient.getAllFiatApi();
    }

    public void getAllCoinsApi(int start, int limit, String convert, String sort, String cryptoType){
        mStart = start;
        mLimit = limit;
        mConvert = convert;
        mSort = sort;
        mCryptoType = cryptoType;

        coinMarketCapApiClient.getAllCoinsApi(start, limit, convert, sort, cryptoType);
    }

    public void getNextPage(){
        getAllCoinsApi(mStart+mLimit, mLimit, mConvert, mSort, mCryptoType);
    }

    public int getFiatPosition(String fiat){
        List<FiatModel> fiatList = getFiat().getValue();
        int currentFiatPosition = 0;
        for(int i=0; i<fiatList.size(); i++){
            if(fiatList.get(i).getSymbol().equals(fiat)){
                return i;
            }
        }
        return 0;
    }

    public void setSelectedFiat(String fiat){
        selectedFiat = fiat;
    }

    public String getSelectedFiat(){
        if(selectedFiat == "" && coinMarketCapApiClient.getFiat() != null){
            selectedFiat = coinMarketCapApiClient.getFiat().getValue().get(0).getSymbol();
            return selectedFiat;
        }
        return selectedFiat;
    }

    public HashMap<String, String> getSortOptions(){
        if(sortOptions.size() == 0) {
            sortOptions.put("Market Cap", "market_cap");
            sortOptions.put("Name", "name");
            sortOptions.put("Date Added", "date_added");
            sortOptions.put("Volume 7 Days", "volume_7d");
        }
        return sortOptions;
    }

    public int getSortPosition(String sort){
        List<String> items = new ArrayList<>();
        for(String key : sortOptions.keySet()){
            items.add(key);
        }
        for(int i=0; i<sortOptions.size(); i++){
            if(items.get(i).equals(sort)){
                return i;
            }
        }
        return 0;
    }

    public void setSelectedSort(String selectedSort) {
        this.selectedSort = selectedSort;
    }

    public String getSelectedSort() {
        return selectedSort;
    }

    public List<String> getCryptoTypeOptions(){
        if(cryptoTypeOptions.size() == 0) {
            cryptoTypeOptions.add("all");
            cryptoTypeOptions.add("coins");
            cryptoTypeOptions.add("tokens");
        }
        return cryptoTypeOptions;
    }

    public int getCryptoTypePosition(String cryptoType){
        for(int i=0; i<cryptoTypeOptions.size(); i++){
            if(cryptoTypeOptions.get(i).equals(cryptoType)){
                return i;
            }
        }
        return 0;
    }

    public String getSelectedCryptoType(){
        return selectedCryptoType;
    }

    public void setSelectedCryptoType(String selectedCryptoType) {
        this.selectedCryptoType = selectedCryptoType;
    }

}
