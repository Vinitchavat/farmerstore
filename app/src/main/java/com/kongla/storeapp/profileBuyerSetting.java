package com.kongla.storeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileBuyerSetting extends AppCompatActivity {
    private ArrayList<String> item1 = new ArrayList<>();
    private ArrayList<String> item2 = new ArrayList<>();
    private FirebaseDatabase database;
    private Query query;
    private DatabaseReference databaseReference, dref, dref1;
    private String data, data2, data3, data4;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitty_profile_buyersetting);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        editor = sp.edit();
        String userID = sp.getString("IDKey", "0");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        TextView status = findViewById(R.id.textChangeMode);
        String s = status.getText().toString();
        if (s.matches("สินค้าสั่งจอง")) {
            query = databaseReference.child("Order").child("preorderProduct")
                    .orderByChild("buyerID").equalTo(userID);
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item1.clear();item2.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String status = d.child("orderStatus").getValue(String.class);
                    if (status.equals("finish")) {
                        data = d.child("productID").getValue(String.class);
                        data2 = d.child("date").getValue(String.class);
                        dref1 = database.getReference().child("product").child("preorderProduct").child(data2).child(data);
                        dref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String fname = dataSnapshot.child("fruitName").getValue(String.class);
                                String pname = dataSnapshot.child("productName").getValue(String.class);
                                item1.add(fname+pname);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        data3 = d.child("farmID").getValue(String.class);
                        dref = database.getReference().child("farmer").child(data3);
                        dref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                data4 = dataSnapshot.child("farmName").getValue(String.class);
                                item2.add(data4);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        ListView list2 = (ListView) findViewById(R.id.list);
                        AdapterPH adapter = new AdapterPH(profileBuyerSetting.this, item1, item2);
                        list2.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                item1.add("Error3");
            }
        });
    }
}
