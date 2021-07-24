package com.example.our_stories.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.our_stories.MyApplication;

@Database(entities = {User.class}, version = 1)
abstract class LocalDBRepository extends RoomDatabase {
    public abstract UserDao userDao();
}

public class LocalDB{
    final static public LocalDBRepository db =
            Room.databaseBuilder(MyApplication.context,
                    LocalDBRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
