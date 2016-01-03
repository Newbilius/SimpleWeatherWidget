package com.newbilius.simpleweatherwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.newbilius.simpleweatherwidget.Options.OptionsActivity;
import com.newbilius.simpleweatherwidget.YandexParsingParts.WeatherInDay;

public class WeatherWidget extends AppWidgetProvider {

    static final String UPDATE_ALL_WIDGETS = "update_all_widgets";

    public static void UpdateWidget(Context context, AppWidgetManager appWidgetManager,  int widgetID) {
        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
        WeatherWidgetUpdateHelper weatherWidgetUpdateHelper=new WeatherWidgetUpdateHelper(widgetView);
        ConfigKeeper configKeeper=new ConfigKeeper(context);

        widgetView.setTextViewText(R.id.textViewTopLabel, configKeeper.getCityName(widgetID) + " - загрузка данных");

        for (int i=1;i<=4;i++)
            weatherWidgetUpdateHelper.UpdateDate(i, "", "");

        Intent configIntent = new Intent(context, OptionsActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pIntent = PendingIntent.getActivity(context, widgetID, configIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        widgetView.setOnClickPendingIntent(R.id.widgetId, pIntent);

        appWidgetManager.updateAppWidget(widgetID, widgetView);

        if (configKeeper.haveCityId(widgetID))
            new GetWeatherAsyncTask(
                    configKeeper.getCityId(widgetID),
                    new GetWeatherAsyncTaskResult(widgetID, context, appWidgetManager, configKeeper.getIs4DaysMode(widgetID))
            ).execute();
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        UpdateTimerOfUpdates(context,System.currentTimeMillis());
    }

    public static void UpdateTimerOfUpdates(Context context,long triggerInTimeInMs) {
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC,
                triggerInTimeInMs,
                60000 * 60, pIntent);//раз в час
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int id : appWidgetIds)
            UpdateWidget(context, appWidgetManager, id);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        new ConfigKeeper(context).delete(appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(context);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int id : appWidgetIds)
                UpdateWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }
}