package com.azarcorp.duitdroid.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.azarcorp.duitdroid.database.DataBaseDao;

import com.azarcorp.duitdroid.model.ModelDatabase;

@Database(entities = {ModelDatabase.class}, version = 1)

public abstract class AppDataBase extends RoomDatabase {
    public abstract DataBaseDao databaseDao();
}