package com.newbilius.simpleweatherwidget.YandexParsingParts;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class WeatherInPartOfTheDay {
    @Attribute(name="typeid")
    private String typeId;

    @Element(name="temperature_from",required = false)
    public String temperatureFrom;
    @Element(name="temperature_to",required = false)
    public String temperatureTo;
    @Element(name="weather_type")
    public String weatherType;
    @Element(required = false)
    public String temperature;

    public String GetPartOfTheDayName() {
        if (typeId.equals("1"))
            return "Утро";
        if (typeId.equals("2"))
            return "День";
        if (typeId.equals("3"))
            return "Вечер";
        if (typeId.equals("4"))
            return "Ночь";
        if (typeId.equals("5"))
            return "Весь день";
        if (typeId.equals("6"))
            return "Вся ночь";
        return "???";
    }
}