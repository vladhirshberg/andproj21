package com.example.our_stories.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StoryDao {
    @Query("select * from Story")
    List<Story> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Story... stories);

    @Query("select * from Story where id = :id")
    User getStoryById(String id);

}
