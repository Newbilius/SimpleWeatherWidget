package com.newbilius.simpleweatherwidget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity {
    TextView textSiteYandex;
    Button buttonEmail, buttonSite, buttonPrograms;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        buttonEmail = (Button) findViewById(R.id.buttonEmail);
        buttonSite = (Button) findViewById(R.id.buttonSite);
        textSiteYandex = (TextView) findViewById(R.id.textSiteYandex);
        buttonPrograms = (Button) findViewById(R.id.buttonPrograms);

        textSiteYandex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://pogoda.yandex.ru")));
            }
        });
        buttonSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://moikrug.ru/newbilius")));
            }
        });
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"newbilius@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Простой погодный виджет");
                startActivity(Intent.createChooser(emailIntent, "Отправить письмо автору"));
            }
        });
        buttonPrograms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=%D0%94%D0%BC%D0%B8%D1%82%D1%80%D0%B8%D0%B9+%D0%9C%D0%BE%D0%B8%D1%81%D0%B5%D0%B5%D0%B2")));
            }
        });
    }
}