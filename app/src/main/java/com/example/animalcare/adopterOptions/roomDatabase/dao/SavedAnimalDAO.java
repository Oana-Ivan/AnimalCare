package com.example.animalcare.adopterOptions.roomDatabase.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.animalcare.adopterOptions.roomDatabase.entity.SavedAnimal;
import com.example.animalcare.roomDatabase.entity.User;

import java.util.List;

@Dao
public interface SavedAnimalDAO {
    @Query("SELECT * FROM saved_animals")
    List<SavedAnimal> getAll();

    @Query("SELECT count(*) FROM saved_animals")
    int getNoOfSavedAnimals();

    @Query("SELECT * FROM saved_animals WHERE animal_id IN (:animalIds)")
    List<SavedAnimal> loadAllByIds(int[] animalIds);

    @Query("SELECT * FROM saved_animals WHERE adopterID LIKE :adopterID")
    List<SavedAnimal> findByAdopterID(String adopterID);

    @Query("SELECT * FROM saved_animals WHERE animal_id LIKE :animalID LIMIT 1")
    SavedAnimal findByAnimalID(String animalID);

    @Insert
    void insertAll(SavedAnimal... saved_animals);

    @Update
    public void update(SavedAnimal... saved_animals);

    @Delete
    void delete(SavedAnimal savedAnimal);
}
