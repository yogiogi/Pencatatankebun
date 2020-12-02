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

import com.sensus.diginidea.pencatatankebun.R;
import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.real_menu_list;
import com.sensus.diginidea.pencatatankebun.submenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class real_produk_perbulan extends AppCompatActivity {
    TextView id_sensus, txJml_luas, lbJenisKomoditas, txKonversi;
    TextView txsatHARGA, txSatjmlProd, txBulan, txHARGA, txSatKonversi;
    EditText txJmlProd, txNilaiJual, txKeterangan; //inti

    Double jumlahProd = 0.0, nilaiJual = 0.0, Harga = 0.0;
    TableRow trKonversi, trSatuan;

    String idSENS;
    int id_obj;
    Spinner spWjdProduksi, spSatuan;
    DbHelper dbhelper;
    ArrayAdapter<String> adapWjdProduksi;
    ArrayAdapter<String> adapSatuan;

    long selectWujudProd = 0;
    long selectSatuan = 0;
    int check = 0;

    String tabelreal, tabel;
    String jenisKomoditas;
    int id_komoditas, posisi = 1;
    private BottomNavigationView bottomNavigation;
    String WujudProduksi;
    String Satuan = "KG";

    int cKmoditas;
    Boolean setupdate = false;
    Cursor model = null;
    String RowID;
    int statusKirim;
    Double hasilkonversi = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_produk_perbulan);

        id_sensus = (TextView) findViewById(R.id.id_sensus);
        txsatHARGA = (TextView) findViewById(R.id.txsatHARGA);
        lbJenisKomoditas = (TextView) findViewById(R.id.txJenisKomoditas);
        txJml_luas = (TextView) findViewById(R.id.txJml_luas);
        txSatKonversi = (TextView) findViewById(R.id.txSatuanKonversi);
        txSatjmlProd = (TextView) findViewById(R.id.txsatJmlProd);
        txJmlProd = (EditText) findViewById(R.id.txJmlProd);
        txNilaiJual = (EditText) findViewById(R.id.txNilaiJual);
        txHARGA = (TextView) findViewById(R.id.txHarga);
        txKeterangan = (EditText) findViewById(R.id.txKeterangan); //inti
        txKeterangan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        spWjdProduksi = (Spinner) findViewById(R.id.txWjdProduksi);
        spSatuan = (Spinner) findViewById(R.id.txSatuan);
        txBulan = (TextView) findViewById(R.id.txBulan);
        txKonversi = (TextView) findViewById(R.id.txKonversi);

        trKonversi = (TableRow) findViewById(R.id.trKonversi);
        trSatuan = (TableRow) findViewById(R.id.trSatuan);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        dbhelper = new DbHelper(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            Log.d("Response", extras + "");

            setupdate = getIntent().getExtras().getBoolean("set");
            Log.i("setupdate", setupdate + "");

            idSENS = extras.getString("id_sensus");
            Log.d("id_sensus", idSENS);

            jenisKomoditas = extras.getString("komoditas");
            Log.d("komoditas", jenisKomoditas + "");

            WujudProduksi = extras.getString("wujud_produksi");
            Log.d("wujud_produksi", WujudProduksi + "");

            posisi = extras.getInt("posisi");
            Log.d("posisi", posisi + "");

            RowID = extras.getString("ident");
            Log.d("rowid", RowID + "");

            id_sensus.setText(idSENS);
            lbJenisKomoditas.setText(jenisKomoditas);
            id_komoditas = Integer.parseInt(dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", jenisKomoditas));
            id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", idSENS));

            if (id_obj == 1) {
                tabelreal = "tbl_realisasi_petani";
            } else {
                tabelreal = "tbl_realisasi_perusahaan";
            }

            loadWujudProduksi(id_komoditas);

            if (setupdate) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();

                model = db.rawQuery("SELECT * FROM " + tabelreal + " where id_sensus =" + idSENS + " and id = " + RowID + "", null);
                Log.d("query", "SELECT * FROM " + tabelreal + " where id_sensus =" + idSENS + " and id = " + RowID + "");
                model.moveToPosition(0);

                statusKirim = Integer.parseInt(model.getString(model.getColumnIndex("status")));
                if (statusKirim == 1) {
                    bottomNavigation.setVisibility(View.GONE);
                }
                posisi = model.getInt(model.getColumnIndex("bulan"));
                String bul = dbhelper.instantSelect("bulan", "mst_bulan", "id", posisi + "");
                Log.d("bulan", bul);
                txBulan.setText(bul);

                Harga = model.getDouble(model.getColumnIndex("harga"));
                jumlahProd = model.getDouble(model.getColumnIndex("jumlah_prod"));
                Satuan = model.getString(model.getColumnIndex("satuan"));
                WujudProduksi = model.getString(model.getColumnIndex("wujud_produksi"));

                if (jenisKomoditas.equals("Kelapa") || jenisKomoditas.equals("Karet")) {
                    txKonversi.setText(String.format("%.2f", jumlahProd).toString().replaceAll("[,.]", "."));
                    hasilkonversi = jumlahProd;

                    Double konvert = 1.0;
                    try {
                        konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                    } catch (Exception e) {
                    }

                    if (jenisKomoditas.equals("Kelapa")) {
                        if (Satuan.equals("KG")) {
                            konvert = 1.0;
                        }
                    }

                    jumlahProd = (hasilkonversi / konvert);
                    Harga = nilaiJual / hasilkonversi;
                }

                txHARGA.setText(String.format("%.0f", Harga).replaceAll("[,.]", "."));
                txJmlProd.setText(String.format("%.2f", jumlahProd).toString().replaceAll("[,.]", "."));

                nilaiJual = model.getDouble(model.getColumnIndex("nilai_jual"));
                txNilaiJual.setText(String.format("%.0f", nilaiJual).toString().replaceAll("[,.]", "."));

                Harga = model.getDouble(model.getColumnIndex("harga"));
                txHARGA.setText(String.format("%.0f", Harga));
                txHARGA.setText(String.format("%.0f", Harga).toString().replaceAll("[,.]", "."));

                txKeterangan.setText(model.getString(model.getColumnIndex("keterangan")));
            }
        }

        if (!WujudProduksi.isEmpty()) {
            for (int i = 0; i < adapWjdProduksi.getCount(); i++) {
                if (WujudProduksi.equals(adapWjdProduksi.getItem(i).toString())) {
                    spWjdProduksi.setSelection(i);
                    Log.d("jenis tanaman ", adapWjdProduksi.getItem(i).toString());
                    break;
                }
            }
        }

        if (jenisKomoditas.equals("Kelapa") || jenisKomoditas.equals("Karet")) {
            loadSatuan(id_komoditas, WujudProduksi);
            Log.d("buah kelapa",WujudProduksi);
            Log.d("buah kelapa",Satuan);
            if (!Satuan.isEmpty()) {
                for (int i = 0; i < adapSatuan.getCount(); i++) {
                    if (Satuan.equals(adapSatuan.getItem(i).toString())) {
                        spSatuan.setSelection(i);
                        Log.d("Satuan ", adapSatuan.getItem(i).toString());
                        break;
                    }
                }
            }
        }

        if (id_obj == 1) {
            tabel = "tbl_luas_kebun_petani";
            txsatHARGA.setText("PER KG");
            txSatjmlProd.setText("KG");
        } else if (id_obj == 2) {
            tabel = "tbl_luas_kebun_perusahaan";
            txsatHARGA.setText("PER TON");
            txSatjmlProd.setText("TON");
        }

        txJml_luas.setText(dbhelper.count(tabel + " WHERE id_sensus = '" + id_sensus.getText().toString() + "'") + "");
        txJmlProd.addTextChangedListener(new TextWatcher() {
            private String current = "";

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
                    jumlahProd = Double.valueOf(cleanString);
                } else {
                    jumlahProd = 0.0;
                }

                Double konvert = 1.0;
                try {
                    konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                } catch (Exception e) {
                }

                if (jenisKomoditas.equals("Kelapa")) {
                    if (Satuan.equals("KG")) {
                        konvert = 1.0;
                    }
                }

                hasilkonversi = (konvert * jumlahProd);
                txKonversi.setText(String.format("%.2f", hasilkonversi) + "");

                Harga = nilaiJual / hasilkonversi;
                if (nilaiJual == 0.0 || jumlahProd == 0.0) {
                    Harga = 0.0;
                }
                txHARGA.setText(String.format("%.0f", Harga) + "");
            }
        });

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
                    nilaiJual = Double.valueOf(cleanString);
                } else {
                    nilaiJual = 0.0;
                }

                Double konvert = 1.0;
                try {
                    konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                } catch (Exception e) {
                }

                if (jenisKomoditas.equals("Kelapa")) {
                    if (Satuan.equals("KG")) {
                        konvert = 1.0;
                    }
                }

                hasilkonversi = (konvert * jumlahProd);
                txKonversi.setText(String.format("%.2f", hasilkonversi) + "");

                Harga = nilaiJual / hasilkonversi;

                if (nilaiJual == 0.0 || jumlahProd == 0.0) {
                    Harga = 0.0;
                }
                txHARGA.setText(String.format("%.0f", Harga) + "");
            }
        });

        if (!setupdate) {
            if (posisi <= 9) {
                txBulan.setText(dbhelper.instantSelect("bulan", "mst_bulan", "id", posisi + ""));
            }

            int adabulan = dbhelper.count(tabelreal + " where bulan = '" + posisi + "' and id_sensus = '" + idSENS + "' and id_komoditas = '" + id_komoditas + "'");
            if (adabulan != 0) {
                Toast.makeText(real_produk_perbulan.this, "Bulan " + txBulan.getText().toString() + " sudah di input", Toast.LENGTH_SHORT).show();

                Intent i = null;
                if (posisi <= 9) {
                    i = new Intent(real_produk_perbulan.this, real_produk_perbulan.class);
                } else {
                    i = new Intent(real_produk_perbulan.this, real_menu_list.class);
                }
                i.putExtra("id_sensus", idSENS);
                i.putExtra("komoditas", jenisKomoditas);
                i.putExtra("posisi", posisi + 1);
                i.putExtra("wujud_produksi", WujudProduksi);
                startActivity(i);
            }
        }

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener()

                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Bt_menu_lanjut:
                                if (checkIsian()) {
                                    if (setupdate) {
                                        updateData();
                                    } else {
                                        savedata();

                                        Intent intent = null;
                                        if (posisi < 9) {
                                            intent = new Intent(real_produk_perbulan.this,
                                                    real_produk_perbulan.class);
                                            intent.putExtra("id_sensus", idSENS);
                                            intent.putExtra("komoditas", jenisKomoditas);
                                            intent.putExtra("posisi", posisi + 1);
                                            intent.putExtra("wujud_produksi", WujudProduksi);
                                        } else {
                                            int id_luas = Integer.parseInt(dbhelper.instantSelect("id", tabel, " id_sensus = " + idSENS + " and id_komoditas", id_komoditas + ""));
                                            dbhelper.updateStatus(tabel, "status_realisasi", id_luas, 1);
                                            intent = new Intent(real_produk_perbulan.this,
                                                    real_menu_list.class);
                                            intent.putExtra("id_sensus", idSENS);
                                            intent.putExtra("lanjut", "lanjut");
                                        }
                                        startActivity(intent);
                                    }
                                }
