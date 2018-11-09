package com.kongla.storeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kongla.storeapp.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;


public class MainActivity extends AppCompatActivity {

    private Button LoginButton, RegisterButton;
    private EditText inputEmail, inputPassword;
    private ProgressDialog loadingBar;
    private FirebaseAuth auth;

    private String parentDbName = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LoginButton = (Button) findViewById(R.id.btn_login);
        RegisterButton = (Button) findViewById(R.id.btn_register);
        inputEmail = (EditText) findViewById(R.id.main_email);
        inputPassword = (EditText) findViewById(R.id.main_password);
        loadingBar = new ProgressDialog(this);


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loginUser() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

//            //authenticate user
//            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new
//                    OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (!task.isSuccessful()) {
//// there was an error
//                                if (password.length() < 6) {
//                                    inputPassword.setError(getString(R.string.minimum_password));
//                                } else {
//                                    Toast.makeText(MainActivity.this, getString(R.string.auth_failed),
//                                            Toast.LENGTH_LONG).show();
//                                }
//                            }
//                            else {
//                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//                    });

            AllowAccessToAccount(email, password);
        }
    }


    private void AllowAccessToAccount(final String email, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(email).exists()) {
                    Users usersData = dataSnapshot.child(parentDbName).child(email).getValue(Users.class);

                    if (usersData.getEmail().equals(email)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(MainActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Account with this " + email + " do not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
