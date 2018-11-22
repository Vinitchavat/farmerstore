package com.kongla.storeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterPH extends BaseAdapter {
    ArrayList<String> productID;
    Context mContext;
    ArrayList<String> farmID;
    ArrayList<String> date;

    public AdapterPH(Context context, ArrayList<String> productID,ArrayList<String> farmID, ArrayList<String> date) {
        this.productID = productID;
        this.mContext = context;
        this.farmID = farmID;
        this.date = date;
    }

    @Override
    public int getCount() {
        return productID.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.listviewhistory, viewGroup, false);
            holder = new ViewHolder();
            view.setTag(holder);
        }
        final int j = i;
        final View fview = view;
        DatabaseReference databaseReference;
        if(productID!=null){
            if (date.get(i).matches("none")){
                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("product").child("marketProduct").child(productID.get(i));
            }
            else {
                databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("product").child("preorderProduct").child(date.get(i)).child(productID.get(i));
            }

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String fname = dataSnapshot.child("fruitName").getValue(String.class);
                    String pname = dataSnapshot.child("productName").getValue(String.class);
                    holder.tv_info = (TextView) fview.findViewById(R.id.item1);
                    holder.tv_info.setText(fname+pname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    holder.tv_info = (TextView) fview.findViewById(R.id.item1);
                    holder.tv_info.setText("Error");
                }
            });
            DatabaseReference dref = FirebaseDatabase.getInstance().getReference()
                    .child("farmer").child(farmID.get(i));
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String farmName = dataSnapshot.child("farmName").getValue(String.class);
                    holder.tv_info = (TextView) fview.findViewById(R.id.item2);
                    holder.tv_info.setText(farmName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        return view;
    }
}