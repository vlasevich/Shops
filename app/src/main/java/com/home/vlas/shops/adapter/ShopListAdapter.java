package com.home.vlas.shops.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.home.vlas.shops.R;
import com.home.vlas.shops.dto.ShopDTO;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.RemindViewHolder> {
    private List<ShopDTO> data;

    public ShopListAdapter(List<ShopDTO> data) {
        this.data = data;
    }

    @Override
    public RemindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new RemindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RemindViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class RemindViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView title;

        public RemindViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("CLICK");
                }
            });
        }
    }

}
