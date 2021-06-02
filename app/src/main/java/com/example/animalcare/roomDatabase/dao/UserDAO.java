package com.example.animalcare.roomDatabase.dao;

import androidx.room.*;

import com.example.animalcare.roomDatabase.entity.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT count(*) FROM users")
    int getNoOfUsers();

    @Query("SELECT * FROM users WHERE user_id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM users WHERE username LIKE :username LIMIT 1")
    User findByUsername(String username);

    @Insert
    void insertAll(User... users);

    @Update
    public void update(User... users);

    @Delete
    void delete(User user);
}
