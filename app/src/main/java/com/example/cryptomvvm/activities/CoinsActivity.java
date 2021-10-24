package com.example.cryptomvvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cryptomvvm.R;
import com.example.cryptomvvm.adapters.CoinsRecyclerViewAdapter;
import com.example.cryptomvvm.models.CoinModel;
import com.example.cryptomvvm.models.FiatModel;
import com.example.cryptomvvm.viewmodels.CoinsViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CoinsActivity extends AppCompatActivity implements CoinsRecyclerViewAdapter.ClickListener {

    //ViewModel
    private CoinsViewModel coinsViewModel;

    //RecyclerView
    private RecyclerView coinsRecyclerView;
    private CoinsRecyclerViewAdapter coinsRecyclerViewAdapter;

    Chip chipFiatSelect;
    Chip chipSortSelect;
    Chip chipCryptoTypeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coinsRecyclerView = findViewById(R.id.coinsRecyclerView);
        chipFiatSelect = findViewById(R.id.currencySelect);
        chipSortSelect = findViewById(R.id.sortSelect);
        chipCryptoTypeSelect = findViewById(R.id.cryptoTypeSelect);

        coinsViewModel = new ViewModelProvider(this).get(CoinsViewModel.class);

        //Calling observers
        observeDataChange();

        configureRecyclerView();

        getAllFiatApi();

        chipFiatSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFiatPopup(v);
            }
        });
        chipSortSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortPopup(v);
            }
        });
        chipCryptoTypeSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCryptoTypePopup();
            }
        });

    }

    //Observing data changes
    private void observeDataChange(){
        coinsViewModel.getAllCoins().observe(this, new Observer<List<CoinModel>>() {
            @Override
            public void onChanged(List<CoinModel> coinModels) {
                if(coinModels != null){
                    for (CoinModel coin : coinModels){
                        Log.d("Tag", coin.getName());
                        coinsRecyclerViewAdapter.setCoins(coinModels);
                    }
                }
            }
        });
        coinsViewModel.getFiat().observe(this, new Observer<List<FiatModel>>() {
            @Override
            public void onChanged(List<FiatModel> fiatModels) {
                if(fiatModels != null){
                    getAllCoinsApi(1, 20, coinsViewModel.getSelectedFiat(), coinsViewModel.getSelectedSort(), coinsViewModel.getSelectedCryptoType());
                }
            }
        });
    }

    private void configureRecyclerView(){
        coinsRecyclerViewAdapter = new CoinsRecyclerViewAdapter(this, this);
        coinsRecyclerView.setAdapter(coinsRecyclerViewAdapter);
        coinsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Pagination
        coinsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!recyclerView.canScrollVertically(1)){
                    coinsViewModel.getNextPage();
                }
            }
        });
    }

    private void getAllCoinsApi(int start, int limit, String convert, String sort, String cryptoType){
        coinsViewModel.getAllCoinsApi(start, limit, convert, sort, cryptoType);
    }

    private void getAllFiatApi(){
        coinsViewModel.getAllFiatApi();
    }

    private LiveData<List<FiatModel>> getFiat(){
       return coinsViewModel.getFiat();
    }

    private HashMap<String, String> getSortOptions(){
        return coinsViewModel.getSortOptions();
    }

    private List<String> getCryptoTypeOptions(){return coinsViewModel.getCryptoTypeOptions();}

    private void showFiatPopup(View v){
        List<FiatModel> fiatList = getFiat().getValue();

        String[] items = new String[fiatList.size()];
        for(int i=0; i<fiatList.size(); i++){
            items[i] = fiatList.get(i).getSymbol();
        }

        String currentFiat = chipFiatSelect.getText().toString();
        int currentFiatPosition = coinsViewModel.getFiatPosition(currentFiat);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Select Currency")
                .setSingleChoiceItems(items, currentFiatPosition, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        String selected = fiatList.get(selectedPosition).getSymbol();
                        chipFiatSelect.setText(selected);
                        coinsViewModel.setSelectedFiat(selected);
                        getAllCoinsApi(1, 20, selected, coinsViewModel.getSelectedSort(), coinsViewModel.getSelectedCryptoType());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showSortPopup(View v){
        HashMap<String, String> options = getSortOptions();

        List<String> items = new ArrayList<>();
        for(String key : options.keySet()){
            items.add(key);
        }
        String[] itemsArray = new String[items.size()];
        items.toArray(itemsArray);

        String currentSort = chipSortSelect.getText().toString();
        int currentSortPosition = coinsViewModel.getSortPosition(currentSort);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Select Sort")
                .setSingleChoiceItems(itemsArray, currentSortPosition, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        String key = items.get(selectedPosition);
                        chipSortSelect.setText(key);
                        String selectedSort = options.get(key);
                        coinsViewModel.setSelectedSort(selectedSort);
                        getAllCoinsApi(1, 20, coinsViewModel.getSelectedFiat(), selectedSort, coinsViewModel.getSelectedCryptoType());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showCryptoTypePopup(){
        List<String> items = getCryptoTypeOptions();

        String[] itemsArray = new String[items.size()];
        items.toArray(itemsArray);

        String currentCryptoType = chipCryptoTypeSelect.getText().toString().toLowerCase();
        int currentCryptoTypePosition = coinsViewModel.getCryptoTypePosition(currentCryptoType);


        new MaterialAlertDialogBuilder(this)
                .setTitle("Select Sort")
                .setSingleChoiceItems(itemsArray, currentCryptoTypePosition, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        String selectedCryptoType = items.get(selectedPosition);
                        chipCryptoTypeSelect.setText(selectedCryptoType.substring(0,1).toUpperCase() + selectedCryptoType.substring(1).toLowerCase());
                        coinsViewModel.setSelectedCryptoType(selectedCryptoType);
                        getAllCoinsApi(1, 20, coinsViewModel.getSelectedFiat(), coinsViewModel.getSelectedSort(), selectedCryptoType);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public void onCoinClick(int position) {
        Toast.makeText(getApplicationContext(), "Clicked " + position, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, CoinDetailsActivity.class);
        Gson gson = new Gson();
        String coinGson = gson.toJson(coinsRecyclerViewAdapter.getSelectedCoin(position));
        intent.putExtra("coin", coinGson);
        startActivity(intent);

    }
}