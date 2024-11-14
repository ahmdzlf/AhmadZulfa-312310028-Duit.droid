package com.azarcorp.duitdroid.database;

import android.content.Context;

import androidx.room.Room;

public class DataBaseClient {

    private static DataBaseClient mInstance;
    private AppDataBase mAppDatabase;

    private DataBaseClient(Context context){
        mAppDatabase = Room.databaseBuilder(context, AppDataBase.class, "keuangan_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DataBaseClient getInstance(Context context){
        if (mInstance == null){
            mInstance = new DataBaseClient(context);
        }
        return mInstance;
    }

    public static DataBaseClient getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new IllegalArgumentException("Should use getInstance(Context) " +
                "at least once before using this method.");
    }

    public AppDataBase getAppDatabase(){
        return mAppDatabase;
    }
}