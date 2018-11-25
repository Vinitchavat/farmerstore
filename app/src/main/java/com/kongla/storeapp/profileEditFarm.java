package com.kongla.storeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class profileEditFarm extends AppCompatActivity {
    private EditText editFarmName, editFarmDes;
    private String farmName, farmDes;
    private TextView txtFarmNum;
    private static String farmID;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editfarm);

        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        editor = sp.edit();
        final String userID = sp.getString("IDKey", "0");

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("farmer");
        Query q = database.orderByChild("memberID").equalTo(userID);
        editFarmName = findViewById(R.id.editFarmName);
        editFarmDes = findViewById(R.id.editFarmDes);
        txtFarmNum = findViewById(R.id.farmerNum);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()){
                    editFarmName.setText(d.child("farmName").getValue().toString());
                    txtFarmNum.setText(d.child("farmerNumber").getValue().toString());
                    editFarmDes.setText(d.child("farmDescription").getValue().toString());
                    farmID = d.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        farmName = editFarmName.getText().toString().trim();
        farmDes = editFarmDes.getText().toString().trim();
        if (id == R.id.actionbarcheck && farmName.length() >= 5 && farmDes.length() >= 5) {
            AlertDialog.Builder builder = new AlertDialog.Builder(profileEditFarm.this);
            builder.setMessage("ยืนยันการแก้ไข");
            builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                            .child("farmer").child(farmID);
                    database.child("farmName").setValue(farmName);
                    database.child("farmDescription").setValue(farmDes);
                    Toast.makeText(getApplicationContext(),
                            "การแก้ไขเสร็จสิ้น", Toast.LENGTH_SHORT).show();
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
        else if (id == R.id.actionbarcheck){
            Toast.makeText(getApplicationContext(), "กรุณากรอกข้อมูลให้ครบถ้วน"
                    , Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "กดปุ่มกลับอีกครั้ง เพื่อไปยังหน้าตั้งค่าผู้ขาย", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