//                                item.setEnabled(false);
                                break;
                        }
                        return false;
                    }
                });
//        }
    }

    private void loadWujudProduksi(int id) {
        Set<String> set = dbhelper.getAll("a.id_komoditas, b.wujud_produksi ", "mst_komoditas a inner join mst_wujud_produksi b on a.id_komoditas = b.id_komoditas where a.id_komoditas = '" + id + "'", "b.wujud_produksi");
//        select a.id_komoditas, b.wujud_produksi from mst_komoditas a inner join mst_wujud_produksi b on a.id_komoditas = b.id_komoditas where a.id_komoditas = 1

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapWjdProduksi = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapWjdProduksi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spWjdProduksi.setAdapter(adapWjdProduksi);
        spWjdProduksi.setWillNotDraw(false);
    }

    private void loadSatuan(int id, String wujud_produksi) {
        Log.d("wujud", wujud_produksi);
        Log.d("id", id + "");

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


    public void onResume() {
        super.onResume();
        spWjdProduksi.setOnItemSelectedListener(new MyOnItemSelectedListener());
        if (jenisKomoditas.equals("Kelapa") || jenisKomoditas.equals("Karet")) {
            spSatuan.setOnItemSelectedListener(new MyOnItemSelectedListener());
        }
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (setupdate) {
                check = check + 1;
                Spinner spinner = (Spinner) parent;
                if (spinner.getId() == R.id.txWjdProduksi) {
                    WujudProduksi = parent.getItemAtPosition(position)
                            .toString();
                    Log.d("wujud", WujudProduksi);

                    Double konvert = 1.0;
                    try {
                        konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                    } catch (Exception e) {
                    }

                    hasilkonversi = (konvert * jumlahProd);
                    txKonversi.setText(String.format("%.2f", hasilkonversi) + "");
                    Harga = nilaiJual / hasilkonversi;
                    if (nilaiJual == 0.0 || jumlahProd == 0.0) {
                        Harga = 0.0;
                    }
                    txHARGA.setText(String.format("%.0f", Harga) + "");

                    if (jenisKomoditas.equals("Kelapa")) {
                        txSatjmlProd.setVisibility(View.GONE);
                        trSatuan.setVisibility(View.VISIBLE);
                        if (WujudProduksi.equals("Buah Kelapa")) {
                            trKonversi.setVisibility(View.VISIBLE);
                            txSatKonversi.setText(" KG KOPRA");
                        }
                    } else if (jenisKomoditas.equals("Karet")) {
                        txSatjmlProd.setVisibility(View.GONE);
                        trKonversi.setVisibility(View.VISIBLE);
                        txSatKonversi.setText(" KG");
                        if (!WujudProduksi.equals("Kadar Karet Kering")) {
                            txSatKonversi.setText(" KG KADAR KARET KERING");
                        }
                    }

                    String id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString();
                    loadSatuan(Integer.parseInt(id_komoditas), WujudProduksi);

                    if (!Satuan.isEmpty()) {
                        for (int i = 0; i < adapSatuan.getCount(); i++) {
                            if (Satuan.equals(adapSatuan.getItem(i).toString())) {
                                spSatuan.setSelection(i);
                                Log.d("Satuan satan", adapSatuan.getItem(i).toString());
                                break;
                            }
                        }
                    }
                }

                if (spinner.getId() == R.id.txSatuan) {
                    Satuan = parent.getItemAtPosition(position)
                            .toString();

                    Double konvert = 1.0;
                    try {
                        konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                    } catch (Exception e) {
                    }

                    if (jenisKomoditas.equals("Kelapa")) {
                        txSatjmlProd.setVisibility(View.GONE);
                        trSatuan.setVisibility(View.VISIBLE);
                        if (Satuan.equals("Butir")) {
                            txSatKonversi.setText(" KG KOPRA");
                            txSatjmlProd.setText("Butir");
                        } else if (Satuan.equals("KG")) {
                            konvert = 1.0;
                            txSatKonversi.setText(" KG");
                        }
                    } else if (jenisKomoditas.equals("Karet")) {
                        trKonversi.setVisibility(View.VISIBLE);
                        txSatKonversi.setText(" KG");
                        if (!WujudProduksi.equals("Kadar Karet Kering")) {
                            txSatKonversi.setText(" KG KADAR KARET KERING");
                        }
                    }

                    hasilkonversi = (konvert * jumlahProd);
                    txKonversi.setText(String.format("%.2f", hasilkonversi) + "");

                    Harga = nilaiJual / hasilkonversi;
                    if (nilaiJual == 0.0 || jumlahProd == 0.0) {
                        Harga = 0.0;
                    }
                    txHARGA.setText(String.format("%.0f", Harga) + "");
                }
            } else {
                Spinner spinner = (Spinner) parent;
                if (spinner.getId() == R.id.txWjdProduksi) {
                    WujudProduksi = parent.getItemAtPosition(position)
                            .toString();
                    String id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString();

                    Double konvert = 1.0;
                    try {
                        konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                    } catch (Exception e) {
                    }

                    hasilkonversi = (konvert * jumlahProd);
                    txKonversi.setText(String.format("%.2f", hasilkonversi) + "");
                    Harga = nilaiJual / hasilkonversi;
                    if (nilaiJual == 0.0 || jumlahProd == 0.0) {
                        Harga = 0.0;
                    }
                    txHARGA.setText(String.format("%.0f", Harga) + "");

                    if (jenisKomoditas.equals("Kelapa")) {
                        txSatjmlProd.setVisibility(View.GONE);
                        trSatuan.setVisibility(View.VISIBLE);
                        if (WujudProduksi.equals("Buah Kelapa")) {
                            trKonversi.setVisibility(View.VISIBLE);
                            txSatKonversi.setText(" KG KOPRA");
                        }
                    } else if (jenisKomoditas.equals("Karet")) {
                        trKonversi.setVisibility(View.VISIBLE);
                        txSatKonversi.setText(" KG");
                        if (!WujudProduksi.equals("Kadar Karet Kering")) {
                            txSatKonversi.setText(" KG KADAR KARET KERING");
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

                    if (jenisKomoditas.equals("Kelapa")) {
                        txSatjmlProd.setVisibility(View.GONE);
                        trSatuan.setVisibility(View.VISIBLE);
                        if (Satuan.equals("Butir")) {
                            txSatKonversi.setText(" KG KOPRA");
                            txSatjmlProd.setText("Butir");
                        } else if (Satuan.equals("KG")) {
                            konvert = 1.0;
                            txSatKonversi.setText(" KG");
                        }
                    } else if (jenisKomoditas.equals("Karet")) {
                        trKonversi.setVisibility(View.VISIBLE);
                        txSatKonversi.setText(" KG");
                        if (!WujudProduksi.equals("Kadar Karet Kering")) {
                            txSatKonversi.setText(" KG KADAR KARET KERING");
                        }
                    }

                    hasilkonversi = (konvert * jumlahProd);
                    txKonversi.setText(String.format("%.2f", hasilkonversi) + "");

                    Harga = nilaiJual / hasilkonversi;
                    if (nilaiJual == 0.0 || jumlahProd == 0.0) {
                        Harga = 0.0;
                    }
                    txHARGA.setText(String.format("%.0f", Harga) + "");
                }
            }

        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }

    }

    void savedata() {
        if (jenisKomoditas.equals("Kelapa") || jenisKomoditas.equals("Karet")) {
            jumlahProd = hasilkonversi;
        }

        if (id_obj == 1) {
            Log.d("query",
                    idSENS + "," +
                            id_komoditas + "," +
                            posisi + "," +
                            jumlahProd + "," +
                            WujudProduksi + "," +
                            nilaiJual + "," +
                            Harga + "," +
                            txKeterangan.getText().toString().trim());

            if (dbhelper.insertRealisasiPetani(idSENS,
                    id_komoditas,
                    posisi,
                    jumlahProd,
                    WujudProduksi,
                    Satuan,
                    nilaiJual,
                    Harga,
                    txKeterangan.getText().toString().trim())) {
                Toast.makeText(this, "realisasi produksi disimpan", Toast.LENGTH_SHORT).show();
            }
        } else if (id_obj == 2) {
            if (dbhelper.insertRealisasiPerusahaan(idSENS,
                    id_komoditas,
                    posisi,
                    jumlahProd,
                    WujudProduksi,
                    Satuan,
                    nilaiJual,
                    Harga,
                    txKeterangan.getText().toString().trim())) {
                Toast.makeText(this, "realisasi produksi disimpan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void updateData() {
        if (jenisKomoditas.equals("Kelapa") || jenisKomoditas.equals("Karet")) {
            jumlahProd = hasilkonversi;
        }

        if (id_obj == 1) {
            Log.d("query",
                    idSENS + "," +
                            id_komoditas + "," +
                            posisi + "," +
                            jumlahProd + "," +
                            WujudProduksi + "," +
                            nilaiJual + "," +
                            Harga + "," +
                            txKeterangan.getText().toString().trim());

            if (dbhelper.updateRealisasiPetani(Long.parseLong(RowID),
                    id_komoditas,
                    posisi,
                    jumlahProd,
                    WujudProduksi,
                    Satuan,
                    nilaiJual,
                    Harga,
                    txKeterangan.getText().toString().trim())) {
                Toast.makeText(this, "realisasi produksi pekebun berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (id_obj == 2) {
            if (dbhelper.updateRealisasiPerusahaan(Long.parseLong(RowID),
                    id_komoditas,
                    posisi,
                    jumlahProd,
                    WujudProduksi,
                    Satuan,
                    nilaiJual,
                    Harga,
                    txKeterangan.getText().toString().trim())) {
                Toast.makeText(this, "realisasi produksi pekebun berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean checkIsian() {
        if (txJmlProd.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom jumlah produksi masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txNilaiJual.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom nilai jual masih kosong",
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
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