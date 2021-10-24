package com.example.cryptomvvm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.cryptomvvm.R;
import com.example.cryptomvvm.models.CoinModel;
import com.google.gson.Gson;

public class CoinDetailsActivity extends AppCompatActivity {

    TextView coinName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);

        coinName = findViewById(R.id.coinName);

        getDataFromIntent();
    }

    private void getDataFromIntent(){
        if(getIntent().hasExtra("coin")){

            Gson gson = new Gson();
            CoinModel coin = getIntent().getParcelableExtra("coin");
            coin = gson.fromJson(getIntent().getStringExtra("coin"), CoinModel.class);

            coinName.setText(String.valueOf(coin.getQuote().get(coin.getQuote().keySet().iterator().next()).getPrice()));
        }
    }
}