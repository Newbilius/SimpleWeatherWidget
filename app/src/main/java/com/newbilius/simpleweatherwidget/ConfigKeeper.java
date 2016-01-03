package com.newbilius.simpleweatherwidget;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigKeeper {
    private final Context context;
    private final SharedPreferences sharedPreferences;

    public ConfigKeeper(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("OptionsActivity", Context.MODE_PRIVATE);
    }

    public void Save(WidgetConfigItem data){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getCityIdPreferenceName(data.WidgetId), data.CityId);
        editor.putString(getCityNamePreferenceName(data.WidgetId), data.CityName);
        editor.putBoolean(get4DayModePreferenceName(data.WidgetId), data.Is4DaysMode);
        editor.apply();
    }

    public String getCityName(int widgetId){
        return sharedPreferences.getString(getCityNamePreferenceName(widgetId), "");
    }

    public String getCityId(int widgetId){
        return sharedPreferences.getString(getCityIdPreferenceName(widgetId), "");
    }

    public boolean haveCityId(int widgetId){
        return !sharedPreferences.getString(getCityIdPreferenceName(widgetId), "").isEmpty();
    }

    public boolean getIs4DaysMode(int widgetId){
        return sharedPreferences.getBoolean(get4DayModePreferenceName(widgetId),false);
    }

    public void delete(int[] widgetIds){
        SharedPreferences.Editor editor = context.getSharedPreferences("OptionsActivity", Context.MODE_PRIVATE).edit();
        for (int widgetID : widgetIds) {
            editor.remove(getCityIdPreferenceName(widgetID));
            editor.remove(getCityNamePreferenceName(widgetID));
        }
        editor.apply();
    }

    private String getCityIdPreferenceName(int widgetId){
        return widgetId + "CityId";
    }

    private String getCityNamePreferenceName(int widgetId){
        return widgetId + "CityName";
    }

    private String get4DayModePreferenceName(int widgetId){
        return widgetId + "4days";
    }
}