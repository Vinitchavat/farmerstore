package com.kongla.storeapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class AdapterPL extends BaseAdapter {
    ArrayList<String> name;
    Context mContext;
    ArrayList<Integer> productSell;
    ArrayList<String> productID;
    ArrayList<String> imgLink;
    String type;

    public AdapterPL(Context context, ArrayList<String> name, ArrayList<Integer> productSell,
                     ArrayList<String> productID, ArrayList<String> imgLink, String type) {
        this.name = name;
        this.productSell = productSell;
        this.mContext = context;
        this.productID = productID;
        this.imgLink = imgLink;
        this.type = type;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public String getItem(int i) {
        return productID.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.listproduct, viewGroup, false);
            holder = new ViewHolder();
            view.setTag(holder);
        }
        holder.tv_info = (TextView) view.findViewById(R.id.productName);
        holder.tv_info.setText(name.get(i));
        holder.tv_info = (TextView) view.findViewById(R.id.productSell);
        String pSell = productSell.get(i).toString();
        holder.tv_info.setText("มีทั้งหมด " + pSell + " คำสั่งซื้อ");
        holder.imageView = view.findViewById(R.id.productImage);
        new DownloadImageTask(holder.imageView).execute(imgLink.get(i));

        final String id = productID.get(i);
        holder.button = (Button) view.findViewById(R.id.btn_editproduct);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,profileAddProduct.class);
                intent.putExtra("productID",id);
                intent.putExtra("do","edit");
                if (type.matches("preorder")){
                    intent.putExtra("type","preorder");
                }
                else{
                    intent.putExtra("type","market");
                }
                mContext.startActivity(intent);
            }
        });

        return view;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
class ViewHolder {
    TextView tv_info;
    Button button;
    ImageView imageView;
}
