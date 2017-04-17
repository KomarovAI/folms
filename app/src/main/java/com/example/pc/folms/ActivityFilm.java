package com.example.pc.folms;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.example.pc.folms.R.id.btn1;
import static com.example.pc.folms.R.id.textView;

/**
 * Created by PC on 15.04.2017.
 */

public class ActivityFilm extends AppCompatActivity  implements View.OnClickListener{

    ImageView imgHead;
    TextView filmName;
    TextView textView;
    TextView textView2;
    Bitmap bitmap = null;
    Button button;
    MyTask mt;
    Elements str; //общая строка для считывания веб текста
    Elements str2;
    Elements str3;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        imgHead = (ImageView) findViewById(R.id.headImg);
        filmName = (TextView) findViewById(R.id.nameFilm);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener((View.OnClickListener) this);

        //Задаю картинки на кнопки с веба GetImg описан в отдельном классе файле
        GetImg getImg = new GetImg(this);
        getImg.execute("http://topfilmec.ddns.net/img/0001-1.png");//ссылка на картинку
        try {
            bitmap = getImg.get();
        } catch (Exception e) {
        }
        imgHead.setImageBitmap(bitmap);

        mt = new MyTask();
        mt.execute();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, ActivityPlayer.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //Сторонний поток для задания текста с сайта
    class MyTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            try {
                doc = Jsoup.connect("http://topfilmec.ddns.net/0001.html").get();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            str = doc.select("h2");
            str2 = doc.select("h5");
            str3 = doc.select("p");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            filmName.setText(str.text());
            textView.setText(str2.text());
            textView2.setText(str3.text());
        }
    }
}
