package com.kongla.storeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

import static com.kongla.storeapp.chatNew.PICK_IMAGE;

public class RegisterFarm extends AppCompatActivity {

    private EditText editName, editDes, editNum, editAdd;
    ProgressDialog loadingBar;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    FirebaseAuth firebaseAuth;
    String ID;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_farmer);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        editor = sp.edit();
        final Intent i = getIntent();
        final Button btn_regis = (Button) findViewById(R.id.btn_farmRegis);
        editName = (EditText) findViewById(R.id.editTextFarmName);
        editDes = (EditText) findViewById(R.id.editTextFarmDes);
        editNum = (EditText) findViewById(R.id.editTextFarmNum);
        editAdd = (EditText) findViewById(R.id.editTextFarmAdd);
        loadingBar = new ProgressDialog(RegisterFarm.this);
        Button choosePic = findViewById(R.id.choosePic);
        choosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent
                        , "Select Picture"), PICK_IMAGE);
                ID = UUID.randomUUID().toString();
            }
        });

        String mes = i.getStringExtra("txt");
        if (mes.matches("not")) {
            TextView textView = (TextView) findViewById(R.id.txtuser);
            textView.setText("*เนื่องจากบัญชีของคุณเป็นผู้ขายที่ยังไม่มีการลงทะเบียนฟาร์ม จึงพบหน้านี้");
        }

        btn_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = editName.getText().toString().trim();
                String fDes = editDes.getText().toString().trim();
                String fNum = editNum.getText().toString().trim();
                String fAdd = editAdd.getText().toString().trim();
                if (TextUtils.isEmpty(fName) || TextUtils.isEmpty(fDes) || TextUtils.isEmpty(fNum) || TextUtils.isEmpty(fAdd)
                        || fName.length()<3 || fAdd.length()<10) {
                    Toast.makeText(getApplicationContext(), R.string.register_empty_warning, Toast.LENGTH_LONG).show();
                }
                else if (fNum.length() != 13) {
                    Toast.makeText(getApplicationContext(), "กรุณาใส่หมายเลขบัตรประชาชนให้ครบ", Toast.LENGTH_LONG).show();
                }
                else if (uri==null){
                    Toast.makeText(getApplicationContext(), "กรุณาใส่ภาพ", Toast.LENGTH_LONG).show();
                }
                else {
                    loadingBar.setMessage("กรุณารอสักครู่");
                    loadingBar.show();

                    String userID = i.getStringExtra("userID");

                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("farmer");
                    GetFarmer farmerModel = new GetFarmer(fName, fDes, fNum, fAdd, userID);
                    final String fid = databaseReference.push().getKey();
                    editor.putString("farmID",fid);
                    editor.commit();
                    databaseReference.push().setValue(farmerModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            uploadImage(fid);
                            Toast.makeText(RegisterFarm.this, "ลงทะเบียนเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterFarm.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            loadingBar.dismiss();
                        }
                    });
                }
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

    private void uploadImage(final String fID) {
        final String farmID = fID;
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String userID = sp.getString("IDKey", "0");

        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Image").child("farmRegister/" + userID);
        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),
                        "ระบบได้รับภาพแล้ว", Toast.LENGTH_SHORT).show();
            }
        });
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                        .child("farmer").child(farmID);
                databaseReference.child("imgLink").setValue(uri.toString());
            }
        });
        storageRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),
                        "มีข้อผิดพลาดเกิดขึ้น กรุณาลองใหม่อีกครั้ง", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            File f = new File("" + uri);

            String pic =  f.getName();
            TextView PicName = (TextView)findViewById(R.id.picName);
            PicName.setText(pic);
        }
    }
}
