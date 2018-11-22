package com.kongla.storeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CustomAdapShowBasPre extends BaseAdapter {
    Context mContext;
    String fruitName, price, productName, quantity, unitPro;
    int draw;
    ArrayList<String> productID = new ArrayList<String>();
    ArrayList<String> farmID = new ArrayList<String>();
    String day,status;



    public DatabaseReference calllistMar,callfarm;


    public CustomAdapShowBasPre(Context context,String day ,ArrayList<String> productID,ArrayList<String> farmID ,String status) {
        this.mContext = context;
        this.productID = productID;
        this.farmID = farmID;
        this.day = day;
        this.status=status;
    }

    @Override
    public int getCount() {
        return productID.size();
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
        view = mInflater.inflate(R.layout.showlistpre, parent, false);
        final ImageView imageView = view.findViewById(R.id.ImgPro);
        final int[] fruit = {R.drawable.mangosteen, R.drawable.longan, R.drawable.orange, R.drawable.pineapple, R.drawable.rambutan, R.drawable.durian};
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        calllistMar = database.getReference().child("product").child("preorderProduct").child(day).child(productID.get(position));
        calllistMar.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map map = (Map) dataSnapshot.getValue();
                fruitName = String.valueOf(map.get("fruitName"));
                price = String.valueOf(map.get("price"));
                productName = String.valueOf(map.get("productName"));
                quantity = String.valueOf(map.get("quantity"));
                unitPro = String.valueOf(map.get("unit"));
                if (fruitName.matches("ทุเรียน")) {
                    imageView.setImageResource(fruit[5]);
                } else if (fruitName.matches("ลำไย")) {
                    imageView.setImageResource(fruit[1]);
                } else if (fruitName.matches("แตงโม")) {
                    imageView.setImageResource(fruit[3]);
                } else if (fruitName.matches("มังคุด")) {
                    imageView.setImageResource(fruit[0]);
                } else if (fruitName.matches("เงาะ")) {
                    imageView.setImageResource(fruit[4]);
                } else {
                    imageView.setImageResource(fruit[2]);
                }
                TextView textView1 =view.findViewById(R.id.showName);
                TextView textView2 =view.findViewById(R.id.showPriceandUnit);
                textView1.setText("ชื่อ : "+fruitName+" "+productName);
                textView2.setText("ราคา : "+price+" บาท"+" ต่อ "+unitPro);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(status.matches("buyer")) {
            callfarm = database.getReference().child("farmer").child(farmID.get(position));
            callfarm.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map map = (Map) dataSnapshot.getValue();
                    String farmname = String.valueOf(map.get("farmName"));

                    TextView textView3 = view.findViewById(R.id.showFarmName);
                    textView3.setText("ผู้ขาย : " + farmname);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return view;
    }
}
