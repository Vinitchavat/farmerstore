package com.kongla.storeapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ProgressDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileBuyerSettingPreorder extends AppCompatActivity {
    private ArrayList<String> item1 = new ArrayList<>();
    private ArrayList<String> item2 = new ArrayList<>();
    private ArrayList<String> item3 = new ArrayList<>();
    private FirebaseDatabase database;
    private Query query;
    private DatabaseReference databaseReference, dref, dref1;
    private String data, data2, data3, data4;
    SharedPreferences sp;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitty_profile_buyersetting);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String userID = sp.getString("IDKey", "0");
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("กำลังโหลข้อมูล");
        loadingBar.show();

        TextView header = findViewById(R.id.header);
        header.setText("ประวัติสินค้าสั่งจอง");

        query = databaseReference.child("Order").child("preorderProduct")
                .orderByChild("buyerID").equalTo(userID);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                item1.clear();
                item2.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String status = d.child("orderStatus").getValue(String.class);
                    if (status.matches("finish")) {
                        data = d.child("productID").getValue(String.class);
                        data2 = d.child("farmID").getValue(String.class);
                        String date = d.child("date").getValue(String.class);
                        item1.add(data);
                        item2.add(data2);
                        item3.add(date);
                    }
                }
                ListView list2 = (ListView) findViewById(R.id.list);
                AdapterPH adapter = new AdapterPH(profileBuyerSettingPreorder.this, item1, item2, item3);
                list2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                item1.add("Error3");
            }
        });
        loadingBar.dismiss();
    }
}
