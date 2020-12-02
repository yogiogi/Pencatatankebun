package com.sensus.diginidea.pencatatankebun.listData;

/**
 * Created by Yogi on 9/23/2017.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.sensus.diginidea.pencatatankebun.MainActivity;
import com.sensus.diginidea.pencatatankebun.R;
import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapterLuasAreaPerusahaan;
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapterLuasAreaPetani;
import com.sensus.diginidea.pencatatankebun.edit.est_luas_areal;
import com.sensus.diginidea.pencatatankebun.edit.luas_areal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class list_data_est_luas_area extends Fragment {
    Cursor model = null;
    DbHelper dbhelper;
    String getIDent;
    String getIDsensus;
    String id_komoditas = "";

    private ArrayList<String> KOMODITAS_ArrayList = new ArrayList<String>();
    private ArrayList<Double> TBM_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TM_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TTM_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> JMLINTI_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TBMPLASMA_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TMPLASMA_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TTMPLASMA_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> JMLPLASMA_ArrayList = new ArrayList<Double>();
    private ArrayList<String> KETERANGAN_ArrayList = new ArrayList<String>();

    ListView LISTVIEW;
    String id_sensus, tabelEstL, tabelLuas;
    int id_obj;
    Spinner spKomoditas;
    private ArrayAdapter<String> adapKomoditas;
    SQLiteListAdapterLuasAreaPetani ListAdapterPETANI;
    SQLiteListAdapterLuasAreaPerusahaan ListAdapterPERUSAHAAN;
    private BottomNavigationView bottomNavigation;
    String Komoditas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_data_estluas_area, container, false);

        dbhelper = new DbHelper(getActivity());
        Bundle extras = getActivity().getIntent().getExtras();
        Log.d("Response", extras + "");
        if (extras != null) {
            id_sensus = getActivity().getIntent().getExtras().getString("id_sensus");
            Log.d("id_sensus", id_sensus + "");
        }

        LISTVIEW = (ListView) rootView.findViewById(R.id.listview);
        LISTVIEW.setScrollingCacheEnabled(false);
        spKomoditas = (Spinner) rootView.findViewById(R.id.txKomoditas);

        String tabelProduksi, tabelEstP;
        id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));
        if (id_obj == 1) {
            tabelEstL = "tbl_est_luas_kebun_petani";
            tabelEstP = "tbl_est_produksi_kebun_petani";
            tabelLuas = "tbl_luas_kebun_petani";
            tabelProduksi = "tbl_produksi_petani";
        } else {
            tabelEstL = "tbl_est_luas_kebun_perusahaan";
            tabelEstP = "tbl_est_produksi_kebun_perusahaan";
            tabelLuas = "tbl_luas_kebun_perusahaan";
            tabelProduksi = "tbl_produksi_perusahaan";
        }

        Log.d("tabel yaya", tabelEstL);
        loadSpinnerKomoditas(tabelEstL);

        String st_estl = dbhelper.instantSelect("status", tabelEstL, "id_sensus", id_sensus);
        int idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + id_sensus + "");
        int cEL = dbhelper.count(tabelLuas + " where id_sensus = " + id_sensus + " and status_est_luas_area = 1");

        bottomNavigation = (BottomNavigationView) rootView.findViewById(R.id.navigation);

        if (st_estl != null && !st_estl.isEmpty() && !st_estl.equals("null")) {
            if (idLuas != cEL) {
                bottomNavigation.setVisibility(View.VISIBLE);
            } else {
                bottomNavigation.setVisibility(View.GONE);
            }
        } else {
            bottomNavigation.setVisibility(View.VISIBLE);
        }

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener()

                {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tambah:
                                Intent ee = new Intent(getActivity().getApplicationContext(), est_luas_areal.class);
                                ee.putExtra("id_sensus", id_sensus);
                                startActivity(ee);
                                break;
                        }
                        return false;
                    }
                });

        return rootView;
    }

    public void alertCancel() {
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());
        alert.setTitle("Perhatian");
        alert.setMessage("Anda Ingin keluar ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getActivity().getApplicationContext(),
                        MainActivity.class);
                intent.putExtra("id_sensus", id_sensus);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private void loadSpinnerKomoditas(String tabel) {
        ArrayList<String> KOMODITASlist = new ArrayList<String>();
        KOMODITASlist.add("Semua Komoditas");

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        model = db.rawQuery("SELECT distinct b.id_komoditas, b.komoditas FROM " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas WHERE a.id_sensus = '" + id_sensus + "' ORDER BY komoditas", null);

        if (model.moveToFirst()) {
            do {
                KOMODITASlist.add(model.getString(model.getColumnIndex("komoditas")));

            } while (model.moveToNext());
        }

        adapKomoditas = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, KOMODITASlist);

        adapKomoditas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spKomoditas.setAdapter(adapKomoditas);
        spKomoditas.setWillNotDraw(false);
    }

    public void onResume() {
        ShowSQLiteDBdata();
        spKomoditas.setOnItemSelectedListener(new MyOnItemSelectedListener());
        super.onResume();
    }

    private void ShowSQLiteDBdata() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        if (Komoditas == "Semua Komoditas") {
            Komoditas = "";
        }
        model = db.rawQuery("SELECT a.* FROM " + tabelEstL + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                "WHERE a.id_sensus='" + id_sensus + "' AND a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'", null);

        KOMODITAS_ArrayList.clear();
        TBM_ArrayList.clear();
        TM_ArrayList.clear();
        TTM_ArrayList.clear();
        JMLINTI_ArrayList.clear();
        TBMPLASMA_ArrayList.clear();
        TMPLASMA_ArrayList.clear();
        TTMPLASMA_ArrayList.clear();
        JMLPLASMA_ArrayList.clear();
        KETERANGAN_ArrayList.clear();

        if (model.moveToFirst()) {
            do {
                int id_komoditas = model.getInt(model.getColumnIndex("id_komoditas"));
                String komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", id_komoditas + "");
                KOMODITAS_ArrayList.add(komoditas);

                if (id_obj == 1) {
                    TBM_ArrayList.add(model.getDouble(model.getColumnIndex("tbm")));
                    TM_ArrayList.add(model.getDouble(model.getColumnIndex("tm")));
                    TTM_ArrayList.add(model.getDouble(model.getColumnIndex("ttm")));
                    JMLINTI_ArrayList.add(model.getDouble(model.getColumnIndex("tbm")) + model.getDouble(model.getColumnIndex("tm")) + model.getDouble(model.getColumnIndex("ttm")));

                } else {
                    TBM_ArrayList.add(model.getDouble(model.getColumnIndex("tbm_inti")));
                    TM_ArrayList.add(model.getDouble(model.getColumnIndex("tm_inti")));
                    TTM_ArrayList.add(model.getDouble(model.getColumnIndex("ttm_inti")));
                    JMLINTI_ArrayList.add(model.getDouble(model.getColumnIndex("tbm_inti")) + model.getDouble(model.getColumnIndex("tm_inti")) + model.getDouble(model.getColumnIndex("ttm_inti")));
                    TBMPLASMA_ArrayList.add(model.getDouble(model.getColumnIndex("tbm_plasma")));
                    TMPLASMA_ArrayList.add(model.getDouble(model.getColumnIndex("tm_plasma")));
                    TTMPLASMA_ArrayList.add(model.getDouble(model.getColumnIndex("ttm_plasma")));
                    JMLPLASMA_ArrayList.add(model.getDouble(model.getColumnIndex("tbm_plasma")) + model.getDouble(model.getColumnIndex("tm_plasma")) + model.getDouble(model.getColumnIndex("ttm_plasma")));
                }

                String mmodel = model.getString(model.getColumnIndex(dbhelper.KEY_KET));
                if (mmodel.isEmpty()) {
                    mmodel = "-";
                }
                KETERANGAN_ArrayList.add(mmodel);

            } while (model.moveToNext());
        }

        if (id_obj == 1) {
            ListAdapterPETANI = new SQLiteListAdapterLuasAreaPetani(getActivity().getApplicationContext(),
                    KOMODITAS_ArrayList,
                    TBM_ArrayList,
                    TM_ArrayList,
                    TTM_ArrayList,
                    JMLINTI_ArrayList,
                    KETERANGAN_ArrayList
            );
            LISTVIEW.setAdapter(ListAdapterPETANI);
        } else {
            ListAdapterPERUSAHAAN = new SQLiteListAdapterLuasAreaPerusahaan(getActivity().getApplicationContext(),
                    KOMODITAS_ArrayList,
                    TBM_ArrayList,
                    TM_ArrayList,
                    TTM_ArrayList,
                    JMLINTI_ArrayList,
                    TBMPLASMA_ArrayList,
                    TMPLASMA_ArrayList,
                    TTMPLASMA_ArrayList,
                    JMLPLASMA_ArrayList,
                    KETERANGAN_ArrayList
            );
            LISTVIEW.setAdapter(ListAdapterPERUSAHAAN);
        }

        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                model = db.rawQuery("SELECT a.* FROM " + tabelEstL + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                        "WHERE a.id_sensus='" + id_sensus + "' AND a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'", null);
                Log.d("log error", "SELECT a.* FROM " + tabelEstL + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                        "WHERE a.id_sensus='" + id_sensus + "' AND a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'");

                model.moveToPosition(arg2);
                getIDent = model.getString(model.getColumnIndex("id"));
                getIDsensus = model.getString(model.getColumnIndex("id_sensus"));

                String jenisKomo = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", model.getString(model.getColumnIndex("id_komoditas")));
                final CharSequence[] dialogitem = {"Lihat Data Estimasi Luas Area " + jenisKomo};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent ee = new Intent(getActivity().getApplicationContext(), est_luas_areal.class);
                                ee.putExtra("ident", getIDent);
                                ee.putExtra("id_sensus", getIDsensus);
                                ee.putExtra("set", true);
                                startActivity(ee);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {

            Spinner spinner = (Spinner) parent;
            if (spinner.getId() == R.id.txKomoditas) {
                Komoditas = parent.getItemAtPosition(position)
                        .toString();
                if (Komoditas.equals("Semua Komoditas")) {
                    id_komoditas = "";
                } else {
                    id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", Komoditas).toString();
                }

                ShowSQLiteDBdata();
                Log.d("Response komoditas", Komoditas);
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }

}


