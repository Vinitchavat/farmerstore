package com.kongla.storeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
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
    int Fcount = 0;

    public CustomAdapterForPreorder(Context context, ArrayList<String> clubkey) {
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

        final TextView date = (TextView) view.findViewById(R.id.text1);
        date.setText(setDate(clubkey.get(position)));

        ImageView img1 = view.findViewById(R.id.img1);
        ImageView img2 = view.findViewById(R.id.img2);
        ImageView img3 = view.findViewById(R.id.img3);
        ImageView img4 = view.findViewById(R.id.img4);
        ImageView img5 = view.findViewById(R.id.img5);
        ImageView img6 = view.findViewById(R.id.img6);

        final ImageView[] lay = {img1, img2, img3, img4, img5, img6};
        final int[] fruit = {R.drawable.mangosteen, R.drawable.longan, R.drawable.orange, R.drawable.mango, R.drawable.rambutan, R.drawable.durian
                , R.drawable.lychee, R.drawable.grape, R.drawable.dragonfruit, R.drawable.pineapple};

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        callPre = database.getReference().child("product").child("preorderProduct").child(clubkey.get(position));
        callPre.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fruitName.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    getData = d.getValue(GetData.class);
                    fruitName.add(getData.getFruitName());
                }
                for (int count = 0; count < fruitName.size(); count++) {
                    for (int countIn = count + 1; countIn < fruitName.size(); countIn++) {
                        if (fruitName.get(count).matches(fruitName.get(countIn))) {
                            fruitName.remove(countIn);
                        }
                    }
                }
                Fcount = fruitName.size();
                if (fruitName.size() >= 6) {
                    Fcount = 6;
                }
                for (int count = 0; count < Fcount; count++) {
                    if (fruitName.get(count).matches("มังคุด")) {
                        lay[count].setImageResource(fruit[0]);
                    } else if (fruitName.get(count).matches("ลำไย")) {
                        lay[count].setImageResource(fruit[1]);
                    } else if (fruitName.get(count).matches("ส้ม")) {
                        lay[count].setImageResource(fruit[2]);
                    } else if (fruitName.get(count).matches("มะม่วง")) {
                        lay[count].setImageResource(fruit[3]);
                    } else if (fruitName.get(count).matches("เงาะ")) {
                        lay[count].setImageResource(fruit[4]);
                    } else if (fruitName.get(count).matches("ทุเรียน")) {
                        lay[count].setImageResource(fruit[5]);
                    } else if (fruitName.get(count).matches("ลิ้นจี่")) {
                        lay[count].setImageResource(fruit[6]);
                    } else if (fruitName.get(count).matches("องุ่น")) {
                        lay[count].setImageResource(fruit[7]);
                    } else if (fruitName.get(count).matches("แก้วมังกร")) {
                        lay[count].setImageResource(fruit[8]);
                    } else {
                        lay[count].setImageResource(fruit[9]);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    public String setDate(String date) {
        String d = date.substring(date.lastIndexOf("-") + 1);
        String m = date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
        String month;
        String y = date.substring(0, date.indexOf("-"));
        if (m.substring(0,1).matches("0")){
            m = m.substring(1);
        }
        if (d.substring(0,1).matches("0")){
            d = d.substring(1);
        }
        switch (m) {
            case "1":
                month = "มกราคม";
                break;
            case "2":
                month = "กุมภาพันธ์";
                break;
            case "3":
                month = "มีนาคม";
                break;
            case "4":
                month = "เมษายน";
                break;
            case "5":
                month = "พฤษภาคม";
                break;
            case "6":
                month = "มิถุนายน";
                break;
            case "7":
                month = "กรกฎาคม";
                break;
            case "8":
                month = "สิงหาคม";
                break;
            case "9":
                month = "กันยายน";
                break;
            case "10":
                month = "ตุลาคม";
                break;
            case "11":
                month = "พฤศจิกายน";
                break;
            case "12":
                month = "ธันวาคม";
                break;
            default:
                month = m;
        }
        String wtd = d + " " + month + " " + y;
        return wtd;
    }
}
