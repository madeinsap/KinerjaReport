package com.project.solomode.kinerjareport.DatabaseSetup;

import android.app.Application;
import android.arch.persistence.room.Room;

public class MyApplication extends Application {

    public static KegiatanDatabase db;
    public static ExportDatabase dbExport;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),
                KegiatanDatabase.class,"dbKegiatan").allowMainThreadQueries().build();

        dbExport = Room.databaseBuilder(getApplicationContext(),
                ExportDatabase.class,"dbExport").allowMainThreadQueries().build();
    }

}
