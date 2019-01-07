package com.project.solomode.kinerjareport.DatabaseSetup;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by madeinsap on 3/12/2018.
 */

@Entity
public class Kegiatan {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "tanggal")
    private String tanggal;
    @ColumnInfo(name = "kegiatan")
    private String kegiatan;
    @ColumnInfo(name = "volume")
    private String volume;
    @ColumnInfo(name = "satuan")
    private String satuan;
    @ColumnInfo(name = "output")
    private String output;
    @ColumnInfo(name = "keterangan")
    private String keterangan;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
