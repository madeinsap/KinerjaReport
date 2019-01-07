package com.project.solomode.kinerjareport.InsertActivity;

import android.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.project.solomode.kinerjareport.DatabaseSetup.MyApplication.db;

public class InsertActivity extends AppCompatActivity {

    private EditText inputTanggal, inputKegiatan, inputVolume, inputSatuan, inputOutput, inputKeterangan;
    private TextInputLayout inputLayoutTanggal, inputLayoutKegiatan, inputLayoutVolume, inputLayoutSatuan, inputLayoutOutput, inputLayoutKeterangan;
    private Button btnSimpan, btnKembali;
    private ImageView clearKegiatan, clearVolume, clearSatuan, clearOutput, clearKeterangan;
    private CheckBox checkOutput, checkKeterangan;

    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insertupdate_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Kinerja");
        toolbar.setSubtitle("Menambah Kegiatan");
        Drawable backArrow = getResources().getDrawable(R.drawable.ic_arrow_back);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);

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

        inputTanggal.addTextChangedListener(new MyTextWatcher(inputTanggal));
        myCalendar = Calendar.getInstance();
        updateLabel();
        inputTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(InsertActivity.this,
                        AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        inputKegiatan.addTextChangedListener(new MyTextWatcher(inputKegiatan));
        inputKegiatan.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        inputKegiatan.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        inputVolume.addTextChangedListener(new MyTextWatcher(inputVolume));
        inputSatuan.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        inputSatuan.addTextChangedListener(new MyTextWatcher(inputSatuan));
        inputSatuan.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        inputSatuan.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        inputOutput.addTextChangedListener(new MyTextWatcher(inputOutput));
        inputOutput.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        inputOutput.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        inputOutput.setEnabled(false);
        inputOutput.setText("-");

        inputKeterangan.addTextChangedListener(new MyTextWatcher(inputKeterangan));
        inputKeterangan.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputKeterangan.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        inputKeterangan.setEnabled(false);
        inputKeterangan.setText("-");

        checkOutput = findViewById(R.id.check_output);
        checkOutput.setChecked(false);
        checkOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkOutput.isChecked()) {
                    inputOutput.setText("");
                    inputOutput.setEnabled(true);
                    inputOutput.requestFocus();
                } else if (!checkOutput.isChecked()) {
                    checkOutput.setChecked(false);
                    inputOutput.setText("-");
                    inputOutput.setEnabled(false);
                }
            }
        });

        checkKeterangan = findViewById(R.id.check_keterangan);
        checkKeterangan.setChecked(false);
        checkKeterangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkKeterangan.isChecked()) {
                    inputKeterangan.setEnabled(true);
                    inputKeterangan.setText("");
                    inputOutput.requestFocus();
                } else if (!checkKeterangan.isChecked()) {
                    checkKeterangan.setChecked(false);
                    inputKeterangan.setText("-");
                    inputKeterangan.setEnabled(false);
                }
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
                checkOutput.setChecked(true);
                inputOutput.setEnabled(true);
            }
        });

        clearKeterangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputKeterangan.setText("");
                checkKeterangan.setChecked(true);
                inputKeterangan.setEnabled(true);
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        inputTanggal.setFocusable(false);
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

        createData(inputTanggal.getText().toString(), inputKegiatan.getText().toString(), inputVolume.getText().toString(), inputSatuan.getText().toString(), inputOutput.getText().toString(), inputKeterangan.getText().toString());
        FancyToast.makeText(InsertActivity.this, "Data berhasil disimpan", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
        super.onBackPressed();
    }

    private void createData(String tanggal, String kegiatan, String volume, String satuan, String output, String keterangan) {
        Kegiatan newKegiatan = new Kegiatan();
        newKegiatan.setTanggal(tanggal);
        newKegiatan.setKegiatan(kegiatan);
        newKegiatan.setVolume(volume);
        newKegiatan.setSatuan(satuan);
        newKegiatan.setOutput(output);
        newKegiatan.setKeterangan(keterangan);

        db.kegiatanDao().insertKegiatan(newKegiatan);
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
