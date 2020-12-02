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
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapterProduk;
import com.sensus.diginidea.pencatatankebun.edit.produksi;
import com.sensus.diginidea.pencatatankebun.edit.luas_areal;
import com.sensus.diginidea.pencatatankebun.prod_menu_list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class list_data_produksi extends Fragment {
    Cursor model = null;
    DbHelper dbhelper;
    SQLiteListAdapterProduk ListAdapterPETANI;
    String getIDent;
    String getIDsensus;
    String getIDkomoditas;
    String id_komoditas = "";
    String Komoditas;

    private ArrayList<String> IDsensus_ArrayList = new ArrayList<String>();
    private ArrayList<String> KOMODITAS_ArrayList = new ArrayList<String>();
    private ArrayList<Double> JUMLAHPROD_ArrayList = new ArrayList<Double>();
    private ArrayList<String> WUJUDPROD_ArrayList = new ArrayList<String>();
    private ArrayList<Double> DIJUAL_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> DISIMPAN_ArrayList = new ArrayList<Double>();
    private ArrayList<Double> KONSUMSI_ArrayList = new ArrayList<Double>();
    private ArrayList<String> KETERANGAN_ArrayList = new ArrayList<String>();
    private ArrayList<String> TVKG_ArrayList = new ArrayList<String>();

    int idLuas, cP;
    ListView LISTVIEW;
    String id_sensus, tabelProduksi, tabelLuas;
    int id_obj;
    Spinner spKomoditas;
    private ArrayAdapter<String> adapKomoditas;
    private BottomNavigationView bottomNavigation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_data_produksi, container, false);

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

        String tabelEstP, tabelEstL;
        id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));
        if (id_obj == 1) {
            tabelProduksi = "tbl_produksi_petani";
            tabelLuas = "tbl_luas_kebun_petani";
            tabelEstP = "tbl_est_produksi_kebun_petani";
            tabelEstL = "tbl_est_luas_kebun_petani";
        } else {
            tabelProduksi = "tbl_produksi_perusahaan";
            tabelLuas = "tbl_luas_kebun_perusahaan";
            tabelEstP = "tbl_est_produksi_kebun_perusahaan";
            tabelEstL = "tbl_est_luas_kebun_perusahaan";
        }

        Log.d("tabel ", tabelProduksi);
        loadSpinnerKomoditas(tabelProduksi);

        String st_prod = dbhelper.instantSelect("status", tabelProduksi, "id_sensus", id_sensus);
        idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + id_sensus + "");
        cP = dbhelper.count(tabelLuas + " where id_sensus = " + id_sensus + " and status_prod = 1");

        bottomNavigation = (BottomNavigationView) rootView.findViewById(R.id.navigation);

        if (st_prod != null && !st_prod.isEmpty() && !st_prod.equals("null")) {
            if (idLuas != cP) {
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
                                Intent ee = new Intent(getActivity().getApplicationContext(), prod_menu_list.class);
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
        model = db.rawQuery("SELECT a.* FROM " + tabelProduksi + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                "WHERE a.id_sensus='" + id_sensus + "' AND a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'", null);
        Log.d("query produksi", "SELECT * FROM " + tabelProduksi + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komoditas + "%'");

        IDsensus_ArrayList.clear();
        KOMODITAS_ArrayList.clear();
        JUMLAHPROD_ArrayList.clear();
        WUJUDPROD_ArrayList.clear();
        DIJUAL_ArrayList.clear();
        DISIMPAN_ArrayList.clear();
        KONSUMSI_ArrayList.clear();
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

                DIJUAL_ArrayList.add(model.getDouble(model.getColumnIndex("dijual")));
                DISIMPAN_ArrayList.add(model.getDouble(model.getColumnIndex("disimpan")));
                KONSUMSI_ArrayList.add(model.getDouble(model.getColumnIndex("konsumsi")));
                KETERANGAN_ArrayList.add(model.getString(model.getColumnIndex("keterangan")));

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
                    if (!WujudProduksi.equals("Kadar Karet Kering")) {
                        SatKon = " KG K3";
                    }
                }
                TVKG_ArrayList.add(SatKon);

                String mmodel = model.getString(model.getColumnIndex("keterangan"));
                if (mmodel.isEmpty()) {
                    mmodel = "-";
                }
                KETERANGAN_ArrayList.add(mmodel);

            } while (model.moveToNext());
        }

        ListAdapterPETANI = new SQLiteListAdapterProduk(getActivity().getApplicationContext(),
                IDsensus_ArrayList,
                KOMODITAS_ArrayList,
                JUMLAHPROD_ArrayList,
                WUJUDPROD_ArrayList,
                DIJUAL_ArrayList,
                DISIMPAN_ArrayList,
                KONSUMSI_ArrayList,
                KETERANGAN_ArrayList,
                TVKG_ArrayList
        );

        LISTVIEW.setAdapter(ListAdapterPETANI);
        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                SQLiteDatabase db = dbhelper.getWritableDatabase();
                model = db.rawQuery("SELECT a.* FROM " + tabelProduksi + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                        "WHERE a.id_sensus='" + id_sensus + "' AND a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'", null);
                Log.d("query produksi", "SELECT a.* FROM " + tabelProduksi + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas " +
                        "WHERE a.id_sensus='" + id_sensus + "' AND a.id_komoditas like '%" + id_komoditas + "%' AND b.komoditas like '%" + Komoditas + "%'");

                model.moveToPosition(arg2);
                getIDent = model.getString(model.getColumnIndex("id"));
                getIDsensus = model.getString(model.getColumnIndex("id_sensus"));
                getIDkomoditas = model.getString(model.getColumnIndex("id_komoditas"));
                String jenisKomo = dbhelper.instantSelect("komoditas", "mst_komoditas", "id_komoditas", getIDkomoditas);

                String st_prod = dbhelper.instantSelect("status", tabelProduksi, "id_sensus", getIDsensus);
                idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + getIDsensus + "");
                cP = dbhelper.count(tabelLuas + " where id_sensus = " + getIDsensus + " and status_prod = 1");

                int idProd = dbhelper.count(tabelProduksi + " where id_sensus = " + id_sensus + " and status = 0");

                final CharSequence[] dialogitem;
                if (st_prod != null && !st_prod.isEmpty() && !st_prod.equals("null")) {
                    if (idProd == 0) {
                        dialogitem = new CharSequence[]{"Lihat Data Produksi " + jenisKomo};
                    } else {
                        dialogitem = new CharSequence[]{"Lihat Data Produksi " + jenisKomo, "Tambah data"};
                    }
                } else {
                    dialogitem = new CharSequence[]{"Lihat Data Produksi " + jenisKomo, "Tambah data"};
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent ee = new Intent(getActivity().getApplicationContext(), produksi.class);
                                ee.putExtra("ident", getIDent);
                                ee.putExtra("id_sensus", getIDsensus);
                                ee.putExtra("komoditas", getIDkomoditas);
                                ee.putExtra("set", true);
                                startActivity(ee);
                                break;
                            case 1:
                                Intent ii = new Intent(getActivity().getApplicationContext(), produksi.class);
                                ii.putExtra("ident", getIDent);
                                ii.putExtra("id_sensus", getIDsensus);
                                ii.putExtra("komoditas", getIDkomoditas);
                                ii.putExtra("tambah", "tambah");
                                startActivity(ii);
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