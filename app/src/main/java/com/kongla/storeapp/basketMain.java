package com.kongla.storeapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.kongla.storeapp.R;

public class basketMain extends AppCompatActivity {

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
                    i = new Intent(getApplicationContext(), PreMain.class);
                    startActivity(i);
                    return true;
                case R.id.navigation_order:
                    /* ***Selected Activity NO Intent*** */
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
        setContentView(R.layout.activity_basket_main);

        /* *** Set Selected Menu *** */
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_order);

        Button Market = (Button)findViewById(R.id.btnMarket);
        Market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Market = new Intent(basketMain.this ,basketMandP.class);
                String x = "Mar";
                Market.putExtra("text",x);
                startActivity(Market);
            }
        });

        Button Pre = (Button)findViewById(R.id.btnPre);
        Pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Pre = new Intent(basketMain.this ,basketMandP.class);
                String x = "Pre";
                Pre.putExtra("text",x);
                startActivity(Pre);
            }
        });
    }


}
