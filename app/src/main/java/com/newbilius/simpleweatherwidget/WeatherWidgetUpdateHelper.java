package com.newbilius.simpleweatherwidget;

import android.widget.RemoteViews;

public class WeatherWidgetUpdateHelper {
    private final RemoteViews widgetView;

    public WeatherWidgetUpdateHelper(RemoteViews widgetView){
        this.widgetView = widgetView;
    }

    public void UpdateDate(int numberOfColumn,String title,String text){
        int idOfTitle=0;
        int idOfText=0;
        if (numberOfColumn==1)
        {
            idOfTitle=R.id.textViewDate1;
            idOfText=R.id.textViewTemperature1;
        }
        if (numberOfColumn==2)
        {
            idOfTitle=R.id.textViewDate2;
            idOfText=R.id.textViewTemperature2;
        }
        if (numberOfColumn==3)
        {
            idOfTitle=R.id.textViewDate3;
            idOfText=R.id.textViewTemperature3;
        }
        if (numberOfColumn==4)
        {
            idOfTitle=R.id.textViewDate4;
            idOfText=R.id.textViewTemperature4;
        }

        widgetView.setTextViewText(idOfTitle, title);
        widgetView.setTextViewText(idOfText,text);
    }
}
