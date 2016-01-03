package com.newbilius.simpleweatherwidget.Options;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.newbilius.simpleweatherwidget.R;
import com.newbilius.simpleweatherwidget.YandexParsingParts.City;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends BaseAdapter {

    List<City> cities;
    List<City> citiesFiltered;
    LayoutInflater lInflater;

    public CityListAdapter(Context context, List<City> cities) {
        this.cities = cities;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Filter("");
    }

    public void Filter(String text) {
        List<City> citiesFilteredNew = new ArrayList<>();

        for (City city : cities)
            if (city.Name.toLowerCase().contains(text.toLowerCase()))
                citiesFilteredNew.add(city);

        citiesFiltered = citiesFilteredNew;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return citiesFiltered.size();
    }

    @Override
    public City getItem(int position) {
        return citiesFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) view = lInflater.inflate(R.layout.city_list_item, viewGroup, false);

        TextView textViewText = (TextView) view.findViewById(R.id.textViewText);
        City city = citiesFiltered.get(position);
        textViewText.setText(String.format("%s [%s]",city.Name,city.Country));

        return view;
    }
}