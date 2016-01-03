package com.newbilius.simpleweatherwidget;

import android.os.AsyncTask;
import com.newbilius.simpleweatherwidget.YandexParsingParts.Weather;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class GetWeatherAsyncTask extends AsyncTask<Object, Object, Object> {

    private final String CityId;
    private final IGetWeatherAsyncOnResult onResult;
    private Weather result;

    public GetWeatherAsyncTask(String Id, IGetWeatherAsyncOnResult onResult) {
        CityId = Id;
        this.onResult = onResult;
    }

    @Override
    protected Object doInBackground(Object... params) {

        try {
            URL url = new URL("https://export.yandex.ru/weather-ng/forecasts/" + CityId + ".xml");
            InputStream stream = url.openStream();

            Serializer serializer = new Persister();
            result = serializer.read(Weather.class, stream);

        }catch (IOException e) {
            e.printStackTrace();
            onResult.OnError("Ошибка интернет-соединения.");
        } catch (Exception e) {
            e.printStackTrace();
            onResult.OnError(e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (result != null)
            onResult.OnResult(result);
    }

    public interface IGetWeatherAsyncOnResult {
        void OnResult(Weather result);

        void OnError(String error);
    }
}