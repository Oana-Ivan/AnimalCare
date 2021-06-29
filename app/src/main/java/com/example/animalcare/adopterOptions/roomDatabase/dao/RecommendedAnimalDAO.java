package com.example.animalcare.adopterOptions.roomDatabase.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.animalcare.adopterOptions.roomDatabase.entity.RecommendedAnimal;

import java.util.List;

@Dao
public interface RecommendedAnimalDAO {
    @Query("SELECT * FROM recommended_animals")
    List<RecommendedAnimal> getAll();

    @Query("SELECT count(*) FROM recommended_animals")
    int getNoOfRecommendedAnimals();

    @Query("SELECT * FROM recommended_animals WHERE animal_id IN (:animalIds)")
    List<RecommendedAnimal> loadAllByIds(int[] animalIds);

    @Query("SELECT * FROM recommended_animals WHERE adopterID LIKE :adopterID")
    List<RecommendedAnimal> findByAdopterID(String adopterID);

    @Query("SELECT * FROM recommended_animals WHERE animal_id LIKE :animalID LIMIT 1")
    RecommendedAnimal findByAnimalID(String animalID);

    @Query("SELECT * FROM recommended_animals WHERE adopterID LIKE :adopterID AND animal_id LIKE :animalID LIMIT 1")
    List<RecommendedAnimal> findByAdopterIDAndAnimalID(String adopterID, String animalID);

    @Insert
    void insertAll(RecommendedAnimal... recommendedAnimals);

    @Update
    public void update(RecommendedAnimal... recommendedAnimals);

    @Delete
    void delete(RecommendedAnimal savedAnimal);

    @Query("DELETE FROM recommended_animals")
    public void clearTable();
}
