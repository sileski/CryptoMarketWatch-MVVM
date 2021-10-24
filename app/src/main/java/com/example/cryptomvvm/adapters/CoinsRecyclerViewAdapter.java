package com.example.cryptomvvm.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.cryptomvvm.R;
import com.example.cryptomvvm.models.CoinModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Set;

public class CoinsRecyclerViewAdapter extends RecyclerView.Adapter<CoinsRecyclerViewAdapter.ViewHolder> {
    private List<CoinModel> mCoins;
    private static ClickListener clickListener;
    private Context context;

    public CoinsRecyclerViewAdapter(Context context, ClickListener clickListener){
        this.context = context;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set<String> key = mCoins.get(position).getQuote().keySet();
        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();
        priceFormat.setMaximumFractionDigits(2);
        priceFormat.setCurrency(Currency.getInstance(key.iterator().next()));

        DecimalFormat percentageFormat = new DecimalFormat("0.00'%'");
        holder.coinShortName.setText(mCoins.get(position).getSymbol());
        holder.coinName.setText(mCoins.get(position).getName());
        holder.coinPrice.setText(priceFormat.format(mCoins.get(position).getQuote().get(key.iterator().next()).getPrice()));
        holder.coinPercentageChange.setText(percentageFormat.format(mCoins.get(position).getQuote().get(key.iterator().next()).getPercent_change_24h()));
        double percent = mCoins.get(position).getQuote().get(key.iterator().next()).getPercent_change_24h();
        if(percent > 0){
            holder.coinPercentageChange.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if(percent < 0){
            holder.coinPercentageChange.setTextColor(context.getResources().getColor(R.color.red));
        }

        String imageUrl = "https://cryptoicon-api.vercel.app/api/icon/" + mCoins.get(position).getSymbol().toLowerCase();
        Glide.with(holder.itemView)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(new ColorDrawable(Color.BLACK))
                .into(holder.coinImage);

    }

    @Override
    public int getItemCount() {
        if(mCoins != null) {
            return mCoins.size();
        }
        return 0;
    }

    public void setCoins(List<CoinModel> mCoins){
        this.mCoins = mCoins;
        notifyDataSetChanged();
    }

    public CoinModel getSelectedCoin(int position){
        if(mCoins != null && mCoins.size() > 0){
            return mCoins.get(position);
        }
        return null;
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView coinShortName;
        TextView coinName;
        TextView coinPrice;
        TextView coinPercentageChange;
        ImageView coinImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            coinShortName = itemView.findViewById(R.id.coinShortName);
            coinName = itemView.findViewById(R.id.coinName);
            coinPrice = itemView.findViewById(R.id.coinPrice);
            coinPercentageChange = itemView.findViewById(R.id.coinPercentageChange);
            coinImage = itemView.findViewById(R.id.coinImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onCoinClick(getAdapterPosition());
        }
    }

    public interface ClickListener{
        void onCoinClick(int position);
    }
}



