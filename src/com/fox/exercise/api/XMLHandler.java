package com.fox.exercise.api;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.fox.exercise.api.entity.WeatherForecast;

public class XMLHandler extends DefaultHandler {
    private static final String TAG = "XMLHandler";
    private WeatherForecast currentWeather;
    private String content;

    public WeatherForecast getCurrentWeather() {
        return currentWeather;
    }

    public XMLHandler() {
        currentWeather = new WeatherForecast();
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        // 获得标签中的文本
        content = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        String tagName = localName.length() != 0 ? localName : qName;
        tagName = tagName.toLowerCase();
        if (tagName.equals("temperature1")) {
            Log.d(TAG, "temphigh" + content);
            currentWeather.temp_tomorrow_high = content;
        } else if (tagName.equals("temperature2")) {
            Log.d(TAG, "templow" + content);
            currentWeather.temp_tomorrow_low = content;
        } else if (tagName.equals("status1")) {
            Log.d(TAG, "weather" + content);
            currentWeather.weather_tomorrow = content;
        }
    }
}