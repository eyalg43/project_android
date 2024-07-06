package com.example.project_android;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converters {

    @TypeConverter
    public String fromList(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : list) {
            if (s != null && !s.isEmpty()) {
                stringBuilder.append(s).append(",");
            }
        }
        // Check if stringBuilder length is greater than zero before removing the last comma
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    @TypeConverter
    public List<String> fromString(String value) {
        if (value == null || value.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(value.split(",")));
    }
}
