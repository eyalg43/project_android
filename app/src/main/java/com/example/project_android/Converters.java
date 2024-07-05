package com.example.project_android;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public String fromList(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            stringBuilder.append(s).append(",");
        }
        // Remove the last comma
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    @TypeConverter
    public List<String> fromString(String value) {
        return new ArrayList<>(Arrays.asList(value.split(",")));
    }
}
