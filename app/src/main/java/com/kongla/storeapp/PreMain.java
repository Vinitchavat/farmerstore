package com.kongla.storeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PreMain extends AppCompatActivity {
    public DatabaseReference callPre;
    CustomAdapterForPreorder adapterCus;
    ArrayList<String> clubkey = new ArrayList<>();
    String getK;
    int countKey;

    BottomNavigationView navigation;

    /* *** Bottom Navigation *** */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_preorder:
                    /* ***Selected Activity NO Intent*** */
                    return true;
                case R.id.navigation_order:
                    i = new Intent(getApplicationContext(), basketMain.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_profile:
                    i = new Intent(getApplicationContext(), profileMain.class);
                    startActivity(i);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.premain);

        /* *** Set Selected Menu *** */
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_preorder);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        callPre = database.getReference().child("product").child("preorderProduct");
        callPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countKey = 0;
                clubkey.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    getK = d.getKey();
                    clubkey.add(getK);
                    countKey = countKey + 1;
                }
                String[] mStringArray = new String[clubkey.size()];
                mStringArray = clubkey.toArray(mStringArray);
                final String[] finalMStringArray = mStringArray;
                ListView list = (ListView) findViewById(R.id.list);
                adapterCus = new CustomAdapterForPreorder(getApplicationContext(),clubkey);
                list.setAdapter(adapterCus);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent next = new Intent(PreMain.this, PreListDay.class);
                        next.putExtra("DayPre", finalMStringArray[position]);
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
