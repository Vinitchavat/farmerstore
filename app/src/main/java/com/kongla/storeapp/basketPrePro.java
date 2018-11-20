package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class basketPrePro extends AppCompatActivity {
    String day;
    SharedPreferences sp;

    ArrayList<String> productID = new ArrayList<String>();
    ArrayList<String> farmID = new ArrayList<String>();
//    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_pre_pro);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String IDKey = sp.getString("IDKey", "0");

        Bundle extras = getIntent().getExtras();
        day = extras.getString("day");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query callbuylist = database.getReference().child("Order").child("preorderProduct").orderByChild("buyerID").equalTo(IDKey);
        callbuylist.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                key.clear();
                farmID.clear();
                productID.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    key.add(d.getKey());
                    OrderIdMar m = d.getValue(OrderIdMar.class);
                    productID.add(m.productID);
                    farmID.add(m.getFarmID());
                }
                CustomAdapShowBasPre customAdapShowBasPre = new CustomAdapShowBasPre(getApplicationContext(),day, productID, farmID);
                ListView listviewMarket = (ListView) findViewById(R.id.listview);
                listviewMarket.setAdapter(customAdapShowBasPre);
                listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent next = new Intent(basketPrePro.this, chatNew.class);
                        next.putExtra("orderid", key.get(position));
                        startActivity(next);
                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
