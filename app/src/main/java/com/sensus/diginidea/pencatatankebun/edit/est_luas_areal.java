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
import com.sensus.diginidea.pencatatankebun.R;
import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.submenu;
import com.sensus.diginidea.pencatatankebun.view_data_estimasi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class est_luas_areal extends AppCompatActivity {
    TextView txId_sensus, jmlPlasma, jmlTegak, jml_luas;
    TextView txSatuanJumlahTegakan;
    EditText tbm, tm, ttm; //inti
    EditText tbmPlasma, tmPlasma, ttmPlasma; //inti
    Double TBMI = 0.0, TMI = 0.0, TTMI = 0.0, TBMP = 0.0, TMP = 0.0, TTMP = 0.0;
    Double JMLINTI = 0.0, JMLPLASMA = 0.0;
    TextView headInti, headPlasma, headTegakan;

    EditText keterangan;

    String id_sensus;
    int id_obj;
    Spinner spKomoditas;
    Spinner spJenisTanaman;

    DbHelper dbhelper;
    ArrayAdapter<String> adapKomoditas;
    ArrayAdapter<String> adapJenisTanaman;

    long selectKomoditas = 0;
    String id_komoditas, id_jenisTanaman;
    String komoditas, jenis_tanaman;
    String tabelEstLuas, tabelLuas;
    TableRow trTTMplasma, trTMplasma, trTBMplasma, trJmlPlasma;
    private BottomNavigationView bottomNavigation;
    String RowID;
    Boolean setupdate = false;
    Cursor model = null;
    int check = 0;
    int statusKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.est_luas_areal);

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

        spJenisTanaman = (Spinner) findViewById(R.id.txJenisTanaman);
        spKomoditas = (Spinner) findViewById(R.id.txKomoditas);
        txId_sensus = (TextView) findViewById(R.id.id_sensus);

        trTTMplasma = (TableRow) findViewById(R.id.ttmPlasma);
        trTMplasma = (TableRow) findViewById(R.id.tmPlasma);
        trTBMplasma = (TableRow) findViewById(R.id.tbmPlasma);
        trJmlPlasma = (TableRow) findViewById(R.id.JumlahPlasma);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        dbhelper = new DbHelper(this);
