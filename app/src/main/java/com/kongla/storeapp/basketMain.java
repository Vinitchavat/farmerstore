package com.kongla.storeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongla.storeapp.R;

public class basketMain extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
