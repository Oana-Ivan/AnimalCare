package com.example.animalcare.adopterOptions.roomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.animalcare.adopterOptions.roomDatabase.dao.RecommendedAnimalDAO;
import com.example.animalcare.adopterOptions.roomDatabase.dao.SavedAnimalDAO;
import com.example.animalcare.adopterOptions.roomDatabase.entity.RecommendedAnimal;
import com.example.animalcare.adopterOptions.roomDatabase.entity.SavedAnimal;

@Database(entities = {SavedAnimal.class, RecommendedAnimal.class}, version = 1, exportSchema = false)
public abstract class AdopterOptionsDatabase extends RoomDatabase {
    public abstract SavedAnimalDAO savedAnimalDAO();
    public abstract RecommendedAnimalDAO recommendedAnimalDAO();

}
