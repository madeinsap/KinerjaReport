package com.project.solomode.kinerjareport.UpdateActivity;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.project.solomode.kinerjareport.DatabaseSetup.Kegiatan;
import com.project.solomode.kinerjareport.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.project.solomode.kinerjareport.DatabaseSetup.MyApplication.db;

public class UpdateActivity extends AppCompatActivity {

    private int valueId;

    private EditText inputTanggal, inputKegiatan, inputVolume, inputSatuan, inputOutput, inputKeterangan;
    private TextInputLayout inputLayoutTanggal, inputLayoutKegiatan, inputLayoutVolume, inputLayoutSatuan, inputLayoutOutput, inputLayoutKeterangan;
    private Button btnSimpan, btnKembali;
    private ImageView clearKegiatan, clearVolume, clearSatuan, clearOutput, clearKeterangan;
    private CheckBox checkOutput, checkKeterangan;
    private Kegiatan curentKegiatan;

    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertupdate_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Kinerja");
        toolbar.setSubtitle("Memperbarui Kegiatan");
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            valueId = getIntent().getIntExtra("id", 0);
        }

        inputLayoutTanggal = findViewById(R.id.tanggal);
        inputLayoutKegiatan = findViewById(R.id.kegiatan);
        inputLayoutVolume = findViewById(R.id.volume);
        inputLayoutSatuan = findViewById(R.id.satuan);
        inputLayoutOutput = findViewById(R.id.output);
        inputLayoutKeterangan = findViewById(R.id.keterangan);

        inputTanggal = findViewById(R.id.input_tanggal);
        inputKegiatan = findViewById(R.id.input_kegiatan);
        inputVolume = findViewById(R.id.input_volume);
        inputSatuan = findViewById(R.id.input_satuan);
        inputOutput = findViewById(R.id.input_output);
        inputKeterangan = findViewById(R.id.input_keterangan);

        btnSimpan = findViewById(R.id.btn_save);
        btnKembali = findViewById(R.id.btn_cencel);

        clearKegiatan = findViewById(R.id.clear_kegiatan);
        clearVolume = findViewById(R.id.clear_volume);
        clearSatuan = findViewById(R.id.clear_satuan);
        clearOutput = findViewById(R.id.clear_output);
        clearKeterangan = findViewById(R.id.clear_keterangan);

        inputTanggal.addTextChangedListener(new UpdateActivity.MyTextWatcher(inputTanggal));
        inputKegiatan.addTextChangedListener(new UpdateActivity.MyTextWatcher(inputKegiatan));
        inputKegiatan.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        inputKegiatan.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        inputVolume.addTextChangedListener(new UpdateActivity.MyTextWatcher(inputVolume));
        inputSatuan.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        inputSatuan.addTextChangedListener(new UpdateActivity.MyTextWatcher(inputSatuan));
        inputSatuan.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        inputSatuan.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        inputOutput.addTextChangedListener(new UpdateActivity.MyTextWatcher(inputOutput));
        inputOutput.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        inputOutput.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        inputKeterangan.addTextChangedListener(new UpdateActivity.MyTextWatcher(inputKeterangan));
        inputKeterangan.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputKeterangan.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        checkOutput = findViewById(R.id.check_output);
        checkOutput.setChecked(true);
        inputOutput.setEnabled(true);
        checkOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkOutput.isChecked()) {
                    inputOutput.setEnabled(true);
                } else if (!checkOutput.isChecked()) {
                    checkOutput.setChecked(false);
                    inputOutput.setText("-");
                    inputOutput.setEnabled(false);
                }
            }
        });

        checkKeterangan = (CheckBox) findViewById(R.id.check_keterangan);
        checkKeterangan.setChecked(true);
        inputKeterangan.setEnabled(true);
        checkKeterangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkKeterangan.isChecked()) {
                    inputKeterangan.setEnabled(true);
                } else if (!checkKeterangan.isChecked()) {
                    checkKeterangan.setChecked(false);
                    inputKeterangan.setText("-");
                    inputKeterangan.setEnabled(false);
                }
            }
        });

        curentKegiatan = db.kegiatanDao().getKegiatan(valueId);

        inputTanggal.setText(curentKegiatan.getTanggal());
        inputKegiatan.setText(curentKegiatan.getKegiatan());
        inputVolume.setText(curentKegiatan.getVolume());
        inputSatuan.setText(curentKegiatan.getSatuan());
        inputOutput.setText(curentKegiatan.getOutput());
        inputKeterangan.setText(curentKegiatan.getKeterangan());

        //Calendar support
        myCalendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            myCalendar.setTime(simpleDateFormat.parse(curentKegiatan.getTanggal()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        inputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UpdateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        clearKegiatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputKegiatan.setText("");
            }
        });

        clearVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputVolume.setText("");
            }
        });

        clearSatuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputSatuan.setText("");
            }
        });

        clearOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOutput.setText("");
                inputOutput.setEnabled(true);
                inputOutput.setFocusable(true);
                checkOutput.setChecked(true);
            }
        });

        clearKeterangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputKeterangan.setText("");
                inputKeterangan.setEnabled(true);
                inputKeterangan.setFocusable(true);
                checkKeterangan.setChecked(true);
            }
        });

    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        inputTanggal.setText(sdf.format(myCalendar.getTime()));
    }

    private void submitForm() {
        if (!validateTanggal()) {
            return;
        }

        if (!validatekegiatan()) {
            return;
        }

        if (!validateVolume()) {
            return;
        }

        if (!validateSatuan()) {
            return;
        }

        if (!validateOutput()) {
            return;
        }

        if (!validateKeterangan()) {
            return;
        }
        updateData(valueId, inputTanggal.getText().toString(), inputKegiatan.getText().toString(),
                inputVolume.getText().toString(), inputSatuan.getText().toString(), inputOutput.getText().toString(),
                inputKeterangan.getText().toString());
        FancyToast.makeText(UpdateActivity.this, "Data berhasil diubah", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        super.onBackPressed();
    }

    private void updateData(int id, String tanggal, String kegiatan, String volume, String satuan, String output, String keterangan) {
        curentKegiatan.setId(id);
        curentKegiatan.setTanggal(tanggal);
        curentKegiatan.setKegiatan(kegiatan);
        curentKegiatan.setVolume(volume);
        curentKegiatan.setSatuan(satuan);
        curentKegiatan.setOutput(output);
        curentKegiatan.setKeterangan(keterangan);

        db.kegiatanDao().updateKegiatan(curentKegiatan);
        super.onBackPressed();
    }

    private boolean validateTanggal() {
        if (inputTanggal.getText().toString().trim().isEmpty()) {
            inputLayoutTanggal.setError(getString(R.string.err_tanggal));
            requestFocus(inputTanggal);
            return false;
        } else {
            inputLayoutTanggal.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatekegiatan() {
        if (inputKegiatan.getText().toString().trim().isEmpty()) {
            inputLayoutKegiatan.setError(getString(R.string.err_kegiatan));
            requestFocus(inputKegiatan);
            clearKegiatan.setVisibility(View.GONE);
            return false;
        } else {
            inputLayoutKegiatan.setErrorEnabled(false);
            clearKegiatan.setVisibility(View.VISIBLE);
        }

        return true;
    }

    private boolean validateVolume() {
        if (inputVolume.getText().toString().trim().isEmpty()) {
            inputLayoutVolume.setError(getString(R.string.err_volume));
            requestFocus(inputVolume);
            clearVolume.setVisibility(View.GONE);
            return false;
        } else {
            inputLayoutVolume.setErrorEnabled(false);
            clearVolume.setVisibility(View.VISIBLE);
        }

        return true;
    }

    private boolean validateSatuan() {
        if (inputSatuan.getText().toString().trim().isEmpty()) {
            inputLayoutSatuan.setError(getString(R.string.err_satuan));
            requestFocus(inputSatuan);
            clearSatuan.setVisibility(View.GONE);
            return false;
        } else {
            inputLayoutSatuan.setErrorEnabled(false);
            clearSatuan.setVisibility(View.VISIBLE);
        }

        return true;
    }

    private boolean validateOutput() {
        if (inputOutput.getText().toString().trim().isEmpty()) {
            inputLayoutOutput.setError(getString(R.string.err_output));
            requestFocus(inputOutput);
            clearOutput.setVisibility(View.GONE);
            checkOutput.setChecked(true);
            return false;
        } else {
            inputLayoutOutput.setErrorEnabled(false);
            clearOutput.setVisibility(View.VISIBLE);
        }

        return true;
    }

    private boolean validateKeterangan() {
        if (inputKeterangan.getText().toString().trim().isEmpty()) {
            inputLayoutKeterangan.setError(getString(R.string.err_keterangan));
            requestFocus(inputKeterangan);
            clearKeterangan.setVisibility(View.GONE);
            checkKeterangan.setChecked(true);
            return false;
        } else {
            inputLayoutKeterangan.setErrorEnabled(false);
            clearKeterangan.setVisibility(View.VISIBLE);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_tanggal:
                    validateTanggal();
                    break;
                case R.id.input_kegiatan:
                    validatekegiatan();
                    break;
                case R.id.input_volume:
                    validateVolume();
                    break;
                case R.id.input_satuan:
                    validateSatuan();
                    break;
                case R.id.input_output:
                    validateOutput();
                    break;
                case R.id.input_keterangan:
                    validateKeterangan();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();

        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
