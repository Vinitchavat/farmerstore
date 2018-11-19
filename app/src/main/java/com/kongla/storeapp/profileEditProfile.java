package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileEditProfile extends AppCompatActivity {
    private TextView textEmail, textPassword, textName, textPhone, textAddress;
    private FirebaseAuth firebaseAuth;
    public DatabaseReference databaseReference;
    private static final String PREFS = "PREFS";
    private String email, name, phone, address;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editprofile);

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");

        firebaseAuth = FirebaseAuth.getInstance();
        textEmail = (TextView) findViewById(R.id.textEmail);
        textPassword = (TextView) findViewById(R.id.textPassword);
        textName = (TextView) findViewById(R.id.textName);
        textPhone = (TextView) findViewById(R.id.textPhone);
        textAddress = (TextView) findViewById(R.id.textAddress);

        RelativeLayout l1 = (RelativeLayout) findViewById(R.id.editlayout1);
        RelativeLayout l2 = (RelativeLayout) findViewById(R.id.editlayout2);
        RelativeLayout l3 = (RelativeLayout) findViewById(R.id.editlayout3);
        RelativeLayout l4 = (RelativeLayout) findViewById(R.id.editlayout4);

        /* ** Click and Edit ** */
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "กรุณาติดต่อเจ้าหน้าเพื่อแก้ไขอีเมล\nหรือรหัสผ่าน", Toast.LENGTH_SHORT).show();
            }
        });
       l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), profileEditActivity.class);
                i.putExtra("data", "name");
                startActivity(i);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), profileEditActivity.class);
                i.putExtra("data", "phone");
                startActivity(i);
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), profileEditActivity.class);
                i.putExtra("data", "address");
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String userID = sp.getString("IDKey", "0");
        if(firebaseAuth != null) {
            /* ** Show Data from Database ** */
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference().child("Users").child(userID);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    email = dataSnapshot.child("email").getValue(String.class);
                    name = dataSnapshot.child("name").getValue(String.class);
                    phone = dataSnapshot.child("phone").getValue(String.class);
                    address = dataSnapshot.child("address").getValue(String.class);
                    textEmail.setText(email);
                    textName.setText(name);
                    textPhone.setText(phone);
                    textAddress.setText(address);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),
                            "เกิดข้อผิดพลาด กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            });
        }
    }
}
