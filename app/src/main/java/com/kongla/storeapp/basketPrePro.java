package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class basketPrePro extends AppCompatActivity {
    String day;
    SharedPreferences sp;

    ArrayList<String> productID = new ArrayList<String>();
    ArrayList<String> farmID = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();
    ArrayList<String> buyerID = new ArrayList<String>();
    ArrayList<String> sellerID = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_pre_pro);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String KKK = sp.getString("IDKey", "0");
        final String status = sp.getString("Status", "none");

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(status.matches("seller")){
            actionBar.setTitle("รายการสินค้าที่ถูกสั่งซื้อ");
        }
        else if(status.matches("buyer")){
            actionBar.setTitle("รายการสินค้าที่สั่งซื้อ");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        day = extras.getString("day");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (status.matches("buyer")) {
            Query callbuylist = database.getReference().child("Order").child("preorderProduct").orderByChild("date").equalTo(day);
            callbuylist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    key.clear();
                    farmID.clear();
                    productID.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        OrderIdMar m = d.getValue(OrderIdMar.class);
                        String buyer = m.getBuyerID();
                        if (buyer.matches(KKK)) {
                            key.add(d.getKey());
                            productID.add(m.productID);
                            farmID.add(m.getFarmID());
                            buyerID.add(m.getBuyerID());
                        }
                    }

                    CustomAdapShowBasPre customAdapShowBasPre = new CustomAdapShowBasPre(getApplicationContext(), day, productID, farmID, status);
                    ListView listviewMarket = (ListView) findViewById(R.id.listview);
                    listviewMarket.setAdapter(customAdapShowBasPre);
                    listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent next = new Intent(basketPrePro.this, chatNew.class);
                            next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            next.putExtra("farmID", farmID.get(position));
                            next.putExtra("orderid", key.get(position));
                            next.putExtra("statusPro","preorderProduct");
                            startActivity(next);
                        }

                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        else {
            Query callbuylist = database.getReference().child("Order").child("preorderProduct").orderByChild("date").equalTo(day);
            callbuylist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    farmID.clear();
                    productID.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        OrderIdMar m = d.getValue(OrderIdMar.class);
                        String seller = m.getSellerID();
                        if (seller.matches(KKK)) {
                            productID.add(m.productID);
                            farmID.add(m.getFarmID());
                            sellerID.add(m.getSellerID());
                        }
                    }
                    for (int count = 0; count < productID.size(); count++) {
                        for (int countIn = count + 1; countIn < productID.size(); countIn++) {
                            if (productID.get(count).matches(productID.get(countIn))) {
                                productID.remove(countIn);
                                farmID.remove(countIn);
                                countIn--;
                            }
                        }
                    }
                    CustomAdapShowBasPre customAdapShowBasPre = new CustomAdapShowBasPre(getApplicationContext(), day, productID, farmID, status);
                    ListView listviewMarket = (ListView) findViewById(R.id.listview);
                    listviewMarket.setAdapter(customAdapShowBasPre);
                    listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent next = new Intent(basketPrePro.this, basketBuyerNameList.class);
                            next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            next.putExtra("product", "preorder");
                            next.putExtra("productID", productID.get(position));
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
    @Override
    public boolean onSupportNavigateUp(){
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
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
