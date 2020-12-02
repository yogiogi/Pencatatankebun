package com.sensus.diginidea.pencatatankebun.edit;

/**
 * Created by Yogi on 9/23/2017.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sensus.diginidea.pencatatankebun.DecimalDigitsInputFilter;
import com.sensus.diginidea.pencatatankebun.MainActivity;
import com.sensus.diginidea.pencatatankebun.R;
import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.real_menu_list;
import com.sensus.diginidea.pencatatankebun.view_data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class luas_areal extends AppCompatActivity {
    TextView txId_sensus, jmlPlasma, jmlTegak, jml_luas;
    TextView txSatuanJumlahTegakan;
    TextView headInti, headPlasma, headTegakan;

    EditText tbm, tm, ttm; //inti
    EditText tbmPlasma, tmPlasma, ttmPlasma; //inti

    Double TBMI = 0.0, TMI = 0.0, TTMI = 0.0, TBMP = 0.0, TMP = 0.0, TTMP = 0.0;
    Double JMLINTI = 0.0, JMLPLASMA = 0.0;

    EditText keterangan;

    String RowID;
    String id_sensus;
    int id_obj;
    Spinner spKomoditas;
    Spinner spJenisTanaman;

    DbHelper dbhelper;
    ArrayAdapter<String> adapKomoditas;
    ArrayAdapter<String> adapJenisTanaman;
    long selectKomoditas = 0;
    long selectJenisTanaman = 0;
    String id_komoditas, id_jenisTanaman;
    String komoditas, jenis_tanaman;
    String tabel;
    TableRow trTTMplasma, trTMplasma, trTBMplasma, trJmlPlasma;
    private BottomNavigationView bottomNavigation;
    Boolean setupdate = false;
    Cursor model = null;
    int check = 0;
    int statusKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luas_areal);

        headInti = (TextView) findViewById(R.id.headInti);
        headPlasma = (TextView) findViewById(R.id.headPlasma);
        headTegakan = (TextView) findViewById(R.id.headTegakan);

        tbm = (EditText) findViewById(R.id.txTBM);
//        tbm.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        tm = (EditText) findViewById(R.id.txTM);
//        tm.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        ttm = (EditText) findViewById(R.id.txTTM);
//        ttm.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        tbmPlasma = (EditText) findViewById(R.id.txTBMplasma);
//        tbmPlasma.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        tmPlasma = (EditText) findViewById(R.id.txTMplasma);
//        tmPlasma.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});
        ttmPlasma = (EditText) findViewById(R.id.txTTMplasma);
//        ttmPlasma.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(5, 2)});

        jml_luas = (TextView) findViewById(R.id.txJml_luas);
        jmlTegak = (TextView) findViewById(R.id.txJumlahTegakan);
        jmlPlasma = (TextView) findViewById(R.id.txJumlahPlasma);

//        tvSatuanTBM = (TextView) findViewById(R.id.txSatuanTBM);
//        tvSatuanTM = (TextView) findViewById(R.id.txSatuanTM);
//        tvSatuanTTM = (TextView) findViewById(R.id.txSatuanTTM);
        txSatuanJumlahTegakan = (TextView) findViewById(R.id.txSatuanJumlahTegakan);
//        txSatuanTBMplasma = (TextView) findViewById(R.id.txSatuanTBMplasma);
//        txSatuanTMplasma = (TextView) findViewById(R.id.txSatuanTMplasma);
//        txSatuanTTMplasma = (TextView) findViewById(R.id.txSatuanTTMplasma);
//        txSatuanJumlahplasma = (TextView) findViewById(R.id.txSatuanJumlahplasma);

        keterangan = (EditText) findViewById(R.id.txKeterangan);
        keterangan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        spKomoditas = (Spinner) findViewById(R.id.txKomoditas);
        spJenisTanaman = (Spinner) findViewById(R.id.txJenisTanaman);
        txId_sensus = (TextView) findViewById(R.id.id_sensus);

        trTTMplasma = (TableRow) findViewById(R.id.ttmPlasma);
        trTMplasma = (TableRow) findViewById(R.id.tmPlasma);
        trTBMplasma = (TableRow) findViewById(R.id.tbmPlasma);
        trJmlPlasma = (TableRow) findViewById(R.id.JumlahPlasma);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        dbhelper = new DbHelper(this);
        loadSpinnerJenisTanaman();

        Bundle extras = getIntent().getExtras();
        if (getIntent().getExtras() != null) {
            setupdate = getIntent().getExtras().getBoolean("set");
//            Log.i("Value", extras.getString("id_sensus"));
            Log.d("Response", extras + "");

            RowID = getIntent().getExtras().getString("ident");
            Log.d("RowID ", RowID + "");
            id_sensus = extras.getString("id_sensus");
            Log.d("id_sensus", id_sensus + "");

            id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));

            if (id_obj == 1) {
                headTegakan.setText("JUMLAH TEGAKAN");
                tbmPlasma.setVisibility(View.GONE);
                tmPlasma.setVisibility(View.GONE);
                ttmPlasma.setVisibility(View.GONE);
//                tvSatuanTBM.setText("POHON/RUMPUN");
//                tvSatuanTM.setText("POHON/RUMPUN");
//                tvSatuanTTM.setText("POHON/RUMPUN");
                txSatuanJumlahTegakan.setText("POHON/RUMPUN");
                tabel = "tbl_luas_kebun_petani";

                headInti.setVisibility(View.GONE);
                headPlasma.setVisibility(View.GONE);
                trTTMplasma.setVisibility(View.GONE);
                trTMplasma.setVisibility(View.GONE);
                trTBMplasma.setVisibility(View.GONE);
                trJmlPlasma.setVisibility(View.GONE);
            } else if (id_obj == 2) {
                headTegakan.setText("LUAS AREAL");
                tbmPlasma.setVisibility(View.VISIBLE);
                tmPlasma.setVisibility(View.VISIBLE);
                ttmPlasma.setVisibility(View.VISIBLE);
//                tvSatuanTBM.setText("HA");
//                tvSatuanTM.setText("HA");
//                tvSatuanTTM.setText("HA");
                txSatuanJumlahTegakan.setText("HA");
//                txSatuanTBMplasma.setText("HA");
//                txSatuanTMplasma.setText("HA");
//                txSatuanTTMplasma.setText("HA");
//                txSatuanJumlahplasma.setText("HA");
                tabel = "tbl_luas_kebun_perusahaan";

                headInti.setVisibility(View.VISIBLE);
                headPlasma.setVisibility(View.VISIBLE);
                trTTMplasma.setVisibility(View.VISIBLE);
                trTMplasma.setVisibility(View.VISIBLE);
                trTBMplasma.setVisibility(View.VISIBLE);
                trJmlPlasma.setVisibility(View.VISIBLE);
            }

            if (setupdate) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();

                model = db.rawQuery("SELECT * FROM " + tabel + " where id_sensus =" + id_sensus + " and id = " + RowID + "", null);
                model.moveToPosition(0);

                statusKirim = Integer.parseInt(model.getString(model.getColumnIndex("status")));
                if (statusKirim == 1) {
                    bottomNavigation.setVisibility(View.GONE);
                }

                id_jenisTanaman = model.getString(model.getColumnIndex("jenis_tanaman"));
                jenis_tanaman = dbhelper.instantSelect("jenis_tanaman", "mst_jenis_tanaman", "id", id_jenisTanaman);

                id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", id_komoditas);

                if (id_obj == 1) {
                    TBMI = model.getDouble(model.getColumnIndex("tbm"));
                    tbm.setText(String.format("%.2f", TBMI).toString().replaceAll("[,.]", "."));

                    TMI = model.getDouble(model.getColumnIndex("tm"));
                    tm.setText(String.format("%.2f", TMI).toString().replaceAll("[,.]", "."));

                    TTMI = model.getDouble(model.getColumnIndex("ttm"));
                    ttm.setText(String.format("%.2f", TTMI).toString().replaceAll("[,.]", "."));
                } else {
                    TBMI = model.getDouble(model.getColumnIndex("tbm_inti"));
                    tbm.setText(String.format("%.2f", TBMI).toString().replaceAll("[,.]", "."));

                    TMI = model.getDouble(model.getColumnIndex("tm_inti"));
                    tm.setText(String.format("%.2f", TMI).toString().replaceAll("[,.]", "."));

                    TTMI = model.getDouble(model.getColumnIndex("ttm_inti"));
                    ttm.setText(String.format("%.2f", TTMI).toString().replaceAll("[,.]", "."));

                    TBMP = model.getDouble(model.getColumnIndex("tbm_plasma"));
                    tbmPlasma.setText(String.format("%.2f", TBMP).toString().replaceAll("[,.]", "."));

                    TMP = model.getDouble(model.getColumnIndex("tm_plasma"));
                    tmPlasma.setText(String.format("%.2f", TMP).toString().replaceAll("[,.]", "."));

                    TTMP = model.getDouble(model.getColumnIndex("ttm_plasma"));
                    ttmPlasma.setText(String.format("%.2f", TTMP).toString().replaceAll("[,.]", "."));
                }

                JMLINTI = TBMI + TMI + TTMI;
                jmlTegak.setText(String.format("%.2f", JMLINTI));

                JMLPLASMA = TBMP + TMP + TTMP;
                jmlPlasma.setText(String.format("%.2f", JMLPLASMA));

                keterangan.setText(model.getString(model.getColumnIndex("keterangan")));

                loadJenisTanaman(jenis_tanaman);

                for (int i = 0; i < adapJenisTanaman.getCount(); i++) {
                    if (jenis_tanaman.equals(adapJenisTanaman.getItem(i).toString())) {
                        spJenisTanaman.setSelection(i);
                        Log.d("jenis tanaman ", adapJenisTanaman.getItem(i).toString());
                        break;
                    }
                }

                loadKomoditas(id_komoditas);
                for (int i = 0; i < adapKomoditas.getCount(); i++) {
                    if (komoditas.equals(adapKomoditas.getItem(i).toString())) {
                        spKomoditas.setSelection(i);
                        Log.d("komoditas ", adapKomoditas.getItem(i).toString());
                        break;
                    }
                }
            }
        }
        Log.d("id_obj", id_obj + "");

        txId_sensus.setText(id_sensus);

        int jumlahKomo = dbhelper.count(tabel + " WHERE id_sensus = '" + id_sensus + "'");
        jml_luas.setText(jumlahKomo + "");

        tm.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        tm.setText(formatted);
                        tm.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        tm.setText(formatted);
                        tm.setSelection(formatted.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString() != null && !s.toString().equals(",") && !s.toString().equals(".") && !s.toString().trim().equals("")) {
                    String cleanString = s.toString().replaceAll("[,.]", ".");
                    TMI = Double.valueOf(cleanString);
                    JMLINTI = TBMI + TMI + TTMI;
                    jmlTegak.setText(String.format("%.2f", JMLINTI) + "");
                } else {
                    TMI = 0.0;
                    JMLINTI = TBMI + TMI + TTMI;
                    jmlTegak.setText(String.format("%.2f", JMLINTI) + "");
                }
            }
        });

        ttm.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        ttm.setText(formatted);
                        ttm.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        ttm.setText(formatted);
                        ttm.setSelection(formatted.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString() != null && !s.toString().equals(",") && !s.toString().equals(".") && !s.toString().trim().equals("")) {
                    String cleanString = s.toString().replaceAll("[,.]", ".");
                    TTMI = Double.parseDouble(cleanString);
                    JMLINTI = TBMI + TMI + TTMI;
                    jmlTegak.setText(String.format("%.2f", JMLINTI) + "");
                } else {
                    TTMI = 0.0;
                    JMLINTI = TBMI + TMI + TTMI;
                    jmlTegak.setText(String.format("%.2f", JMLINTI) + "");
                }
            }
        });

        tbm.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        tbm.setText(formatted);
                        tbm.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        tbm.setText(formatted);
                        tbm.setSelection(formatted.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString() != null && !s.toString().equals(",") && !s.toString().equals(".") && !s.toString().trim().equals("")) {
                    String cleanString = s.toString().replaceAll("[,.]", ".");
                    TBMI = Double.valueOf(cleanString);
                    JMLINTI = TBMI + TMI + TTMI;
                    jmlTegak.setText(String.format("%.2f", JMLINTI) + "");
                } else {
                    TBMI = 0.0;
                    JMLINTI = TBMI + TMI + TTMI;
                    jmlTegak.setText(String.format("%.2f", JMLINTI) + "");
                }
            }
        });

        tmPlasma.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        tmPlasma.setText(formatted);
                        tmPlasma.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        tmPlasma.setText(formatted);
                        tmPlasma.setSelection(formatted.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString() != null && !s.toString().equals(",") && !s.toString().equals(".") && !s.toString().trim().equals("")) {
                    String cleanString = s.toString().replaceAll("[,.]", ".");
                    TMP = Double.valueOf(cleanString);
                    JMLPLASMA = TBMP + TMP + TTMP;
                    jmlPlasma.setText(String.format("%.2f", JMLPLASMA) + "");
                } else {
                    TMP = 0.0;
                    JMLPLASMA = TBMP + TMP + TTMP;
                    jmlPlasma.setText(String.format("%.2f", JMLPLASMA) + "");
                }
            }
        });

        ttmPlasma.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        ttmPlasma.setText(formatted);
                        ttmPlasma.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        ttmPlasma.setText(formatted);
                        ttmPlasma.setSelection(formatted.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString() != null && !s.toString().equals(",") && !s.toString().equals(".") && !s.toString().trim().equals("")) {
                    String cleanString = s.toString().replaceAll("[,.]", ".");
                    TTMP = Double.valueOf(cleanString);
                    JMLPLASMA = TBMP + TMP + TTMP;
                    jmlPlasma.setText(String.format("%.2f", JMLPLASMA) + "");
                } else {
                    TTMP = 0.0;
                    JMLPLASMA = TBMP + TMP + TTMP;
                    jmlPlasma.setText(String.format("%.2f", JMLPLASMA) + "");
                }
            }
        });

        tbmPlasma.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        tbmPlasma.setText(formatted);
                        tbmPlasma.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        tbmPlasma.setText(formatted);
                        tbmPlasma.setSelection(formatted.length());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString() != null && !s.toString().equals(",") && !s.toString().equals(".") && !s.toString().trim().equals("")) {
                    String cleanString = s.toString().replaceAll("[,.]", ".");
                    TBMP = Double.parseDouble(cleanString);
                    JMLPLASMA = TBMP + TMP + TTMP;
                    jmlPlasma.setText(String.format("%.2f", JMLPLASMA) + "");
                } else {
                    TBMP = 0.0;
                    JMLPLASMA = TBMP + TMP + TTMP;
                    jmlPlasma.setText(String.format("%.2f", JMLPLASMA) + "");
                }
            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                if (checkIsian()) {
                                    if (setupdate) {
                                        updateData();
                                    } else {
                                        savedata();
                                    }
                                    item.setEnabled(false);
                                }
                                break;
                        }
                        return false;
                    }
                });
    }

    private void loadJenisTanaman(String jenis) {
        Set<String> set = dbhelper.getAll(" id, jenis_tanaman",
                " mst_jenis_tanaman where jenis_tanaman = '" + jenis + "'", " id");

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapJenisTanaman = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapJenisTanaman.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spJenisTanaman.setAdapter(adapJenisTanaman);
        spJenisTanaman.setWillNotDraw(false);
    }

    private void loadSpinnerJenisTanaman() {
        Set<String> set = dbhelper.getAll(" id, jenis_tanaman",
                " mst_jenis_tanaman", " id");

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapJenisTanaman = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapJenisTanaman.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spJenisTanaman.setAdapter(adapJenisTanaman);
        spJenisTanaman.setWillNotDraw(false);
    }

    private void loadKomoditas(String idKomo) {
        Set<String> set = dbhelper.getAll(" a.id_komoditas, a.komoditas", " mst_komoditas a where a.id_komoditas= '" + idKomo + "' ", " a.komoditas");

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapKomoditas = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapKomoditas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKomoditas.setAdapter(adapKomoditas);
        spKomoditas.setWillNotDraw(false);
    }

    private void loadSpinnerKomoditas(String jenis) {
        Set<String> set = null;
        if (setupdate) {
            set = dbhelper.getAll("a.id_komoditas, a.komoditas", "mst_komoditas a inner join mst_jenis_tanaman b on b.id = a.id_tanaman where a.id_tanaman = " + jenis + "", "a.komoditas");
        } else {
            set = dbhelper.getAll(" a.id_komoditas, a.komoditas",
                    " mst_komoditas a inner join mst_jenis_tanaman b on b.id = a.id_tanaman where a.id_tanaman = " + jenis + " and not exists (select b.id_komoditas from " + tabel + " b where a.id_komoditas = b.id_komoditas and id_sensus = " + id_sensus + ") ", " a.komoditas");
        }

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapKomoditas = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapKomoditas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKomoditas.setAdapter(adapKomoditas);
        spKomoditas.setWillNotDraw(false);
    }

    public void onResume() {
        super.onResume();
        spKomoditas.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spJenisTanaman.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            if (setupdate) {
                check = check + 1;
                if (check > 2) {
                    Spinner spinner = (Spinner) parent;
                    if (spinner.getId() == R.id.txJenisTanaman) {
                        String JenisTanaman = parent.getItemAtPosition(position)
                                .toString();
                        selectJenisTanaman = parent.getItemIdAtPosition(position);
                        id_jenisTanaman = dbhelper.instantSelect("id", "mst_jenis_tanaman", "jenis_tanaman", JenisTanaman).toString();
                        Log.d("jenisTanaman", JenisTanaman + "");
                        Log.d("Response pengawas", id_jenisTanaman);

                        loadSpinnerKomoditas(id_jenisTanaman);
                    } else if (spinner.getId() == R.id.txKomoditas) {
                        String Komoditas = parent.getItemAtPosition(position)
                                .toString();
                        selectKomoditas = parent.getItemIdAtPosition(position);
                        id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", Komoditas).toString();
                        Log.d("Create selectKomoditas", selectKomoditas + "");
                        Log.d("Response komoditas", Komoditas);
                    }
                }
            } else {
                Spinner spinner = (Spinner) parent;
                if (spinner.getId() == R.id.txJenisTanaman) {
                    String JenisTanaman = parent.getItemAtPosition(position)
                            .toString();
                    selectJenisTanaman = parent.getItemIdAtPosition(position);
                    id_jenisTanaman = dbhelper.instantSelect("id", "mst_jenis_tanaman", "jenis_tanaman", JenisTanaman).toString();
                    Log.d("jenisTanaman", JenisTanaman + "");
                    Log.d("Response pengawas", id_jenisTanaman);

                    loadSpinnerKomoditas(id_jenisTanaman);
                } else if (spinner.getId() == R.id.txKomoditas) {
                    String Komoditas = parent.getItemAtPosition(position)
                            .toString();
                    selectKomoditas = parent.getItemIdAtPosition(position);
                    id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", Komoditas).toString();
                    Log.d("Create selectKomoditas", selectKomoditas + "");
                    Log.d("Response komoditas", Komoditas);
                }
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    void updateData() {
        if (id_obj == 1) {
            if (dbhelper.updateLuasKPetani(Long.parseLong(RowID), Integer.parseInt(id_jenisTanaman),
                    Integer.parseInt(id_komoditas),
                    TBMI, TMI, TTMI,
                    keterangan.getText().toString().trim())) {
                Toast.makeText(this, "luas areal petani berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (id_obj == 2) {
            if (dbhelper.updateLuasKPerusahaan(Long.parseLong(RowID), Integer.parseInt(id_jenisTanaman),
                    Integer.parseInt(id_komoditas),
                    TBMI, TMI, TTMI,
                    TBMP, TMP, TMI,
                    keterangan.getText().toString().trim())) {
                Toast.makeText(this, "luas areal perusahaan berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    void savedata() {
        Log.d("simpan", id_sensus + ",id_obj," + id_obj + "," + Integer.parseInt(id_komoditas) + "," + TBMI + "," +
                TMI + "," + TTMI
                + "," + keterangan.getText().toString().trim());

        if (id_obj == 1) {
            if (dbhelper.insertLuasKPetani(id_sensus, Integer.parseInt(id_jenisTanaman),
                    Integer.parseInt(id_komoditas),
                    TBMI, TMI, TTMI,
                    keterangan.getText().toString().trim())) {
                Toast.makeText(this, "luas areal disimpan", Toast.LENGTH_SHORT).show();
            }
        } else if (id_obj == 2) {
            if (dbhelper.insertLuasKPerusahaan(id_sensus, Integer.parseInt(id_jenisTanaman),
                    Integer.parseInt(id_komoditas),
                    TBMI, TMI, TTMI,
                    TBMP, TMP, TMI,
                    keterangan.getText().toString().trim())) {
                Toast.makeText(this, "luas areal disimpan", Toast.LENGTH_SHORT).show();
            }
        }

        AlertTambahan();
    }

    private boolean checkIsian() {
        if (tbm.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom TBM masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (tm.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom TM masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (ttm.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom TTM masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tbmPlasma.getVisibility() != View.GONE) {
            if (tbmPlasma.getText().toString().trim().length() == 0) {
                Toast.makeText(getBaseContext(),
                        "Kolom TBM PLASMA masih kosong",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (tmPlasma.getVisibility() != View.GONE) {
            if (tmPlasma.getText().toString().trim().length() == 0) {
                Toast.makeText(getBaseContext(),
                        "Kolom TM PLASMA masih kosong",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (ttmPlasma.getVisibility() != View.GONE) {
            if (ttmPlasma.getText().toString().trim().length() == 0) {
                Toast.makeText(getBaseContext(),
                        "Kolom TTM PLASMA masih kosong",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        goListData();
    }

    public void goListData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(luas_areal.this);
        alert.setTitle("Perhatian");
        alert.setMessage("Anda Ingin keluar ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (setupdate) {
                    finish();
                } else {
                    Intent intent = new Intent(luas_areal.this,
                            view_data.class);
                    intent.putExtra("id_obj_pencatatan", id_obj);
                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(luas_areal.this,
//                            MainActivity.class);
//                    startActivity(intent);
                }
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    public void AlertTambahan() {
        AlertDialog.Builder alert = new AlertDialog.Builder(luas_areal.this);
        alert.setTitle("PERHATIAN");
        alert.setCancelable(false);
        alert.setMessage("Ingin menambahkan komoditas ?");
        alert.setPositiveButton("TAMBAHKAN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(luas_areal.this,
                        luas_areal.class);
                intent.putExtra("id_sensus", id_sensus);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(luas_areal.this,
                        real_menu_list.class);
                intent.putExtra("id_sensus", id_sensus);
                startActivity(intent);
            }
        });
        alert.show();
    }
}
