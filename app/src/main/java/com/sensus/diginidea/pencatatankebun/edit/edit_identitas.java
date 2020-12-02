package com.sensus.diginidea.pencatatankebun.edit;

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
import android.view.Menu;
import android.view.MenuInflater;
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
import com.sensus.diginidea.pencatatankebun.view_data;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Yogi on 9/24/2017.
 */

public class edit_identitas extends AppCompatActivity {
    TextView a1, a2, a3, a4, a5, a6, a7, a8, a9;
    EditText txnoHPmanbun, txNamaPekebun, txTelp, txKelTani, txNourut;
    TextView lbNama, txTglCatatManbun, txKodeKabupaten,
            txKodeKecamatan, txKodeDesa, tvNotif, manbun,
            txId_sensus, tvHeadSatu, txketPetugas;
    Spinner spKabupaten, spKec, spDesa, spManbun;

    TableRow manbunrow1, manbunrow11, manbunrow2, manbunrow3;
    TableRow trkeltani1, trkeltani2;
    String propinsi = "62";
    //    String kabupaten = "01";
    int id_obj;
    //    int id_ROW;
    String id_manbun = "";
    String kabupaten, namaKabupaten, kecamatan, namaKecamatan, namaManbun;
    String desa, namaDesa;
    String checkon;
    Boolean setupdate = false;

    String RowID, id_sensus, id_sensuslama = "";
    ArrayAdapter<String> adapKab;
    ArrayAdapter<String> adapKec;
    ArrayAdapter<String> adapDesa;
    ArrayAdapter<String> adapManbun;
    long selectDesa = 0;
    long selectKec = 0;
    long selectkab = 0;
    long selectManbun = 0;
    private boolean isUpdate;
    DbHelper dbhelper;
    Cursor model = null;
    int statusKirim;

    int nourut;
    private BottomNavigationView bottomNavigation;
    //    TextView notifTelp;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identitas_edit);

        dbhelper = new DbHelper(this);
        txNamaPekebun = (EditText) findViewById(R.id.txNamaPekebun);
        txNamaPekebun.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        txKelTani = (EditText) findViewById(R.id.txKelompokTani);
        txKelTani.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        txketPetugas = (TextView) findViewById(R.id.ketPetugas);
        txKodeKabupaten = (TextView) findViewById(R.id.kode_kabupaten);
        txKodeKecamatan = (TextView) findViewById(R.id.kode_kecamatan);
        txKodeDesa = (TextView) findViewById(R.id.kode_desa);
        tvNotif = (TextView) findViewById(R.id.tvNotif);
        lbNama = (TextView) findViewById(R.id.lbNama);

        txTelp = (EditText) findViewById(R.id.txTelp);
        txNourut = (EditText) findViewById(R.id.txNourut);

        spKabupaten = (Spinner) findViewById(R.id.txKabupaten);
        spKec = (Spinner) findViewById(R.id.txkec);
        spDesa = (Spinner) findViewById(R.id.txDesa);
        spManbun = (Spinner) findViewById(R.id.txNamaManbun);

        tvHeadSatu = (TextView) findViewById(R.id.tvHeadSatu);
        txnoHPmanbun = (EditText) findViewById(R.id.txnoHPmanbun);
        txTglCatatManbun = (TextView) findViewById(R.id.txTglCatatManbun);
        txId_sensus = (TextView) findViewById(R.id.id_sensus);
        manbun = (TextView) findViewById(R.id.manbun);

        a2 = (TextView) findViewById(R.id.a2);
        a3 = (TextView) findViewById(R.id.a3);
        a4 = (TextView) findViewById(R.id.a4);
        a5 = (TextView) findViewById(R.id.a5);
        a6 = (TextView) findViewById(R.id.a6);
        a7 = (TextView) findViewById(R.id.a7);
        a8 = (TextView) findViewById(R.id.a8);

