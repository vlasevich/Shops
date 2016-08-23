package com.home.vlas.shops.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.home.vlas.shops.R;
import com.home.vlas.shops.model.Instrument;

import java.util.ArrayList;
import java.util.List;

public class InstrumentsListAdapter extends BaseAdapter {
    private List<Instrument> instList = new ArrayList<>();
    private Activity activity;
    private LayoutInflater layoutInflater;

    public InstrumentsListAdapter(Activity activity, List<Instrument> instList) {
        this.activity = activity;
        this.instList = instList;
    }

    @Override
    public int getCount() {
        if (instList != null) {
        return instList.size();
        } else
            return 0;

    }

    @Override
    public Object getItem(int i) {
        return instList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (layoutInflater == null) {
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null) {
            view = layoutInflater.inflate(R.layout.inst_list_row, null);
        }
/*        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }*/

        TextView id = (TextView) view.findViewById(R.id.instId);
        TextView brand = (TextView) view.findViewById(R.id.instBrand);
        TextView model = (TextView) view.findViewById(R.id.instModel);
        TextView image = (TextView) view.findViewById(R.id.instImage);
        TextView type = (TextView) view.findViewById(R.id.instType);
        TextView price = (TextView) view.findViewById(R.id.instPrice);
        TextView quantity = (TextView) view.findViewById(R.id.instQuantity);


        final Instrument inst = instList.get(i);

        id.setText(inst.getInstrument().getId().toString());
        brand.setText(inst.getInstrument().getModel());
        model.setText(inst.getInstrument().getBrand());
        image.setText(inst.getInstrument().getImageUrl());
        type.setText(inst.getInstrument().getType());
        price.setText(inst.getInstrument().getPrice().toString());
        quantity.setText(inst.getQuantity().toString());

        return view;
    }
}
