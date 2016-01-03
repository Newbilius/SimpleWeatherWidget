package com.newbilius.simpleweatherwidget.YandexParsingParts;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(strict = false)
public class WeatherInDay {
    @Attribute(name = "date")
    public String Date;
    @ElementList(entry="day_part", inline = true, type=WeatherInPartOfTheDay.class)
    public List<WeatherInPartOfTheDay> DayParts;
}
