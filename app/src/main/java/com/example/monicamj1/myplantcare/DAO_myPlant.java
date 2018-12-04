package com.example.monicamj1.myplantcare;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DAO_myPlant {

    @Insert
    void insertPnat(Plant plant);

    @Query("SELECT * FROM MyPlants WHERE myPlant_id = :id_plant")
    List<Plant> loadPlantById(int id_plant);

    @Query("SELECT * FROM MyPlants")
    List<Plant> loadAllMyPlants();

    @Update
    void updatePlant(Plant plant);

    @Delete
    void deletePlant(Plant plant);
}
