package com.kongla.storeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterFarm extends AppCompatActivity {

    private EditText editName,editDes,editNum,editAdd;
    ProgressDialog loadingBar;

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
                        startActivity(intent);
                        loadingBar.dismiss();
                    }
                });
            }
        });
    }
}