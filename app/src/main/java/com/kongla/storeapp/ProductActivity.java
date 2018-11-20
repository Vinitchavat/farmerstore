package com.kongla.storeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    private Button AddtobagButton, BuyButton;
    String product, key, day, farmID, fruitName, productName, unitPro, img, price, quantity;
    public DatabaseReference callMarket, callfarmname,sendMar,sendPre;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        BuyButton = findViewById(R.id.product_button2);
        BuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ProductActivity.this);
                dialog.setMessage("ยืนยันคำสั่งซื้อ");
                dialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addData();
                    }
                });
                dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();startActivity(new Intent());
                    }
                });
                dialog.show();
            }
        });

        Bundle extras = getIntent().getExtras();
        product = extras.getString("product");
        key = extras.getString("key");
        if (product.matches("marketProduct")) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            callMarket = database.getReference().child("product").child("marketProduct").child(key);
            callMarket.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map map = (Map) dataSnapshot.getValue();
                    farmID = String.valueOf(map.get("farmID"));
                    fruitName = String.valueOf(map.get("fruitName"));
                    price = String.valueOf(map.get("price"));
                    productName = String.valueOf(map.get("productName"));
                    quantity = String.valueOf(map.get("quantity"));
                    unitPro = String.valueOf(map.get("unit"));
                    img = String.valueOf(map.get("imgLink"));

                    final ImageView imageView = findViewById(R.id.image1);
                    new DownloadImageTask(imageView).execute(img);
                    TextView textName = findViewById(R.id.product_name1);
                    TextView textPrice = findViewById(R.id.product_price1);
                    TextView textDetail1 = findViewById(R.id.product_subdetails1);
                    TextView textDetail2 = findViewById(R.id.product_subdetails2);

                    textName.setText("" + fruitName +" "+ productName);
                    textPrice.setText("" + price +"บาท ต่อ "+unitPro);
                    textDetail1.setText("พันธ์ผลไม้ :" +productName);
                    textDetail2.setText("ปริมาณสินค้า : " + quantity + " " + unitPro);

                    callfarmname = database.getReference().child("farmer").child(farmID);
                    callfarmname.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map map = (Map) dataSnapshot.getValue();
                            String farmname = String.valueOf(map.get("farmName"));

                            TextView textDetail3 = findViewById(R.id.product_subdetails3);
                            textDetail3.setText("ชื่อฟาร์ม : " + farmname);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            day = extras.getString("DayPre");
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            callMarket = database.getReference().child("product").child("preorderProduct").child(day).child(key);
            callMarket.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map map = (Map) dataSnapshot.getValue();
                    farmID = String.valueOf(map.get("farmID"));
                    fruitName = String.valueOf(map.get("fruitName"));
                    price = String.valueOf(map.get("price"));
                    productName = String.valueOf(map.get("productName"));
                    quantity = String.valueOf(map.get("quantity"));
                    unitPro = String.valueOf(map.get("unit"));
                    img = String.valueOf(map.get("imgLink"));

                    final ImageView imageView = findViewById(R.id.image1);
                    new DownloadImageTask(imageView).execute(img);
                    TextView textName = findViewById(R.id.product_name1);
                    TextView textPrice = findViewById(R.id.product_price1);
                    TextView textDetail1 = findViewById(R.id.product_subdetails1);
                    TextView textDetail2 = findViewById(R.id.product_subdetails2);

                    textName.setText("" + fruitName +" "+ productName);
                    textPrice.setText("ราคา" + price +"บาท ต่อ "+unitPro);
                    textDetail1.setText("พันธ์ผลไม้ :" +productName);
                    textDetail2.setText("ปริมาณสินค้า : " + quantity + " " + unitPro);

                    callfarmname = database.getReference().child("farmer").child(farmID);
                    callfarmname.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map map = (Map) dataSnapshot.getValue();
                            String farmname = String.valueOf(map.get("farmName"));

                            TextView textDetail3 = findViewById(R.id.product_subdetails3);
                            textDetail3.setText("ชื่อฟาร์ม : " + farmname);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
//        BuyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent =  new Intent(ProductActivity.this, basketMain.class);
//                startActivity(intent);
//            }
//        });
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
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    public void addData() {
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String IDKey = sp.getString("IDKey", "0");
        Bundle extras = getIntent().getExtras();
        product = extras.getString("product");
        key = extras.getString("key");
        if (product.matches("marketProduct")) {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            callMarket = database.getReference().child("product").child("marketProduct").child(key);
            callMarket.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map map = (Map) dataSnapshot.getValue();
                    farmID = String.valueOf(map.get("farmID"));
                    callfarmname = database.getReference().child("farmer").child(farmID);
                    callfarmname.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map map = (Map) dataSnapshot.getValue();
                            final String memberID = String.valueOf(map.get("memberID"));
                            OrderModel orderModel = new OrderModel(IDKey,memberID,farmID,key,"none");
                            sendMar = database.getReference().child("Order").child("marketProduct");
                            sendMar.push().setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"การสั่งซื้อเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),basketMain.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        else {
            day = extras.getString("DayPre");
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            callMarket = database.getReference().child("product").child("preorderProduct").child(day).child(key);
            callMarket.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map map = (Map) dataSnapshot.getValue();
                    farmID = String.valueOf(map.get("farmID"));
                    callfarmname = database.getReference().child("farmer").child(farmID);
                    callfarmname.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map map = (Map) dataSnapshot.getValue();
                            final String memberID = String.valueOf(map.get("memberID"));
                            OrderModel orderModel = new OrderModel(IDKey,memberID,farmID,key,"none",day);
                            sendPre = database.getReference().child("Order").child("preorderProduct");
                            sendPre.push().setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"การสั่งซื้อเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),basketMain.class);
                                    startActivity(intent);
                                }
                            });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        }

}
