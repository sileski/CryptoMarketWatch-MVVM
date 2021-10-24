package com.example.cryptomvvm.api;

import com.example.cryptomvvm.models.FiatModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FiatResponse {

    @SerializedName("data")
    private List<FiatModel> fiatList;

    public List<FiatModel> getFiatList() {
        return fiatList;
    }
}
