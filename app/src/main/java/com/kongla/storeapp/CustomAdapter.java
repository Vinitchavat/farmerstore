package com.kongla.storeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> Mes, sender, type;
    ImageView imageView;
    String user;


    public CustomAdapter(Context context, ArrayList<String> Mes, ArrayList<String> sender, ArrayList<String> type, String user) {
        this.mContext = context;
        this.Mes = Mes;
        this.sender = sender;
        this.type = type;
        this.user = user;
    }

    @Override
    public int getCount() {
        return Mes.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (user.matches("buyer")) {
            if (type.get(position).equals("Message")) {
                if (sender.get(position).equals("buyer")) {
                    LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = mInflater.inflate(R.layout.layout, parent, false);
                    TextView textView = (TextView) view.findViewById(R.id.message_text);
                    textView.setText(Mes.get(position));
                    return view;
                } else {
                    LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = mInflater.inflate(R.layout.layoutsend, parent, false);
                    TextView textView = (TextView) view.findViewById(R.id.text_message);
                    textView.setText(Mes.get(position));
                    return view;
                }

            } else {
                if (sender.get(position).equals("buyer")) {
                    LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = mInflater.inflate(R.layout.picleft, parent, false);
                    final ImageView imageView = (ImageView) view.findViewById(R.id.imgView);
                /*FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference storageReference = firebaseStorage.getReference();
                storageReference.child("Image").child("all/" + Mes.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {*/
                    new DownloadImageTask(imageView).execute(Mes.get(position));
                    return view;
                } else {
                    Uri uri;
                    LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = mInflater.inflate(R.layout.picright, parent, false);
                    final ImageView imageView = (ImageView) view.findViewById(R.id.imgView);
                    new DownloadImageTask(imageView).execute(Mes.get(position));
                    return view;
                }
            }
        }
        else {
            if (type.get(position).equals("Message")) {
                if (sender.get(position).equals("sender")) {
                    LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = mInflater.inflate(R.layout.layout, parent, false);
                    TextView textView = (TextView) view.findViewById(R.id.message_text);
                    textView.setText(Mes.get(position));
                    return view;
                } else {
                    LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = mInflater.inflate(R.layout.layoutsend, parent, false);
                    TextView textView = (TextView) view.findViewById(R.id.text_message);
                    textView.setText(Mes.get(position));
                    return view;
                }

            } else {
                if (sender.get(position).equals("sender")) {
                    LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = mInflater.inflate(R.layout.picleft, parent, false);
                    final ImageView imageView = (ImageView) view.findViewById(R.id.imgView);
                    new DownloadImageTask(imageView).execute(Mes.get(position));
                    return view;
                } else {
                    Uri uri;
                    LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = mInflater.inflate(R.layout.picright, parent, false);
                    final ImageView imageView = (ImageView) view.findViewById(R.id.imgView);
                    new DownloadImageTask(imageView).execute(Mes.get(position));
                    return view;
                }
            }
        }
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

