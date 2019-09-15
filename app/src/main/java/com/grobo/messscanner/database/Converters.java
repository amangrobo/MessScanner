package com.grobo.messscanner.database;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public static String stringFromArray(List<String> strings) {
        if (strings != null) {
            StringBuilder string = new StringBuilder();
            for (String s : strings) string.append(s).append(",");

            return string.toString();
        }
        return "";
    }

    @TypeConverter
    public static List<String> arrayFromString(String concatenatedStrings) {

        if (concatenatedStrings != null) {
            return new ArrayList<>(Arrays.asList(concatenatedStrings.split(",")));
        }
        return null;
    }

    @TypeConverter
    public static String stringFromLongArray(List<Long> strings) {
        if (strings != null) {
            StringBuilder string = new StringBuilder();
            for (Long s : strings) string.append(s).append(",");

            return string.toString();
        }
        return "";
    }

    @TypeConverter
    public static List<Long> longArrayFromString(String concatenatedStrings) {

        if (concatenatedStrings != null) {

            List<String> a = Arrays.asList(concatenatedStrings.split(","));

            List<Long> ret = new ArrayList<>();
            for (String b : a) {
                ret.add(Long.parseLong(b));
            }

            return ret;
        }
        return null;
    }

}