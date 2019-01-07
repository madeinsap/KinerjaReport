package com.project.solomode.kinerjareport.ExportActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.project.solomode.kinerjareport.DatabaseSetup.Kegiatan;
import com.project.solomode.kinerjareport.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.project.solomode.kinerjareport.DatabaseSetup.MyApplication.db;
import static com.project.solomode.kinerjareport.DatabaseSetup.MyApplication.dbExport;

public class ExportActivity extends AppCompatActivity {

    private ExportAdapter mAdapter;
    private List<Kegiatan> exportList = new ArrayList<>();
    private RecyclerView recyclerView;
    EditText selectedDate;
    private ShimmerFrameLayout mShimmerViewContainer;
    ImageView noData;
    Button cari, export;

    Calendar myCalendar;

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

        mAdapter = new ExportAdapter(this, exportList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        selectedDate = findViewById(R.id.selected_date);
        myCalendar = Calendar.getInstance();
        selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ExportActivity.this,
                        android.app.AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        updateLabel();
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().findViewById(Resources.getSystem().
                        getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();
            }
        });

        cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedDate.getText().toString().trim().equals("")) {
                    FancyToast.makeText(ExportActivity.this, "Pilih waktu terlebih dahulu",
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    final String valueBulan;
                    final String valueTahun;

                    valueBulan = "0" + String.valueOf(myCalendar.get(Calendar.MONTH)+1);
                    valueTahun = String.valueOf(myCalendar.get(Calendar.YEAR));
                    exportList.clear();
                    noData.setVisibility(View.GONE);
                    mShimmerViewContainer.startShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.VISIBLE);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            exportList = db.kegiatanDao().getAllByMonthAndYear(valueBulan, valueTahun);
                            toggleEmptyData();

                            mAdapter = new ExportAdapter(getApplicationContext(), exportList);
                            recyclerView.setAdapter(mAdapter);
                        }
                    }, 1000);
                }
            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exportList.size() == 0) {
                    FancyToast.makeText(ExportActivity.this, "Gagal! Tidak ada data",
                            FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {
                    dbExport.exportDao().deleteExport();
                    for (int positionData = 0; positionData < exportList.size(); positionData++) {
                        dbExport.exportDao().insertExport(exportList.get(positionData));
                    }
                    export2excel();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        selectedDate.setFocusable(false);
        selectedDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void toggleEmptyData() {
        if (exportList.size() == 0) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }
    }

    private void export2excel() {
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/LKI Pegawai/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }

        SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), "dbExport", directory_path);
        sqliteToExcel.exportSingleTable("kegiatan", "LKI.xls", new SQLiteToExcel.ExportListener() {
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

                open = aboutView.findViewById(R.id.open);
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
