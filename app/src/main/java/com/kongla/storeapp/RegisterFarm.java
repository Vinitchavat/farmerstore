package com.kongla.storeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFarm extends AppCompatActivity {

    private EditText editName,editDes,editNum,editAdd;
    ProgressDialog loadingBar;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_farmer);

        final Intent i = getIntent();
        final Button btn_regis = (Button) findViewById(R.id.btn_farmRegis);
        editName = (EditText) findViewById(R.id.editTextFarmName) ;
        editDes = (EditText) findViewById(R.id.editTextFarmDes) ;
        editNum = (EditText) findViewById(R.id.editTextFarmNum) ;
        editAdd = (EditText) findViewById(R.id.editTextFarmAdd) ;
        loadingBar = new ProgressDialog(RegisterFarm.this);

        String mes = i.getStringExtra("txt");
        if(mes.matches("not")){
            TextView textView = (TextView) findViewById(R.id.txtuser);
            textView.setText("*เนื่องจากบัญชีของคุณเป็นผู้ขายที่ยังไม่มีการลงทะเบียนฟาร์ม จึงพบหน้านี้");
        }

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setMessage("กรุณารอสักครู่");
                loadingBar.show();
                String fName = editName.getText().toString().trim();
                String fDes = editDes.getText().toString().trim();
                String fNum = editNum.getText().toString().trim();
                String fAdd = editAdd.getText().toString().trim();

                String userID = i.getStringExtra("userID");

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("farmer");
                GetFarmer farmerModel = new GetFarmer(fName,fDes,fNum,fAdd,userID);
                databaseReference.push().setValue(farmerModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterFarm.this,"ลงทะเบียนเสร็จสิ้น",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterFarm.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        loadingBar.dismiss();
                    }
                });
            }
        });

        TextView btn_logout = (TextView) findViewById(R.id.farmlogout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* *** Progess Bar *** */
                loadingBar = new ProgressDialog(RegisterFarm.this);
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
