package com.kongla.storeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class basketBuyerNameList extends AppCompatActivity {
    String productId,product,date;
    Query callbuyerlist,callbuyerName;
    ArrayList<String> buyerID = new ArrayList<String>();
    ArrayList<String> buyerName = new ArrayList<String>();
    ArrayList<String> key = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_buyer_name_list);

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("รายชื่อผู้สั่งซื้อ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        productId = extras.getString("productID");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        callbuyerlist = database.getReference().child("Order").child("marketProduct").orderByChild("productID").equalTo(productId);
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
                for(int position = 0 ; position <buyerID.size() ; position++){
                callbuyerName = database.getReference().child("Users").child(buyerID.get(position));
                    callbuyerName.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map map = (Map) dataSnapshot.getValue();
                            String buyername = String.valueOf(map.get("name"));
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
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
