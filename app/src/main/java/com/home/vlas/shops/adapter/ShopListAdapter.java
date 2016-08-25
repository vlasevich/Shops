package com.home.vlas.shops.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.vlas.shops.R;
import com.home.vlas.shops.activity.ShopActivity;
import com.home.vlas.shops.model.Shop;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.RemindViewHolder> {
    private static List<Shop> data;

    public ShopListAdapter(List<Shop> data) {
        ShopListAdapter.data = data;
    }

    @Override
    public RemindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RemindViewHolder holder, int position) {
        holder.title.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (data != null) {
        return data.size();
        } else return 0;
    }

    public static class RemindViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        TextView title;

        public RemindViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);
        }

        private void openShopActivity(View view, int position) {
            //push intent with shop's data to activity
            Intent myIntent = new Intent(view.getContext(), ShopActivity.class);
            myIntent.putExtra("SHOP_ID", position);
            myIntent.putExtra("SHOP_NAME", data.get(position).getName());
            myIntent.putExtra("SHOP_ADDRESS", data.get(position).getAddress());
            myIntent.putExtra("SHOP_PHONE", data.get(position).getPhone());
            myIntent.putExtra("SHOP_WEBSITE", data.get(position).getWebsite());
            view.getContext().startActivity(myIntent);
        }

        @Override
        public void onClick(View view) {
            openShopActivity(view, getAdapterPosition());
        }
    }
}
