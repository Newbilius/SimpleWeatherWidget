package com.newbilius.simpleweatherwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.widget.RemoteViews;
import com.newbilius.simpleweatherwidget.YandexParsingParts.Weather;
import com.newbilius.simpleweatherwidget.YandexParsingParts.WeatherInDay;
import com.newbilius.simpleweatherwidget.YandexParsingParts.WeatherInPartOfTheDay;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GetWeatherAsyncTaskResult implements GetWeatherAsyncTask.IGetWeatherAsyncOnResult {

    private final boolean daysMode;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dayFormat = new SimpleDateFormat("E");
    private int widgetID;
    private Context context;
    private AppWidgetManager appWidgetManager;

    public GetWeatherAsyncTaskResult(int widgetID, Context context, AppWidgetManager appWidgetManager, boolean daysMode) {
        this.widgetID = widgetID;
        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.daysMode = daysMode;
    }

    @Override
    public void OnResult(Weather result) {
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
        WeatherWidgetUpdateHelper weatherWidgetUpdateHelper=new WeatherWidgetUpdateHelper(widgetView);

        widgetView.setTextViewText(R.id.textViewTopLabel, result.City + " - "+result.Days.get(0).DayParts.get(5).weatherType);

        if (daysMode) {
            for (int i=0;i<=3;i++){
                WeatherInDay day=result.Days.get(i);
                weatherWidgetUpdateHelper.UpdateDate(i+1,
                        GetYear(day),
                        GetTemperatureString(day.DayParts.get(5))
                );
            }
        } else {
            Calendar now = GregorianCalendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);

            int startValue=0;
            if (hour > 0 && hour <= 8)
                startValue = 0;
            if (hour > 8 && hour <= 18)
                startValue = 1;
            if (hour > 18)
                startValue = 2;

            for (int i=0;i<=3;i++){
                WeatherInDay day=result.Days.get(i);
            }

            WeatherInPartOfTheDay part=result.Days.get(0).DayParts.get(startValue);
            weatherWidgetUpdateHelper.UpdateDate(1,
                    part.GetPartOfTheDayName(),
                    GetTemperatureString(part)
            );

            for (int i=2;i<=4;i++){
                startValue++;
                part=result.Days.get(GetValuePart1(startValue)).DayParts.get(GetValuePart2(startValue));
                weatherWidgetUpdateHelper.UpdateDate(i,
                        part.GetPartOfTheDayName(),
                        GetTemperatureString(part)
                );
            }
        }
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

    private int GetValuePart1(int value){
        return value <= 3 ? 0 : 1;
    }

    private int GetValuePart2(int value){
        return value <= 3 ? value : value-4;
    }

    private String GetYear(WeatherInDay day) {
        String[] params = day.Date.split("-");
        calendar.set(Integer.parseInt(params[0]), Integer.parseInt(params[1])-1, Integer.parseInt(params[2]));
        String dayName = dayFormat.format(calendar.getTime());
        return dayName.substring(0, 1).toUpperCase() + dayName.substring(1) + ", " + calendar.get(Calendar.DAY_OF_MONTH);
    }

    private String GetTemperatureString(WeatherInPartOfTheDay weather) {
        if (weather.temperatureFrom != null && weather.temperatureTo!= null)
            return GetSign(weather.temperatureFrom) + "..." + GetSign(weather.temperatureTo);

        return GetSign(weather.temperature);
    }

    private String GetSign(String value) {
        return Integer.parseInt(value) > 0 ? "+" + value : value;
    }

    @Override
    public void OnError(String error) {
        //в случае ошибки попробовать снова через 5 минут. Не обновлять виджет.
        WeatherWidget.UpdateTimerOfUpdates(context,System.currentTimeMillis()+60000*5);
    }
}