package com.kongla.storeapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

public class profileMain extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    BottomNavigationView navigation;
    private ImageView imgProfile;
    private TextView txtName;
    private static String imgLink;

    /* *** Bottom Navigation *** */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    return true;
                case R.id.navigation_preorder:
                    i = new Intent(getApplicationContext(), PreMain.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    return true;
                case R.id.navigation_order:
                    i = new Intent(getApplicationContext(), basketMain.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        editor = sp.edit();
        final String userID = sp.getString("IDKey", "0");
        final String status = sp.getString("Status", "0");

        imgProfile = findViewById(R.id.profile_img);
        txtName = findViewById(R.id.profile_textName);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child(userID).child("name").getValue(String.class);
                txtName.setText(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getImage();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), profileEditImage.class);
                startActivity(i);
            }
        });

        /* *** Set Selected Menu *** */
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_profile);

        /* ** Button Click ** */
        Button button = (Button) findViewById(R.id.profile_btnedit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), profileEditProfile.class);
                startActivity(i);
            }
        });

        if (status.matches("seller")) {
            Button button2 = (Button) findViewById(R.id.profile_btnSeller);
            Button addProduct = (Button) findViewById(R.id.profile_add) ;
            button2.setVisibility(View.VISIBLE);
            addProduct.setVisibility(View.VISIBLE);
            addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), profileAddProduct.class);
                    i.putExtra("do", "add");
                    startActivity(i);
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i2 = new Intent(getApplicationContext(), profileSellerSetting.class);
                    startActivity(i2);
                }
            });
        } else {
            Button button3 = (Button) findViewById(R.id.profile_btnBuyer);
            Button button4 = (Button) findViewById(R.id.profile_btnBuyer2);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i3 = new Intent(getApplicationContext(), profileBuyerSetting.class);
                    startActivity(i3);
                }
            });
            button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i4 = new Intent(getApplicationContext(), profileBuyerSettingPreorder.class);
                    startActivity(i4);
                }
            });
        }

        Button button4 = (Button) findViewById(R.id.profile_btnLogout);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* *** Progess Bar *** */
                loadingBar = new ProgressDialog(profileMain.this);
                loadingBar.setTitle("ออกจากระบบ");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                /* *** Sign out and delete state *** */
                sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.remove("farmName");
                editor.remove("UserName");
                editor.remove("IDKey"); // userID
                editor.remove("farmID"); //
                editor.remove("Status"); // Buyer or Seller
                editor.commit();
                sp = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.remove("farmID");
                editor.commit();

                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();

                /* *** Go back to Login *** */
                Intent i5 = new Intent(getApplicationContext(), MainActivity.class);
                i5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i5);
                finish();
            }
        });
    }

    private void getImage() {
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String userID = sp.getString("IDKey", "0");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Image").child("users/" + userID).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String photoURL = uri.toString();
                        if (photoURL != null) {
                            new profileMain.DownloadImageTask((ImageView) findViewById(R.id.profile_img)).execute(photoURL);
                        }
                    }
                });
        storageReference.child("Image").child("users/" + userID).getDownloadUrl()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        TextView textView = (TextView) findViewById(R.id.clicktochangeimg);
                        textView.setVisibility(View.VISIBLE);
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
    }
}
