package com.newbilius.simpleweatherwidget.YandexParsingParts;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(strict = false)
public class City {
    @Attribute(name = "country")
    public String Country;
    @Attribute(name = "id")
    public String Id;
    @Text
    public String Name;
}
