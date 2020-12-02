package com.sensus.diginidea.pencatatankebun.listData;

/**
 * Created by Yogi Prasetyo 19/10/2017.
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
import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.R;
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapterLuasAreaPerusahaan;
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapterLuasAreaPetani;
import com.sensus.diginidea.pencatatankebun.edit.luas_areal;
import com.sensus.diginidea.pencatatankebun.submenu;
import com.sensus.diginidea.pencatatankebun.view_data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class list_data_luasan extends Fragment implements list_data.OnDataPassedListener {
    Cursor model = null;
    DbHelper dbhelper;
    SQLiteListAdapterLuasAreaPetani ListAdapterPETANI;
    SQLiteListAdapterLuasAreaPerusahaan ListAdapterPERUSAHAAN;
    String getIDent;
    String getIDsensus;
    String getIDkomoditas;
    String tabelReal = "", tabelProd = "", tabelEstP = "", tabelEstL = "";
    String Komoditas;

    private ArrayList<String> IDsensus_ArrayList = new ArrayList<String>();
    private ArrayList<String> KOMODITAS_ArrayList = new ArrayList<String>();
    private ArrayList<Double> TBM_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TM_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TTM_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> JumlahInti_ArrayList = new ArrayList<Double>();

    private ArrayList<Double> TBMplasma_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TMplasma_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> TTMplasma_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> JumlahPlasma_ArrayList = new ArrayList<Double>();
    private ArrayList<String> KETERANGAN_ArrayList = new ArrayList<String>();

    ListView LISTVIEW;
    String id_sensus, tabel;
    String id_komoditas = "";
    int id_obj;
    Spinner spKomoditas;
    private ArrayAdapter<String> adapKomoditas;
    private BottomNavigationView bottomNavigation;
    String nama_komoditas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_data_luas_area, container, false);

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

        id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));
        if (id_obj == 1) {
            tabel = "tbl_luas_kebun_petani";
            tabelReal = "tbl_realisasi_petani";
            tabelProd = "tbl_produksi_petani";
            tabelEstP = "tbl_est_produksi_kebun_petani";
            tabelEstL = "tbl_est_luas_kebun_petani";
        } else {
            tabel = "tbl_luas_kebun_perusahaan";
            tabelReal = "tbl_realisasi_perusahaan";
            tabelProd = "tbl_produksi_perusahaan";
            tabelEstP = "tbl_est_produksi_kebun_perusahaan";
            tabelEstL = "tbl_est_luas_kebun_perusahaan";
        }

        String tabelIde = "tbl_identitas";
        Log.d("tabel ", tabel);

        loadSpinnerKomoditas(tabel);

        String st_ide = dbhelper.instantSelect("status", tabelIde, "id_sensus", id_sensus);
        String st_luas = dbhelper.instantSelect("status", tabel, "id_sensus", id_sensus);
        String st_real = dbhelper.instantSelect("status", tabelReal, "id_sensus", id_sensus);

        bottomNavigation = (BottomNavigationView) rootView.findViewById(R.id.navigation);
        if (st_luas != null && !st_luas.isEmpty() && !st_luas.equals("null")) {
            if (Integer.parseInt(st_luas) == 1 && Integer.parseInt(st_real) == 1) {
                bottomNavigation.setVisibility(View.GONE);
            } else {
                bottomNavigation.setVisibility(View.VISIBLE);
            }
        } else {
            bottomNavigation.setVisibility(View.VISIBLE);
        }

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.tambah:
                                Intent ee = new Intent(getActivity().getApplicationContext(), luas_areal.class);
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

    @Override
    public void onDataPassed(String text) {
        Log.d("text", text);
    }

    private void loadSpinnerKomoditas(String tabel) {
        ArrayList<String> KOMODITASlist = new ArrayList<String>();
        KOMODITASlist.add("Semua Komoditas");

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        model = db.rawQuery("SELECT a.id_komoditas, b.komoditas FROM " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where a.id_sensus = " + id_sensus + " ORDER BY b.komoditas", null);

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
        if (id_obj == 1) {
            ShowSQLiteDBdataPetani();
        } else {
            ShowSQLiteDBdataPerusahaan();
        }
        spKomoditas.setOnItemSelectedListener(new MyOnItemSelectedListener());
        super.onResume();
    }

    private void ShowSQLiteDBdataPetani() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        if(Komoditas =="Semua Komoditas")
        {
            Komoditas="";
        }

        model = db.rawQuery("SELECT a.* FROM " + tabel + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                "WHERE a.id_sensus='" + id_sensus + "' and a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'", null);
        Log.d("query luasan", "SELECT a.* FROM " + tabel + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                "WHERE a.id_sensus='" + id_sensus + "' and a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'");
//        Log.d("ShowSQLiteDBdata query,", "SELECT * FROM " + tabelReal + " where id_sensus ='" + id_sensus + "' and id_komoditas like '%" + id_komoditas + "%' and bulan like '%" + id_bulan + "%'");

        IDsensus_ArrayList.clear();
        KOMODITAS_ArrayList.clear();
        TBM_ArrayList.clear();
        TM_ArrayList.clear();
        TTM_ArrayList.clear();
        JumlahInti_ArrayList.clear();

        TBMplasma_ArrayList.clear();
        TMplasma_ArrayList.clear();
        TTMplasma_ArrayList.clear();
        JumlahPlasma_ArrayList.clear();
        KETERANGAN_ArrayList.clear();

        if (model.moveToFirst()) {
            do {
                int id_komoditas = model.getInt(model.getColumnIndex(dbhelper.KEY_IDKOMODITAS));
                String komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", id_komoditas + "");
                KOMODITAS_ArrayList.add(komoditas);

                TBM_ArrayList.add(model.getDouble(model.getColumnIndex("tbm")));
                TM_ArrayList.add(model.getDouble(model.getColumnIndex("tm")));
                TTM_ArrayList.add(model.getDouble(model.getColumnIndex("ttm")));
                JumlahInti_ArrayList.add(model.getDouble(model.getColumnIndex("tbm")) + model.getDouble(model.getColumnIndex("tm")) + model.getDouble(model.getColumnIndex("ttm")));

                String mmodel = model.getString(model.getColumnIndex("keterangan"));
                if (mmodel.isEmpty()) {
                    mmodel = "-";
                }
                KETERANGAN_ArrayList.add(mmodel);

            } while (model.moveToNext());
        }

        ListAdapterPETANI = new SQLiteListAdapterLuasAreaPetani(getActivity().getApplicationContext(),
                KOMODITAS_ArrayList,
                TBM_ArrayList,
                TM_ArrayList,
                TTM_ArrayList,
                JumlahInti_ArrayList,
                KETERANGAN_ArrayList
        );

        LISTVIEW.setAdapter(ListAdapterPETANI);
        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                model = db.rawQuery("SELECT a.* FROM " + tabel + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                        "WHERE a.id_sensus='" + id_sensus + "' and a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'", null);
                Log.d("query luasan", "SELECT a.* FROM " + tabel + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                        "WHERE a.id_sensus='" + id_sensus + "' and a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'");

                model.moveToPosition(arg2);
                getIDent = model.getString(model.getColumnIndex("id"));
                getIDsensus = model.getString(model.getColumnIndex("id_sensus"));
                getIDkomoditas = model.getString(model.getColumnIndex("id_komoditas"));
                String jenisKomo = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", model.getString(model.getColumnIndex("id_komoditas")));
                final CharSequence[] dialogitem = {"Lihat data luasan " + jenisKomo, "Hapus Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent ee = new Intent(getActivity().getApplicationContext(), luas_areal.class);
                                ee.putExtra("ident", getIDent);
                                Log.d("ident", getIDent);
                                ee.putExtra("id_sensus", getIDsensus);
                                ee.putExtra("set", true);
                                startActivity(ee);
                                break;
                            case 1:
                                SQLiteDatabase db = dbhelper.getWritableDatabase();
                                db.execSQL("delete from " + tabel + " where id_komoditas = '" + getIDkomoditas + "' and id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelReal + " where id_komoditas = '" + getIDkomoditas + "' and id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelProd + " where id_komoditas = '" + getIDkomoditas + "' and id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelEstP + " where id_komoditas = '" + getIDkomoditas + "' and id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelEstL + " where id_komoditas = '" + getIDkomoditas + "' and id_sensus = '" + getIDsensus + "'");

                                Intent intent = new Intent(getActivity().getApplicationContext(), view_data.class);
                                intent.putExtra("id_obj_pencatatan", id_obj);
                                startActivity(intent);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    private void ShowSQLiteDBdataPerusahaan() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        model = db.rawQuery("SELECT * FROM " + tabel + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komoditas + "%'", null);
        Log.d("query luasan", "SELECT * FROM " + tabel + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komoditas + "%'");

        IDsensus_ArrayList.clear();
        KOMODITAS_ArrayList.clear();
        TBM_ArrayList.clear();
        TM_ArrayList.clear();
        TTM_ArrayList.clear();
        JumlahInti_ArrayList.clear();

        TBMplasma_ArrayList.clear();
        TMplasma_ArrayList.clear();
        TTMplasma_ArrayList.clear();
        JumlahPlasma_ArrayList.clear();
        KETERANGAN_ArrayList.clear();

        if (model.moveToFirst()) {
            do {
                int id_komoditas = model.getInt(model.getColumnIndex(dbhelper.KEY_IDKOMODITAS));
                String komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", id_komoditas + "");
                KOMODITAS_ArrayList.add(komoditas);

                TBM_ArrayList.add(model.getDouble(model.getColumnIndex("tbm_inti")));
                TM_ArrayList.add(model.getDouble(model.getColumnIndex("tm_inti")));
                TTM_ArrayList.add(model.getDouble(model.getColumnIndex("ttm_inti")));
                JumlahInti_ArrayList.add(model.getDouble(model.getColumnIndex("tbm_inti")) + model.getDouble(model.getColumnIndex("tm_inti")) + model.getDouble(model.getColumnIndex("ttm_inti")));

                TBMplasma_ArrayList.add(model.getDouble(model.getColumnIndex("tbm_plasma")));
                TMplasma_ArrayList.add(model.getDouble(model.getColumnIndex("tm_plasma")));
                TTMplasma_ArrayList.add(model.getDouble(model.getColumnIndex("ttm_plasma")));
                JumlahPlasma_ArrayList.add(model.getDouble(model.getColumnIndex("tbm_plasma")) + model.getDouble(model.getColumnIndex("tm_plasma")) + model.getDouble(model.getColumnIndex("ttm_plasma")));

                String mmodel = model.getString(model.getColumnIndex("keterangan"));
                if (mmodel.isEmpty()) {
                    mmodel = "-";
                }
                KETERANGAN_ArrayList.add(mmodel);

            } while (model.moveToNext());
        }

        ListAdapterPERUSAHAAN = new SQLiteListAdapterLuasAreaPerusahaan(getActivity().getApplicationContext(),
                KOMODITAS_ArrayList,
                TBM_ArrayList,
                TM_ArrayList,
                TTM_ArrayList,
                JumlahInti_ArrayList,
                TBMplasma_ArrayList,
                TMplasma_ArrayList,
                TTMplasma_ArrayList,
                JumlahPlasma_ArrayList,
                KETERANGAN_ArrayList
        );

        LISTVIEW.setAdapter(ListAdapterPERUSAHAAN);
        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                model = db.rawQuery("SELECT * FROM " + tabel + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komoditas + "%'", null);
                Log.d("query luasan", "SELECT * FROM " + tabel + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komoditas + "%'");

                model.moveToPosition(arg2);
                getIDent = model.getString(model.getColumnIndex("id"));
                getIDsensus = model.getString(model.getColumnIndex("id_sensus"));
                getIDkomoditas = model.getString(model.getColumnIndex("id_komoditas"));
                String jenisKomo = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", model.getString(model.getColumnIndex("id_komoditas")));

                final CharSequence[] dialogitem = {"Lihat data komoditas " + jenisKomo};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent ee = new Intent(getActivity().getApplicationContext(), luas_areal.class);
                                ee.putExtra("ident", getIDent);
                                Log.d("getIDent", getIDent);
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
                    nama_komoditas = "";
                    id_komoditas = "";
                } else {
                    id_komoditas = dbhelper.instantSelect("id_komoditas", "mst_komoditas", "komoditas", Komoditas).toString();
                }

                if (id_obj == 1) {
                    ShowSQLiteDBdataPetani();
                } else {
                    ShowSQLiteDBdataPerusahaan();
                }

                Log.d("Response komoditas", Komoditas);
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
}