package com.project.solomode.kinerjareport.DatabaseSetup;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface KegiatanDao {

    @Query("SELECT * FROM kegiatan")
    List<Kegiatan> getAll();

    @Query("SELECT * FROM kegiatan WHERE id = :id")
    Kegiatan getKegiatan(int id);

    @Query("SELECT * FROM kegiatan WHERE strftime('%m', tanggal) = :bulan " +
            "AND strftime('%Y', tanggal) = :tahun ORDER BY date(tanggal) DESC")
    List<Kegiatan> getAllByMonthAndYear(String bulan, String tahun);

    @Query("DELETE FROM kegiatan WHERE id = :id")
    int deleteKegiatan(int id);

    @Insert
    void insertKegiatan(Kegiatan kegiatan);

    @Update
    void updateKegiatan(Kegiatan kegiatan);

}
