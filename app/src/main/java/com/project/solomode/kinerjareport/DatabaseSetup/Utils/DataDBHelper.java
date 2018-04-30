package com.project.solomode.kinerjareport.DatabaseSetup.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.solomode.kinerjareport.DatabaseSetup.Models.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by madeinsap on 3/12/2018.
 */

public class DataDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "kinerja_db";

    public DataDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Data.CREATE_TABLE_KINERJA);
        db.execSQL(Data.CREATE_TABLE_EXPORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Data.TABLE_KINERJA);
        db.execSQL("DROP TABLE IF EXISTS " + Data.TABLE_EXPORT);
        onCreate(db);
    }

    public List<Data> getAllData(){
        List<Data> dataList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Data.TABLE_KINERJA + " ORDER BY date(tanggal) DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Data data = new Data();
                data.setId(cursor.getInt(cursor.getColumnIndex(Data.COLUMN_ID)));
                data.setTanggal(cursor.getString(cursor.getColumnIndex(Data.COLUMN_TANGGAL)));
                data.setKegiatan(cursor.getString(cursor.getColumnIndex(Data.COLUMN_KEGIATAN)));
                data.setVolume(cursor.getString(cursor.getColumnIndex(Data.COLUMN_VOLUME)));
                data.setSatuan(cursor.getString(cursor.getColumnIndex(Data.COLUMN_SATUAN)));
                data.setOutput(cursor.getString(cursor.getColumnIndex(Data.COLUMN_OUTPUT)));
                data.setKeterangan(cursor.getString(cursor.getColumnIndex(Data.COLUMN_KETERANGAN)));

                dataList.add(data);
            } while (cursor.moveToNext());
        }

        db.close();
        return dataList;
    }

    public long insertData(String tanggal, String kegiatan, String volume, String satuan, String output, String keterangan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Data.COLUMN_TANGGAL, tanggal);
        values.put(Data.COLUMN_KEGIATAN, kegiatan);
        values.put(Data.COLUMN_VOLUME, volume);
        values.put(Data.COLUMN_SATUAN, satuan);
        values.put(Data.COLUMN_OUTPUT, output);
        values.put(Data.COLUMN_KETERANGAN, keterangan);

        long id = db.insert(Data.TABLE_KINERJA, null, values);

        db.close();
        return id;
    }

    public void insertExport(String bulan, String tahun) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlInsert = "INSERT INTO " + Data.TABLE_EXPORT + "(kegiatan, volume, satuan, output, keterangan) SELECT kegiatan, SUM(volume), satuan, output, keterangan FROM " + Data.TABLE_KINERJA + " WHERE strftime('%m', tanggal) IN('" + bulan + "') AND strftime('%Y', tanggal) IN('" + tahun + "') GROUP BY kegiatan, satuan";
        db.execSQL(sqlInsert);
        db.close();
    }

    public void insertNull(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.putNull(Data.EXPORT_KEGIATAN);
        cv.putNull(Data.EXPORT_VOLUME);
        cv.putNull(Data.EXPORT_SATUAN);
        cv.putNull(Data.COLUMN_OUTPUT);
        cv.putNull(Data.EXPORT_KETERANGAN);
        db.insert(Data.TABLE_EXPORT, null, cv);
    }

    public void updateData(int id, String tanggal, String kegiatan, String volume, String satuan, String output, String keterangan) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Data.COLUMN_TANGGAL, tanggal);
        values.put(Data.COLUMN_KEGIATAN, kegiatan);
        values.put(Data.COLUMN_VOLUME, volume);
        values.put(Data.COLUMN_SATUAN, satuan);
        values.put(Data.COLUMN_OUTPUT, output);
        values.put(Data.COLUMN_KETERANGAN, keterangan);

        db.update(Data.TABLE_KINERJA, values, Data.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getDataCount() {
        String countQuery = "SELECT * FROM " + Data.TABLE_KINERJA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getExportCount(String bulan, String tahun) {
        String countQuery = "SELECT * FROM " + Data.TABLE_KINERJA + " WHERE strftime('%m', tanggal) IN('" + bulan + "') AND strftime('%Y', tanggal) IN('" + tahun + "')";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public void deleteData(Data data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Data.TABLE_KINERJA, Data.COLUMN_ID + " = ?",
                new String[]{String.valueOf(data.getId())});
        db.close();
    }

    public void deleteExport() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Data.TABLE_EXPORT);
        db.close();
    }

    public List<Data> getExportData(String bulan, String tahun){
        List<Data> dataList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Data.TABLE_KINERJA + " WHERE strftime('%m', tanggal) IN('" + bulan + "') AND strftime('%Y', tanggal) IN ('" + tahun + "') ORDER BY date(tanggal) DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Data data = new Data();
                data.setId(cursor.getInt(cursor.getColumnIndex(Data.COLUMN_ID)));
                data.setTanggal(cursor.getString(cursor.getColumnIndex(Data.COLUMN_TANGGAL)));
                data.setKegiatan(cursor.getString(cursor.getColumnIndex(Data.COLUMN_KEGIATAN)));
                data.setVolume(cursor.getString(cursor.getColumnIndex(Data.COLUMN_VOLUME)));
                data.setSatuan(cursor.getString(cursor.getColumnIndex(Data.COLUMN_SATUAN)));
                data.setOutput(cursor.getString(cursor.getColumnIndex(Data.COLUMN_OUTPUT)));
                data.setKeterangan(cursor.getString(cursor.getColumnIndex(Data.COLUMN_KETERANGAN)));

                dataList.add(data);
            } while (cursor.moveToNext());
        }

        db.close();
        return dataList;
    }
}
