package com.kongla.storeapp;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MarListFruit extends AppCompatActivity {
    String order;
    GetData getData;
    int draw;
    ArrayList<String> farmID = new ArrayList<String>();
    ArrayList<String> fruitName = new ArrayList<String>();
    ArrayList<Integer> price = new ArrayList<Integer>();
    ArrayList<String> productName = new ArrayList<String>();
    ArrayList<Integer> quantity = new ArrayList<Integer>();
    ArrayList<String> unitPro = new ArrayList<String>();
    ArrayList<String> allKey = new ArrayList<>();
    CustomAdapShowPre customAdapShowMar;
    Query callDataPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mar_list_fruit);

        Bundle extras = getIntent().getExtras();
        order = extras.getString("MarFruit");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(!order.matches("all")) {
            callDataPre= database.getReference().child("product").child("marketProduct").orderByChild("fruitName").equalTo(order);
        }
        else {
            callDataPre = database.getReference().child("product").child("marketProduct");
        }
        callDataPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allKey.clear();unitPro.clear();farmID.clear();fruitName.clear();
                price.clear();productName.clear();quantity.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String s = d.child("status").getValue(String.class);
                    if (s==null){
                        getData = d.getValue(GetData.class);
                        String key = d.getKey();
                        allKey.add(key);
                        farmID.add(getData.getFarmID());
                        fruitName.add(getData.getFruitName());
                        price.add(getData.getPrice());
                        productName.add(getData.getProductName());
                        quantity.add(getData.getQuantity());
                        unitPro.add(getData.getUnitPro());
                    }
                }

                for (int i=0; i<fruitName.size(); i++){
                    for (int j=i+1; j<fruitName.size(); j++){
                        if(fruitName.get(i).matches(fruitName.get(j))){
                            fruitName.add(i+1,fruitName.get(j));
                            allKey.add(i+1,allKey.get(j));
                            farmID.add(i+1,farmID.get(j));
                            price.add(i+1,price.get(j));
                            productName.add(i+1,productName.get(j));
                            quantity.add(i+1,quantity.get(j));
                            unitPro.add(i+1,unitPro.get(j));
                            fruitName.remove(j+1);
                            allKey.remove(j+1);
                            farmID.remove(j+1);
                            price.remove(j+1);
                            productName.remove(j+1);
                            quantity.remove(j+1);
                            unitPro.remove(j+1);
                            break;
                        }
                    }
                }
                int position =0;
                if(!order.matches("all")) {
                    if (fruitName.get(0).matches("ทุเรียน")) {
                        draw = R.drawable.durian;
                    } else if (fruitName.get(0).matches("สับปะรด")) {
                        draw = R.drawable.pineapple;
                    } else if (fruitName.get(0).matches("องุ่น")) {
                        draw = R.drawable.grape;
                    } else if (fruitName.get(0).matches("แก้วมังกร")) {
                        draw = R.drawable.dragonfruit;
                    } else if (fruitName.get(0).matches("ลิ้นจี่")) {
                        draw = R.drawable.lychee;
                    } else if (fruitName.get(0).matches("มังคุด")) {
                        draw = R.drawable.mangosteen;
                    } else if (fruitName.get(0).matches("เงาะ")) {
                        draw = R.drawable.rambutan;
                    } else if (fruitName.get(0).matches("ส้ม")) {
                        draw = R.drawable.orange;
                    } else if (fruitName.get(0).matches("มะม่วง")) {
                        draw = R.drawable.mango;
                    } else {
                        draw = R.drawable.longan;
                    }
                }
                else {
                    draw = 1;
                }
                ListView listView =findViewById(R.id.listMar);
                customAdapShowMar = new CustomAdapShowPre(getApplicationContext(),draw,farmID,fruitName,price,productName,quantity,unitPro);
                listView.setAdapter(customAdapShowMar);

                //send Data to next page here

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent next = new Intent(MarListFruit.this, ProductActivity.class);
                        next.putExtra("product", "marketProduct");
                        next.putExtra("key", allKey.get(position));
                        startActivity(next);
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
