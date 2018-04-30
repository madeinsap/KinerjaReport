package com.project.solomode.kinerjareport.DatabaseSetup.Models;

/**
 * Created by madeinsap on 3/12/2018.
 */

public class Data {
    public static final String TABLE_KINERJA = "table_kinerja"; // NAMA TABEL
    public static final String TABLE_EXPORT = "table_export"; // NAMA TABEL
    public static final String COLUMN_ID = "id"; // NAMA KOLOM ID
    public static final String COLUMN_TANGGAL = "tanggal"; // NAMA KOLOM TANGGAL
    public static final String COLUMN_KEGIATAN = "kegiatan"; // NAMA KOLOM KEGIATAN
    public static final String EXPORT_KEGIATAN = "kegiatan"; // NAMA KOLOM KEGIATAN
    public static final String COLUMN_VOLUME = "volume"; // NAMA KOLOM VOLUME
    public static final String EXPORT_VOLUME = "volume"; // NAMA KOLOM VOLUME
    public static final String COLUMN_SATUAN = "satuan"; // NAMA KOLOM SATUAN
    public static final String EXPORT_SATUAN = "satuan"; // NAMA KOLOM SATUAN
    public static final String COLUMN_OUTPUT = "output"; // NAMA KOLOM OUTPUT
    public static final String EXPORT_OUTPUT = "output"; // NAMA KOLOM OUTPUT
    public static final String COLUMN_KETERANGAN = "keterangan"; // NAMA KOLOM KETERANGAN
    public static final String EXPORT_KETERANGAN = "keterangan"; // NAMA KOLOM KETERANGAN

    private int id;
    private String tanggal;
    private String kegiatan;
    private String volume;
    private String satuan;
    private String output;
    private String keterangan;

    public static final String CREATE_TABLE_KINERJA =
            "CREATE TABLE " + TABLE_KINERJA + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TANGGAL + " TEXT,"
                    + COLUMN_KEGIATAN + " TEXT,"
                    + COLUMN_VOLUME + " INTEGER,"
                    + COLUMN_SATUAN + " TEXT,"
                    + COLUMN_OUTPUT + " TEXT,"
                    + COLUMN_KETERANGAN + " TEXT"
                    + ")";

    public static final String CREATE_TABLE_EXPORT =
            "CREATE TABLE " + TABLE_EXPORT + "("
                    + EXPORT_KEGIATAN + " TEXT,"
                    + EXPORT_VOLUME + " INTEGER,"
                    + EXPORT_SATUAN + " TEXT,"
                    + EXPORT_OUTPUT + " TEXT,"
                    + EXPORT_KETERANGAN + " TEXT"
                    + ")";

    public Data() {
        //Empty constructor
    }


    public Data(int id, String tanggal, String kegiatan, String volume, String satuan, String output, String keterangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.kegiatan = kegiatan;
        this.volume = volume;
        this.satuan = satuan;
        this.output = output;
        this.keterangan = keterangan;
    }

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
