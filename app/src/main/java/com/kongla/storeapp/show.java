package com.kongla.storeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class show extends AppCompatActivity {
    String day,order;
    GetData getData;
    int draw;
    ArrayList<String> farmID = new ArrayList<String>();
    ArrayList<String> fruitName = new ArrayList<String>();
    ArrayList<Integer> price = new ArrayList<Integer>();
    ArrayList<String> productName = new ArrayList<String>();
    ArrayList<Integer> quantity = new ArrayList<Integer>();
    ArrayList<String> unitPro = new ArrayList<String>();
    ArrayList<String> allKey = new ArrayList<String>();
    CustomAdapShowPre customAdapShowPre;
    public DatabaseReference callDataPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("รายการสินค้า");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        day = extras.getString("DayPre");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        callDataPre = database.getReference().child("product").child("preorderProduct").child(day);
        callDataPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allKey.clear();
                farmID.clear();
                fruitName.clear();
                productName.clear();
                price.clear();
                quantity.clear();
                unitPro.clear();
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
                draw = 1;

                ListView listView =findViewById(R.id.listShowPre);
                customAdapShowPre = new CustomAdapShowPre(getApplicationContext(),draw,farmID,fruitName,price,productName,quantity,unitPro);
                listView.setAdapter(customAdapShowPre);

                //send Data to next page here

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent next = new Intent(show.this, ProductActivity.class);
                        next.putExtra("product", "preorderProduct");
                        next.putExtra("farmname", "preorderProduct");
                        next.putExtra("DayPre", day);
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
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
