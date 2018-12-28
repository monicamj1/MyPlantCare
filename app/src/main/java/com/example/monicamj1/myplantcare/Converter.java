package com.example.monicamj1.myplantcare;

import android.arch.persistence.room.TypeConverter;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class Converter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static List<String> fromString(String str) {
       String[] images = str.split(";");
       return Arrays.asList(images);
    }

    @TypeConverter
    public static String fromList(List<String> li) {
        if(li==null){
            return "";
        }
        return TextUtils.join(";", li);
    }


}