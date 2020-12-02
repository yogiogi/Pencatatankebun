package com.sensus.diginidea.pencatatankebun;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapterKomoditas;
import com.sensus.diginidea.pencatatankebun.edit.est_luas_areal;
import com.sensus.diginidea.pencatatankebun.edit.est_produksi;

import java.util.ArrayList;

/**
 * Created by Yogi on 9/25/2017.
 */

public class view_data_estimasi extends AppCompatActivity {
    Cursor model = null;
    SQLiteListAdapterKomoditas ListAdapter;
    String getIDent;
    String getIDsensus;
    String getIDobj;
    String id_sensus;
    String id_obj_sensus;
    int index;

    private ArrayList<String> ID_ArrayList = new ArrayList<String>();
    private ArrayList<String> NAME_ArrayList = new ArrayList<String>();
    private ArrayList<String> STATUSR_ArrayList = new ArrayList<String>();

    TextView ketNama, txJml_luas, ketJml_luas;
    ListView LISTVIEW;
    int id_obj;
    DbHelper dbcenter;
    SQLiteDatabase db;
    EditText cari;
    Button btCari;
    String tabel, tabelLuas, tabelReal, tabelProd, tabelEstLuas, tabelEstProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_est_menu_list);

        ketJml_luas = (TextView) findViewById(R.id.ketJml_luas);
        ketNama = (TextView) findViewById(R.id.textView11);
        txJml_luas = (TextView) findViewById(R.id.txJml_luas);
        LISTVIEW = (ListView) findViewById(R.id.lsView);
        LISTVIEW.setScrollingCacheEnabled(false);
        cari = (EditText) findViewById(R.id.txCari);
        btCari = (Button) findViewById(R.id.btCari);

        dbcenter = new DbHelper(this);

        Bundle extras = getIntent().getExtras();
        Log.d("Response", extras + "");
        if (extras != null) {
            id_obj = getIntent().getExtras().getInt("id_obj_pencatatan");
        }

        if (id_obj == 1) {
            ketJml_luas.setText("JUMLAH PETANI : ");
            tabel = "tbl_luas_kebun_petani";
            ketNama.setText("LIST PETANI");

            tabelReal = "tbl_realisasi_petani";
            tabelProd = "tbl_produksi_petani";
            tabelEstLuas = "tbl_est_luas_kebun_petani";
            tabelEstProd = "tbl_est_produksi_kebun_petani";
        } else {
            ketJml_luas.setText("JUMLAH PERUSAHAAN : ");
            tabel = "tbl_luas_kebun_perusahaan";
            ketNama.setText("LIST PERUSAHAAN");

            tabelReal = "tbl_realisasi_perusahaan";
            tabelProd = "tbl_produksi_perusahaan";
            tabelEstLuas = "tbl_est_luas_kebun_perusahaan";
            tabelEstProd = "tbl_est_produksi_kebun_perusahaan";
        }

        int JumlahPetani = dbcenter.count("tbl_identitas a " +
                "where exists (select b. id_sensus from tbl_realisasi_petani b where b.id_sensus = a.id_sensus) and " +
                "exists (select c. id_sensus from tbl_luas_kebun_petani c where c.id_sensus = a.id_sensus) " +
                "and a.id_obj_sensus = 1 ORDER BY a.create_date");
        txJml_luas.setText(JumlahPetani + "");

//        cari.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!s.toString().isEmpty() && s.toString() != null) {
//                    ShowSQLiteDBdata(s.toString());
//                }
//            }
//        });

        ShowSQLiteDBdata("");
        btCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSQLiteDBdata(cari.getText().toString().trim());
                Log.d("cari ", cari.getText().toString().trim());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void ShowSQLiteDBdata(String id) {
        db = dbcenter.getReadableDatabase();
//        model = db.rawQuery("select a.id_sensus as id_sensus, a.nama as nama from tbl_identitas a where a.id_obj_sensus = " + id_obj + " and a.id_sensus like '%" + id + "%' ORDER BY a.create_date", null);
        model = db.rawQuery("select a.id_sensus as id_sensus, a.nama as nama from tbl_identitas a " +
                "where exists (select b. id_sensus from tbl_realisasi_petani b where b.id_sensus = a.id_sensus) and " +
                "exists (select c. id_sensus from tbl_luas_kebun_petani c where c.id_sensus = a.id_sensus) " +
                "and a.id_obj_sensus = 1 ORDER BY a.create_date", null);

        ID_ArrayList.clear();
        NAME_ArrayList.clear();

        if (model.moveToFirst())

        {
            do {
                getIDsensus = model.getString(model.getColumnIndex("id_sensus"));
                ID_ArrayList.add(getIDsensus);
                NAME_ArrayList.add(model.getString(model.getColumnIndex("nama")));

                String st_prodl, st_estLL, st_estPl;
                st_prodl = dbcenter.instantSelect("status", tabelProd, "id_sensus", getIDsensus);
                st_estPl = dbcenter.instantSelect("status", tabelEstProd, "id_sensus", getIDsensus);
                st_estLL = dbcenter.instantSelect("status", tabelEstLuas, "id_sensus", getIDsensus);

                if (st_prodl != null && !st_prodl.isEmpty() && !st_prodl.equals("null")
                        && st_estPl != null && !st_estPl.isEmpty() && !st_estPl.equals("null")
                        && st_estLL != null && !st_estLL.isEmpty() && !st_estLL.equals("null")) {
                    STATUSR_ArrayList.add("not ok");
                } else {
                    STATUSR_ArrayList.add("ok");
                }


            } while (model.moveToNext());
        }

        ListAdapter = new
                SQLiteListAdapterKomoditas(view_data_estimasi.this,
                ID_ArrayList,
                NAME_ArrayList,
                STATUSR_ArrayList
        );

        LISTVIEW.setAdapter(ListAdapter);
        LISTVIEW.setSelected(true);
        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
//                model = dbcenter.getAllIdentitas(id_obj);
                model = db.rawQuery("select a.id_sensus as id_sensus, a.nama as nama from tbl_identitas a " +
                        "where exists (select b. id_sensus from tbl_realisasi_petani b where b.id_sensus = a.id_sensus) and " +
                        "exists (select c. id_sensus from tbl_luas_kebun_petani c where c.id_sensus = a.id_sensus) " +
                        "and a.id_obj_sensus = 1 ORDER BY a.create_date", null);

                model.moveToPosition(arg2);
                getIDsensus = model.getString(model.getColumnIndex("id_sensus"));

                final CharSequence[] dialogitem = {"Produksi", "Estimasi Produksi", "Estimasi Luas Areal"};
                AlertDialog.Builder builder = new AlertDialog.Builder(view_data_estimasi.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent ee = new Intent(getApplicationContext(), prod_menu_list.class);
                                ee.putExtra("id_sensus", getIDsensus);
                                Log.d("id_sensus", getIDsensus + "");
                                startActivity(ee);
                                break;
                            case 1:
                                Intent pr = new Intent(getApplicationContext(), est_produksi.class);
                                pr.putExtra("id_sensus", getIDsensus);
                                Log.d("id_sensus", getIDsensus + "");
                                startActivity(pr);
                                break;
                            case 2:
                                Intent la = new Intent(getApplicationContext(), est_luas_areal.class);
                                la.putExtra("id_sensus", getIDsensus);
                                Log.d("id_sensus", getIDsensus + "");
                                startActivity(la);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
    }
}