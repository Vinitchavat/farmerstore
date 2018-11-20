package com.kongla.storeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomAdapterForPreorder extends BaseAdapter {
    Context mContext;
    ArrayList<String> clubkey = new ArrayList<>();

    public DatabaseReference callPre;
    GetData getData;
    ArrayList<String> fruitName = new ArrayList<String>();

    public CustomAdapterForPreorder(Context context, ArrayList<String> clubkey){
        this.mContext = context;
        this.clubkey = clubkey;
    }
    @Override
    public int getCount() {
        return clubkey.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        final View view;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.listday, parent, false);

        final TextView date = (TextView)view.findViewById(R.id.text1);
        date.setText(""+clubkey.get(position));

        ImageView img1 = view.findViewById(R.id.img1);
        ImageView img2 = view.findViewById(R.id.img2);
        ImageView img3 = view.findViewById(R.id.img3);
        ImageView img4 = view.findViewById(R.id.img4);
        ImageView img5 = view.findViewById(R.id.img5);
        ImageView img6 = view.findViewById(R.id.img6);

        final ImageView[] lay = {img1,img2,img3,img4,img5,img6};
        final int[] fruit = {R.drawable.mangosteen, R.drawable.longan, R.drawable.orange, R.drawable.pineapple, R.drawable.rambutan, R.drawable.durian};

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        callPre = database.getReference().child("product").child("preorderProduct").child(clubkey.get(position));
        callPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fruitName.clear();
                int countxx = 0;
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    getData = d.getValue(GetData.class);
                    fruitName.add(getData.getFruitName());
                }
                for(int count = 0 ; count<fruitName.size();count++){
                    for(int countIn = count+1 ; countIn<fruitName.size();countIn++){
                        if(fruitName.get(count).matches(fruitName.get(countIn))){
                            fruitName.remove(countIn);
                        }
                        else {
                            countxx++;
                        }
                    }
                }
                for(int count = 0 ; count<fruitName.size();count++){
                    if(fruitName.get(count).matches("ทุเรียน")){
                        lay[count].setImageResource(fruit[5]);
                    }
                    else if(fruitName.get(count).matches("ลำไย")){
                        lay[count].setImageResource(fruit[1]);
                    }
                    else if(fruitName.get(count).matches("แตงโม")){
                        lay[count].setImageResource(fruit[3]);
                    }
                    else if(fruitName.get(count).matches("มังคุด")){
                        lay[count].setImageResource(fruit[0]);
                    }
                    else if(fruitName.get(count).matches("เงาะ")){
                        lay[count].setImageResource(fruit[4]);
                    }
                    else {
                        lay[count].setImageResource(fruit[2]);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    return view;
    }
}
