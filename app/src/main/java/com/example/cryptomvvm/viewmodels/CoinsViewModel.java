package com.example.cryptomvvm.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cryptomvvm.models.CoinModel;
import com.example.cryptomvvm.models.FiatModel;
import com.example.cryptomvvm.repositories.CoinsRepository;

import java.util.HashMap;
import java.util.List;

public class CoinsViewModel extends ViewModel {

    private CoinsRepository coinsRepository;

    public CoinsViewModel(){
        coinsRepository = CoinsRepository.getInstance();
    }

    public LiveData<List<CoinModel>> getAllCoins(){
        return coinsRepository.getCoins();
    }

    public LiveData<List<FiatModel>> getFiat(){ return coinsRepository.getFiat(); };

    public void getAllFiatApi(){
        coinsRepository.getAllFiatApi();
    }

    public void getAllCoinsApi(int start, int limit, String convert, String sort, String cryptoType){
        coinsRepository.getAllCoinsApi(start, limit, convert, sort, cryptoType);
    }

    public void getNextPage(){
        coinsRepository.getNextPage();
    }

    public void setSelectedFiat(String fiat){
        coinsRepository.setSelectedFiat(fiat);
    }

    public String getSelectedFiat(){
        return coinsRepository.getSelectedFiat();
    }

    public int getFiatPosition(String fiat){return coinsRepository.getFiatPosition(fiat);}

    public HashMap<String, String> getSortOptions(){
        return coinsRepository.getSortOptions();
    }

    public int getSortPosition(String sort){return  coinsRepository.getSortPosition(sort);}

    public String getSelectedSort(){
        return coinsRepository.getSelectedSort();
    }

    public void setSelectedSort(String sort){
        coinsRepository.setSelectedSort(sort);
    }

    public List<String> getCryptoTypeOptions(){
        return coinsRepository.getCryptoTypeOptions();
    }

    public int getCryptoTypePosition(String cryptoType){ return  coinsRepository.getCryptoTypePosition(cryptoType);}

    public void setSelectedCryptoType(String cryptoType){
        coinsRepository.setSelectedCryptoType(cryptoType);
    }

    public String getSelectedCryptoType(){
        return coinsRepository.getSelectedCryptoType();
    }

}
