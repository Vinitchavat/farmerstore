package com.kongla.storeapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    CustomAdapShowMar customAdapShowMar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mar_list_fruit);

        Bundle extras = getIntent().getExtras();
        order = extras.getString("MarFruit");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query callDataPre = database.getReference().child("product").child("marketProduct").orderByChild("fruitName").equalTo(order);
        callDataPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    getData = d.getValue(GetData.class);
                    farmID.add(getData.getFarmID());
                    fruitName.add(getData.getFruitName());
                    price.add(getData.getPrice());
                    productName.add(getData.getProductName());
                    quantity.add(getData.getQuantity());
                    unitPro.add(getData.getUnitPro());
                }
                if(fruitName.get(0).matches("ทุเรียน")){
                    draw = R.drawable.durian;
                }
                else if(fruitName.get(0).matches("มังคุด")){
                    draw = R.drawable.mangosteen;
                }
                else if(fruitName.get(0).matches("เงาะ")){
                    draw = R.drawable.rambutan;
                }
                else if(fruitName.get(0).matches("ส้ม")){
                    draw = R.drawable.orange;
                }
                else  if(fruitName.get(0).matches("แตงโม")){
                    draw = R.drawable.pineapple;
                }
                else{
                    draw = R.drawable.longan;
                }
                ListView listView =findViewById(R.id.listMar);
                customAdapShowMar = new CustomAdapShowMar(getApplicationContext(),draw,farmID,fruitName,price,productName,quantity,unitPro);
                listView.setAdapter(customAdapShowMar);

                //send Data to next page here

//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Intent next = new Intent(show.this, page.class);
//                        next.putExtra("DayPre", order);
//                        next.putExtra("DayPreFruit", fruitName.get(position));
//                        startActivity(next);
//                    }
//                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
