package com.example.pc.folms;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetImg extends AsyncTask<String,Void,Bitmap> {

    private String src;
    Context mcontext;

    public GetImg(Context context){
        mcontext = context;
    }

    @Override
    protected Bitmap doInBackground(String... path) {
        src = path[0];

        try {//возвращаем битмапу
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {//Делаем что-то если не получилась загрузка
            Bitmap icon = BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.ic_drawer);
            return icon;
        }

    }
}