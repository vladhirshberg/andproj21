package com.example.our_stories.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.our_stories.MyApplication;

@Database(entities = {User.class, Story.class}, version = 3)
@TypeConverters({Converter.class})
abstract class LocalDBRepository extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract StoryDao storyDao();
}

public class LocalDB{
    final static public LocalDBRepository db =
            Room.databaseBuilder(MyApplication.context,
                    LocalDBRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
