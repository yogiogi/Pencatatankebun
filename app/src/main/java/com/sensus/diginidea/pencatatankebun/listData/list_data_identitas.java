package com.sensus.diginidea.pencatatankebun.listData;

/**
 * Created by Yogi on 9/23/2017.
 */

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sensus.diginidea.pencatatankebun.R;
import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapter;

import java.util.ArrayList;

public class list_data_identitas extends Fragment {
    SQLiteDatabase SQLITEDATABASE;

    String[] daftar;
    Cursor model = null;
    DbHelper dbhelper;
    SQLiteListAdapter ListAdapter;
    String getIDent;
    String getIDsensus;

    private ArrayList<String> ID_ArrayList = new ArrayList<String>();
    private ArrayList<String> NAME_ArrayList = new ArrayList<String>();
    private ArrayList<String> PHONE_NUMBER_ArrayList = new ArrayList<String>();
    private ArrayList<String> SUBJECT_ArrayList = new ArrayList<String>();
    ListView LISTVIEW;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list_data_identitas, container, false);

        LISTVIEW = (ListView) rootView.findViewById(R.id.listview);
        LISTVIEW.setScrollingCacheEnabled(false);
        dbhelper = new DbHelper(getActivity());

        if (getArguments() != null) {
            String mParam1 = getArguments().getString("params");
            Log.d("parameter", mParam1);
        }
        return rootView;
    }

    public void onResume() {
        ShowSQLiteDBdata();
        super.onResume();
    }

    private void ShowSQLiteDBdata() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        model = db.rawQuery("SELECT * FROM tbl_identitas ORDER BY create_date", null);

        ID_ArrayList.clear();
        NAME_ArrayList.clear();
        PHONE_NUMBER_ArrayList.clear();
        SUBJECT_ArrayList.clear();

        if (model.moveToFirst()) {
            do {
                ID_ArrayList.add(model.getString(model.getColumnIndex("a.id_sensus")));
                NAME_ArrayList.add(model.getString(model.getColumnIndex("a.nama")));
                PHONE_NUMBER_ArrayList.add(model.getString(model.getColumnIndex("a.telp")));
                SUBJECT_ArrayList.add(model.getString(model.getColumnIndex(dbhelper.KEY_KELTANI)));

            } while (model.moveToNext());
        }

//        ListAdapter = new SQLiteListAdapter(getActivity(),
//                ID_ArrayList,
//                NAME_ArrayList,
//                PHONE_NUMBER_ArrayList,
//                SUBJECT_ArrayList
//        );

        LISTVIEW.setAdapter(ListAdapter);
//        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position,
//                                    long id) {
//
//                model = dbhelper.getAllIdentitas(id_obj);
//                model.moveToPosition(position);
//                getIDent = dbhelper.getIDent(model);
//                getIDsensus = dbhelper.getIDsensus(model);
//
//                LISTVIEW.setSelected(true);
////                LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                    public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
////                        final CharSequence[] dialogitem = {"Lihat Identitas", "+ luas Areal Tanaman Perkebunan", "+ Realisasi Produksi Bulan Januari s/d Agustus 2017", "+ Produksi", "+ Est Produksi dan Luas Areal Desember 2017", "Hapus Data"};
////                        AlertDialog.Builder builder = new AlertDialog.Builder(list_data_identitas.this);
////                        builder.setTitle("Pilihan");
////                        builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int item) {
////                                switch (item) {
////                                    case 0:
////                                        Intent ee = new Intent(getApplicationContext(), edit_identitas.class);
////                                        ee.putExtra("ident", getIDent);
////                                        ee.putExtra("id_sensus", getIDsensus);
////                                        ee.putExtra("set",true);
////                                        startActivity(ee);
////                                        break;
////                                    case 1:
////                                        Intent la = new Intent(getApplicationContext(), view_dataLuas.class);
////                                        la.putExtra("id_sensus", getIDsensus);
////                                        startActivity(la);
////                                        break;
////                                    case 2:
////                                        Intent rp = new Intent(getApplicationContext(), real_produk.class);
////                                        rp.putExtra("id_sensus", getIDsensus);
////                                        startActivity(rp);
////                                        break;
////                                    case 3:
////                                        Intent p = new Intent(getApplicationContext(), produksi.class);
////                                        p.putExtra("id_sensus", getIDsensus);
////                                        startActivity(p);
////                                        break;
////                                    case 4:
////                                        Intent e = new Intent(getApplicationContext(), estimasi.class);
////                                        e.putExtra("id_sensus", getIDsensus);
////                                        startActivity(e);
////                                        break;
////                                    case 5:
////                                        SQLiteDatabase db = dbhelper.getWritableDatabase();
//////                                db.execSQL("delete from biodata where nama = '" + selection + "'");
////                                        ShowSQLiteDBdata();
////                                        break;
////                                }
////                            }
////                        });
////                        builder.create().show();
////                    }
////                });
//                model.close();
//            }
//        });
    }
}


