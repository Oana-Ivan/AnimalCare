package com.example.animalcare.roomDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.animalcare.roomDatabase.dao.UserDAO;
import com.example.animalcare.roomDatabase.entity.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDao();
}
