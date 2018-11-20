package com.kongla.storeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterPH extends BaseAdapter {
    ArrayList<String> productName;
    Context mContext;
    ArrayList<String> farmName;

    public AdapterPH(Context context, ArrayList<String> productName, ArrayList<String> farmName) {
        this.productName = productName;
        this.farmName = farmName;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return productName.size();
    }

    @Override
    public String getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.listviewhistory, viewGroup, false);
            holder = new ViewHolder();
            view.setTag(holder);
        }
        holder.tv_info = (TextView) view.findViewById(R.id.item1);
        holder.tv_info.setText(productName.get(i));
        holder.tv_info = (TextView) view.findViewById(R.id.item2);
        holder.tv_info.setText(farmName.get(i));
        return view;
    }
}
