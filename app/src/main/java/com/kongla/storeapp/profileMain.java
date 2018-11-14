package com.kongla.storeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class profileMain extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
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
                case R.id.navigation_dashboard:
                    /*i = new Intent(getApplicationContext(), preorderMain.class);
                    startActivity(i);*/
                    return true;
                case R.id.navigation_notifications:
                    i = new Intent(getApplicationContext(), basketMain.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_profile:
                    /* ***Selected Activity NO Intent*** */
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        /* *** Set Selected Menu *** */
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_profile);

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

        Button button4 = (Button) findViewById(R.id.profile_btnLogout);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* *** Progess Bar *** */
                loadingBar = new ProgressDialog(profileMain.this);
                loadingBar.setTitle("Logging out");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                /* *** Sign out and delete state *** */
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.remove("IDKey");
                editor.commit();

                /* *** Go back to Login *** */
                Intent i5 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i5);
                finish();
            }
        });
    }
}