        manbunrow1 = (TableRow) findViewById(R.id.manbunrow1);
        manbunrow11 = (TableRow) findViewById(R.id.manbunrow11);
        manbunrow2 = (TableRow) findViewById(R.id.manbunrow2);
        manbunrow3 = (TableRow) findViewById(R.id.manbunrow3);
        trkeltani1 = (TableRow) findViewById(R.id.trkeltani1);
        trkeltani2 = (TableRow) findViewById(R.id.trkeltani2);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.navigation);

        loadSpinnerKab(propinsi);

        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        checkon = dfs.format(currentTime.getTime());

        txTglCatatManbun.setText(checkon);

        Bundle extras = getIntent().getExtras();
        Log.d("Response", extras + "");
        if (extras != null) {
            setupdate = getIntent().getExtras().getBoolean("set");
            RowID = getIntent().getExtras().getString("ident");
            id_obj = getIntent().getExtras().getInt("id_obj_pencatatan");
            id_sensus = getIntent().getExtras().getString("id_sensus");
            id_sensuslama = id_sensus;
            Log.d("id_sensus", id_sensus + "");
            Log.d("RowID", RowID + "");
            Log.d("id_objs", id_obj + "");
            txId_sensus.setText(id_sensus);

            if (setupdate) {
                Log.d("isupdate", "");
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                model = db.rawQuery("SELECT * FROM tbl_identitas where id_sensus =" + id_sensus + " and id = " + RowID + "", null);
                model.moveToPosition(0);

                statusKirim = Integer.parseInt(model.getString(model.getColumnIndex("status")));
                if (statusKirim == 1) {
                    bottomNavigation.setVisibility(View.GONE);
                }
                id_obj = Integer.parseInt(model.getString(model.getColumnIndex("id_obj_sensus")));
                Log.d("nama", model.getString(model.getColumnIndex("nama")));
                txNamaPekebun.setText(model.getString(model.getColumnIndex("nama")));
                Log.d("telp", model.getString(model.getColumnIndex("telp")));
                txTelp.setText(model.getString(model.getColumnIndex("telp")));
                Log.d("keltani", model.getString(model.getColumnIndex("kelompok_tani")));
                txKelTani.setText(model.getString(model.getColumnIndex("kelompok_tani")));
                nourut = model.getInt(model.getColumnIndex("no_urut"));
                txNourut.setText(nourut + "");

                Log.d("kecamatan", model.getString(model.getColumnIndex("kode_kecamatan")));
                kecamatan = model.getString(model.getColumnIndex("kode_kecamatan"));
                Log.d("kabupaten", model.getString(model.getColumnIndex("kode_kabupaten")));
                kabupaten = model.getString(model.getColumnIndex("kode_kabupaten"));

                namaKabupaten = dbhelper.instantSelect("nama_kab_kot", "mst_kabkot", "kode_kabupaten", propinsi + kabupaten);
                Log.d("nama kab", namaKabupaten);
                namaKecamatan = dbhelper.instantSelect("nama_kecamatan", "mst_kecamatan", "kode_kecamatan", propinsi + kabupaten + kecamatan);
                Log.d("nama kec", namaKecamatan);

                txKodeKabupaten.setText("[" + kabupaten + "]");
                txKodeKecamatan.setText("[" + kecamatan + "]");
                desa = model.getString(model.getColumnIndex("kode_desa"));
                namaDesa = dbhelper.instantSelect("nama_desa", "mst_desa", "kode_desa", propinsi + kabupaten + kecamatan + desa);
                Log.d("nama desa", namaDesa);
                txKodeDesa.setText("[" + desa + "]");

                if (id_obj != 2) {
                    id_manbun = model.getString(model.getColumnIndex("id_manbun"));
                    namaManbun = dbhelper.instantSelect("nama", "mst_manbun", "id_manbun", id_manbun);
                    txnoHPmanbun.setText(dbhelper.instantSelect("telp", "mst_manbun", "id_manbun", id_manbun));
                    Log.d("namaManbun", namaManbun);
                }

                txId_sensus.setText(id_sensus);
                Log.d("kabupaten", adapKab.getCount() + "");

                for (int i = 0; i < adapKab.getCount(); i++) {
                    if (namaKabupaten.equals(adapKab.getItem(i).toString())) {
                        spKabupaten.setSelection(i);
                        loadSpinnerKec(adapKab.getItem(i).toString());
                        Log.d("kab ", adapKab.getItem(i).toString());
                        break;
                    }
                }

                for (int i = 0; i < adapKec.getCount(); i++) {
                    if (namaKecamatan.equals(adapKec.getItem(i).toString())) {
                        spKec.setSelection(i);
                        loadSpinnerDesa(adapKec.getItem(i).toString());
                        loadSpinnerManbun(dbhelper.instantSelect("kode_kecamatan", "mst_kecamatan", "nama_kecamatan", adapKec.getItem(i).toString()));
                        Log.d("kece ", adapKec.getItem(i).toString());
                        break;
                    }
                }

                for (int i = 0; i < adapDesa.getCount(); i++) {
                    if (namaDesa.equals(adapDesa.getItem(i).toString())) {
                        Log.d("get", adapDesa.getItem(i).toString());
                        Log.d("get => ", i + "");
                        spDesa.setSelection(i);
                        break;
                    }
                }

                if (id_obj != 2) {
                    for (int i = 0; i < adapManbun.getCount(); i++) {
                        if (namaManbun.equals(adapManbun.getItem(i).toString())) {
                            Log.d("get", adapManbun.getItem(i).toString());
                            Log.d("get => ", i + "");
                            spManbun.setSelection(i);
                            break;
                        }
                    }
                }
            }
        }

        if (id_obj == 1) {
            tvHeadSatu.setText("I. KETERANGAN PEKEBUN");
            lbNama.setText("NAMA PEKEBUN : ");
            txketPetugas.setVisibility(View.VISIBLE);
            trkeltani1.setVisibility(View.VISIBLE);
            trkeltani2.setVisibility(View.VISIBLE);
            manbun.setVisibility(View.VISIBLE);
            manbunrow1.setVisibility(View.VISIBLE);
            manbunrow2.setVisibility(View.VISIBLE);
            manbunrow3.setVisibility(View.VISIBLE);
            txKelTani.setVisibility(View.VISIBLE);
            spManbun.setVisibility(View.VISIBLE);
            a4.setText("4.");
            a5.setText("5.");
            a6.setText("6.");
            a7.setText("7.");
            a8.setText("8.");
            setTitle("IDENTITAS PEKEBUN");
        } else {
            tvHeadSatu.setText("I. KETERANGAN PERUSAHAAN");
            txketPetugas.setVisibility(View.GONE);
            trkeltani1.setVisibility(View.GONE);
            trkeltani2.setVisibility(View.GONE);
            lbNama.setText("NAMA PERUSAHAAN : ");
            manbun.setVisibility(View.GONE);
            manbunrow1.setVisibility(View.GONE);
            manbunrow2.setVisibility(View.GONE);
            manbunrow3.setVisibility(View.GONE);
            txKelTani.setVisibility(View.GONE);
            spManbun.setVisibility(View.GONE);
            a4.setText("3.");
            a5.setText("4.");
            a6.setText("5.");
            a7.setText("6.");
            a8.setText("7.");
            setTitle("IDENTITAS PERUSAHAAN");
        }

