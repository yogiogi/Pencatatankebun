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
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapterEstProduk;
import com.sensus.diginidea.pencatatankebun.edit.est_luas_areal;
import com.sensus.diginidea.pencatatankebun.edit.est_produksi;
import com.sensus.diginidea.pencatatankebun.edit.luas_areal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class list_data_est_produk extends Fragment {
    String[] daftar;
    Cursor model = null;
    DbHelper dbhelper;
    SQLiteListAdapterEstProduk ListAdapterPETANI;
    String getIDent;
    String getIDsensus;

    private ArrayList<String> IDsensus_ArrayList = new ArrayList<String>();
    private ArrayList<String> KOMODITAS_ArrayList = new ArrayList<String>();
    private ArrayList<Double> JUMLAHPROD_ArrayList = new ArrayList<Double>();
    private ArrayList<String> WUJUDPROD_ArrayList = new ArrayList<String>();
    private ArrayList<Double> NILAIJUAL_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> HARGA_ArrayList = new ArrayList<Double>();
    private ArrayList<String> KETERANGAN_ArrayList = new ArrayList<String>();
    private ArrayList<String> TVKG_ArrayList = new ArrayList<String>();

    ListView LISTVIEW;
    String id_sensus, tabelEstimasi, tabelLuas;
    String id_komoditas;
    int id_obj;
    Spinner spKomoditas;
    private ArrayAdapter<String> adapKomoditas;
    private BottomNavigationView bottomNavigation;
    String Komoditas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_data_estproduksi, container, false);

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
        bottomNavigation = (BottomNavigationView) rootView.findViewById(R.id.navigation);

        id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));

        String tabelProduksi, tabelEstL;
        if (id_obj == 1) {
            tabelProduksi = "tbl_produksi_petani";
            tabelEstimasi = "tbl_est_produksi_kebun_petani";
            tabelLuas = "tbl_luas_kebun_petani";
            tabelEstL = "tbl_est_luas_kebun_petani";
        } else {
            tabelProduksi = "tbl_produksi_perusahaan";
            tabelEstimasi = "tbl_est_produksi_kebun_perusahaan";
            tabelLuas = "tbl_luas_kebun_perusahaan";
            tabelEstL = "tbl_est_luas_kebun_perusahaan";
        }

        Log.d("tabel ", tabelEstimasi);
        loadSpinnerKomoditas(tabelEstimasi);

        String st_estp = dbhelper.instantSelect("status", tabelEstimasi, "id_sensus", id_sensus);
        int idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + id_sensus + "");
        int cEP = dbhelper.count(tabelLuas + " where id_sensus = " + id_sensus + " and status_est_prod = 1");

        if (st_estp != null && !st_estp.isEmpty() && !st_estp.equals("null")) {
            if (idLuas != cEP) {
                bottomNavigation.setVisibility(View.VISIBLE);
            } else {
                bottomNavigation.setVisibility(View.GONE);
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
                                Intent ee = new Intent(getActivity().getApplicationContext(), est_produksi.class);
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
        model = db.rawQuery("SELECT distinct b.id_komoditas, b.komoditas FROM " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas WHERE a.id_sensus = " + id_sensus + " ORDER BY komoditas", null);

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
        if(Komoditas == "Semua Komoditas")
        {
            Komoditas="";
        }
        model = db.rawQuery("SELECT a.* FROM " + tabelEstimasi + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                "WHERE a.id_sensus='" + id_sensus + "' and a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'", null);

        IDsensus_ArrayList.clear();
        KOMODITAS_ArrayList.clear();
        JUMLAHPROD_ArrayList.clear();
        WUJUDPROD_ArrayList.clear();
        NILAIJUAL_ArrayList.clear();
        HARGA_ArrayList.clear();
        KETERANGAN_ArrayList.clear();
        TVKG_ArrayList.clear();

        if (model.moveToFirst()) {
            do {
                IDsensus_ArrayList.add(model.getString(model.getColumnIndex("id_sensus")));

                String komoditas = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", model.getString(model.getColumnIndex("id_komoditas")) + "");
                KOMODITAS_ArrayList.add(komoditas);

                JUMLAHPROD_ArrayList.add(model.getDouble(model.getColumnIndex("jumlah_prod")));

                String WujudProduksi = model.getString(model.getColumnIndex("wujud_produksi"));
                WUJUDPROD_ArrayList.add(WujudProduksi);
                NILAIJUAL_ArrayList.add(model.getDouble(model.getColumnIndex("nilai_jual")));
                HARGA_ArrayList.add(model.getDouble(model.getColumnIndex("harga")));

                String mmodel = model.getString(model.getColumnIndex("keterangan"));
                if (mmodel.isEmpty()) {
                    mmodel = "-";
                }
                KETERANGAN_ArrayList.add(mmodel);

                String Satuan = model.getString(model.getColumnIndex("satuan"));
                String SatKon = " KG";

                if (komoditas.equals("Kelapa")) {
                    if (Satuan.equals("Butir")) {
                        SatKon = " KG KOPRA";
//                        txSatjmlProd.setText("Butir");
                    } else if (Satuan.equals("KG")) {
                        SatKon = " KG";
                    }
                } else if (komoditas.equals("Karet")) {
                    SatKon = " KG";
                    if (!WujudProduksi.equals(" Kadar Karet Kering")) {
                        SatKon = " KG K3";
                    }
                }

                TVKG_ArrayList.add(SatKon);
            } while (model.moveToNext());
        }

        ListAdapterPETANI = new SQLiteListAdapterEstProduk(getActivity().getApplicationContext(),
                IDsensus_ArrayList,
                KOMODITAS_ArrayList,
                JUMLAHPROD_ArrayList,
                WUJUDPROD_ArrayList,
                NILAIJUAL_ArrayList,
                HARGA_ArrayList,
                KETERANGAN_ArrayList,
                TVKG_ArrayList
        );

        LISTVIEW.setAdapter(ListAdapterPETANI);
        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                model = db.rawQuery("SELECT a.* FROM " + tabelEstimasi + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                        "WHERE a.id_sensus='" + id_sensus + "' and a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'", null);

                model.moveToPosition(arg2);
                getIDent = model.getString(model.getColumnIndex("id"));
                getIDsensus = model.getString(model.getColumnIndex("id_sensus"));

                String jenisKomo = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", model.getString(model.getColumnIndex("id_komoditas")));
                final CharSequence[] dialogitem = {"Lihat Data Estimasi Produksi " + jenisKomo};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent ee = new Intent(getActivity().getApplicationContext(), est_produksi.class);
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


