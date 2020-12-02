package com.sensus.diginidea.pencatatankebun;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapterNama;
import com.sensus.diginidea.pencatatankebun.edit.est_produksi;
import com.sensus.diginidea.pencatatankebun.edit.produksi;

import java.util.ArrayList;

public class prod_menu_list extends AppCompatActivity {
    ListView ListView01;
    TextView txJml_luas, textView11, txPemilik;
    Menu menu;
    protected Cursor cursor;
    DbHelper dbcenter;
    SQLiteDatabase db;
    public static MainActivity ma;
    String id_sensus, getIDent;
    int id_obj;
    String tabel, tabelProduksi;

    private ArrayList<String> ID_ArrayList = new ArrayList<String>();
    private ArrayList<String> NAME_ArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_menu_list);

        textView11 = (TextView) findViewById(R.id.textView11);
        textView11.setText("KOMODITAS YANG DITANAM");
        txJml_luas = (TextView) findViewById(R.id.txJml_luas);
        txPemilik = (TextView) findViewById(R.id.txPemilik);
        ListView01 = (ListView) findViewById(R.id.lsView);
        ListView01.setScrollingCacheEnabled(false);

        dbcenter = new DbHelper(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            Log.i("Value", extras.getString("id_sensus"));
            Log.d("Response", extras + "");

            id_sensus = extras.getString("id_sensus");
            Log.d("id_sensus", id_sensus + "");
        }
        id_obj = Integer.parseInt(dbcenter.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));
        txPemilik.setText(dbcenter.instantSelect("nama", "tbl_identitas", "id_sensus", id_sensus));

        if (id_obj == 1) {
            tabel = "tbl_luas_kebun_petani";
            tabelProduksi = "tbl_produksi_perusahaan";
        } else {
            tabel = "tbl_luas_kebun_perusahaan";
            tabelProduksi = "tbl_produksi_petani";
        }

        int cKmoditas = dbcenter.count(tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_prod = 0");
        txJml_luas.setText(cKmoditas + "");
        if (cKmoditas == 0) {
            Toast.makeText(prod_menu_list.this, "Bagian produksi sudah selesai diinput", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(prod_menu_list.this, est_produksi.class);
            i.putExtra("id_sensus", id_sensus);
            startActivity(i);
        }

        RefreshList();
    }

    public void RefreshList() {
        db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("select a.id_komoditas as id, b.komoditas as komoditas from " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_prod = 0", null);
        Log.d("query", "select a.id_komoditas as id, b.komoditas as komoditas from " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + "");

        ID_ArrayList.clear();
        NAME_ArrayList.clear();

        if (cursor.moveToFirst()) {
            do {
                ID_ArrayList.add(cursor.getString(cursor.getColumnIndex("id")));
                NAME_ArrayList.add(cursor.getString(cursor.getColumnIndex("komoditas")));
            } while (cursor.moveToNext());
        }

        SQLiteListAdapterNama ListAdapter = new SQLiteListAdapterNama(prod_menu_list.this,
                NAME_ArrayList
        );

        ListView01.setAdapter(ListAdapter);
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                cursor = db.rawQuery("select a.id_komoditas as id, b.komoditas as komoditas from " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_prod = 0", null);
                Log.d("query", "select a.id_komoditas as id, b.komoditas as komoditas from " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_prod = 0");
                cursor.moveToPosition(arg2);
                String selection = dbcenter.getIDent(cursor);
                Intent i = new Intent(getApplicationContext(), produksi.class);
                i.putExtra("id_sensus", id_sensus);
                i.putExtra("komoditas", selection);
                i.putExtra("posisi", 1);
                startActivity(i);
            }
        });
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        goListData();
    }

    public void goListData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(prod_menu_list.this);
        alert.setTitle("Perhatian");
        alert.setCancelable(false);
        alert.setMessage("Anda Ingin keluar ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent;
                intent = new Intent(prod_menu_list.this,
                        submenu.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
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