package com.fox.exercise.api;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.fox.exercise.location.SportWeatherInformation;

public class XMLHandlerNew extends DefaultHandler {
    private static final String XMLHandler_TAG = "XMLHandler";
    private SportWeatherInformation currentWeather;
    private String content;

    public SportWeatherInformation getCurrentWeather() {
        return currentWeather;
    }

    public XMLHandlerNew() {
        currentWeather = new SportWeatherInformation();
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        content = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        String tagName = localName.length() != 0 ? localName : qName;
        tagName = tagName.toLowerCase();
        if (tagName.equals("temperature1")) {
            Log.d(XMLHandler_TAG, "Temp_high--------" + content);
            currentWeather.setTemp_high(content);
        } else if (tagName.equals("temperature2")) {
            Log.d(XMLHandler_TAG, "Temp_low--------" + content);
            currentWeather.setTemp_low(content);
        } else if (tagName.equals("status1")) {
            Log.d(XMLHandler_TAG, "weather" + content);
            currentWeather.setWeather(content);
        }
    }
}