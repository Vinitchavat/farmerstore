package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    public DatabaseReference callMarket;
    Intent i;
    GetData getData;
    ArrayList<String> fruitDraw = new ArrayList<String>();
    ArrayList<Integer> fruitRec = new ArrayList<Integer>();
    int Fcount=0;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    /* ***Selected Activity NO Intent*** */
                    return true;

                case R.id.navigation_preorder:
                    i = new Intent(getApplicationContext(), PreMain.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
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
        setContentView(R.layout.activity_home);

        final ImageView img1 = findViewById(R.id.img1);
        ImageView img2 = findViewById(R.id.img2);
        ImageView img3 = findViewById(R.id.img3);
        ImageView img4 = findViewById(R.id.img4);
        ImageView img5 = findViewById(R.id.img5);
        ImageView img6 = findViewById(R.id.img6);
        ImageView img7 = findViewById(R.id.img7);
        ImageView img8 = findViewById(R.id.img8);


        final ImageView[] lay = {img3,img4,img5,img6,img7,img8};
        final ImageView[] layRec = {img1,img2};
        final int[] fruit = {R.drawable.mangosteen, R.drawable.longan, R.drawable.orange, R.drawable.mango, R.drawable.rambutan, R.drawable.durian
                ,R.drawable.lychee,R.drawable.grape,R.drawable.dragonfruit,R.drawable.pineapple};

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        callMarket = database.getReference().child("product").child("marketProduct");
        callMarket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    fruitDraw.clear();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        String s = d.child("status").getValue(String.class);
                        if(s==null){
                            getData = d.getValue(GetData.class);
                            fruitDraw.add(getData.getFruitName());
                        }
                    }
                    for(int count = 0 ; count<fruitDraw.size();count++){
                        for(int countIn = count+1 ; countIn<fruitDraw.size();countIn++){
                            if(fruitDraw.get(count).matches(fruitDraw.get(countIn))){
                                fruitDraw.remove(countIn);
                            }
                        }
                    }
                    Fcount = fruitDraw.size();
                    if(fruitDraw.size()>=6){
                        Fcount=6;
                    }
                    if(fruitDraw.size()>=2) {
                        fruitRec.clear();
                        int min = 0;
                        int max = fruitDraw.size();
                        Random r = new Random();
                        int ran1 = r.nextInt(max - min);
                        int ran2 = r.nextInt(max - min);
                        while (ran1 == ran2) {
                            int ran3 = r.nextInt(max - min);
                            ran2 = ran3;
                        }
                        fruitRec.add(ran1);
                        fruitRec.add(ran2);
                    }
                    for(int count = 0 ; count<fruitRec.size();count++){
                        if(fruitDraw.get(fruitRec.get(count)).matches("มังคุด")){
                            layRec[count].setImageResource(fruit[0]);
                        }
                        else if(fruitDraw.get(fruitRec.get(count)).matches("ลำไย")){
                            layRec[count].setImageResource(fruit[1]);
                        }
                        else if(fruitDraw.get(fruitRec.get(count)).matches("ส้ม")){
                            layRec[count].setImageResource(fruit[2]);
                        }
                        else if(fruitDraw.get(fruitRec.get(count)).matches("มะม่วง")){
                            layRec[count].setImageResource(fruit[3]);
                        }
                        else if(fruitDraw.get(fruitRec.get(count)).matches("เงาะ")){
                            layRec[count].setImageResource(fruit[4]);
                        }
                        else if(fruitDraw.get(fruitRec.get(count)).matches("ทุเรียน")){
                            layRec[count].setImageResource(fruit[5]);
                        }
                        else if(fruitDraw.get(fruitRec.get(count)).matches("ลิ้นจี่")){
                            layRec[count].setImageResource(fruit[6]);
                        }
                        else if(fruitDraw.get(fruitRec.get(count)).matches("องุ่น")){
                            layRec[count].setImageResource(fruit[7]);
                        }
                        else if(fruitDraw.get(fruitRec.get(count)).matches("แก้วมังกร")){
                            layRec[count].setImageResource(fruit[8]);
                        }
                        else {
                            layRec[count].setImageResource(fruit[9]);
                        }



                    }
                    for(int count = 0 ; count<Fcount;count++){
                        if(fruitDraw.get(count).matches("มังคุด")){
                            lay[count].setImageResource(fruit[0]);
                        }
                        else if(fruitDraw.get(count).matches("ลำไย")){
                            lay[count].setImageResource(fruit[1]);
                        }
                        else if(fruitDraw.get(count).matches("ส้ม")){
                            lay[count].setImageResource(fruit[2]);
                        }
                        else if(fruitDraw.get(count).matches("มะม่วง")){
                            lay[count].setImageResource(fruit[3]);
                        }
                        else if(fruitDraw.get(count).matches("เงาะ")){
                            lay[count].setImageResource(fruit[4]);
                        }
                        else if(fruitDraw.get(count).matches("ทุเรียน")){
                            lay[count].setImageResource(fruit[5]);
                        }
                        else if(fruitDraw.get(count).matches("ลิ้นจี่")){
                            lay[count].setImageResource(fruit[6]);
                        }
                        else if(fruitDraw.get(count).matches("องุ่น")){
                            lay[count].setImageResource(fruit[7]);
                        }
                        else if(fruitDraw.get(count).matches("แก้วมังกร")){
                            lay[count].setImageResource(fruit[8]);
                        }
                        else {
                            lay[count].setImageResource(fruit[9]);
                        }
                    }
                    for(int count=0 ; count<fruitRec.size();count++){
                        String[] mStringArray= new String[fruitDraw.size()];
                        mStringArray = fruitDraw.toArray(mStringArray);
                        final String[] finalMStringArray = mStringArray;
                        final int finalCount = count;
                        layRec[count].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent next = new Intent(HomeActivity.this, MarListFruit.class);
                                next.putExtra("MarFruit", finalMStringArray[fruitRec.get(finalCount)]);
                                startActivity(next);
                            }
                        });
                    }
                    for(int count=0 ; count<Fcount;count++){
                        String[] mStringArray= new String[fruitDraw.size()];
                        mStringArray = fruitDraw.toArray(mStringArray);
                        final String[] finalMStringArray = mStringArray;
                        final int finalCount = count;
                        lay[count].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent next = new Intent(HomeActivity.this, MarListFruit.class);
                                next.putExtra("MarFruit", finalMStringArray[finalCount]);
                                startActivity(next);
                            }
                        });
                    }
                    Button allfruit = findViewById(R.id.text3);
                    allfruit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent next = new Intent(HomeActivity.this, MarListFruit.class);
                            next.putExtra("MarFruit", "all");
                            startActivity(next);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
