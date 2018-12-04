package com.example.monicamj1.myplantcare;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Plant.class}, version = 1, exportSchema = false)
@TypeConverters({Converter.class})
public abstract class AppDatabase extends RoomDatabase{
    public abstract DAO_myPlant plantDao();
}