//        txTelp.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                String input = s.toString();
//                if(input.contains(".") && s.charAt(s.length()-1) != '.'){
//                    if(input.indexOf(".") + 3 <= input.length()-1){
//                        String formatted = input.substring(0, input.indexOf(".") + 3);
//                        txTelp.setText(formatted);
//                        txTelp.setSelection(formatted.length());
//                    }
//                }else if(input.contains(",") && s.charAt(s.length()-1) != ','){
//                    if(input.indexOf(",") + 3 <= input.length()-1){
//                        String formatted = input.substring(0, input.indexOf(",") + 3);
//                        txTelp.setText(formatted);
//                        txTelp.setSelection(formatted.length());
//                    }
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        spDesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        txNourut.addTextChangedListener(new TextWatcher() {
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
                String Prop = "62";

                if (!s.toString().isEmpty() && s.toString() != null) {
                    nourut = Integer.parseInt(s.toString());
                    txId_sensus.setText(Prop + kabupaten + kecamatan + desa + String.format("%04d", Integer.parseInt(s.toString())));
                    int i = dbhelper.count("tbl_identitas where id_sensus = '" + Prop + kabupaten + kecamatan + desa + String.format("%04d", Integer.parseInt(s.toString())) + "'");
                    if (i != 0) {
                        tvNotif.setVisibility(View.VISIBLE);
                    } else {
                        tvNotif.setVisibility(View.GONE);
                    }
                } else {
                    txId_sensus.setText(Prop + kabupaten + kecamatan + desa + String.format("%04d", 0));
                    tvNotif.setVisibility(View.GONE);
                }
            }
        });

