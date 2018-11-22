package com.kongla.storeapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    int countxx;

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
                    startActivity(i);
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
        setContentView(R.layout.activity_home);

        final ImageView img1 = findViewById(R.id.img1);
        ImageView img2 = findViewById(R.id.img2);
        ImageView img3 = findViewById(R.id.img3);
        ImageView img4 = findViewById(R.id.img4);
        ImageView img5 = findViewById(R.id.img5);
        ImageView img6 = findViewById(R.id.img6);

        final ImageView[] lay = {img3,img4,img5,img6};
        final ImageView[] layRec = {img1,img2};
        final int[] fruit = {R.drawable.mangosteen, R.drawable.longan, R.drawable.orange, R.drawable.pineapple, R.drawable.rambutan, R.drawable.durian};

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        callMarket = database.getReference().child("product").child("marketProduct");
        callMarket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    getData = d.getValue(GetData.class);
                    fruitDraw.add(getData.getFruitName());
                }
                for(int count = 0 ; count<fruitDraw.size();count++){
                    for(int countIn = count+1 ; countIn<fruitDraw.size();countIn++){
                        if(fruitDraw.get(count).matches(fruitDraw.get(countIn))){
                            fruitDraw.remove(countIn);
                        }
                        else {
                            countxx++;
                        }
                    }
                }
                fruitRec.add(0);
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
                    if(fruitDraw.get(fruitRec.get(count)).matches("ทุเรียน")){
                        layRec[count].setImageResource(fruit[5]);
                    }
                    else if(fruitDraw.get(fruitRec.get(count)).matches("ลำไย")){
                        layRec[count].setImageResource(fruit[1]);
                    }
                    else if(fruitDraw.get(fruitRec.get(count)).matches("แตงโม")){
                        layRec[count].setImageResource(fruit[3]);
                    }
                    else if(fruitDraw.get(fruitRec.get(count)).matches("มังคุด")){
                        layRec[count].setImageResource(fruit[0]);
                    }
                    else if(fruitDraw.get(fruitRec.get(count)).matches("เงาะ")){
                        layRec[count].setImageResource(fruit[4]);
                    }
                    else {
                        layRec[count].setImageResource(fruit[2]);
                    }
                }
                for(int count = 0 ; count<fruitDraw.size();count++){
                    if(fruitDraw.get(count).matches("ทุเรียน")){
                        lay[count].setImageResource(fruit[5]);
                    }
                    else if(fruitDraw.get(count).matches("ลำไย")){
                        lay[count].setImageResource(fruit[1]);
                    }
                    else if(fruitDraw.get(count).matches("แตงโม")){
                        lay[count].setImageResource(fruit[3]);
                    }
                    else if(fruitDraw.get(count).matches("มังคุด")){
                        lay[count].setImageResource(fruit[0]);
                    }
                    else if(fruitDraw.get(count).matches("เงาะ")){
                        lay[count].setImageResource(fruit[4]);
                    }
                    else {
                        lay[count].setImageResource(fruit[2]);
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
               for(int count=0 ; count<fruitDraw.size();count++){
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
