package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Queue;

public class basketMandP extends AppCompatActivity {
    Intent i;

    SharedPreferences sp;

    public DatabaseReference callbuylistx;

    String newString;
    ArrayList<String> productID = new ArrayList<String>();
    ArrayList<String> farmID = new ArrayList<String>();
    ArrayList<String> date = new ArrayList<String>();
    ArrayList<String> dateshow = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();
    Query callbuylist;
    String fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_mand_p);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String IDKey = sp.getString("IDKey", "0");
        final String user = sp.getString("Status", "none");

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(user.matches("seller")){
            actionBar.setTitle("รายการสินค้าที่ถูกสั่งซื้อ");
        }
        else if(user.matches("buyer")){
            actionBar.setTitle("รายการสินค้าที่สั่งซื้อ");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        newString = extras.getString("text");

        if (newString.equals("Mar")) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            if (user.matches("buyer")) {
                callbuylist = database.getReference().child("Order").child("marketProduct").orderByChild("buyerID").equalTo(IDKey);
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
                        CustomAdapShowBas customAdapShowBas = new CustomAdapShowBas(getApplicationContext(), productID, farmID,user);
                        ListView listviewMarket = (ListView) findViewById(R.id.listviewM);
                        listviewMarket.setAdapter(customAdapShowBas);
                        listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                final Intent next = new Intent(basketMandP.this, chatNew.class);
                                next.putExtra("farmID", farmID.get(position) );
                                next.putExtra("orderid", key.get(position));
                                startActivity(next);
                            }

                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            } else {
                callbuylist = database.getReference().child("Order").child("marketProduct").orderByChild("sellerID").equalTo(IDKey);
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
                        for (int count = 0; count < productID.size(); count++) {
                            for (int countIn = count + 1; countIn < productID.size(); countIn++) {
                                if (productID.get(count).matches(productID.get(countIn))) {
                                    productID.remove(countIn);
                                    farmID.remove(countIn);
                                    key.remove(count);
                                }
                            }
                        }
                        CustomAdapShowBas customAdapShowBas = new CustomAdapShowBas(getApplicationContext(), productID, farmID,user);
                        ListView listviewMarket = (ListView) findViewById(R.id.listviewM);
                        listviewMarket.setAdapter(customAdapShowBas);
                        listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent next = new Intent(basketMandP.this, basketBuyerNameList.class);
                                next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                next.putExtra("productID", productID.get(position));
                                next.putExtra("product", "market");
                                startActivity(next);
                            }

                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        } else {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            if (user.matches("buyer")) {
                callbuylist = database.getReference().child("Order").child("preorderProduct").orderByChild("buyerID").equalTo(IDKey);
                callbuylist.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        date.clear();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            OrderIdPre m = d.getValue(OrderIdPre.class);
                            date.add(m.getDate());
                        }
                        for (int count = 0; count < date.size(); count++) {
                            for (int countIn = count + 1; countIn < date.size(); countIn++) {
                                if (date.get(count).matches(date.get(countIn))) {
                                    date.remove(countIn);
                                }
                            }
                        }
                        for (int count = 0; count < date.size(); count++) {
                            for (int countIn = count + 1; countIn < date.size(); countIn++) {
                                if (date.get(count).matches(date.get(countIn))) {
                                    date.remove(countIn);
                                }
                            }
                        }
                        for(int position=0 ; position<date.size();position++) {
                            dateshow.add(setDate(date.get(position)));
                        }

                        String[] mStringDate = new String[dateshow.size()];
                        mStringDate = dateshow.toArray(mStringDate);
                        final String[] finalMStringDate = mStringDate;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(basketMandP.this, android.R.layout.simple_list_item_1, android.R.id.text1, finalMStringDate);
                        ListView listviewMarket = (ListView) findViewById(R.id.listviewM);
                        listviewMarket.setAdapter(adapter);
                        listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent next = new Intent(basketMandP.this, basketPrePro.class);
                                next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                next.putExtra("day", date.get(position));
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
                callbuylist = database.getReference().child("Order").child("preorderProduct").orderByChild("sellerID").equalTo(IDKey);
                callbuylist.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        date.clear();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            OrderIdPre m = d.getValue(OrderIdPre.class);
                            date.add(m.getDate());
                        }
                        for (int count = 0; count < date.size(); count++) {
                            for (int countIn = count + 1; countIn < date.size(); countIn++) {
                                if (date.get(count).matches(date.get(countIn))) {
                                    date.remove(countIn);
                                }
                            }
                        }
                        for(int position=0 ; position<date.size();position++) {
                            dateshow.add(setDate(date.get(position)));
                        }
                        String[] mStringDate = new String[dateshow.size()];
                        mStringDate = dateshow.toArray(mStringDate);
                        final String[] finalMStringDate = mStringDate;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(basketMandP.this, android.R.layout.simple_list_item_1, android.R.id.text1, finalMStringDate);
                        ListView listviewMarket = (ListView) findViewById(R.id.listviewM);
                        listviewMarket.setAdapter(adapter);
                        listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent next = new Intent(basketMandP.this, basketPrePro.class);
                                next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                next.putExtra("day", date.get(position));
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
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    public String setDate(String date){
        String d = date.substring(date.lastIndexOf("-")+1);
        String m = date.substring(date.indexOf("-")+1,date.lastIndexOf("-"));
        String month;
        String y = date.substring(0,date.indexOf("-"));
        switch (m){
            case "1": month = "มกราคม"; break;
            case "2": month = "กุมภาพันธ์"; break;
            case "3": month = "มีนาคม"; break;
            case "4": month = "เมษายน"; break;
            case "5": month = "พฤษภาคม"; break;
            case "6": month = "มิถุนายน"; break;
            case "7": month = "กรกฎาคม"; break;
            case "8": month = "สิงหาคม"; break;
            case "9": month = "กันยายน"; break;
            case "10": month = "ตุลาคม"; break;
            case "11": month = "พฤศจิกายน"; break;
            case "12": month = "ธันวาคม"; break;
            default: month = m;
        }
        return d+" "+ month + " " + y;
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
