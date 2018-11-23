package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class basketBuyerNameList extends AppCompatActivity {
    String productID, product, date;
    Query callbuyerlist, callbuyerName;
    ArrayList<String> buyerID = new ArrayList<String>();
    ArrayList<String> buyerName = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();
    ArrayList<String> sellerID = new ArrayList<String>();
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_buyer_name_list);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String IDKey = sp.getString("IDKey", "0");

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("รายชื่อผู้สั่งซื้อ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        productID = extras.getString("productID");
        product = extras.getString("product");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (product.matches("market")) {
            callbuyerlist = database.getReference().child("Order").child("marketProduct").orderByChild("productID").equalTo(productID);
            callbuyerlist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    buyerID.clear();
                    buyerName.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String k = d.getKey();
                        key.add(k);
                        OrderIdMar m = d.getValue(OrderIdMar.class);
                        buyerID.add(m.getBuyerID());
                    }
                    for (int position = 0; position < buyerID.size(); position++) {
                        callbuyerName = database.getReference().child("Users").child(buyerID.get(position));
                        callbuyerName.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map map = (Map) dataSnapshot.getValue();
                                final String buyername = String.valueOf(map.get("name"));
                                buyerName.add(buyername);
                                String[] buyerNameString = new String[buyerName.size()];
                                buyerNameString = buyerName.toArray(buyerNameString);
                                final String[] finalBuyerArray = buyerNameString;
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(basketBuyerNameList.this, android.R.layout.simple_list_item_1, android.R.id.text1, finalBuyerArray);
                                ListView listView = findViewById(R.id.listview);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent next = new Intent(basketBuyerNameList.this, chatNew.class);
                                        next.putExtra("orderid", key.get(position));
                                        next.putExtra("buyerName",finalBuyerArray[position]);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            callbuyerlist = database.getReference().child("Order").child("preorderProduct").orderByChild("productID").equalTo(productID);
            callbuyerlist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    buyerID.clear();
                    buyerName.clear();
                    sellerID.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String k = d.getKey();
                        key.add(k);
                        OrderIdMar m = d.getValue(OrderIdMar.class);
                        buyerID.add(m.getBuyerID());
                        sellerID.add(m.getSellerID());
                    }
                    for (int count = 0; count < sellerID.size(); count++) {
                        if (!sellerID.get(count).matches(IDKey)) {
                            buyerID.remove(count);
                            key.remove(count);
                            buyerID.remove(count);
                        }
                    }
                    for (int position = 0; position < buyerID.size(); position++) {
                        callbuyerName = database.getReference().child("Users").child(buyerID.get(position));
                        callbuyerName.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map map = (Map) dataSnapshot.getValue();
                                final String buyername = String.valueOf(map.get("name"));
                                buyerName.add(buyername);
                                String[] buyerNameString = new String[buyerName.size()];
                                buyerNameString = buyerName.toArray(buyerNameString);
                                final String[] finalBuyerArray = buyerNameString;
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(basketBuyerNameList.this, android.R.layout.simple_list_item_1, android.R.id.text1, finalBuyerArray);
                                ListView listView = findViewById(R.id.listview);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent next = new Intent(basketBuyerNameList.this, chatNew.class);
                                        next.putExtra("orderid", key.get(position));
                                        next.putExtra("buyerName",finalBuyerArray[position]);
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.clickbacktwice, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
