package com.example.animalcare.roomDatabase.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.animalcare.roomDatabase.entity.Volunteer;

import java.util.List;

@Dao
public interface VolunteerDAO {
    @Query("SELECT * FROM volunteers")
    List<Volunteer> getAll();

    @Query("SELECT count(*) FROM volunteers")
    int getNoOfVolunteers();

    @Query("SELECT * FROM volunteers WHERE volunteer_id IN (:volunteerIds)")
    List<Volunteer> loadAllByIds(int[] volunteerIds);

    @Query("SELECT * FROM volunteers WHERE username LIKE :username LIMIT 1")
    Volunteer findByUsername(String username);

    @Insert
    void insertAll(Volunteer... volunteer);

    @Update
    public void update(Volunteer... volunteer);

    @Delete
    void delete(Volunteer volunteer);
}
