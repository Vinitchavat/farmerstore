package com.kongla.storeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private String fname, farmID;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        editor = sp.edit();
        final String IDKey = sp.getString("IDKey", "0");
        String status = sp.getString("Status","none");
        String fid = sp.getString("farmID","none");
        if (!IDKey.matches("0") && status.matches("seller")){
            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
            Query dRef = dataRef.child("farmer").orderByChild("memberID").equalTo(IDKey);
            dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     /* ** Check if no register farm ** */
                    if (!dataSnapshot.exists()) {
                        Intent intent = new Intent(MainActivity.this, RegisterFarm.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("userID",IDKey);
                        intent.putExtra("txt","not");
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (!IDKey.equals("0")) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

//        getActionBar().hide();

        LoginButton = (Button) findViewById(R.id.btn_login);
        RegisterButton = (Button) findViewById(R.id.btn_register);
        inputEmail = (EditText) findViewById(R.id.main_email);
        inputPassword = (EditText) findViewById(R.id.main_password);
        loadingBar = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();

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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }


    private void loginUser() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), R.string.login_email, Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), R.string.login_password, Toast.LENGTH_SHORT).show();
            return;
        } else {
            loadingBar.setMessage("กรุณารอสักครู่");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
        }

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
//                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            loadingBar.dismiss();
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(MainActivity.this, R.string.loginfailed, Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            /* *** SET Login State *** */
                            sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                            editor = sp.edit();
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            final String uID = currentFirebaseUser.getUid();
                            editor.putString("IDKey", uID);

                            final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
                            /* *** GET user status *** */
                            DatabaseReference databaseReference = dataRef.child("Users").child(uID);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final String s = dataSnapshot.child("status").getValue(String.class);
                                    final String name = dataSnapshot.child("name").getValue(String.class);
                                    if (s.matches("seller")) {
                                        Query dRef = dataRef.child("farmer").orderByChild("memberID").equalTo(uID);
                                        dRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                                    fname = d.child("farmName").getValue(String.class);
                                                    farmID = d.getKey();
                                                    editor.putString("farmName", fname);
                                                    editor.putString("farmID", farmID);
                                                    editor.commit();
                                                }
                                                /* ** Check if no register farm ** */
                                                if (!dataSnapshot.exists()) {
                                                    Intent intent = new Intent(MainActivity.this, RegisterFarm.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.putExtra("userID",uID);
                                                    intent.putExtra("txt","not");
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    editor.putString("Status", s);
                                    editor.putString("UserName", name);
                                    editor.commit();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), R.string.error_tryagain, Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                            loadingBar.dismiss();

                            /* *** GO to Homepage *** */
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
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
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
