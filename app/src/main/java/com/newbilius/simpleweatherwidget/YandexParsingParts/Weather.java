package com.newbilius.simpleweatherwidget.YandexParsingParts;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "forecast", strict = false)
public class Weather {
    @Attribute(name = "city")
    public String City;
    @ElementList(entry = "day", inline = true, type = WeatherInDay.class)
    public List<WeatherInDay> Days;
}