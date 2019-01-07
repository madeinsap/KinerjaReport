package com.project.solomode.kinerjareport.DatabaseSetup;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Kegiatan.class}, version = 1)
public abstract class ExportDatabase extends RoomDatabase {
    public abstract ExportDao exportDao();
}
