package com.newbilius.simpleweatherwidget.YandexParsingParts;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(strict = false)
public class WeatherCountriesItem {
    @ElementList(entry="city", inline = true, type= City.class)
    public List<City> City;
}