//        loadSpinnerKomoditas(id_jenisTanaman);

        Bundle extras = getIntent().getExtras();
        if (getIntent().getExtras() != null) {
            setupdate = getIntent().getExtras().getBoolean("set");
            Log.i("Value", extras.getString("id_sensus"));
            Log.d("Response", extras + "");

            id_sensus = extras.getString("id_sensus");
            Log.d("id_sensus", id_sensus + "");
            RowID = getIntent().getExtras().getString("ident");

            id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));
            if (id_obj == 1) {
                headTegakan.setText("JUMLAH TEGAKAN");
                tbmPlasma.setVisibility(View.GONE);
                tmPlasma.setVisibility(View.GONE);
                ttmPlasma.setVisibility(View.GONE);
                jml_luas.setText("Jumlah data : " + dbhelper.count("tbl_luas_kebun_petani WHERE id_sensus = '" + id_sensus + "'"));
//                tvSatuanTBM.setText("POHON/RUMPUN");
//                tvSatuanTM.setText("POHON/RUMPUN");
//                tvSatuanTTM.setText("POHON/RUMPUN");
                txSatuanJumlahTegakan.setText("POHON/RUMPUN");
//                txSatuanTBMplasma.setText("");
//                txSatuanTMplasma.setText("");
//                txSatuanTTMplasma.setText("");
//                txSatuanJumlahplasma.setText("");

                headInti.setVisibility(View.GONE);
                headPlasma.setVisibility(View.GONE);
                trTTMplasma.setVisibility(View.GONE);
                trTMplasma.setVisibility(View.GONE);
                trTBMplasma.setVisibility(View.GONE);
                trJmlPlasma.setVisibility(View.GONE);
                tabelEstLuas = "tbl_est_luas_kebun_petani";
                tabelLuas = "tbl_luas_kebun_petani";
            } else if (id_obj == 2) {
                headTegakan.setText("LUAS AREAL");
                tbmPlasma.setVisibility(View.VISIBLE);
                tmPlasma.setVisibility(View.VISIBLE);
                ttmPlasma.setVisibility(View.VISIBLE);
                jml_luas.setText("Jumlah data : " + dbhelper.count("tb_luas_kebun_perusahaan WHERE id_sensus = '" + id_sensus + "'"));
//                tvSatuanTBM.setText(" HA");
//                tvSatuanTM.setText(" HA");
//                tvSatuanTTM.setText(" HA");
                txSatuanJumlahTegakan.setText(" HA");
//                txSatuanTBMplasma.setText(" HA");
//                txSatuanTMplasma.setText(" HA");
//                txSatuanTTMplasma.setText(" HA");
//                txSatuanJumlahplasma.setText(" HA");
                tabelLuas = "tbl_luas_kebun_perusahaan";
                tabelEstLuas = "tbl_est_luas_kebun_perusahaan";
            }
            loadSpinnerJenisTanaman();

            if (setupdate) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();

                model = db.rawQuery("SELECT * FROM " + tabelEstLuas + " where id_sensus =" + id_sensus + " and id = " + RowID + "", null);
                model.moveToPosition(0);

                statusKirim = Integer.parseInt(model.getString(model.getColumnIndex("status")));
                if (statusKirim == 1) {
                    bottomNavigation.setVisibility(View.GONE);
                }
                id_jenisTanaman = model.getString(model.getColumnIndex("jenis_tanaman"));
                jenis_tanaman = dbhelper.instantSelect("jenis_tanaman", "mst_jenis_tanaman", "id", id_jenisTanaman);

                id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", id_komoditas);

                loadJenisTanaman(id_jenisTanaman);
                loadKomoditas(id_komoditas);

                if (id_obj == 1) {
                    TBMI = model.getDouble(model.getColumnIndex("tbm"));
                    tbm.setText(String.format("%.2f", TBMI).toString().replaceAll("[,.]", "."));

                    TMI = model.getDouble(model.getColumnIndex("tm"));
                    tm.setText(String.format("%.2f", TMI).toString().replaceAll("[,.]", "."));

                    TTMI = model.getDouble(model.getColumnIndex("ttm"));
                    ttm.setText(String.format("%.2f", TTMI).toString().replaceAll("[,.]", "."));

                    jmlTegak.setText((TBMI + TMI + TTMI) + "");
                } else {
                    TBMI = model.getDouble(model.getColumnIndex("tbm_inti"));
                    tbm.setText(String.format("%.2f", TBMI).toString().replaceAll("[,.]", "."));

                    TMI = model.getDouble(model.getColumnIndex("tm_inti"));
                    tm.setText(String.format("%.2f", TMI).toString().replaceAll("[,.]", "."));

                    TTMI = model.getDouble(model.getColumnIndex("ttm_inti"));
                    ttm.setText(String.format("%.2f", TTMI).toString().replaceAll("[,.]", "."));
                    jmlTegak.setText((TBMI + TMI + TTMI) + "");

                    TBMP = model.getDouble(model.getColumnIndex("tbm_plasma"));
                    tbmPlasma.setText(String.format("%.2f", TBMP).toString().replaceAll("[,.]", "."));

                    TMP = model.getDouble(model.getColumnIndex("tm_plasma"));
                    tmPlasma.setText(String.format("%.2f", TMP).toString().replaceAll("[,.]", "."));

                    TTMP = model.getDouble(model.getColumnIndex("ttm_plasma"));
                    ttmPlasma.setText(String.format("%.2f", TTMP).toString().replaceAll("[,.]", "."));
                    jmlTegak.setText((TBMP + TMP + TTMP) + "");
                }
                keterangan.setText(model.getString(model.getColumnIndex("keterangan")));
            }
        }

        txId_sensus.setText(id_sensus);
        id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));

        String jumlahKomo = dbhelper.instantSelect("count (distinct id_komoditas)", tabelLuas, "id_sensus", id_sensus);
        jml_luas.setText(jumlahKomo);

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
                    jmlTegak.setText(String.format("%.2f", JMLINTI));
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
                    TTMI = Double.valueOf(cleanString);
                    JMLINTI = TBMI + TMI + TTMI;
                    jmlTegak.setText(String.format("%.2f", JMLINTI));
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
                    jmlTegak.setText(String.format("%.2f", JMLINTI));
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
                    jmlPlasma.setText(JMLPLASMA + "");
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
                    jmlPlasma.setText(JMLPLASMA + "");
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
                    TBMP = Double.valueOf(cleanString);
                    JMLPLASMA = TBMP + TMP + TTMP;
                    jmlPlasma.setText(JMLPLASMA + "");
                }
            }
        });

        if (!setupdate) {
            int cKmoditas = dbhelper.count(tabelLuas + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_est_luas_area = 0");
            if (cKmoditas == 0) {
                Toast.makeText(est_luas_areal.this, "Bagian perkiraan luas areal sudah selesai diinput", Toast.LENGTH_SHORT).show();

                Intent i = null;
                i = new Intent(est_luas_areal.this, submenu.class);
                i.putExtra("id_obj_pencatatan", id_obj);
                startActivity(i);
            }
        }

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

    private void loadKomoditas(String idKomo) {
        Set<String> set = dbhelper.getAll("id_komoditas, komoditas", "mst_komoditas where id_komoditas = " + idKomo + "", "komoditas");
        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapKomoditas = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapKomoditas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKomoditas.setAdapter(adapKomoditas);
        spKomoditas.setWillNotDraw(false);
    }

    private void loadJenisTanaman(String jenis) {
        Set<String> set = dbhelper.getAll("id, jenis_tanaman", "mst_jenis_tanaman where id = " + jenis, "jenis_tanaman");

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapJenisTanaman = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapJenisTanaman.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spJenisTanaman.setAdapter(adapJenisTanaman);
        spJenisTanaman.setWillNotDraw(false);
    }

    private void loadSpinnerJenisTanaman() {
//        Set<String> set = dbhelper.getAll("id, jenis_tanaman", "mst_jenis_tanaman", "jenis_tanaman");
        Set<String> set = dbhelper.getAll("distinct c.id, c.jenis_tanaman",
                tabelLuas + " a inner join mst_jenis_tanaman c on c.id = a.jenis_tanaman where a.id_sensus = " + id_sensus + " and a.status_est_luas_area = 0 ", "c.jenis_tanaman");

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapJenisTanaman = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapJenisTanaman.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spJenisTanaman.setAdapter(adapJenisTanaman);
        spJenisTanaman.setWillNotDraw(false);
    }

    private void loadSpinnerKomoditas(String jenis) {
        Set<String> set = null;
//        if (setupdate) {
//            set = dbhelper.getAll("a.id_komoditas, a.komoditas", "mst_komoditas a inner join ", "komoditas");
//        } else {
        set = dbhelper.getAll("a.id_komoditas, b.komoditas",
                tabelLuas + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas inner join mst_jenis_tanaman c on c.id = a.jenis_tanaman where a.jenis_tanaman = " +
                        jenis + " and a.id_sensus = " + id_sensus + " and a.status_est_luas_area = 0", "b.komoditas");
//        }

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

            Spinner spinner = (Spinner) parent;
            if (setupdate) {
                check = check + 1;
                if (check > 2) {
                    if (spinner.getId() == R.id.txJenisTanaman) {
                        String JenisTanaman = parent.getItemAtPosition(position)
                                .toString();
                        id_jenisTanaman = dbhelper.instantSelect("id", "mst_jenis_tanaman", "jenis_tanaman", JenisTanaman).toString();
                        Log.d("jenisTanaman22", JenisTanaman + "");
                        Log.d("Response pengawas22", id_jenisTanaman);

                        loadSpinnerKomoditas(id_jenisTanaman);
                    } else if (spinner.getId() == R.id.txKomoditas) {
                        String Komoditas = parent.getItemAtPosition(position)
                                .toString();
                        selectKomoditas = parent.getItemIdAtPosition(position);
                        id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", Komoditas).toString();
                        Log.d("Create selectPengawas22", selectKomoditas + "");
                        Log.d("Response pengawas22", Komoditas);
                    }
                }
            } else {
                if (spinner.getId() == R.id.txJenisTanaman) {
                    String JenisTanaman = parent.getItemAtPosition(position)
                            .toString();
                    id_jenisTanaman = dbhelper.instantSelect("id", "mst_jenis_tanaman", "jenis_tanaman", JenisTanaman).toString();
                    Log.d("jenisTanaman", JenisTanaman + "");
                    Log.d("Response pengawas", id_jenisTanaman);

                    loadSpinnerKomoditas(id_jenisTanaman);
                } else if (spinner.getId() == R.id.txKomoditas) {
                    String Komoditas = parent.getItemAtPosition(position)
                            .toString();
                    selectKomoditas = parent.getItemIdAtPosition(position);
                    id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", Komoditas).toString();
                    Log.d("Create selectPengawas", selectKomoditas + "");
                    Log.d("Response pengawas", Komoditas);
                }
            }
            Log.d("check", check + "");
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

    void updateData() {
        if (id_obj == 1) {
            if (dbhelper.updateEstLuasPetani(Long.parseLong(RowID), Integer.parseInt(id_jenisTanaman),
                    Integer.parseInt(id_komoditas),
                    TBMI, TMI, TTMI,
                    keterangan.getText().toString().trim())) {
                Toast.makeText(this, "estimasi luas areal pekebun berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (id_obj == 2) {
            if (dbhelper.updateEstLuasPerusahaan(Long.parseLong(RowID), Integer.parseInt(id_jenisTanaman),
                    Integer.parseInt(id_komoditas),
                    TBMI, TMI, TTMI,
                    TBMP, TMP, TTMP,
                    keterangan.getText().toString().trim())) {
                Toast.makeText(this, "estimasi luas areal perusahaan berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    void savedata() {
        Log.d("simpan", id_sensus + ",id_obj" + id_obj + "," + Integer.parseInt(id_komoditas) + "," + Double.parseDouble(tbm.getText().toString().trim()) + "," +
                Double.parseDouble(tm.getText().toString().trim()) + "," + Double.parseDouble(ttm.getText().toString().trim())
                + "," + keterangan.getText().toString().trim());

        if (id_obj == 1) {
            if (dbhelper.insertEstLuasPetani(id_sensus,
                    Integer.parseInt(id_jenisTanaman),
                    Integer.parseInt(id_komoditas),
                    TBMI, TMI, TTMI,
                    keterangan.getText().toString().trim())) {
                Toast.makeText(this, "estimasi luas areal disimpan", Toast.LENGTH_SHORT).show();
            }
        } else if (id_obj == 2) {
            if (dbhelper.insertEstLuasPerusahaan(id_sensus,
                    Integer.parseInt(id_jenisTanaman),
                    Integer.parseInt(id_komoditas),
                    TBMI, TMI, TTMI,
                    TBMP, TMP, TMI,
                    keterangan.getText().toString().trim())) {
                Toast.makeText(this, "estimasi luas areal disimpan", Toast.LENGTH_SHORT).show();
            }
        }

        int count = dbhelper.count(tabelLuas + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_est_luas_area = 0");
        if (count == 0) {
            Intent intent = null;
            intent = new Intent(est_luas_areal.this, submenu.class);
            intent.putExtra("id_obj_pencatatan", id_obj);
            startActivity(intent);
        } else {
            int id_luas = Integer.parseInt(dbhelper.instantSelect("id", tabelLuas, " id_sensus = " + id_sensus + " and id_komoditas", id_komoditas + ""));
            dbhelper.updateStatus(tabelLuas, "status_est_luas_area", id_luas, 1);

            Intent intent = new Intent(est_luas_areal.this,
                    est_luas_areal.class);
            intent.putExtra("id_sensus", id_sensus);
            intent.putExtra("id_obj_pencatatan", id_obj);
            startActivity(intent);
        }
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
        AlertDialog.Builder alert = new AlertDialog.Builder(est_luas_areal.this);
        alert.setTitle("Perhatian");
        alert.setCancelable(false);
        alert.setMessage("Anda Ingin keluar ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (setupdate) {
                    finish();
                } else {
                    Intent intent;
                    intent = new Intent(est_luas_areal.this,
                            view_data_estimasi.class);
                    intent.putExtra("id_obj_pencatatan", id_obj);
                    startActivity(intent);
                    finish();
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
}