package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import com.github.library.bubbleview.BubbleTextView;


public class chatNew extends AppCompatActivity {

    TextView chat, textView2;
    EditText text;
    public DatabaseReference  testapp1, testapp2;
    public ListView list;
    ArrayList<String> Mes = new ArrayList<String>();
    ArrayList<String> sender = new ArrayList<String>();
    ArrayList<String> type = new ArrayList<String>();
    Context context;
    public CustomAdapter adapter;
    String order;
    public static final int PICK_IMAGE = 1;
    public Uri uri;
    String photoURL;
    String UserID;
    ProgressBar progressBar;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_new);

        progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.GONE);
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String s = sp.getString("Status","none");
        final String IDKey = sp.getString("IDKey", "0");

        text = (EditText) findViewById(R.id.editText);

        Bundle extras = getIntent().getExtras();
        order = extras.getString("orderid");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        testapp1 = database.getReference().child("chat").child(order);
        testapp1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mes.clear();
                sender.clear();
                type.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Friendly m = d.getValue(Friendly.class);
                    Mes.add(m.getMessage());
                    sender.add(m.Sender());
                    type.add(m.type());
                }
                ListView list = (ListView) findViewById(R.id.list);
                adapter = new CustomAdapter(getApplicationContext(), Mes, sender, type, s);
                list.setAdapter(adapter);
                list.setStackFromBottom(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                chat.setText("Data not founded!");
            }
        });

        /* Action Bar */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("ติดต่อผู้ซื้อ/ผู้ขาย");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button selectImg = (Button) findViewById(R.id.btnChoose);
        selectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);
                String pickTitle = "Select or take a new Picture";
                Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });
        Button uploadImg = (Button) findViewById(R.id.btnUpload);
        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE); //to show
                UserID = UUID.randomUUID().toString();
                uploadImage();
                ImageView imageView = (ImageView)findViewById(R.id.imgmain);
                imageView.setImageURI(null);
            }
        });
    }
    private void getImage(){
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child("Image").child("all/" + UserID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                photoURL = uri.toString();
                UploadData(photoURL);
            }
        });
    }

    private void uploadImage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Image").child("all/" + UserID);

        storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                getImage();
                progressBar.setVisibility(View.GONE); // to hide
            }
        });
        storageRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UploadData(String photoURL) {
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String s = sp.getString("Status","none");
        Date currentTime = Calendar.getInstance().getTime();
        String photo = UserID;
        String time = currentTime.toString();
        Friendly friendly = new Friendly(s, photoURL, time,"Pic");
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String ordersend;
        Bundle extras = getIntent().getExtras();
        ordersend = extras.getString("orderid");

        testapp2 = database.getReference().child("chat").child(ordersend);
        testapp2.push().setValue(friendly);
    }
    public void addData(View view) {
        sp = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        final String s = sp.getString("Status","none");
        Date currentTime = Calendar.getInstance().getTime();
        String mes = text.getText().toString();
        String time = currentTime.toString();
        Friendly friendly = new Friendly(s, mes, time,"Message");
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String ordersend;
        Bundle extras = getIntent().getExtras();
        ordersend = extras.getString("orderid");

        testapp2 = database.getReference().child("chat").child(ordersend);
        testapp2.push().setValue(friendly);

        text.setText(null);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            super.onActivityResult(requestCode, resultCode, data);
            uri = data.getData();
            ImageView imageView = (ImageView)findViewById(R.id.imgmain);
            imageView.setImageURI(uri);
        }
    }
}


