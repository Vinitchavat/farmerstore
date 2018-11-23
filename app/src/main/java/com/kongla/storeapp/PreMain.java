package com.kongla.storeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    return true;
                case R.id.navigation_preorder:
                    /* ***Selected Activity NO Intent*** */
                    return true;
                case R.id.navigation_order:
                    i = new Intent(getApplicationContext(), basketMain.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    return true;
                case R.id.navigation_profile:
                    i = new Intent(getApplicationContext(), profileMain.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                clubkey.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    getK = d.getKey();
                    if(getTimeStamp(getK)>=getTimeStamp("now")){
                        clubkey.add(getK);
                    }
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
                        Intent next = new Intent(PreMain.this, show.class);
                        next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
    public long getTimeStamp(String date){
        String now;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",new Locale("TH"));
        if (date.matches("now")){
            Calendar calendar = Calendar.getInstance();
            now = dateFormat.format(calendar.getTime());
        }
        else {
            int y = Integer.parseInt(date.substring(0,4))-543;
            now = String.valueOf(y)+date.substring(4);
        }
        try {
            Date d = (Date)dateFormat.parse(now);
            return d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
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
