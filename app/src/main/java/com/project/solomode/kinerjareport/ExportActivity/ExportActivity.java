package com.project.solomode.kinerjareport.ExportActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.project.solomode.kinerjareport.DatabaseSetup.Adapter.ExportAdapter;
import com.project.solomode.kinerjareport.DatabaseSetup.Models.Data;
import com.project.solomode.kinerjareport.DatabaseSetup.Utils.DataDBHelper;
import com.project.solomode.kinerjareport.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExportActivity extends AppCompatActivity {

    private ExportAdapter mAdapter;
    private List<Data> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataDBHelper db;
    String vBulan, vTahun;
    private ShimmerFrameLayout mShimmerViewContainer;
    ImageView noData;
    Button cari, export;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Export Data");
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

        noData = (ImageView) findViewById(R.id.img_nodata);
        cari = (Button) findViewById(R.id.btn_cari);
        export = (Button) findViewById(R.id.btn_export);

        noData.setVisibility(View.VISIBLE);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.rv);

        db = new DataDBHelper(this);
        dataList.clear();

        mAdapter = new ExportAdapter(this, dataList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner_bulan);
        final Spinner spinner1 = (Spinner) findViewById(R.id.spinner_tahun);
        String[] bulan = new String[]{
                "Pilih Bulan",
                "Januari",
                "Februari",
                "Maret",
                "April",
                "Mei",
                "Juni",
                "Juli",
                "Agustus",
                "September",
                "Oktober",
                "November",
                "Desember"
        };

        String[] tahun = new String[]{
                "Pilih Tahun",
                "2018",
                "2019",
                "2020",
                "2021",
                "2022",
        };

        final List<String> monthList = new ArrayList<>(Arrays.asList(bulan));
        final List<String> yearList = new ArrayList<>(Arrays.asList(tahun));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.text_spinner, monthList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, R.layout.text_spinner, yearList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.text_spinner);
        spinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter1.setDropDownViewResource(R.layout.text_spinner);
        spinner1.setAdapter(spinnerArrayAdapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position<10){
                    vBulan = "0" + String.valueOf(position);
                }
                else{
                    vBulan = String.valueOf(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vTahun = spinner1.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData(String.valueOf(vBulan), vTahun);
                Log.d("output", "bulan = "+vBulan);
                Log.d("output", "tahun = "+vTahun);
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getExportCount(vBulan, vTahun) > 0) {
                    db.deleteExport();
                    db.insertNull();
                    db.insertExport(vBulan, vTahun);
                    export2excel();
                } else {
                    FancyToast.makeText(ExportActivity.this,"Gagal! Tidak ada data", FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                }
            }
        });
    }

    private void refreshData(final String bulan, final String tahun){
        dataList.clear();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dataList.addAll(db.getExportData(bulan, tahun));
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                emptyData(bulan, tahun);
                mAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();

        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void emptyData(String bulan, String tahun) {
        // you can check notesList.size() > 0
        if (db.getExportCount(bulan, tahun) > 0) {
            noData.setVisibility(View.GONE);
        } else {
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void export2excel(){
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/LKI Pegawai/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        // Export SQLite DB as EXCEL FILE
        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), "kinerja_db", directory_path);
        sqliteToExcel.exportSingleTable("table_export", "LKI.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                Button open, ok;
                LayoutInflater factory = LayoutInflater.from(ExportActivity.this);
                final View aboutView = factory.inflate(R.layout.window_openfolder, null);
                final AlertDialog successDialog = new AlertDialog.Builder(ExportActivity.this).create();
                successDialog.setView(aboutView);
                successDialog.setCancelable(false);
                successDialog.show();

                open = (Button) aboutView.findViewById(R.id.open);
                open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                                + "/LKI Pegawai/");
                        intent.setDataAndType(uri, "*/*");
                        startActivity(Intent.createChooser(intent, "Open folder"));
                    }
                });

                ok = (Button) aboutView.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        successDialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
