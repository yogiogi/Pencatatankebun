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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class est_produksi extends AppCompatActivity {
    TextView id_sensus, txJml_luas, txKonversi, txSatuanKonversi;
    TextView txsatHARGA, txsatJmlProd, txHARGA;
    EditText txJmlProd, txNilaiJual, txKeterangan; //inti
    TableRow trKonversi, trSatuan;

    String idSENS, tabelLuas;
    int id_obj;
    Spinner spKomoditas, spWujudProduksi, spSatuan;
    DbHelper dbhelper;
    ArrayAdapter<String> adapKomoditas;
    ArrayAdapter<String> adapWujudProduksi;
    ArrayAdapter<String> adapSatuan;
    String WujudProduksi;
    String Satuan = "KG";

    long selectKomoditas = 0;
    String id_komoditas;
    String Komoditas = "";

    long selectWujud = 0;
    long selectSatuan = 0;

    String id_wujudProduksi;
    String id_satuan;

    String tabelEstProduksi;
    double nilaijual = 0.0, harga = 0.0, jumlah = 0.0, sKonversi = 0.0;
    private BottomNavigationView bottomNavigation;
    String RowID;
    Cursor model = null;
    Boolean setupdate = false;
    int check = 0;
    int statusKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.est_produksi);

        txsatHARGA = (TextView) findViewById(R.id.txsatHARGA);
        txsatJmlProd = (TextView) findViewById(R.id.txsatJmlProd);

        id_sensus = (TextView) findViewById(R.id.id_sensus);

        txJmlProd = (EditText) findViewById(R.id.txJmlProd);
        txNilaiJual = (EditText) findViewById(R.id.txNilaiJual);
        txHARGA = (TextView) findViewById(R.id.txHARGA);
        txKeterangan = (EditText) findViewById(R.id.txKeterangan); //inti

        spKomoditas = (Spinner) findViewById(R.id.txKomoditas);
        spWujudProduksi = (Spinner) findViewById(R.id.txWjdProduksi);
        txJml_luas = (TextView) findViewById(R.id.txJml_luas);
        txSatuanKonversi = (TextView) findViewById(R.id.txSatuanKonversi);

        spSatuan = (Spinner) findViewById(R.id.txSatuan);
        txKonversi = (TextView) findViewById(R.id.txKonversi);
        trKonversi = (TableRow) findViewById(R.id.trKonversi);
        trSatuan = (TableRow) findViewById(R.id.trSatuan);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        dbhelper = new DbHelper(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            Log.i("Value", extras.getString("id_sensus"));
            Log.d("Response", extras + "");

            setupdate = getIntent().getExtras().getBoolean("set");
            idSENS = extras.getString("id_sensus");
            Log.d("id_sensus", idSENS + "");

            id_sensus.setText(idSENS);
            RowID = getIntent().getExtras().getString("ident");

            id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", idSENS));
            if (id_obj == 1) {
                tabelEstProduksi = "tbl_est_produksi_kebun_petani";
                tabelLuas = "tbl_luas_kebun_petani";
                txsatHARGA.setText("PER KG");
                txsatJmlProd.setText("KG");
            } else if (id_obj == 2) {
                tabelEstProduksi = "tbl_est_produksi_kebun_perusahaan";
                tabelLuas = "tbl_luas_kebun_perusahaan";
                txsatHARGA.setText("PER TON");
                txsatJmlProd.setText("TON");
            }

            String jumlahKomo = dbhelper.instantSelect("count(distinct id_komoditas)", tabelLuas, "id_sensus", idSENS);
            txJml_luas.setText(jumlahKomo + "");

            loadSpinnerKomoditas();

            if (setupdate) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();

                model = db.rawQuery("SELECT * FROM " + tabelEstProduksi + " where id_sensus =" + idSENS + " and id = " + RowID + "", null);
                model.moveToPosition(0);

                statusKirim = Integer.parseInt(model.getString(model.getColumnIndex("status")));
                if (statusKirim == 1) {
                    bottomNavigation.setVisibility(View.GONE);
                }

                id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                Komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", id_komoditas);
                Log.d("Komoditas", Komoditas);

                loadKomoditas(id_komoditas);
                nilaijual = model.getDouble(model.getColumnIndex("nilai_jual"));
                harga = model.getDouble(model.getColumnIndex("harga"));

                jumlah = model.getDouble(model.getColumnIndex("jumlah_prod"));
                WujudProduksi = model.getString(model.getColumnIndex("wujud_produksi"));
                Satuan = model.getString(model.getColumnIndex("satuan"));
                txNilaiJual.setText(String.format("%.0f", nilaijual).toString().replaceAll("[,.]", "."));

                if (Komoditas.equals("Kelapa") || Komoditas.equals("Karet")) {
                    txKonversi.setText(String.format("%.2f", jumlah).toString().replaceAll("[,.]", "."));
                    sKonversi = jumlah;

                    Double konvert = 1.0;
                    try {
                        konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                    } catch (Exception e) {
                    }

                    if (Komoditas.equals("Kelapa")) {
                        if (Satuan.equals("KG")) {
                            konvert = 1.0;
                        }
                    }

                    jumlah = (sKonversi / konvert);
                    harga = nilaijual / sKonversi;
                }

                txJmlProd.setText(String.format("%.2f", jumlah).toString().replaceAll("[,.]", "."));
                txHARGA.setText(String.format("%.0f", harga).toString().replaceAll("[,.]", "."));

                txKeterangan.setText(model.getString(model.getColumnIndex("keterangan")));

                for (int i = 0; i < adapKomoditas.getCount(); i++) {
                    if (Komoditas.equals(adapKomoditas.getItem(i).toString())) {
                        spKomoditas.setSelection(i);
                        Log.d("jenis komoditas ", adapKomoditas.getItem(i).toString());
                        loadSpinnerWujudProduksi(id_komoditas);
                        break;
                    }
                }

                for (int i = 0; i < adapWujudProduksi.getCount(); i++) {
                    if (WujudProduksi.equals(adapWujudProduksi.getItem(i).toString())) {
                        spWujudProduksi.setSelection(i);
                        Log.d("jenis tanaman ", adapWujudProduksi.getItem(i).toString());
                        break;
                    }
                }

                if (Komoditas.equals("Kelapa") || Komoditas.equals("Karet")) {
                    loadSatuan(Integer.parseInt(id_komoditas), WujudProduksi);

                    for (int i = 0; i < adapSatuan.getCount(); i++) {
                        if (Satuan.equals(adapSatuan.getItem(i).toString())) {
                            spSatuan.setSelection(i);
                            Log.d("Satuan ", adapSatuan.getItem(i).toString());
                            break;
                        }
                    }
                }

                if (Komoditas.equals("Kelapa")) {
                    txsatJmlProd.setVisibility(View.GONE);
                    trKonversi.setVisibility(View.VISIBLE);
                    trSatuan.setVisibility(View.VISIBLE);
                } else if (Komoditas.equals("Karet")) {
                    txsatJmlProd.setVisibility(View.GONE);
                    trKonversi.setVisibility(View.VISIBLE);
                    trSatuan.setVisibility(View.VISIBLE);
                } else {
                    txsatJmlProd.setVisibility(View.VISIBLE);
                    trKonversi.setVisibility(View.GONE);
                    trSatuan.setVisibility(View.GONE);
                }
            }
        }


        txNilaiJual.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty() && s.toString() != null && !s.toString().equals(",") && !s.toString().equals(".") && !s.toString().trim().equals("")) {
                    String cleanString = s.toString().replaceAll("[,.]", ".");
                    nilaijual = Double.valueOf(cleanString);
                }

                Double konvert = 1.0;
                try {
                    konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                } catch (Exception e) {
                }

                if (Komoditas.equals("Kelapa")) {
                    if (Satuan.equals("KG")) {
                        konvert = 1.0;
                    }
                }

                sKonversi = (konvert * jumlah);
                txKonversi.setText(String.format("%.2f", sKonversi) + "");

                harga = nilaijual / sKonversi;
                if (nilaijual == 0.0 || jumlah == 0.0) {
                    harga = 0.0;
                }
                txHARGA.setText(String.format("%.0f", harga) + "");
            }
        });

        txJmlProd.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        txJmlProd.setText(formatted);
                        txJmlProd.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        txJmlProd.setText(formatted);
                        txJmlProd.setSelection(formatted.length());
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
                    jumlah = Double.valueOf(cleanString);
                } else {
                    jumlah = 0.0;
                }

                Double konvert = 1.0;
                try {
                    konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                } catch (Exception e) {
                }

                if (Komoditas.equals("Kelapa")) {
                    if (Satuan.equals("KG")) {
                        konvert = 1.0;
                    }
                }

                sKonversi = (konvert * jumlah);
                txKonversi.setText(String.format("%.2f", sKonversi) + "");

                harga = nilaijual / sKonversi;
                if (nilaijual == 0.0 || jumlah == 0.0) {
                    harga = 0.0;
                }
                txHARGA.setText(String.format("%.0f", harga) + "");
            }
        });

        if (!setupdate) {
            int cKmoditas = dbhelper.count(tabelLuas + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + idSENS + " and status_est_prod = 0");
            if (cKmoditas == 0) {
                Toast.makeText(est_produksi.this, "Bagian perkiraan produksi sudah selesai diinput", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(est_produksi.this, est_luas_areal.class);
                i.putExtra("id_sensus", idSENS);
                startActivity(i);
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener()

                {
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


    private void loadSatuan(int id, String wujud_produksi) {
        Set<String> set = null;
        set = dbhelper.getAll("id, satuan", "tb_satuan where id_komoditas  = 0", "satuan");
        if (wujud_produksi.equals("Buah Kelapa")) {
            set = dbhelper.getAll("id, satuan", "tb_satuan where id_komoditas  = '" + id + "'", "satuan");
        }

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapSatuan = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapSatuan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spSatuan.setAdapter(adapSatuan);
        spSatuan.setWillNotDraw(false);
    }


    private void loadSpinnerKomoditas() {
        Set<String> set = dbhelper.getAll("a.id_komoditas, b.komoditas", tabelLuas + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where a.id_sensus = " + idSENS + " and status_est_prod = 0", "b.komoditas");
        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapKomoditas = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapKomoditas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKomoditas.setAdapter(adapKomoditas);
        spKomoditas.setWillNotDraw(false);
    }

    private void loadSpinnerWujudProduksi(String id) {
        Set<String> set = dbhelper.getAll("a.id_komoditas, b.wujud_produksi ", "mst_komoditas a inner join mst_wujud_produksi b on a.id_komoditas = b.id_komoditas where a.id_komoditas = '" + id + "'", "b.wujud_produksi");

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapWujudProduksi = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapWujudProduksi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spWujudProduksi.setAdapter(adapWujudProduksi);
        spWujudProduksi.setWillNotDraw(false);
    }

    public void onResume() {
        super.onResume();
        spKomoditas.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spWujudProduksi.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spSatuan.setOnItemSelectedListener(new MyOnItemSelectedListener());
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (setupdate) {
                check = check + 1;
                if (check > 2) {
                    Spinner spinner = (Spinner) parent;
                    if (spinner.getId() == R.id.txKomoditas) {
                        Komoditas = parent.getItemAtPosition(position)
                                .toString();
                        selectKomoditas = parent.getItemIdAtPosition(position);
                        id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", Komoditas).toString();
                        Log.d("Create selectKomoditas", selectKomoditas + "");
                        Log.d("Response Komoditas", Komoditas);
                        loadSpinnerWujudProduksi(id_komoditas);

                        if (Komoditas.equals("Kelapa")) {
                            txsatJmlProd.setVisibility(View.GONE);
                            trKonversi.setVisibility(View.VISIBLE);
                            trSatuan.setVisibility(View.VISIBLE);
                        } else if (Komoditas.equals("Karet")) {
                            trKonversi.setVisibility(View.VISIBLE);
                            trSatuan.setVisibility(View.VISIBLE);
                        } else {
                            trKonversi.setVisibility(View.GONE);
                            trSatuan.setVisibility(View.GONE);
                        }
                    }
                    if (spinner.getId() == R.id.txWjdProduksi) {
                        WujudProduksi = parent.getItemAtPosition(position)
                                .toString();
                        selectWujud = parent.getItemIdAtPosition(position);
                        id_wujudProduksi = dbhelper.instantSelect("id", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString();
                        Log.d("Create selectWujud", selectWujud + "");
                        Log.d("Response WujudProduksi", WujudProduksi);

                        Double konvert = 1.0;
                        try {
                            konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                        } catch (Exception e) {
                        }

                        sKonversi = (konvert * jumlah);
                        txKonversi.setText(String.format("%.2f", sKonversi) + "");

                        harga = nilaijual / sKonversi;
                        if (nilaijual == 0.0 || jumlah == 0.0) {
                            harga = 0.0;
                        }
                        txHARGA.setText(String.format("%.0f", harga) + "");

                        if (Komoditas.equals("Kelapa")) {
                            txsatJmlProd.setVisibility(View.GONE);
                            if (WujudProduksi.equals("Buah Kelapa")) {
                                trSatuan.setVisibility(View.VISIBLE);
                                trKonversi.setVisibility(View.VISIBLE);
                                txSatuanKonversi.setText(" KG KOPRA");
                            }
                        } else if (Komoditas.equals("Karet")) {
                            trKonversi.setVisibility(View.VISIBLE);
                            txSatuanKonversi.setText(" KG");
                            if (!WujudProduksi.equals("Kadar Karet Kering")) {
                                txSatuanKonversi.setText(" KG KADAR KARET KERING");
                            }
                        }

                        loadSatuan(Integer.parseInt(id_komoditas), WujudProduksi);
                    }
                    if (spinner.getId() == R.id.txSatuan) {
                        Satuan = parent.getItemAtPosition(position)
                                .toString();

                        Double konvert = 1.0;
                        try {
                            konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                        } catch (Exception e) {
                        }

                        if (Komoditas.equals("Kelapa")) {
                            if (Satuan.equals("Butir")) {
                                txSatuanKonversi.setText(" KG KOPRA");
                                trSatuan.setVisibility(View.VISIBLE);
                                txsatJmlProd.setText(" Butir");
                            } else if (Satuan.equals("KG")) {
                                konvert = 1.0;
                                txSatuanKonversi.setText(" KG");
                                txsatJmlProd.setText(" KG");
                            }
                        } else if (Komoditas.equals("Karet")) {
                            trKonversi.setVisibility(View.VISIBLE);
                            txSatuanKonversi.setText(" KG");
                            if (!WujudProduksi.equals("Kadar Karet Kering")) {
                                txSatuanKonversi.setText(" KG KADAR KARET KERING");
                            }
                        }

                        sKonversi = (konvert * jumlah);
                        txKonversi.setText(String.format("%.2f", sKonversi) + "");

                        harga = nilaijual / sKonversi;
                        if (nilaijual == 0.0 || jumlah == 0.0) {
                            harga = 0.0;
                        }
                        txHARGA.setText(String.format("%.0f", harga) + "");
                    }
                }
            } else {
                Spinner spinner = (Spinner) parent;
                if (spinner.getId() == R.id.txKomoditas) {
                    Komoditas = parent.getItemAtPosition(position)
                            .toString();
                    selectKomoditas = parent.getItemIdAtPosition(position);
                    id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", Komoditas).toString();
                    Log.d("Create selectKomoditas", selectKomoditas + "");
                    Log.d("Response Komoditas", Komoditas);
                    loadSpinnerWujudProduksi(id_komoditas);

                    if (Komoditas.equals("Kelapa")) {
                        txsatJmlProd.setVisibility(View.GONE);
                        trKonversi.setVisibility(View.VISIBLE);
                        trSatuan.setVisibility(View.VISIBLE);
                    } else if (Komoditas.equals("Karet")) {
                        trKonversi.setVisibility(View.VISIBLE);
                        trSatuan.setVisibility(View.VISIBLE);
                    } else {
                        trKonversi.setVisibility(View.GONE);
                        trSatuan.setVisibility(View.GONE);
                    }
                }
                if (spinner.getId() == R.id.txWjdProduksi) {
                    WujudProduksi = parent.getItemAtPosition(position)
                            .toString();
                    selectWujud = parent.getItemIdAtPosition(position);
                    id_wujudProduksi = dbhelper.instantSelect("id", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString();
                    Log.d("Create selectWujud", selectWujud + "");
                    Log.d("Response WujudProduksi", WujudProduksi);

                    Double konvert = 1.0;
                    try {
                        konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                    } catch (Exception e) {
                    }

                    sKonversi = (konvert * jumlah);
                    txKonversi.setText(String.format("%.2f", sKonversi) + "");

                    harga = nilaijual / sKonversi;
                    if (nilaijual == 0.0 || jumlah == 0.0) {
                        harga = 0.0;
                    }
                    txHARGA.setText(String.format("%.0f", harga) + "");

                    if (Komoditas.equals("Kelapa")) {
                        txsatJmlProd.setVisibility(View.GONE);
                        if (WujudProduksi.equals("Buah Kelapa")) {
                            trSatuan.setVisibility(View.VISIBLE);
                            trKonversi.setVisibility(View.VISIBLE);
                            txSatuanKonversi.setText(" KG KOPRA");
                        }
                    } else if (Komoditas.equals("Karet")) {
                        trKonversi.setVisibility(View.VISIBLE);
                        txSatuanKonversi.setText(" KG");
                        if (!WujudProduksi.equals("Kadar Karet Kering")) {
                            txSatuanKonversi.setText(" KG KADAR KARET KERING");
                        }
                    }

                    loadSatuan(Integer.parseInt(id_komoditas), WujudProduksi);
                }
                if (spinner.getId() == R.id.txSatuan) {
                    Satuan = parent.getItemAtPosition(position)
                            .toString();

                    Double konvert = 1.0;
                    try {
                        konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                    } catch (Exception e) {
                    }

                    if (Komoditas.equals("Kelapa")) {
                        if (Satuan.equals("Butir")) {
                            txSatuanKonversi.setText(" KG KOPRA");
                            trSatuan.setVisibility(View.VISIBLE);
                            txsatJmlProd.setText("Butir");
                        } else if (Satuan.equals("KG")) {
                            konvert = 1.0;
                            txSatuanKonversi.setText(" KG");
                            txsatJmlProd.setText(" KG");
                        }
                    } else if (Komoditas.equals("Karet")) {
                        trKonversi.setVisibility(View.VISIBLE);
                        txSatuanKonversi.setText(" KG");
                        if (!WujudProduksi.equals("Kadar Karet Kering")) {
                            txSatuanKonversi.setText(" KG KADAR KARET KERING");
                        }
                    }

                    sKonversi = (konvert * jumlah);
                    txKonversi.setText(String.format("%.2f", sKonversi) + "");

                    harga = nilaijual / sKonversi;
                    if (nilaijual == 0.0 || jumlah == 0.0) {
                        harga = 0.0;
                    }
                    txHARGA.setText(String.format("%.0f", harga) + "");
                }
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }

    }

    void updateData() {
        if (Komoditas.equals("Kelapa") || Komoditas.equals("Karet")) {
            jumlah = sKonversi;
        }

        if (id_obj == 1) {
            if (dbhelper.updateEstProdPetani(Long.parseLong(RowID),
                    Integer.parseInt(id_komoditas),
                    jumlah,
                    WujudProduksi,
                    Satuan,
                    nilaijual,
                    harga,
                    txKeterangan.getText().toString().trim())) {
                Toast.makeText(this, "estimasi produksi pekebun berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (id_obj == 2) {
            if (dbhelper.updateEstProdPerusahaan(Long.parseLong(RowID),
                    Integer.parseInt(id_komoditas),
                    jumlah,
                    WujudProduksi,
                    Satuan,
                    nilaijual,
                    harga,
                    txKeterangan.getText().toString().trim())) {
                Toast.makeText(this, "estimasi produksi perusahaan berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    void savedata() {
        if (Komoditas.equals("Kelapa") || Komoditas.equals("Karet")) {
            jumlah = sKonversi;
        }

        if (id_obj == 1) {
            if (dbhelper.insertEstProdPetani(idSENS,
                    Integer.parseInt(id_komoditas),
                    jumlah,
                    WujudProduksi,
                    Satuan,
                    nilaijual,
                    harga,
                    txKeterangan.getText().toString().trim())) {
                Toast.makeText(this, "estimasi produksi disimpan", Toast.LENGTH_SHORT).show();
            }
        } else if (id_obj == 2) {
            if (dbhelper.insertEstProdPerusahaan(idSENS,
                    Integer.parseInt(id_komoditas),
                    jumlah,
                    WujudProduksi,
                    Satuan,
                    nilaijual,
                    harga,
                    txKeterangan.getText().toString().trim())) {
                Toast.makeText(this, "estimasi produksi disimpan", Toast.LENGTH_SHORT).show();
            }
        }

        int count = dbhelper.count(tabelLuas + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + idSENS + " and status_est_prod = 0");
        if (count == 0) {
            Intent intent = new Intent(getApplicationContext(),
                    est_luas_areal.class);
            intent.putExtra("id_sensus", idSENS);
            startActivity(intent);
        } else {
            int id_luas = Integer.parseInt(dbhelper.instantSelect("id", tabelLuas, " id_sensus = " + idSENS + " and id_komoditas", id_komoditas + ""));
            dbhelper.updateStatus(tabelLuas, "status_est_prod", id_luas, 1);

            Intent intent = new Intent(est_produksi.this,
                    est_produksi.class);
            intent.putExtra("id_sensus", idSENS);
            intent.putExtra("id_obj_pencatatan", id_obj);
            startActivity(intent);
        }
    }

    private boolean checkIsian() {
        if (txNilaiJual.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom nilai jual masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txJmlProd.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom jumlah produksi masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        goListData();
    }

    public void goListData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(est_produksi.this);
        alert.setTitle("Perhatian");
        alert.setCancelable(false);
        alert.setMessage("Anda Ingin keluar ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (setupdate) {
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(),
                            submenu.class);
                    intent.putExtra("id_obj_pencatatan", id_obj);
                    startActivity(intent);
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