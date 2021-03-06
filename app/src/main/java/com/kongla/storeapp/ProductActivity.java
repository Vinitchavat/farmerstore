package com.kongla.storeapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
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
    public DatabaseReference callMarket, callfarmname, sendMar, sendPre;
    FirebaseDatabase database;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        product = extras.getString("product");
        key = extras.getString("key");
        BuyButton = findViewById(R.id.product_button2);
        AddtobagButton = findViewById(R.id.product_button1);
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String userID = sp.getString("IDKey", "0");
        final String s = sp.getString("Status", "none");

        if (!s.matches("buyer")) {
            BuyButton.setVisibility(View.INVISIBLE);
            AddtobagButton.setVisibility(View.INVISIBLE);
        } else {
            BuyButton.setVisibility(View.VISIBLE);
            AddtobagButton.setVisibility(View.VISIBLE);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Order")
                .child(product);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int a = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    String p = d.child("productID").getValue(String.class);
                    String u = d.child("buyerID").getValue(String.class);
                    String st = d.child("orderStatus").getValue(String.class);
                    Log.d("17",st);
                    if (u.matches(userID) && p.matches(key) && !st.equals("finished")) {
                        a++;
                    }
                }
                if (a != 0) {
                    BuyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), "คุณสั่งซื้อสินค้านี้ไปแล้ว\nดูสินค้าได้ที่ตะกร้าสินค้า", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AddtobagButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(), "คุณสั่งซื้อสินค้านี้ไปแล้ว\nดูสินค้าได้ที่ตะกร้าสินค้า", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    BuyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ProductActivity.this);
                            dialog.setMessage("ยืนยันคำสั่งซื้อ");
                            dialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addData("buy");
                                }
                            });
                            dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            dialog.show();
                        }
                    });
                    AddtobagButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ProductActivity.this);
                            dialog.setMessage("ยืนยันคำสั่งซื้อ");
                            dialog.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addData("add");
                                }
                            });
                            dialog.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                            dialog.show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

                    textName.setText("" + fruitName + " " + productName);
                    textPrice.setText("" + price + "บาท ต่อ " + unitPro);
                    textDetail1.setText("พันธ์ุผลไม้ :" + productName);
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
        } else {
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

                    textName.setText("" + fruitName + " " + productName);
                    textPrice.setText("ราคา" + price + "บาท ต่อ " + unitPro);
                    textDetail1.setText("พันธุ์ผลไม้ :" + productName);
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

    public void addData(final String buyoradd) {
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String IDKey = sp.getString("IDKey", "0");
        Bundle extras = getIntent().getExtras();
        product = extras.getString("product");
        key = extras.getString("key");
        if (product.matches("marketProduct")) {
            database = FirebaseDatabase.getInstance();
            callMarket = database.getReference().child("product").child("marketProduct").child(key);
            callMarket.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map map = (Map) dataSnapshot.getValue();
                    farmID = String.valueOf(map.get("farmID"));
                    int pSell = Integer.parseInt(String.valueOf(map.get("productSell")));
                    pSell = pSell + 1;
                    callMarket.child("productSell").setValue(pSell);
                    callfarmname = database.getReference().child("farmer").child(farmID);
                    callfarmname.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String memberID = dataSnapshot.child("memberID").getValue(String.class);
                            OrderModel orderModel = new OrderModel(IDKey, memberID, farmID, key, "none");
                            sendMar = database.getReference().child("Order").child("marketProduct").push();
                            if (buyoradd.matches("add")) {
                                sendMar.setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "การสั่งซื้อเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                                        finish();startActivity(getIntent());
                                    }
                                });
                            } else {
                                sendMar.setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "การสั่งซื้อเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                                        String k = sendMar.getKey();
                                        Intent intent = new Intent(getApplicationContext(), chatNew.class);
                                        intent.putExtra("statusPro", "marketProduct");
                                        intent.putExtra("orderid", k);
                                        intent.putExtra("farmID", farmID);
                                        startActivity(intent);
                                    }
                                });
                            }
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
        } else {
            day = extras.getString("DayPre");
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            callMarket = database.getReference().child("product").child("preorderProduct").child(day).child(key);
            callMarket.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Map map = (Map) dataSnapshot.getValue();
                    farmID = String.valueOf(map.get("farmID"));
                    int pSell = Integer.parseInt(String.valueOf(map.get("productSell")));
                    pSell = pSell+1;
                    callMarket.child("productSell").setValue(pSell);
                    callfarmname = database.getReference().child("farmer").child(farmID);
                    callfarmname.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map map = (Map) dataSnapshot.getValue();
                            final String memberID = String.valueOf(map.get("memberID"));
                            OrderModel orderModel = new OrderModel(IDKey, memberID, farmID, key, "none", day);
                            sendPre = database.getReference().child("Order").child("preorderProduct").push();
                            if (buyoradd.matches("add")) {
                                sendPre.setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "การสั่งซื้อเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                                        finish();startActivity(getIntent());
                                    }
                                });
                            } else {
                                sendPre.setValue(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "การสั่งซื้อเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                                        final String k = sendPre.getKey();
                                        Intent intent = new Intent(getApplicationContext(), basketMain.class);
                                        intent.putExtra("orderid", k);
                                        intent.putExtra("statusPro", "preorderProduct");
                                        intent.putExtra("farmID", farmID);
                                        intent.putExtra("day", day);
                                        startActivity(intent);
                                    }
                                });
                            }
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
