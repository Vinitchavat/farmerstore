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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreListDay extends AppCompatActivity {
    public DatabaseReference callDataPre;
    String order;
    GetData getData;
    ArrayList<String> fruitName = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_list_day);

        Bundle extras = getIntent().getExtras();
        order = extras.getString("DayPre");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        callDataPre = database.getReference().child("product").child("preorderProduct").child(order);
        callDataPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    getData = d.getValue(GetData.class);
                    fruitName.add(getData.getFruitName());
                }
                String[] mStringArray= new String[fruitName.size()];
                mStringArray = fruitName.toArray(mStringArray);
                final String[] finalMStringArray = mStringArray;
                ListView list = (ListView) findViewById(R.id.list2);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PreListDay.this,android.R.layout.simple_list_item_1, android.R.id.text1,finalMStringArray);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent next = new Intent(PreListDay.this, show.class);
                        next.putExtra("DayPre", order);
                        next.putExtra("DayPreFruit", fruitName.get(position));
                        startActivity(next);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        ListView list = findViewById(R.id.list2);
//        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,mStringArray);
//        list.setAdapter(adapter);
    }
}
