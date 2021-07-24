package com.example.our_stories.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("select * from User")
    List<User> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... users);

    @Query("select * from User where id = :id")
    User getUserById(String id);

    @Query("select * from User where username = :username")
    User getUserByUsername(String username);

    @Query("select * from User where email = :email")
    User getUserByEmail(String email);
}

