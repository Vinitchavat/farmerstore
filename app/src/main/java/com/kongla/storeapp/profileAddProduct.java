package com.kongla.storeapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

public class profileAddProduct extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private static String addoredit, ID, urlLink, unitSelect;
    private TextView textName, perunit;
    private EditText editName, editQ, editP, editD, editM, editY;
    private ImageView img;
    private static String[] fruits;
    private String photoURL;
    private Uri uri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_addproduct);

        progressDialog = new ProgressDialog(profileAddProduct.this);
        final Spinner spinnerUnit = findViewById(R.id.dropdownUnit);
        textName = findViewById(R.id.textProductName);
        perunit = findViewById(R.id.perunit);
        Button btn_delProduct = findViewById(R.id.deleteProduct);

        final SharedPreferences sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("เพิ่มสินค้า");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* ** UNIT SELECT ** */
        final String[] unit = {"กิโลกรัม", "ผล"};
        ArrayAdapter<String> adapterSelect = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, unit);
        spinnerUnit.setAdapter(adapterSelect);
        spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                unitSelect = unit[position];
                perunit.setText("ต่อ 1 " + unit[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final Switch preorderSwitch = findViewById(R.id.switchPreorder);
        preorderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RelativeLayout layout = findViewById(R.id.preorderlayout);
                if (isChecked) {
                    layout.setVisibility(RelativeLayout.VISIBLE);
                } else {
                    layout.setVisibility(RelativeLayout.GONE);
                }
            }
        });

        /* ** ADD IMAGE ** */
        ImageView selectImg = findViewById(R.id.addProductImage);
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent
                        , "Select Picture"), PICK_IMAGE);
                ID = UUID.randomUUID().toString();
            }
        });

        /* ** FRUIT NAME SELECT ** */
        textName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("FruitName");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> fname = new ArrayList<>();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            fname.add(d.child("Name").getValue().toString());
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(profileAddProduct.this);
                        builder.setTitle("เลือกชนิดผลไม้");
                        fruits = fname.toArray(new String[fname.size()]);
                        builder.setItems(fruits, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                textName.setText(fruits[which]);
                            }
                        });
                        builder.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        textName.setText("Unable to select fruit name!");
                    }
                });
            }
        });

        /* *** READ DATE *** */
        perunit = findViewById(R.id.perunit);
        editName = findViewById(R.id.addTextProductName);
        editQ = findViewById(R.id.editQuantity);
        editP = findViewById(R.id.editprice);
        editD = findViewById(R.id.editDate);
        editM = findViewById(R.id.editMonth);
        editY = findViewById(R.id.editYear);
        img = findViewById(R.id.addProductImage);
        addoredit = getIntent().getStringExtra("do");
        final String s = getIntent().getStringExtra("type");
        final String id = getIntent().getStringExtra("productID");
        if (addoredit.matches("edit")) {
            preorderSwitch.setVisibility(View.GONE);
            if (s.matches("market")) {
                btn_delProduct.setVisibility(View.VISIBLE);
                btn_delProduct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteProduct();
                    }
                });
                databaseReference = FirebaseDatabase.getInstance().getReference().child("product").child("marketProduct");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProductModel productModel = dataSnapshot.child(id).getValue(ProductModel.class);
                        if (productModel != null) {
                            textName.setText(productModel.getFruitName());
                            editName.setText(productModel.getProductName());
                            perunit.setText("ต่อ 1 " + productModel.getUnit());
                            if (productModel.getUnit().matches("ผล")) {
                                spinnerUnit.setSelection(1);
                            }
                            Integer d3 = productModel.getQuantity();
                            Integer d4 = productModel.getPrice();
                            editQ.setText(d3.toString());
                            editP.setText(d4.toString());
                            urlLink = productModel.getImgLink();
                            if (urlLink != null) {
                                new profileAddProduct.DownloadImageTask(img).execute(urlLink);
                            }
                            img.setPadding(0, 0, 0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                final DatabaseReference dref = FirebaseDatabase.getInstance().getReference()
                        .child("product").child("preorderProduct");

                dref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            final String id = getIntent().getStringExtra("productID");
                            String key = d.getKey();

                            if (dataSnapshot.child(key).child(id).child("fruitName").getValue() != null) {

                                databaseReference = FirebaseDatabase.getInstance().getReference()
                                        .child("product").child("preorderProduct").child(key);

                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ProductModel productModel = dataSnapshot.child(id).getValue(ProductModel.class);
                                        if (productModel != null) {
                                            textName.setText(productModel.getFruitName());
                                            editName.setText(productModel.getProductName());
                                            perunit.setText("ต่อ 1 " + productModel.getUnit());
                                            if (productModel.getUnit().matches("ผล")) {
                                                spinnerUnit.setSelection(1);
                                            }
                                            Integer d3 = productModel.getQuantity();
                                            Integer d4 = productModel.getPrice();
                                            editQ.setText(d3.toString());
                                            editP.setText(d4.toString());
                                            urlLink = productModel.getImgLink();
                                            if (urlLink != null) {
                                                new profileAddProduct.DownloadImageTask(img).execute(urlLink);
                                            }
                                            img.setPadding(0, 0, 0, 0);
                                            if (s.matches("preorder")) {
                                                preorderSwitch.setVisibility(View.VISIBLE);
                                                preorderSwitch.setChecked(true);
                                                String date = dataSnapshot.getKey();
                                                editD.setText(date.substring(date.lastIndexOf("-")));
                                                editM.setText(date.substring(date.indexOf("-")+1, date.lastIndexOf("-")));
                                                editY.setText(date.substring(0, date.indexOf("-")));
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    /* ** SHOW IMAGE PREVIEW ** */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            ImageView imageView = (ImageView)findViewById(R.id.img);
            imageView.setImageURI(uri);
        }
    }

    public void saveData(final String url) {
        final SharedPreferences sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        final String farmID = sp.getString("farmID", "0");
        textName = findViewById(R.id.textProductName);
        perunit = findViewById(R.id.perunit);
        editName = findViewById(R.id.addTextProductName);
        editQ = findViewById(R.id.editQuantity);
        editP = findViewById(R.id.editprice);
        editD = findViewById(R.id.editDate);
        editM = findViewById(R.id.editMonth);
        editY = findViewById(R.id.editYear);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        addoredit = getIntent().getStringExtra("do");

        final String fruitName = textName.getText().toString();
        final String productName = editName.getText().toString().trim();
        final int price = Integer.parseInt(editP.getText().toString().trim());
        final int quantity = Integer.parseInt(editQ.getText().toString().trim());
        String dDate = editD.getText().toString().trim();
        String mDate = editM.getText().toString().trim();
        String yDate = editY.getText().toString().trim();
        Switch preorderSwitch = findViewById(R.id.switchPreorder);

        if (addoredit.matches("add")) {
            if (preorderSwitch.isChecked()) {
                if (dDate.length()<2){
                    dDate = "0"+dDate;
                }
                if (mDate.length()<2){
                    mDate = "0"+mDate;
                }
                String childDate = yDate + "-" + mDate + "-" + dDate;
                DatabaseReference dref = databaseReference.child("product").child("preorderProduct")
                        .child(childDate);
                ProductModel productModel = new ProductModel(farmID, fruitName, productName, price,
                        quantity, unitSelect, 0, url);
                dref.push().setValue(productModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),
                                "เพิ่มสินค้าเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), profileSellerSetting.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
            } else {
                DatabaseReference dref = databaseReference.child("product").child("marketProduct");
                ProductModel productModel = new ProductModel(farmID, fruitName, productName
                        , price, quantity, unitSelect, 0, url);
                dref.push().setValue(productModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),
                                "เพิ่มสินค้าเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), profileSellerSetting.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
            }
        } else if (addoredit.matches("edit")) {
            final String id = getIntent().getStringExtra("productID");
            if (preorderSwitch.isChecked()) {
                String childDate = yDate + "-" + mDate + "-" + dDate;
                final DatabaseReference dref = databaseReference.child("product").child("preorderProduct");

                dref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            String key = d.getKey();
                            if (dataSnapshot.child(key).child(id).child("fruitName").getValue() != null) {

                                ProductModel productModel = new ProductModel(farmID, fruitName, productName, price,
                                        quantity, unitSelect, 0, url);
                                dref.child(key).child(id).setValue(productModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(),
                                                "แก้ไขสินค้าเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), profileSellerSetting.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        i.putExtra("type", "preorder");
                                        startActivity(i);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                DatabaseReference dref = databaseReference.child("product").child("marketProduct").child(id);
                ProductModel productModel = new ProductModel(farmID, fruitName, productName, price
                        , quantity, unitSelect, 0, url);
                dref.setValue(productModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),
                                "แก้ไขสินค้าเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), profileSellerSetting.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });
            }
        }
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.actionbarcheck) {

            /* *** CHECK DATA FIELD *** */
            Switch preorderSwitch = findViewById(R.id.switchPreorder);
            textName = findViewById(R.id.textProductName);
            editName = findViewById(R.id.addTextProductName);
            editQ = findViewById(R.id.editQuantity);
            editP = findViewById(R.id.editprice);
            editD = findViewById(R.id.editDate);
            editM = findViewById(R.id.editMonth);
            editY = findViewById(R.id.editYear);

            if (textName.getText().toString().matches("เลือกชื่อผลไม้")) {
                Toast.makeText(getApplicationContext(), "กรุณาเลือกชื่อผลไม้", Toast.LENGTH_SHORT).show();
                return false;
            } else if (editName.getText().length() <= 0) {
                Toast.makeText(getApplicationContext(), "กรุณาใส่ชื่อสินค้า", Toast.LENGTH_SHORT).show();
                return false;
            } else if (editQ.getText().length() <= 0) {
                Toast.makeText(getApplicationContext(), "กรุณาใส่จำนวนผลไม้", Toast.LENGTH_SHORT).show();
                return false;
            } else if (editP.getText().length() <= 0) {
                Toast.makeText(getApplicationContext(), "กรุณาใส่ราคาผลไม้", Toast.LENGTH_SHORT).show();
                return false;
            } else if (preorderSwitch.isChecked()) {
                if (editD.getText().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "กรุณาใส่วันที่จัดส่งสินค้า", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (editM.getText().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "กรุณาใส่วันที่จัดส่งสินค้า", Toast.LENGTH_SHORT).show();
                    return false;
                } else if (editY.getText().length() <= 0) {
                    Toast.makeText(getApplicationContext(), "กรุณาใส่วันที่จัดส่งสินค้า", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(profileAddProduct.this);
            builder.setMessage(R.string.savedata);
            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (ID == null) {
                        progressDialog.setTitle("กรุณารอสักครู่");
                        progressDialog.show();
                        saveData(urlLink);
                        progressDialog.dismiss();
                    } else {
                        progressDialog.setTitle("กรุณารอสักครู่");
                        progressDialog.show();
                        uploadImage(ID);
                        getURI();
                        progressDialog.dismiss();
                    }
                }
            });
            builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            Intent i = new Intent(getApplicationContext(), profileSellerSetting.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadImage(String id) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Image").child("Product/" + id);
        storageRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getURI() {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Image").child("Product/" + ID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                photoURL = uri.toString();
                saveData(photoURL);
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
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }

    public void deleteProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(profileAddProduct.this);
        builder.setMessage(R.string.deleteproduct);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String Productid = getIntent().getStringExtra("productID");
                SharedPreferences sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Status", "Delete");
                editor.putString("ID", Productid);
                editor.apply();
                Intent i = new Intent(getApplicationContext(), profileSellerSetting.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.clickbacktwice, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
