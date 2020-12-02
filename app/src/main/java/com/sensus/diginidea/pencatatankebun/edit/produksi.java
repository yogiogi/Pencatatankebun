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
//import android.support.design.widget.BottomNavigationView;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.sensus.diginidea.pencatatankebun.DecimalDigitsInputFilter;
import com.sensus.diginidea.pencatatankebun.InputFilterMinMax;
import com.sensus.diginidea.pencatatankebun.R;
import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.prod_menu_list;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class produksi extends AppCompatActivity {
    TextView id_sensus, txKonversi;
    TextView jmlProd, txSatuanDijual, txSatuanKonsumsi, txSatuanDisimpan, txsatJmlProd;
    TextView txTgl, txBulan, txTahun;
    TextView JenisKomoditas, txSatuanKonversi;
    TableRow trKonversi, trSatuan;

    EditText etDijual, etDisimpan, etKonsumsi, etketerangan;
    Double sjual = 0.0, ssimpan = 0.0, skonsumsi = 0.0, sjumlah = 0.0, skonversi = 0.0;
    Button btSimpan;

    String tabelLuas;
    String idSens;
    int id_obj;
    Spinner spWujudProduksi, spSatuan;
    String tabelProd;

    DbHelper dbhelper;
    ArrayAdapter<String> adapWujudProduksi;
    ArrayAdapter<String> adapSatuan;

    String id_komoditas;
    long selectWujud = 0;
    long selectSatuan = 0;

    String id_wujudProduksi;
    String id_satuan;
    String WujudProduksi;
    String Satuan = "KG";

    String TANGGAL;

    protected Cursor cursor;
    String komoditas;
    private BottomNavigationView bottomNavigation;
    Boolean setupdate = false;
    String tambah;
    Cursor model = null;
    String RowID;
    int check = 0;
    int statusKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.produksi);

        id_sensus = (TextView) findViewById(R.id.id_sensus);

        txTgl = (TextView) findViewById(R.id.txDates);
        txBulan = (TextView) findViewById(R.id.txMonth);
        txTahun = (TextView) findViewById(R.id.txYear);
        txSatuanKonversi = (TextView) findViewById(R.id.txSatuanKonversi);

        Integer tgl = Calendar.getInstance().get(Calendar.DATE);
        Integer mth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        Integer year = Calendar.getInstance().get(Calendar.YEAR);

        txBulan.setText(mth + "");
        txTahun.setText(year + "");

        txSatuanDijual = (TextView) findViewById(R.id.txsatDIJUAL);
        txSatuanKonsumsi = (TextView) findViewById(R.id.txsatKONSUMSI);
        txSatuanDisimpan = (TextView) findViewById(R.id.txsatDISIMPAN);
        txsatJmlProd = (TextView) findViewById(R.id.txsatJmlProd);

        jmlProd = (TextView) findViewById(R.id.txJmlProd);
        etDijual = (EditText) findViewById(R.id.txDijual);
        etDisimpan = (EditText) findViewById(R.id.txDisimpan);
        etKonsumsi = (EditText) findViewById(R.id.txKonsumsi);
        etketerangan = (EditText) findViewById(R.id.txKeterangan);
        etketerangan.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        JenisKomoditas = (TextView) findViewById(R.id.txKomoditas);
        spWujudProduksi = (Spinner) findViewById(R.id.txWjdProduksi);
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
            tambah = getIntent().getExtras().getString("tambah");

            idSens = extras.getString("id_sensus");
            id_komoditas = extras.getString("komoditas");
            Log.d("id_sensus", idSens + "");
            RowID = getIntent().getExtras().getString("ident");

            id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", idSens));
            if (id_obj == 1) {
                setTitle("PRODUKSI PEKEBUN");
                tabelProd = "tbl_produksi_petani";
                tabelLuas = "tbl_luas_kebun_petani";
                txSatuanDijual.setText("KG");
                txSatuanKonsumsi.setText("KG");
                txSatuanDisimpan.setText("KG");
                txsatJmlProd.setText("KG");
            } else if (id_obj == 2) {
                setTitle("PRODUKSI PBS/PBN");
                tabelProd = "tbl_produksi_perusahaan";
                tabelLuas = "tbl_luas_kebun_perusahaan";
                txSatuanDijual.setText("TON");
                txSatuanKonsumsi.setText("TON");
                txSatuanDisimpan.setText("TON");
                txsatJmlProd.setText("TON");
            }
            loadSpinnerWujudProduksi(id_komoditas);

            if (setupdate) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                model = db.rawQuery("SELECT * FROM " + tabelProd + " where id_sensus =" + idSens + " and id = " + RowID + "", null);
                Log.d("produksi", "SELECT * FROM " + tabelProd + " where id_sensus =" + idSens + " and id = " + RowID + "");

                model.moveToPosition(0);

                statusKirim = Integer.parseInt(model.getString(model.getColumnIndex("status")));
                if (statusKirim == 1) {
                    bottomNavigation.setVisibility(View.GONE);
                }

                TANGGAL = model.getString(model.getColumnIndex("tgl_pendataan"));
                String arr[] = TANGGAL.split("-");
                txTahun.setText(arr[0]);
                txBulan.setText(arr[1]);
                txTgl.setText(arr[2]);
                WujudProduksi = model.getString(model.getColumnIndex("wujud_produksi"));

                id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", id_komoditas);

                sjual = model.getDouble(model.getColumnIndex("dijual"));
                etDijual.setText(String.format("%.2f", sjual).toString().replaceAll("[,.]", "."));

                Satuan = model.getString(model.getColumnIndex("satuan"));
                ssimpan = model.getDouble(model.getColumnIndex("disimpan"));
                etDisimpan.setText(String.format("%.2f", ssimpan).toString().replaceAll("[,.]", "."));

                skonsumsi = model.getDouble(model.getColumnIndex("konsumsi"));
                etKonsumsi.setText(String.format("%.2f", skonsumsi).toString().replaceAll("[,.]", "."));

                sjumlah = model.getDouble(model.getColumnIndex("jumlah_prod"));
                if (komoditas.equals("Kelapa") || komoditas.equals("Karet")) {
                    txKonversi.setText(String.format("%.2f", sjumlah).toString().replaceAll("[,.]", "."));
                    skonversi = sjumlah;

                    Double konvert = 1.0;
                    try {
                        konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                    } catch (Exception e) {
                    }

                    if (JenisKomoditas.equals("Kelapa")) {
                        if (Satuan.equals("KG")) {
                            konvert = 1.0;
                        }
                    }

                    sjumlah = (skonversi / konvert);
                }
                jmlProd.setText(String.format("%.2f", sjumlah).toString().replaceAll("[,.]", "."));
                etketerangan.setText(model.getString(model.getColumnIndex("keterangan")));

                for (int i = 0; i < adapWujudProduksi.getCount(); i++) {
                    if (WujudProduksi.equals(adapWujudProduksi.getItem(i).toString())) {
                        spWujudProduksi.setSelection(i);
                        Log.d("jenis tanaman ", adapWujudProduksi.getItem(i).toString());
                        break;
                    }
                }

                if (komoditas.equals("Kelapa") || komoditas.equals("Karet")) {
                    loadSatuan(Integer.parseInt(id_komoditas), WujudProduksi);

                    for (int i = 0; i < adapSatuan.getCount(); i++) {
                        if (Satuan.equals(adapSatuan.getItem(i).toString())) {
                            spSatuan.setSelection(i);
                            Log.d("jenis tanaman ", adapSatuan.getItem(i).toString());
                            break;
                        }
                    }
                }
            }
        }

        komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", id_komoditas);
        JenisKomoditas.setText(komoditas);

        if (komoditas.equals("Kelapa")) {
            trKonversi.setVisibility(View.VISIBLE);
            trSatuan.setVisibility(View.VISIBLE);
        } else if (komoditas.equals("Karet")) {
            trKonversi.setVisibility(View.VISIBLE);
            trSatuan.setVisibility(View.VISIBLE);
        } else {
            trKonversi.setVisibility(View.GONE);
            trSatuan.setVisibility(View.GONE);
        }

        id_sensus.setText(idSens);
        id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", idSens));

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

        etDijual.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        etDijual.setText(formatted);
                        etDijual.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        etDijual.setText(formatted);
                        etDijual.setSelection(formatted.length());
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
                    sjual = Double.valueOf(cleanString);
                } else {
                    sjual = 0.0;
                }

                Double konvert = 1.0;
                try {
                    konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                } catch (Exception e) {
                }

                if (komoditas.equals("Kelapa")) {
                    if (Satuan.equals("KG")) {
                        konvert = 1.0;
                    }
                }

                sjumlah = sjual + ssimpan + skonsumsi;
                jmlProd.setText(String.format("%.2f", sjumlah) + "");

                skonversi = (konvert * sjumlah);
                txKonversi.setText(String.format("%.2f", skonversi) + "");
            }
        });

        etKonsumsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        etKonsumsi.setText(formatted);
                        etKonsumsi.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        etKonsumsi.setText(formatted);
                        etKonsumsi.setSelection(formatted.length());
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
                    skonsumsi = Double.valueOf(cleanString);
                } else {
                    skonsumsi = 0.0;
                }

                Double konvert = 1.0;
                try {
                    konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                } catch (Exception e) {
                }

                if (komoditas.equals("Kelapa")) {
                    if (Satuan.equals("KG")) {
                        konvert = 1.0;
                    }
                }

                sjumlah = sjual + ssimpan + skonsumsi;
                jmlProd.setText(String.format("%.2f", sjumlah) + "");

                skonversi = (konvert * sjumlah);
                txKonversi.setText(String.format("%.2f", skonversi) + "");
            }
        });

        etDisimpan.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String input = s.toString();
                if (input.contains(".") && s.charAt(s.length() - 1) != '.') {
                    if (input.indexOf(".") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(".") + 3);
                        etDisimpan.setText(formatted);
                        etDisimpan.setSelection(formatted.length());
                    }
                } else if (input.contains(",") && s.charAt(s.length() - 1) != ',') {
                    if (input.indexOf(",") + 3 <= input.length() - 1) {
                        String formatted = input.substring(0, input.indexOf(",") + 3);
                        etDisimpan.setText(formatted);
                        etDisimpan.setSelection(formatted.length());
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
                    ssimpan = Double.valueOf(cleanString);
                } else {
                    ssimpan = 0.0;
                }

                Double konvert = 1.0;
                try {
                    konvert = Double.parseDouble(dbhelper.instantSelect("konversi", "mst_wujud_produksi", "wujud_produksi", WujudProduksi).toString());
                } catch (Exception e) {
                }

                if (komoditas.equals("Kelapa")) {
                    if (Satuan.equals("KG")) {
                        konvert = 1.0;
                    }
                }

                sjumlah = sjual + ssimpan + skonsumsi;
                jmlProd.setText(String.format("%.2f", sjumlah) + "");

                skonversi = (konvert * sjumlah);
                txKonversi.setText(String.format("%.2f", skonversi) + "");
            }
        });
        txTgl.setFilters(new InputFilter[]{new InputFilterMinMax("1", "31")});
        txBulan.setFilters(new InputFilter[]{new InputFilterMinMax("1", "12")});
    }

    private void loadSatuan(int id, String wujud_produksi) {
//        Set<String> set = dbhelper.getAll("id, satuan", "mst_wujud_produksi where id_komoditas  = " + id + " AND  wujud_produksi = '" + wujud_produksi + "'", "satuan");
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
        spWujudProduksi.setOnItemSelectedListener(new MyOnItemSelectedListener());
        if (komoditas.equals("Kelapa") || komoditas.equals("Karet")) {
            spSatuan.setOnItemSelectedListener(new MyOnItemSelectedListener());
        }
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            if (setupdate) {
                check = check + 1;
//                if (check > 1) {
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

                    skonversi = (konvert * sjumlah);
                    txKonversi.setText(String.format("%.2f", skonversi) + "");

                    if (komoditas.equals("Kelapa")) {
                        txsatJmlProd.setVisibility(View.GONE);
                        if (WujudProduksi.equals("Buah Kelapa")) {
                            trSatuan.setVisibility(View.VISIBLE);
                            trKonversi.setVisibility(View.VISIBLE);
                            txSatuanKonversi.setText(" KG KOPRA");
                        }
                    } else if (komoditas.equals("Karet")) {
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

                    if (komoditas.equals("Kelapa")) {
                        txsatJmlProd.setVisibility(View.GONE);
                        if (Satuan.equals("Butir")) {
                            txSatuanKonversi.setText(" KG KOPRA");
                            trSatuan.setVisibility(View.VISIBLE);
                            txsatJmlProd.setText("Butir");
                            txSatuanDijual.setText("Butir");
                            txSatuanDisimpan.setText("Butir");
                            txSatuanKonsumsi.setText("Butir");
                        } else if (Satuan.equals("KG")) {
                            konvert = 1.0;
                            txSatuanKonversi.setText(" KG");
                            txsatJmlProd.setText(" KG");
                            txSatuanDijual.setText(" KG");
                            txSatuanDisimpan.setText(" KG");
                            txSatuanKonsumsi.setText(" KG");
                        }
                    } else if (komoditas.equals("Karet")) {
                        trKonversi.setVisibility(View.VISIBLE);
                        txSatuanKonversi.setText(" KG");
                        if (!WujudProduksi.equals("Kadar Karet Kering")) {
                            txSatuanKonversi.setText(" KG KADAR KARET KERING");
                        }
                    }


                    skonversi = (konvert * sjumlah);
                    txKonversi.setText(String.format("%.2f", skonversi) + "");
                }
//                }
            } else {
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

                    skonversi = (konvert * sjumlah);
                    txKonversi.setText(String.format("%.2f", skonversi) + "");

                    if (komoditas.equals("Kelapa")) {
                        txsatJmlProd.setVisibility(View.GONE);
                        if (WujudProduksi.equals("Buah Kelapa")) {
                            trSatuan.setVisibility(View.VISIBLE);
                            trKonversi.setVisibility(View.VISIBLE);
                            txSatuanKonversi.setText(" KG KOPRA");
                        }
                    } else if (komoditas.equals("Karet")) {
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

                    if (komoditas.equals("Kelapa")) {
                        txsatJmlProd.setVisibility(View.GONE);
                        if (Satuan.equals("Butir")) {
                            txSatuanKonversi.setText(" KG KOPRA");
                            trSatuan.setVisibility(View.VISIBLE);
                            txsatJmlProd.setText("Butir");
                            txSatuanDijual.setText("Butir");
                            txSatuanDisimpan.setText("Butir");
                            txSatuanKonsumsi.setText("Butir");
                        } else if (Satuan.equals("KG")) {
                            konvert = 1.0;
                            txSatuanKonversi.setText(" KG");
                            txsatJmlProd.setText(" KG");
                            txSatuanDijual.setText(" KG");
                            txSatuanDisimpan.setText(" KG");
                            txSatuanKonsumsi.setText(" KG");
                        }
                    } else if (komoditas.equals("Karet")) {
                        trKonversi.setVisibility(View.VISIBLE);
                        txSatuanKonversi.setText(" KG");
                        if (!WujudProduksi.equals("Kadar Karet Kering")) {
                            txSatuanKonversi.setText(" KG KADAR KARET KERING");
                        }
                    }


                    skonversi = (konvert * sjumlah);
                    txKonversi.setText(String.format("%.2f", skonversi) + "");
                }
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }

    }

    void updateData() {
        if (komoditas.equals("Kelapa") || komoditas.equals("Karet")) {
            sjumlah = skonversi;
        }

        String tanggal = txTgl.getText().toString();
        String bulan = txBulan.getText().toString();
        String tahun = txTahun.getText().toString();
        String keterangan = etketerangan.getText().toString();
        if (keterangan.isEmpty()) {
            keterangan = "";
        }

        if (!tanggal.equals("") && !bulan.equals("") && !tahun.equals("")) {
            TANGGAL = tahun + "-" + bulan + "-" + tanggal;
        } else {
            TANGGAL = "0000-00-00";
        }

        Log.d("query", " " + idSens + " " +
                Integer.parseInt(id_komoditas) + " " +
                sjumlah + " " +
                WujudProduksi + " " +
                Satuan + " " +
                TANGGAL + " " +
                sjual + " " +
                ssimpan + " " +
                skonsumsi + " " +
                etketerangan.getText().toString().trim());

        if (id_obj == 1) {
            if (dbhelper.updateProduksiPetani(Long.parseLong(RowID),
                    Integer.parseInt(id_komoditas),
                    sjumlah,
                    WujudProduksi,
                    Satuan,
                    TANGGAL,
                    sjual,
                    ssimpan,
                    skonsumsi,
                    etketerangan.getText().toString().trim())) {
                Toast.makeText(produksi.this, "Data produksi berhasil di update",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (id_obj == 2) {
            if (dbhelper.updateProduksiPerusahaan(Long.parseLong(RowID),
                    Integer.parseInt(id_komoditas),
                    sjumlah,
                    WujudProduksi,
                    Satuan,
                    TANGGAL,
                    sjual,
                    ssimpan,
                    skonsumsi,
                    etketerangan.getText().toString().trim())) {
                Toast.makeText(this, "Data produksi  berhasil di update", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    void savedata() {
        if (komoditas.equals("Kelapa") || komoditas.equals("Karet")) {
            sjumlah = skonversi;
        }

        String tanggal = txTgl.getText().toString();
        String bulan = txBulan.getText().toString();
        String tahun = txTahun.getText().toString();
        String keterangan = etketerangan.getText().toString();
        if (keterangan.isEmpty()) {
            keterangan = "";
        }

        if (!tanggal.equals("") && !bulan.equals("") && !tahun.equals("")) {
            TANGGAL = tahun + "-" + bulan + "-" + tanggal;
        } else {
            TANGGAL = "0000-00-00";
        }

        Log.d("query", " " + idSens + " " +
                Integer.parseInt(id_komoditas) + " " +
                sjumlah + " " +
                WujudProduksi + " " +
                Satuan + " " +
                TANGGAL + " " +
                sjual + " " +
                ssimpan + " " +
                skonsumsi + " " +
                etketerangan.getText().toString().trim());

        if (id_obj == 1) {
            if (dbhelper.insertProduksiPetani(idSens,
                    Integer.parseInt(id_komoditas),
                    sjumlah,
                    WujudProduksi,
                    Satuan,
                    TANGGAL,
                    sjual,
                    ssimpan,
                    skonsumsi,
                    etketerangan.getText().toString().trim())) {
                Toast.makeText(produksi.this, "Data produksi tersimpan",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (id_obj == 2) {
            if (dbhelper.insertProduksiPerusahaan(idSens,
                    Integer.parseInt(id_komoditas),
                    sjumlah,
                    WujudProduksi,
                    Satuan,
                    TANGGAL,
                    sjual,
                    ssimpan,
                    skonsumsi,
                    etketerangan.getText().toString().trim())) {
                Toast.makeText(this, "Data produksi tersimpan", Toast.LENGTH_SHORT).show();
            }
        }
        AlertsaveData();
    }

    @Override
    public void onBackPressed() {
        goListData();
    }

    public void goListData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(produksi.this);
        alert.setTitle("Perhatian");
        alert.setCancelable(false);
        alert.setMessage("Anda Ingin keluar ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (setupdate) {
                    finish();
                } else {
                    Intent intent;
                    intent = new Intent(produksi.this,
                            prod_menu_list.class);
                    intent.putExtra("id_obj_pencatatan", id_obj);
                    intent.putExtra("id_sensus", idSens);
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

    public void AlertsaveData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(produksi.this);
        alert.setTitle("PERHATIAN");
        alert.setCancelable(false);
        alert.setMessage("TAMBAHKAN DENGAN TANGGAL PENDATAAN YANG LAIN");
        alert.setPositiveButton("TAMBAHKAN", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(produksi.this,
                        produksi.class);
                intent.putExtra("id_sensus", idSens);
                intent.putExtra("id_obj_pencatatan", id_obj);
                intent.putExtra("komoditas", id_komoditas);
                startActivity(intent);
            }
        });

        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (tambah != "tambah") {
                    Intent intent = new Intent(getApplicationContext(),
                            prod_menu_list.class);
                    intent.putExtra("id_obj_pencatatan", id_obj);
                    intent.putExtra("id_sensus", idSens);
                    startActivity(intent);
                } else {
                    finish();
                }
                int id_luas = Integer.parseInt(dbhelper.instantSelect("id", tabelLuas, " id_sensus = " + idSens + " and id_komoditas", id_komoditas + ""));
                dbhelper.updateStatus(tabelLuas, "status_prod", id_luas, 1);

            }
        });
        alert.show();
    }

    private boolean checkIsian() {
        if (txTgl.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom tanggal masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (jmlProd.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom jumlah produksi masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etDijual.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom dijual masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etDisimpan.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom disimpan masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etKonsumsi.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom konsumsi masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}