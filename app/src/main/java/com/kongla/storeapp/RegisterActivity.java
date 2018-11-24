package com.kongla.storeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongla.storeapp.Model.Users;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputName, inputPhone, inputEmail, inputPassword;
    private Button btnRegister;
    private ProgressDialog loadingBar;
    private FirebaseAuth firebaseAuth;
    String[] items = new String[]{"กดเพื่อเลือก","ผู้ซื้อ", "ผู้ขาย"};
    String status = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.register_button);
        inputName = (EditText) findViewById(R.id.register_name);
        inputPhone = (EditText) findViewById(R.id.register_phone);
        inputEmail = (EditText) findViewById(R.id.register_email);
        inputPassword = (EditText) findViewById(R.id.register_password);
        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        Spinner statusDrop = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        statusDrop.setAdapter(adapter);
        statusDrop.setSelection(0);
        statusDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 1:
                        status = "buyer";
                        break;
                    case 2:
                        status = "seller";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.matches("seller")){
                    CreateAccount("seller");
                }
                else{
                    CreateAccount("buyer");
                }
            }
        });

        TextView login = (TextView)findViewById(R.id.changetologin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void CreateAccount(String isSeller) {

        String name = inputName.getText().toString();
        String phone = inputPhone.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        /* *** Check fields *** */
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.register_empty_warning, Toast.LENGTH_SHORT).show();
        } else if (name.length()<3) {
            Toast.makeText(this, R.string.register_name_warning, Toast.LENGTH_SHORT).show();
        } else if (phone.length() < 10) {
            Toast.makeText(this, R.string.register_phone_warning, Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, R.string.register_password_warning, Toast.LENGTH_SHORT).show();
        } else if (status.matches("none")) {
            Toast.makeText(this, "กรุณาเลือกสถานะการลงทะเบียน", Toast.LENGTH_SHORT).show();
        } else {
            registerNewUser(isSeller);
        }

    }

    private void registerNewUser(final String isSeller) {

        loadingBar.setMessage("กรุณารอสักครู่");
        loadingBar.show();
        final String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final String name = inputName.getText().toString().trim();
        final String phone = inputPhone.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Users user = new Users(email, name, phone, status);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                            .getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Register Success",
                                        Toast.LENGTH_SHORT).show();

                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                if (isSeller.matches("seller")){
                                    Intent intent = new Intent(RegisterActivity.this, RegisterFarm.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("userID",uid);
                                    intent.putExtra("txt","none");
                                    startActivity(intent);
                                }
                                else {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                                loadingBar.dismiss();
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Toast.makeText(this, R.string.clickbacktwice, Toast.LENGTH_SHORT).show();
            return;
        }
    }
}

