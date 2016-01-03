package com.newbilius.simpleweatherwidget.YandexParsingParts;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name = "cities", strict = false)
public class Countries {
    @ElementList(entry = "country", inline = true, type = WeatherCountriesItem.class)
    public List<WeatherCountriesItem> Countries;
}
