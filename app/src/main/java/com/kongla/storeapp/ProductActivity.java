package com.kongla.storeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProductActivity extends AppCompatActivity {

    private Button AddtobagButton, BuyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        AddtobagButton =(Button)findViewById(R.id.product_button1);
        BuyButton =(Button)findViewById(R.id.product_button2);

        BuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(ProductActivity.this, basketMain.class);
                startActivity(intent);
            }
        });
    }
}
