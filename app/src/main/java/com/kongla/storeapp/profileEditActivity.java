package com.kongla.storeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class profileEditActivity extends AppCompatActivity {
    private String data, head, txt;
    private TextView attr;
    private EditText editText;
    private int editTextLength;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_editactivity);

        data = getIntent().getStringExtra("data");
        txt = getIntent().getStringExtra("txt");
        attr = (TextView) findViewById(R.id.editProfileText);
        editText = (EditText) findViewById(R.id.editProfileEditText);

        editText.setText(txt);

        /* ** SET condition for user input ** */
        if(data.matches("phone")){
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            editText.setMaxLines(1);
            head = "แก้ไขเบอร์โทร";
            editTextLength = 10;
        }
        else if(data.matches("name")){
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            editText.setMaxLines(1);
            head = "แก้ไขชื่อ-นามสกุล";
            editTextLength = 3;
        }
        else if(data.matches("address")){
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
                    |InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            editText.setMaxLines(4);
            head = "แก้ไขที่อยู่";
            editTextLength = 10;
        }
        attr.setText(head);

        /* Action Bar */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* ** GET userID from shared preferences ** */
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String userID = sp.getString("IDKey", "0");

        int id = item.getItemId();
        final String userInput = editText.getText().toString().trim();

        if (id == R.id.actionbarcheck && userInput.length() >= editTextLength) {
            AlertDialog.Builder builder = new AlertDialog.Builder(profileEditActivity.this);
            builder.setMessage("ยืนยันการแก้ไข");
            builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child("Users").child(userID).child(data).setValue(userInput);
                    Toast.makeText(getApplicationContext(),
                            "การแก้ไขเสร็จสิ้น", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), profileEditProfile.class);
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
}
