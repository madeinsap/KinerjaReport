package com.project.solomode.kinerjareport.HomeActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.solomode.kinerjareport.BuildConfig;
import com.project.solomode.kinerjareport.ExportActivity.ExportActivity;
import com.project.solomode.kinerjareport.DatabaseSetup.Adapter.DataAdapter;
import com.project.solomode.kinerjareport.DatabaseSetup.Models.Data;
import com.project.solomode.kinerjareport.DatabaseSetup.Utils.DataDBHelper;
import com.project.solomode.kinerjareport.InsertActivity.InsertActivity;
import com.project.solomode.kinerjareport.UpdateActivity.UpdateActivity;
import com.project.solomode.kinerjareport.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;

    private DataAdapter mAdapter;
    private List<Data> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout mShimmerViewContainer;
    private ImageView noData;
    Toolbar toolbar;

    private Setup setup;

    private DataDBHelper db;

    CollapsingToolbarLayout collapsingToolbarLayout;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.option));
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Laporan Kinerja");

        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.whiteColor));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.whiteColor));
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerHome);
        noData = findViewById(R.id.img_nodata);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swiper);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        db = new DataDBHelper(this);
        refreshData();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("EEE, d MMM yyyy");
        String strDate = dateformat.format(calendar.getTime());
        TextView date = (TextView) findViewById(R.id.tanggal);
        date.setText(strDate);

        setup = new Setup(this);
        if (setup.isFirstTimeLaunch()) {
            setupFirstTime();
        }

        mAdapter = new DataAdapter(this, dataList, new DataAdapter.MyAdapterListener() {
            @Override
            public void editOnClick(View v, final int position) {
                Intent update = new Intent(getApplicationContext(), UpdateActivity.class);
                update.putExtra("id", dataList.get(position).getId());
                startActivity(update);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }

            @Override
            public void hapusOnClick(View v, final int position) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("Hapus Kegiatan?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Hapus",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteData(position);
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                Button possitive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                possitive.setTextColor(Color.parseColor("#f44336"));
                negative.setTextColor(Color.parseColor("#f44336"));
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                refreshData();
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();

        getVersion();
    }

    private void updateWindow(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View updateView = factory.inflate(R.layout.outofdate_window, null);
        final AlertDialog updateDialog = new AlertDialog.Builder(this).create();
        updateDialog.setView(updateView);
        updateDialog.setCancelable(false);
        updateDialog.show();

        Button ok = (Button) updateView.findViewById(R.id.close);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        Button update = (Button) updateView.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+"com.project.solomode.kinerjareport"));
                startActivity(mIntent);
            }
        });
    }

    private void getVersion(){
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                Double getVersion = data.child("version").getValue(Double.class);
                String currentVersion = String.valueOf(getVersion);
                String nameVersion = BuildConfig.VERSION_NAME;
                if(!currentVersion.equals(nameVersion)){
                    updateWindow();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("No Connection");
                builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getVersion();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void refreshData(){
        dataList.clear();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                dataList.addAll(db.getAllData());
                mAdapter.notifyDataSetChanged();
                toggleEmptyData();
            }
        }, 1000);
    }

    private void setupFirstTime(){
        setup.setFirstTimeLaunch(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupTap();
            }
        }, 1000);
    }

    private void deleteData(int position) {
        db.deleteData(dataList.get(position));

        dataList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                Intent insert = new Intent(this, InsertActivity.class);
                startActivity(insert);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }

    private void toggleEmptyData() {
        if (db.getDataCount() > 0) {
            noData.setVisibility(View.GONE);
        } else {
            noData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export:
                Intent export = new Intent(getApplicationContext(), ExportActivity.class);
                startActivity(export);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                return true;
            case R.id.rating:
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+"com.project.solomode.kinerjareport"));
                startActivity(mIntent);
                return true;
            case R.id.about:
                TextView version;
                Button ok;
                String version_name = BuildConfig.VERSION_NAME;
                LayoutInflater factory = LayoutInflater.from(this);
                final View aboutView = factory.inflate(R.layout.about_window, null);
                final AlertDialog aboutDialog = new AlertDialog.Builder(this).create();
                version = (TextView) aboutView.findViewById(R.id.subtitle);
                version.setText("V" + version_name);
                aboutDialog.setView(aboutView);
                aboutDialog.setCancelable(false);
                aboutDialog.show();

                ok = (Button) aboutView.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aboutDialog.dismiss();
                    }
                });
                return true;
            case R.id.tips:
                setupTap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupTap(){
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        //Tap FAB
                        TapTarget.forView(findViewById(R.id.fab), "Tambah Data", "Pilih untuk menambah data kinerja")
                                .dimColor(android.R.color.black)
                                .tintTarget(false)
                                .cancelable(false)
                                .id(1),

                        //Tap Export
                        TapTarget.forToolbarMenuItem(toolbar, R.id.export, "Export Data", "Pilih untuk export data menjadi file Excel")
                                .dimColor(android.R.color.black)
                                .cancelable(false)
                                .id(2),

                        //Tap Option
                        TapTarget.forToolbarOverflow(toolbar, "Option", "Pilih untuk melihat petunjuk lebih")
                                .dimColor(android.R.color.black)
                                .cancelable(false)
                                .id(3)
                        )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        FancyToast.makeText(MainActivity.this,"Enjoy", FancyToast.LENGTH_LONG, FancyToast.DEFAULT,false).show();
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                });
        sequence.start();
    }
}