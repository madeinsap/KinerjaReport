package com.project.solomode.kinerjareport.DatabaseSetup;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Kegiatan.class}, version = 1)
public abstract class KegiatanDatabase extends RoomDatabase {
    public abstract KegiatanDao kegiatanDao();
}
