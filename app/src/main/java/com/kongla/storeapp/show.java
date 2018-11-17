package com.kongla.storeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class show extends AppCompatActivity {
    String order,order2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Bundle extras = getIntent().getExtras();
        order = extras.getString("DayPre");
        order2 = extras.getString("DayPreFruit");

        TextView textView=findViewById(R.id.textView);
        textView.setText(order+order2);
    }
}
