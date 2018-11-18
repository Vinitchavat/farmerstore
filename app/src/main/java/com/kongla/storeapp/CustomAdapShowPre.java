package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapShowPre extends BaseAdapter {
    Context mContext;
    int draw;
    ArrayList<String> farmID = new ArrayList<String>();
    ArrayList<String> fruitName = new ArrayList<String>();
    ArrayList<Integer> price = new ArrayList<Integer>();
    ArrayList<String> productName = new ArrayList<String>();
    ArrayList<Integer> quantity = new ArrayList<Integer>();
    ArrayList<String> unitPro = new ArrayList<String>();
    public DatabaseReference calllistPre;


    public CustomAdapShowPre(Context context,int draw,ArrayList<String> farmID, ArrayList<String> fruitName,ArrayList<Integer> price,ArrayList<String> productName,ArrayList<Integer> quantity,ArrayList<String> unitPro){
        this.mContext = context;
        this.draw= draw;
        this.farmID=farmID;
        this.fruitName = fruitName;
        this.price = price;
        this.productName=productName;
        this.quantity=quantity;
        this.unitPro=unitPro;
    }

    @Override
    public int getCount() {
        return fruitName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final View view;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.showlistpre,parent,false);

        ImageView imageView = view.findViewById(R.id.ImgPro);
        imageView.setImageResource(draw);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        calllistPre = database.getReference().child("farmer").child(farmID.get(position));
        calllistPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();

                String farmname = String.valueOf(map.get("farmName"));
                TextView textView3 = view.findViewById(R.id.showFarmName);
                textView3.setText("ผู้ขาย : "+farmname);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        TextView textView1 =view.findViewById(R.id.showName);
        TextView textView2 =view.findViewById(R.id.showPriceandUnit);
        textView1.setText("ชื่อ : "+fruitName.get(position)+" "+productName.get(position));
        textView2.setText("ราคา : "+price.get(position)+" บาท"+" ต่อ "+unitPro.get(position));

        return view;
    }
}
