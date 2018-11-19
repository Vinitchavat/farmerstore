package com.kongla.storeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class basketMandP extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_mand_p);


        String MMAN;
        String newString;

        Bundle extras = getIntent().getExtras();
        newString= extras.getString("text");

        if(newString.equals("Mar"))
        {
            final String[] Marlist = {"Order1","Order2","Order3"};
            ListView listviewMarket = (ListView) findViewById(R.id.listviewM);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, android.R.id.text1,Marlist);
            listviewMarket.setAdapter(adapter);
            listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent next = new Intent(basketMandP.this, chatNew.class);
                    next.putExtra("mytext",Marlist[position]);
                    startActivity(next);
                }

            });

        }
        else
        {
            final String[] Marlist = {"Pre1","Pre2","Pre3"};
            ListView listviewMarket = (ListView) findViewById(R.id.listviewM);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>( this,android.R.layout.simple_list_item_1, android.R.id.text1,Marlist);
            listviewMarket.setAdapter(adapter);
            listviewMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent next = new Intent(basketMandP.this, chatNew.class);
                    next.putExtra("mytext",Marlist[position]);
                    startActivity(next);
                }

            });
        }

    }
}
