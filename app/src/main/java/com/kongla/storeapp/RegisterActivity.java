package com.kongla.storeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.register_button);
        inputName = (EditText) findViewById(R.id.register_name);
        inputPhone = (EditText) findViewById(R.id.register_phone );
        inputEmail = (EditText) findViewById(R.id.register_email);
        inputPassword = (EditText) findViewById(R.id.register_password);
        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            CreateAccount();

        } });

    }

    private void CreateAccount(){

        String name = inputName.getText().toString();
        String phone = inputPhone.getText().toString();
        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Enter your phone", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            registerNewUser();
        }
    }

    private void registerNewUser(){

        final String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final String name = inputName.getText().toString().trim();
        final String phone = inputPhone.getText().toString().trim();

        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Users user = new Users(
                            email, name, phone
                    );
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth
                            .getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Register Success",
                                        Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
//                            Toast.makeText(RegisterActivity.this, "Register Success",
//                                    Toast.LENGTH_SHORT).show();

//                            SharedPreferences sp = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sp.edit();
//                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
//                            t = currentFirebaseUser.getUid();
//                            editor.putString("IDKey",t);
//                            editor.commit();
//                            String stringValue = sp.getString("IDKey", "not found!");
//                            ntextview.setText(stringValue);
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

