package com.atc.navigator.utils;

import android.location.Location;

public class CoordinatesUtil {

    public static String getLatString(Location location) {
        String value = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS);
        value = replaceDelimiters(value);
        if (location.getLatitude() < 0) {
            value += "S";
        } else {
            value += "N";
        }
        return value;
    }

    public static String getLongString(Location location) {
        String value = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS);
        value = replaceDelimiters(value);
        if (location.getLongitude() < 0) {
            value += "W";
        } else {
            value += "E";
        }
        return value;
    }

    public static String replaceDelimiters(String str) {
        str = str.replaceFirst(":", "°");
        str = str.replaceFirst(":", "'");
        int pointIndex = str.indexOf(".");
        int endIndex = pointIndex + 2;
        if (endIndex < str.length()) {
            str = str.substring(0, endIndex);
        }
        str = str + "\"";
        return str;
    }
}