//        txId_sensus.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String Prop = "62";
//
//                if (!s.toString().isEmpty() && s.toString() != null) {
//                    txId_sensus.setText(s.toString());
//                    int i = dbhelper.count("tbl_identitas where id_sensus = '" + s.toString() + "'");
//                    if (i != 0) {
//                        tvNotif.setVisibility(View.VISIBLE);
//                    } else {
//                        tvNotif.setVisibility(View.GONE);
//                    }
//                } else {
//                    txId_sensus.setText(s.toString());
//                    tvNotif.setVisibility(View.GONE);
//                }
//            }
//        });

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener()

                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                if (cekIsian()) {
                                    if (setupdate) {
                                        updateData();
                                        Intent intent = new Intent(getApplicationContext(),
                                                view_data.class);
                                        intent.putExtra("id_obj_pencatatan", id_obj);
                                        startActivity(intent);
                                    } else {
                                        savedata();
                                        Intent intent = new Intent(getApplicationContext(),
                                                luas_areal.class);
                                        intent.putExtra("id_sensus", id_sensus);
                                        startActivity(intent);
                                    }
                                    item.setEnabled(false);
                                }
                                break;
                        }
                        return false;
                    }
                });
    }

    private void loadSpinnerDesa(String kodeKec) {
        // here i used Set Because Set doesn't allow duplicates.
        Set<String> set = dbhelper.getAllDesa(kodeKec);

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapDesa = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapDesa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDesa.setAdapter(adapDesa);
        spDesa.setWillNotDraw(false);
    }

    private void loadSpinnerManbun(String kode_kec) {
        Set<String> set = dbhelper.getAll("id_manbun, nama", "mst_manbun where kode_kecamatan = '" + kode_kec + "'", "nama");

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapManbun = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapManbun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spManbun.setAdapter(adapManbun);
        spManbun.setWillNotDraw(false);
    }

    private void loadSpinnerKec(String kodeKab) {
        Set<String> set = dbhelper.getAllKec(kodeKab);

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapKec = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapKec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKec.setAdapter(adapKec);
        spKec.setWillNotDraw(false);
    }

    private void loadSpinnerKab(String kodeProv) {
        Set<String> set = dbhelper.getAllKab(kodeProv);

        List<String> list = new ArrayList<String>(set);
        Collections.sort(list);

        adapKab = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        adapKab.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKabupaten.setAdapter(adapKab);
        spKabupaten.setWillNotDraw(false);
    }


    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            Log.d("updateSp", isUpdate + "");
            if (setupdate) {
                check = check + 1;
                if (check > 4) {
                    Spinner spinner = (Spinner) parent;
                    if (spinner.getId() == R.id.txNamaManbun) {
                        String ManBun = parent.getItemAtPosition(position)
                                .toString();
                        selectManbun = parent.getItemIdAtPosition(position);
                        txnoHPmanbun.setText(dbhelper.instantSelect("telp", "mst_manbun", "nama", ManBun).toString());

                        if (ManBun == "test") {
                            id_manbun = "8888";
                        } else {
                            id_manbun = dbhelper.instantSelect("id_manbun", "mst_manbun", "nama", ManBun).toString();
                        }

//                Log.d("Create selectManbun", selectManbun + "");
                        Log.d("Response manbun", ManBun);
                    } else if (spinner.getId() == R.id.txDesa) {
                        String DesaName = parent.getItemAtPosition(position)
                                .toString();
                        selectDesa = parent.getItemIdAtPosition(position);
                        Log.d("Create selectDesa", selectDesa + "");
                        Log.d("Response desa", DesaName);

                        desa = dbhelper.instantSelect("kode_desa2", "mst_desa", "nama_desa", DesaName);
                        txKodeDesa.setText("[" + desa + "]");
                        txId_sensus.setText(propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut));

                        int i = dbhelper.count("tbl_identitas where id_sensus = '" + propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut) + "'");
                        if (i != 0) {
                            tvNotif.setVisibility(View.VISIBLE);
                        } else {
                            tvNotif.setVisibility(View.GONE);
                        }
                    } else if (spinner.getId() == R.id.txkec) {
                        String KecName = parent.getItemAtPosition(position)
                                .toString();
                        selectKec = parent.getItemIdAtPosition(position);
                        Log.d("Create selectKec", selectKec + "");
                        Log.d("Response kec", KecName);

                        kecamatan = dbhelper.instantSelect("kode_kecamatan2", "mst_kecamatan", "nama_kecamatan", KecName);
                        txKodeKecamatan.setText("[" + kecamatan + "]");
                        loadSpinnerManbun(dbhelper.instantSelect("kode_kecamatan", "mst_kecamatan", "nama_kecamatan", KecName));
                        loadSpinnerDesa(KecName);

                        txId_sensus.setText(propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut));

                        int i = dbhelper.count("tbl_identitas where id_sensus = '" + propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut) + "'");
                        if (i != 0) {
                            tvNotif.setVisibility(View.VISIBLE);
                        } else {
                            tvNotif.setVisibility(View.GONE);
                        }
                    } else if (spinner.getId() == R.id.txKabupaten) {
                        String kabName = parent.getItemAtPosition(position)
                                .toString();
                        selectkab = parent.getItemIdAtPosition(position);
                        Log.d("Create selectkab", selectkab + "");
                        Log.d("Response kab", kabName);

                        kabupaten = dbhelper.instantSelect("kode_kabupaten2", "mst_kabkot", "nama_kab_kot", kabName);
                        txKodeKabupaten.setText("[" + kabupaten + "]");
                        loadSpinnerKec(kabName);
                        txId_sensus.setText(propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut));

                        int i = dbhelper.count("tbl_identitas where id_sensus = '" + propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut) + "'");
                        if (i != 0) {
                            tvNotif.setVisibility(View.VISIBLE);
                        } else {
                            tvNotif.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                Spinner spinner = (Spinner) parent;
                if (spinner.getId() == R.id.txDesa) {
                    String DesaName = parent.getItemAtPosition(position)
                            .toString();
                    selectDesa = parent.getItemIdAtPosition(position);
                    Log.d("Create selectDesa", selectDesa + "");
                    Log.d("Response desa", DesaName);

                    desa = dbhelper.instantSelect("kode_desa2", "mst_desa", "nama_desa", DesaName);
                    txKodeDesa.setText("[" + desa + "]");
                    txId_sensus.setText(propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut));

                    int i = dbhelper.count("tbl_identitas where id_sensus = '" + propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut) + "'");
                    if (i != 0) {
                        tvNotif.setVisibility(View.VISIBLE);
                    } else {
                        tvNotif.setVisibility(View.GONE);
                    }
                } else if (spinner.getId() == R.id.txkec) {
                    String KecName = parent.getItemAtPosition(position)
                            .toString();
                    selectKec = parent.getItemIdAtPosition(position);
                    Log.d("Create selectKec", selectKec + "");
                    Log.d("Response kec", KecName);

                    kecamatan = dbhelper.instantSelect("kode_kecamatan2", "mst_kecamatan", "nama_kecamatan", KecName);
                    txKodeKecamatan.setText("[" + kecamatan + "]");
                    loadSpinnerManbun(dbhelper.instantSelect("kode_kecamatan", "mst_kecamatan", "nama_kecamatan", KecName));
                    loadSpinnerDesa(KecName);
                    txId_sensus.setText(propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut));

                    int i = dbhelper.count("tbl_identitas where id_sensus = '" + propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut) + "'");
                    if (i != 0) {
                        tvNotif.setVisibility(View.VISIBLE);
                    } else {
                        tvNotif.setVisibility(View.GONE);
                    }
                } else if (spinner.getId() == R.id.txKabupaten) {
                    String kabName = parent.getItemAtPosition(position)
                            .toString();
                    selectkab = parent.getItemIdAtPosition(position);
                    Log.d("Create selectkab", selectkab + "");
                    Log.d("Response kab", kabName);

                    kabupaten = dbhelper.instantSelect("kode_kabupaten2", "mst_kabkot", "nama_kab_kot", kabName);
                    txKodeKabupaten.setText("[" + kabupaten + "]");
                    loadSpinnerKec(kabName);
                    txId_sensus.setText(propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut));

                    int i = dbhelper.count("tbl_identitas where id_sensus = '" + propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut) + "'");
                    if (i != 0) {
                        tvNotif.setVisibility(View.VISIBLE);
                    } else {
                        tvNotif.setVisibility(View.GONE);
                    }
                } else if (spinner.getId() == R.id.txNamaManbun) {
                    String ManBun = parent.getItemAtPosition(position)
                            .toString();
                    selectManbun = parent.getItemIdAtPosition(position);
                    txnoHPmanbun.setText(dbhelper.instantSelect("telp", "mst_manbun", "nama", ManBun).toString());

                    if (ManBun == "test") {
                        id_manbun = "8888";
                    } else {
                        id_manbun = dbhelper.instantSelect("id_manbun", "mst_manbun", "nama", ManBun).toString();
                    }

//                Log.d("Create selectManbun", selectManbun + "");
                    Log.d("Response manbun", ManBun);
                }

                int i = dbhelper.count("tbl_identitas where id_sensus = '" + propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut) + "'");
                if (i != 0) {
                    tvNotif.setVisibility(View.VISIBLE);
                } else {
                    tvNotif.setVisibility(View.GONE);
                }
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }

    }

    void savedata() {
        id_sensus = propinsi + "" + kabupaten + kecamatan + desa + String.format("%04d", nourut);
        Log.d("id_senssu", id_sensus);

        Log.d("String ", id_sensus + "," + id_obj + "," + txNamaPekebun.getText().toString().trim() + "," + txTelp.getText().toString().trim() + "," +
                txKelTani.getText().toString().trim() + "," + propinsi + "," + kabupaten + "," + kecamatan + "," +
                desa + "," + String.format("%04d", nourut) + "," + id_manbun + "," + txTglCatatManbun.getText().toString());

        if (dbhelper.insertIdentitas(id_sensus, id_obj, txNamaPekebun.getText().toString().trim(), txTelp.getText().toString().trim(),
                txKelTani.getText().toString().trim(), propinsi, kabupaten, kecamatan,
                desa, String.format("%04d", nourut),
                id_manbun, txTglCatatManbun.getText().toString())) {
            if (id_obj == 1) {
                dbhelper.updateManbun(id_manbun, txnoHPmanbun.getText().toString());
            }
            Toast.makeText(this, "Identitas Pekebun disimpan", Toast.LENGTH_SHORT).show();
        }
    }

    void updateData() {
        if (id_obj == 2) {
            id_manbun = "-";
            txTglCatatManbun.setText("-");
        }

        kabupaten = txKodeKabupaten.getText().toString().replaceAll("\\[", "").replaceAll("\\]", "");
        kecamatan = txKodeKecamatan.getText().toString().replaceAll("\\[", "").replaceAll("\\]", "");
        desa = txKodeDesa.getText().toString().replaceAll("\\[", "").replaceAll("\\]", "");
        nourut = Integer.parseInt(txNourut.getText().toString());

        id_sensus = propinsi + "" + kabupaten + kecamatan + desa + String.format("%04d", nourut);
        if (dbhelper.updateIdentitas(RowID, id_sensus,
                id_obj,
                txNamaPekebun.getText().toString(),
                txTelp.getText().toString(),
                txKelTani.getText().toString(), propinsi, kabupaten, kecamatan, desa,
                String.format("%04d", nourut), id_manbun, txTglCatatManbun.getText().toString())) {

            if (id_obj == 1) {
                dbhelper.updateRealisasiKebunID(id_sensus, id_sensuslama);
                dbhelper.updateLuasKPetanID(id_sensus, id_sensuslama);
                dbhelper.updateProduksiPetaniID(id_sensus, id_sensuslama);
                dbhelper.updateEstLuasKebunPetaniID(id_sensus, id_sensuslama);
                dbhelper.updateEstProduksiPetaniID(id_sensus, id_sensuslama);
            } else if (id_obj == 2) {
                dbhelper.updateLuasKPerusahaanID(id_sensus, id_sensuslama);
                dbhelper.updateRealisasiPerusahaanID(id_sensus, id_sensuslama);
                dbhelper.updateProduksiPerusahaanID(id_sensus, id_sensuslama);
                dbhelper.updateEstLuasKebunPerusahaanID(id_sensus, id_sensuslama);
                dbhelper.updateEstProduksiPerusahaanID(id_sensus, id_sensuslama);
            }

            dbhelper.updateManbun(id_manbun, txnoHPmanbun.getText().toString());
            Toast.makeText(this, "Data identitas berhasil di update", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.simpan:
                if (cekIsian()) {
                    if (setupdate) {
                        updateData();
                        Intent intent = new Intent(getApplicationContext(),
                                view_data.class);
                        intent.putExtra("id_obj_pencatatan", id_obj);
                        startActivity(intent);
                    } else {
                        savedata();
                        Intent intent = new Intent(getApplicationContext(),
                                luas_areal.class);
                        intent.putExtra("id_sensus", id_sensus);
                        startActivity(intent);
                    }
                }
                return true;
            case R.id.cancel:
                alertCancel();
                return true;
        }
        return false;
    }

    private boolean cekIsian() {
        if (txNamaPekebun.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom nama pekebun masih kosong",
                    Toast.LENGTH_SHORT).show();

            return false;
        }
        if (txTelp.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom no telepon masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (txKelTani.getVisibility() != View.GONE) {
            if (txKelTani.getText().toString().trim().length() == 0) {
                Toast.makeText(getBaseContext(),
                        "Kolom nama kelompok tani masih kosong",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (id_obj == 1) {
            if (id_manbun == "") {
                Toast.makeText(getBaseContext(),
                        "Kolom manbun masih kosong",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        if (txNourut.getText().toString().trim().length() == 0) {
            Toast.makeText(getBaseContext(),
                    "Kolom no urut masih kosong",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tvNotif.getVisibility() == View.VISIBLE) {
            Toast.makeText(getBaseContext(),
                    "no urut yang dimasukkan sudah ada",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void onResume() {
        super.onResume();

        spKabupaten.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spKec.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spDesa.setOnItemSelectedListener(new MyOnItemSelectedListener());
        if (id_obj != 2) {
            spManbun.setOnItemSelectedListener(new MyOnItemSelectedListener());
        }
        txId_sensus.setText(propinsi + kabupaten + kecamatan + desa + String.format("%04d", nourut));
    }

    @Override
    public void onBackPressed() {
        alertCancel();
    }

    public void alertCancel() {
        AlertDialog.Builder alert = new AlertDialog.Builder(edit_identitas.this);
        alert.setTitle("Perhatian");
        alert.setMessage("Anda Ingin keluar ?");
        alert.setCancelable(false);
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
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