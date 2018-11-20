package com.kongla.storeapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class profileBuyerSetting extends AppCompatActivity {
    ArrayList<String> item1 = new ArrayList<>();
    ArrayList<String> item2 = new ArrayList<>();
    FirebaseDatabase database;
    Query query;
    DatabaseReference databaseReference, dref, dref1;
    String data, data2, data3, data4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitty_profile_buyersetting);

        /* ** Action Bar ** */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ตั้งค่าผู้ขาย");

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        query = databaseReference.child("Order").orderByChild("buyerID").equalTo("YQlrwQVc6zOPbIVPu15ir1AXXjg1");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String status = d.child("orderStatus").getValue(String.class);
                    if (status.equals("Finish")) {
                        data = d.child("productID").getValue(String.class);
                        dref1 = database.getReference().child("product").child(data);
                        dref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                data = dataSnapshot.child("productName").getValue(String.class);
                                data2 = dataSnapshot.child("productSpecies").getValue(String.class);
                                if (data2 == null) {
                                    data2 = "";
                                }
                                item1.add(data + data2);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                item1.add("Error");
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
                                item2.add("Error");
                            }
                        });
                    } else {
                        item1.add("Error");
                    }
                }
                ListView list2 = (ListView) findViewById(R.id.list);
                AdapterPH adapter = new AdapterPH(profileBuyerSetting.this, item1, item2);
                list2.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                item1.add("Error3");
            }
        });
    }
}
