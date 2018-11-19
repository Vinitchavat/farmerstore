package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;

public class profileSellerSetting extends AppCompatActivity {
    private TextView farmName, farmDes, addProduct, textPreorder;
    private ImageView pImage,imgSeller;
    private static String textFarmName, textFarmDes, imgUrl, type;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ArrayList<Integer> pSell = new ArrayList<>();
    ArrayList<String> pFruit = new ArrayList<>();
    ArrayList<String> pID = new ArrayList<>();
    ArrayList<String> img = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_sellersetting);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.profile_sellerSetting);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        editor = sp.edit();
        final String userID = sp.getString("IDKey", "0");

        farmName = (TextView) findViewById(R.id.farmName);
        farmDes = (TextView) findViewById(R.id.farmDes);
        addProduct = (TextView) findViewById(R.id.addProduct);
        pImage = findViewById(R.id.productImage);
        textPreorder = findViewById(R.id.preorderProduct);
        imgSeller = findViewById(R.id.imgSeller);

        Button button = findViewById(R.id.editFarm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), profileEditFarm.class);
                startActivity(i);
            }
        });

        /* ** ADD TO ADDPRODUCT PAGE ** */
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), profileAddProduct.class);
                i.putExtra("do", "add");
                startActivity(i);
            }
        });

        /* ** Delete Product ** */
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        String status = sp.getString("Status", "None");
        String id = sp.getString("ID", "");
        if (status.matches("Delete")) {
            DatabaseReference dataref1 = FirebaseDatabase.getInstance().getReference().child("product")
                    .child("marketProduct").child(id);
            dataref1.removeValue();
            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            editor.remove("Status");
            editor.remove("ID");
            editor.apply();
        }

        /* *** GET FarmID *** */
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("farmer");
        Query query = databaseReference.orderByChild("memberID").equalTo(userID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    String id = d.getKey();
                    editor.putString("farmID",id);
                    editor.commit();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        final String farmID = sp.getString("farmID", "0");

        /* ** FARM HEADER ** */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dataRef1 = database.getReference().child("farmer").child(farmID);
        dataRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textFarmName = dataSnapshot.child("farmName").getValue(String.class);
                textFarmDes = dataSnapshot.child("farmDescription").getValue(String.class);
                farmName.setText(textFarmName);
                farmDes.setText(textFarmDes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /* *** SHOW Farmer Image *** */
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imgLink = dataSnapshot.child("imgLink").getValue(String.class);
                if (imgLink!=null){
                    new profileSellerSetting.DownloadImageTask(imgSeller).execute(imgLink);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /* ** SHOW PRODUCT LIST ** */
        if(getIntent().getStringExtra("type")!=null && getIntent().getStringExtra("type").matches("preorder")){
            type = "preorder";
            textPreorder.setText("ดูสินค้าตลาด");
            preorderProduct();
        }
        else {
            type = "market";
            textPreorder.setText("ดูสินค้าสั่งจอง");
            marketProduct();
        }
        textPreorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.matches("market")){
                    preorderProduct();
                    type = "preorder";
                    textPreorder.setText("ดูสินค้าตลาด");
                }
                else {
                    marketProduct();
                    type = "market";
                    textPreorder.setText("ดูสินค้าสั่งจอง");
                }
            }
        });
    }
    public void marketProduct(){
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        final String farmID = sp.getString("farmID", "0");
        DatabaseReference dataRef2 = FirebaseDatabase.getInstance().getReference().child("product").child("marketProduct");
        final Query q1 = dataRef2.orderByChild("farmID").equalTo(farmID);
        img.clear();pFruit.clear();pID.clear();pSell.clear();
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String fname = d.child("fruitName").getValue(String.class);
                    String pname = d.child("productName").getValue(String.class);
                    Integer psell = d.child("productSell").getValue(Integer.class);
                    imgUrl = d.child("imgLink").getValue(String.class);
                    String productid = d.getKey();
                    pFruit.add(fname + pname);
                    pSell.add(psell);
                    pID.add(productid);
                    img.add(imgUrl);
                }
                ListView listView = findViewById(R.id.list_product);
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                if (pFruit.size() <= 10) {
                    float pixels = pFruit.size() * 120 * getResources().getDisplayMetrics().density;
                    params.height = (int) pixels;
                    listView.setLayoutParams(params);
                } else {
                    float pixels = 1200 * getResources().getDisplayMetrics().density;
                    params.height = (int) pixels;
                    listView.setLayoutParams(params);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ListView list = (ListView) findViewById(R.id.list_product);
        AdapterPL adapter = new AdapterPL(profileSellerSetting.this, pFruit, pSell, pID, img, "market");
        list.setAdapter(null);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    public void preorderProduct(){
        final ArrayList<String> childDate = new ArrayList<>();
        sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        final String farmID = sp.getString("farmID", "0");
        img.clear();pFruit.clear();pID.clear();pSell.clear();childDate.clear();

        final DatabaseReference dataRef3 = FirebaseDatabase.getInstance().getReference().child("product")
                .child("preorderProduct");
        dataRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    String key = d.getKey();
                    final Query q2 = dataRef3.child(key).orderByChild("farmID").equalTo(farmID);
                    q2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                String fname = d.child("fruitName").getValue(String.class);
                                String pname = d.child("productName").getValue(String.class);
                                Integer psell = d.child("productSell").getValue(Integer.class);
                                imgUrl = d.child("imgLink").getValue(String.class);
                                String productid = d.getKey();
                                pFruit.add(fname + pname);
                                pSell.add(psell);
                                pID.add(productid);
                                img.add(imgUrl);
                            }
                            ListView listView = findViewById(R.id.list_product);
                            ViewGroup.LayoutParams params = listView.getLayoutParams();
                            if (pFruit.size() <= 10) {
                                float pixels = pFruit.size() * 120 * getResources().getDisplayMetrics().density;
                                params.height = (int) pixels;
                                listView.setLayoutParams(params);
                            } else {
                                float pixels = 1200 * getResources().getDisplayMetrics().density;
                                params.height = (int) pixels;
                                listView.setLayoutParams(params);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("error",databaseError.toString()); }
        });
        ListView list = (ListView) findViewById(R.id.list_product);
        AdapterPL adapter = new AdapterPL(profileSellerSetting.this, pFruit, pSell, pID, img, "preorder");
        list.setAdapter(null);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
