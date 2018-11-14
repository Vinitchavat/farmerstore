package com.kongla.storeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class profileMain extends AppCompatActivity {
   // private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        /* ** Button Click ** */

        Button button = (Button) findViewById(R.id.profile_btnedit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(i);*/
            }
        });
        Button button2 = (Button) findViewById(R.id.profile_btnSeller);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i2 = new Intent(getApplicationContext(), SellerSetting.class);
                startActivity(i2);*/
            }
        });
        Button button3 = (Button) findViewById(R.id.profile_btnBuyer);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i3 = new Intent(getApplicationContext(), BuyerSetting.class);
                startActivity(i3);*/
            }
        });
        Button button4 = (Button) findViewById(R.id.profile_btnNoti);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i4 = new Intent(getApplicationContext(), BuyerSetting.class);
                startActivity(i4);*/
            }
        });
        Button button5 = (Button) findViewById(R.id.profile_btnLogout);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i5 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i5);
                finish();*/
            }
        });
    }
}
