package com.newbilius.simpleweatherwidget.Options;

import android.os.AsyncTask;
import com.newbilius.simpleweatherwidget.YandexParsingParts.City;
import com.newbilius.simpleweatherwidget.YandexParsingParts.Countries;
import com.newbilius.simpleweatherwidget.YandexParsingParts.WeatherCountriesItem;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetCityAsyncTask extends AsyncTask<Object, Object, Object> {

    private final IGetCityAsyncResult getCityAsyncResult;
    public List<City> cities;

    public GetCityAsyncTask(IGetCityAsyncResult getCityAsyncResult) {
        this.getCityAsyncResult = getCityAsyncResult;
    }

    @Override
    protected Object doInBackground(Object... params) {

        try {
            URL url = new URL("https://pogoda.yandex.ru/static/cities.xml");
            InputStream stream = url.openStream();

            Serializer serializer = new Persister();
            Countries result = serializer.read(Countries.class, stream);

            cities = new ArrayList<>();

            for (WeatherCountriesItem weather : result.Countries)
                for (City city : weather.City)
                    cities.add(city);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        getCityAsyncResult.GetData(cities);
    }

    public interface IGetCityAsyncResult {
        void GetData(List<City> cites);
    }
}