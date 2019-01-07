package com.project.solomode.kinerjareport.HomeActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.solomode.kinerjareport.BuildConfig;
import com.project.solomode.kinerjareport.DatabaseSetup.Kegiatan;
import com.project.solomode.kinerjareport.ExportActivity.ExportActivity;
import com.project.solomode.kinerjareport.InsertActivity.InsertActivity;
import com.project.solomode.kinerjareport.R;
import com.project.solomode.kinerjareport.UpdateActivity.UpdateActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.project.solomode.kinerjareport.DatabaseSetup.MyApplication.db;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;

    DataAdapter mAdapter;
    List<Kegiatan> listKegiatan = new ArrayList<>();;
    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    ShimmerFrameLayout mShimmerViewContainer;
    ImageView noData;
    Toolbar toolbar;

    SetupFirstLoad setupFirstLoad;

    CollapsingToolbarLayout collapsingToolbarLayout;

    FirebaseDatabase mDatabase;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =  findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.option));
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Laporan Kinerja");
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.whiteColor));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, R.color.whiteColor));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerHome);
        noData = findViewById(R.id.img_nodata);
        swipeRefreshLayout = findViewById(R.id.swiper);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        setupFirstLoad = new SetupFirstLoad(this);
        if (setupFirstLoad.isFirstTimeLaunch()) {
            setupFirstTime();
        }

        checkVersionApp();
        getData();
        initRecycler();
        getCalendar();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fab.getVisibility() == View.VISIBLE) {
                    fab.hide();
                } else if (dy < 0 && fab.getVisibility() != View.VISIBLE) {
                    fab.show();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getData();
            }
        });

    }

    private void getCalendar() {
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new
                SimpleDateFormat("EEE, d MMM yyyy");
        String strDate = dateformat.format(calendar.getTime());
        TextView date = findViewById(R.id.tanggal);
        date.setText(strDate);
    }

    private void getData() {
        listKegiatan.clear();
        noData.setVisibility(View.GONE);
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                listKegiatan = db.kegiatanDao().getAll();
                mAdapter = new DataAdapter(getApplicationContext(), listKegiatan, new DataAdapter.MyAdapterListener() {
                    @Override
                    public void editOnClick(View v, int position) {
                        Intent update = new Intent(getApplicationContext(), UpdateActivity.class);
                        update.putExtra("id", listKegiatan.get(position).getId());
                        startActivity(update);
                        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    }

                    @Override
                    public void hapusOnClick(View v, final int position) {
                        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                        final View deleteConfirmation = factory.inflate(R.layout.window_delete_confirmation, null);
                        final AlertDialog deleteConfirmationDialog = new AlertDialog.Builder(MainActivity.this).create();
                        deleteConfirmationDialog.setView(deleteConfirmation);
                        deleteConfirmationDialog.show();

                        Button batal = deleteConfirmation.findViewById(R.id.batal);
                        batal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteConfirmationDialog.dismiss();
                            }
                        });

                        Button ya = deleteConfirmation.findViewById(R.id.ya);
                        ya.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Integer deleted = db.kegiatanDao().deleteKegiatan(listKegiatan.
                                        get(position).getId());

                                if(deleted==1) {
                                    deleteConfirmationDialog.dismiss();
                                    FancyToast.makeText(MainActivity.this,
                                            "Data berhasil dihapus", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                    getData();
                                }
                            }
                        });
                    }
                });
                recyclerView.setAdapter(mAdapter);
                toggleEmptyData();
            }
        }, 1000);
    }

    private void initRecycler() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void setupFirstTime() {
        setupFirstLoad.setFirstTimeLaunch(false);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupTap();
            }
        }, 1000);
    }

    private void toggleEmptyData() {
        if (listKegiatan.size() == 0) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }
    }

    private void checkVersionApp() {
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mDatabase.getReference();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                Double getVersion = data.child("version").getValue(Double.class);
                String currentVersion = String.valueOf(getVersion);
                String nameVersion = BuildConfig.VERSION_NAME;
                if (!currentVersion.equals(nameVersion)) {
                    openUpdateWindow();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void openUpdateWindow() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View updateView = factory.inflate(R.layout.window_outofdate, null);
        final AlertDialog updateDialog = new AlertDialog.Builder(this).create();
        updateDialog.setView(updateView);
        updateDialog.setCancelable(false);
        updateDialog.show();

        Button ok = updateView.findViewById(R.id.close);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        Button update = updateView.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.project.solomode.kinerjareport"));
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                Intent insert = new Intent(this, InsertActivity.class);
                startActivity(insert);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export:
                Intent intent = new Intent(this, ExportActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                return true;
            case R.id.rating:
                Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.project.solomode.kinerjareport"));
                startActivity(mIntent);
                return true;
            case R.id.about:
                openAboutWindow();
                return true;
            case R.id.tips:
                setupTap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAboutWindow() {
        String version_name = BuildConfig.VERSION_NAME;
        LayoutInflater factory = LayoutInflater.from(this);
        final View aboutView = factory.inflate(R.layout.window_about, null);
        final AlertDialog aboutDialog = new AlertDialog.Builder(this).create();

        TextView version = aboutView.findViewById(R.id.subtitle);
        version.setText("V" + version_name);
        aboutDialog.setView(aboutView);
        aboutDialog.show();

        Button ok = aboutView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDialog.dismiss();
            }
        });
    }

    private void setupTap() {
        final TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        //Tap FAB
                        TapTarget.forView(findViewById(R.id.fab), "Tambah Kegiatan", "Pilih untuk menambah data kinerja")
                                .dimColor(android.R.color.black)
                                .tintTarget(false)
                                .cancelable(false)
                                .id(1),

                        //Tap Export
                        TapTarget.forToolbarMenuItem(toolbar, R.id.export, "Export Kegiatan", "Pilih untuk export data menjadi file Excel")
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

    @Override
    public void onBackPressed() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View exitView = factory.inflate(R.layout.window_exit, null);
        final AlertDialog exitDialog = new AlertDialog.Builder(this).create();
        exitDialog.setView(exitView);
        exitDialog.show();

        Button batal = exitView.findViewById(R.id.batal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

        Button ya = exitView.findViewById(R.id.ya);
        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                System.exit(0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }
}