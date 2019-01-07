package com.project.solomode.kinerjareport.DatabaseSetup;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface ExportDao {

    @Insert
    void insertExport(Kegiatan exportData);

    @Query("DELETE FROM kegiatan")
    void deleteExport();

}
