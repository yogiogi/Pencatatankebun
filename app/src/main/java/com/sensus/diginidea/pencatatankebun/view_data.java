package com.sensus.diginidea.pencatatankebun;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.dbPackage.SQLiteListAdapter;
import com.sensus.diginidea.pencatatankebun.edit.edit_identitas;
import com.sensus.diginidea.pencatatankebun.listData.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Yogi on 9/25/2017.
 */

public class view_data extends AppCompatActivity {
    private int count;
    private boolean[] thumbnailsselection;

    Button mUploadButton, hapusButton;//, pilih_semua;
    Cursor model = null;
    DbHelper dbhelper;
    SQLiteListAdapter ListAdapter;
    String getIDent;
    String getIDsensus;
    String getIDobj;
    String getIDstatus;
    String RowID;
    String id_sensus;
    String kirim;
    ProgressDialog pDialog;
    int idLuas = 0;
    int idProd = 0, idEstP = 0, idEstL = 0;
    int cR = 0, cP = 0, cEP = 0, cEL = 0;
    String cat1 = "", cat2 = "";
    int cLas, cReals;

    private ArrayList<String> ID_ArrayList = new ArrayList<String>();
    private ArrayList<String> NAME_ArrayList = new ArrayList<String>();
    private ArrayList<String> PHONE_NUMBER_ArrayList = new ArrayList<String>();
    private ArrayList<String> KELTANI_ArrayList = new ArrayList<String>();
    private ArrayList<String> OBJEK_ArrayList = new ArrayList<String>();
    private ArrayList<String> MANBUN_ArrayList = new ArrayList<String>();
    private ArrayList<String> STATUSCAT1_ArrayList = new ArrayList<String>();
    private ArrayList<String> STATUSCAT2_ArrayList = new ArrayList<String>();

    ListView LISTVIEW;

    int id_obj;
    String LINK;
    String tabelLuas, tabelReal, tabelProd, tabelEstLuas, tabelEstProd;
    String objek;
    String st_idel, st_luasl, st_reall, st_prodl, st_estLL, st_estPl;
    String detekKirim;
    //    private BottomNavigationView bottomNavigation;
    int Pilihcount = 0;

    private ArrayList<String> pilih_ArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_data_identitas);

        mUploadButton = (Button) findViewById(R.id.upload_button);
        mUploadButton.setEnabled(false);
        hapusButton = (Button) findViewById(R.id.HapusPilih);
        hapusButton.setEnabled(false);
//        pilih_semua = (Button) findViewById(R.id.pilih_semua);
        LISTVIEW = (ListView) findViewById(R.id.listview);
        LISTVIEW.setScrollingCacheEnabled(false);

        dbhelper = new DbHelper(this);

        Bundle extras = getIntent().getExtras();
        Log.d("Response", extras + "");
        if (extras != null) {
            id_obj = getIntent().getExtras().getInt("id_obj_pencatatan");
        }
        if (id_obj == 1) {
            tabelLuas = "tbl_luas_kebun_petani";
            tabelReal = "tbl_realisasi_petani";
            tabelProd = "tbl_produksi_petani";
            tabelEstLuas = "tbl_est_luas_kebun_petani";
            tabelEstProd = "tbl_est_produksi_kebun_petani";
            setTitle("DAFTAR PEKEBUN");
            objek = "Pekebun";
        } else {
            tabelLuas = "tbl_luas_kebun_perusahaan";
            tabelReal = "tbl_realisasi_perusahaan";
            tabelProd = "tbl_produksi_perusahaan";
            tabelEstLuas = "tbl_est_luas_kebun_perusahaan";
            tabelEstProd = "tbl_est_produksi_kebun_perusahaan";
            setTitle("DAFTAR PBS/PBN");
            objek = "PBS/PBN";
        }

        LINK = dbhelper.instantSelect("setURL", "tb_setting", "id_set", "1");

        hapusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goListData();

            }
        });

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                kirim = "semua";
                if (pilih_ArrayList.size() > 0) {
                    new netcheck().execute("");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Tidak ada yang dipilih",
                            Toast.LENGTH_SHORT).show();
                }

//                final ArrayList<Integer> posSel = new ArrayList<Integer>();
//                posSel.clear();
//                boolean noSelect = false;
//                for (int i = 0; i < thumbnailsselection.length; i++) {
//                    if (thumbnailsselection[i] == true) {
//                        noSelect = true;
//                        Log.e("sel pos thu-->", "" + i);
//                        posSel.add(i);
//                        // break;
//                    }
//                }
//                if (!noSelect) {
//                    Toast.makeText(view_data.this, "Please Select Item!",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(view_data.this,
//                            "Selected Items:" + posSel.toString(),
//                            Toast.LENGTH_LONG).show();
//                }
            }
        });

//        pilih_semua.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                for (int i = 0; i < LISTVIEW.getChildCount(); i++) {
//                    LISTVIEW.setItemChecked(i, true);
//                }
//                mUploadButton.setEnabled(!(pilih_ArrayList.size() == 0));
//            }
//        });

//        pilih_semua.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // toggle selections of items to all or none
////                ShowSQLiteDBdata();
//                for (int i = 0; i < LISTVIEW.getChildCount(); i++) {
//                    LISTVIEW.setItemChecked(i, true);
//                }
//                mUploadButton.setEnabled(!(pilih_ArrayList.size() == 0));
//            }
//        });
    }

    @Override
    protected void onResume() {
        ShowSQLiteDBdata();
        count = LISTVIEW.getCount();
        thumbnailsselection = new boolean[count];
        super.onResume();
    }

    private void ShowSQLiteDBdata() {
        cat1 = "";
        cat2 = "";

        SQLiteDatabase db = dbhelper.getWritableDatabase();

        model = db.rawQuery("SELECT a.id_sensus AS id_sensus,a.id_obj_sensus AS id_obj_sensus, a.nama AS objek, a.telp AS telp,a.kelompok_tani AS kelompok,b.nama AS manbun, status FROM tbl_identitas a LEFT JOIN mst_manbun b ON " +
                "a.id_manbun = b.id_manbun WHERE a.id_obj_sensus = " + id_obj + " ORDER BY create_date", null);
        Log.d("array", "SELECT a.id_sensus AS id_sensus,a.id_obj_sensus AS id_obj_sensus, a.nama AS objek, a.telp AS telp,a.kelompok_tani AS kelompok,b.nama AS manbun, status FROM tbl_identitas a LEFT JOIN mst_manbun b ON " +
                "a.id_manbun = b.id_manbun WHERE a.id_obj_sensus = " + id_obj + " ORDER BY create_date");

        ID_ArrayList.clear();
        NAME_ArrayList.clear();
        PHONE_NUMBER_ArrayList.clear();
        KELTANI_ArrayList.clear();
        OBJEK_ArrayList.clear();
        MANBUN_ArrayList.clear();
        STATUSCAT1_ArrayList.clear();
        STATUSCAT2_ArrayList.clear();

        if (model.moveToFirst()) {
            do {
                ID_ArrayList.add(model.getString(model.getColumnIndex("id_sensus")));
                NAME_ArrayList.add(model.getString(model.getColumnIndex("objek")));
                PHONE_NUMBER_ArrayList.add(model.getString(model.getColumnIndex("telp")));
                KELTANI_ArrayList.add(model.getString(model.getColumnIndex("kelompok")));
                OBJEK_ArrayList.add(model.getString(model.getColumnIndex("id_obj_sensus")));

                if (id_obj == 2) {
                    MANBUN_ArrayList.add("");
                } else {
                    MANBUN_ArrayList.add(model.getString(model.getColumnIndex("manbun")));
                }

                int statusID = model.getInt(model.getColumnIndex("status"));

                String st_ide = dbhelper.instantSelect("status", "tbl_identitas", "id_sensus", model.getString(model.getColumnIndex("id_sensus")));
                String st_luas = dbhelper.instantSelect("status", tabelLuas, "id_sensus", model.getString(model.getColumnIndex("id_sensus")));
                String st_real = dbhelper.instantSelect("status", tabelReal, "id_sensus", model.getString(model.getColumnIndex("id_sensus")));

                String st_prod = dbhelper.instantSelect("status", tabelProd, "id_sensus", model.getString(model.getColumnIndex("id_sensus")));
                String st_estP = dbhelper.instantSelect("status", tabelEstProd, "id_sensus", model.getString(model.getColumnIndex("id_sensus")));
                String st_estL = dbhelper.instantSelect("status", tabelEstLuas, "id_sensus", model.getString(model.getColumnIndex("id_sensus")));

                //hitung id di tabelLuas areal
                idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + "");
                idProd = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status = 0");
                idEstP = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status = 0");
                idEstL = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status = 0");

                cR = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status_realisasi = 1");
                cP = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status_prod = 1");
                cEP = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status_est_prod = 1");
                cEL = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status_est_luas_area = 1");

                int cLuasan = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status = 0");
                int cRealisasi = dbhelper.count(tabelReal + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status = 0");
                int cProduksi = dbhelper.count(tabelProd + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status = 0");
                int cEstProd = dbhelper.count(tabelEstProd + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status = 0");
                int cEstLuas = dbhelper.count(tabelEstLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status = 0");

                if (st_ide != null && !st_ide.isEmpty() && !st_ide.equals("null")
                        && st_luas != null && !st_luas.isEmpty() && !st_luas.equals("null")
                        && st_real != null && !st_real.isEmpty() && !st_real.equals("null")) {

                    if (cLuasan == 0 && cRealisasi == 0) {
                        cat1 = "Pencatatan 1 Terkirim";
                    } else {
                        cat1 = "Pencatatan 1 lengkap, belum terkirim";
                        if (idLuas != cR) {
                            cat1 = "Pencatatan 1 belum lengkap";
                        }
                    }
                } else {
                    cat1 = "Pencatatan 1 kosong";
                    if (idLuas != cR) {
                        cat1 = "Pencatatan 1 belum lengkap";
                    }
                }

                if (st_prod != null && !st_prod.isEmpty() && !st_prod.equals("null")
                        && st_estP != null && !st_estP.isEmpty() && !st_estP.equals("null")
                        && st_estL != null && !st_estL.isEmpty() && !st_estL.equals("null")) {
                    if (cProduksi == 0 && cEstLuas == 0 && cEstProd == 0) {
                        cat2 = "Pencatatan 2 Terkirim";
                    } else {
                        cat2 = "Pencatatan 2 lengkap, belum terkirim";
                        if (idLuas != cP || idLuas != cEL || idLuas != cEP) {
                            cat2 = "Pencatatan 2 belum lengkap";
                        }
                    }
                } else {
                    if (cP == 0 && cEL == 0 && cEP == 0) {
                        cat2 = "Pencatatan 2 kosong";
                    } else if (idLuas != cP) {
                        cat2 = "Pencatatan 2 belum lengkap";
                    } else if (idLuas != cEP) {
                        cat2 = "Pencatatan 2 belum lengkap";
                    } else if (idLuas != cEL) {
                        cat2 = "Pencatatan 2 belum lengkap";
                    }
                }

                STATUSCAT1_ArrayList.add(cat1);
                STATUSCAT2_ArrayList.add(cat2);

            } while (model.moveToNext());
        }

        ListAdapter = new
                SQLiteListAdapter(view_data.this,
                ID_ArrayList,
                NAME_ArrayList,
                PHONE_NUMBER_ArrayList,
                KELTANI_ArrayList,
                OBJEK_ArrayList,
                MANBUN_ArrayList,
                STATUSCAT1_ArrayList,
                STATUSCAT2_ArrayList
        );

        LISTVIEW.setAdapter(ListAdapter);
        LISTVIEW.setLongClickable(true);
        LISTVIEW.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int arg2, long arg3) {

                model = dbhelper.getAllIdentitas(id_obj);
                model.moveToPosition(arg2);
                getIDent = dbhelper.getIDent(model);
                getIDsensus = dbhelper.getIDsensus(model);
                getIDobj = model.getString(model.getColumnIndex("nama"));
                getIDstatus = dbhelper.getIDStatus(model);
                String nama_pekebun = model.getString(model.getColumnIndex("nama"));

                st_idel = dbhelper.instantSelect("status", "tbl_identitas", "id_sensus", getIDsensus);
                st_luasl = dbhelper.instantSelect("status", tabelLuas, "id_sensus", getIDsensus);
                st_reall = dbhelper.instantSelect("status", tabelReal, "id_sensus", getIDsensus);

                st_prodl = dbhelper.instantSelect("status", tabelProd, "id_sensus", getIDsensus);
                st_estPl = dbhelper.instantSelect("status", tabelEstProd, "id_sensus", getIDsensus);
                st_estLL = dbhelper.instantSelect("status", tabelEstLuas, "id_sensus", getIDsensus);

                //hitung id di tabelLuas areal
                idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + getIDsensus);
                idProd = dbhelper.count(tabelProd + " where id_sensus = " + getIDsensus + " and status = 0");
                idEstP = dbhelper.count(tabelEstProd + " where id_sensus = " + getIDsensus + " and status = 0");
                idEstL = dbhelper.count(tabelEstLuas + " where id_sensus = " + getIDsensus + " and status = 0");

                cLas = dbhelper.count(tabelLuas + " where id_sensus = " + getIDsensus + " and status = 0");
                cReals = dbhelper.count(tabelReal + " where id_sensus = " + getIDsensus + " and status = 0");

                cR = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status_realisasi = 1");
                cP = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status_prod = 1");
                cEP = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status_est_prod = 1");
                cEL = dbhelper.count(tabelLuas + " where id_sensus = " + model.getString(model.getColumnIndex("id_sensus")) + " and status_est_luas_area = 1");

                final CharSequence[] dialogitem = {"Detail Identitas " + objek, "Pencatatan 1", "Pencatatan 2", "Kirim Pencatatan 1", "Kirim Pencatatan 2", "Kirim Data Pencatatan", "Hapus Data"};
                AlertDialog.Builder builder = new AlertDialog.Builder(view_data.this);
                builder.setTitle(nama_pekebun);
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                Intent ee = new Intent(getApplicationContext(), edit_identitas.class);
                                ee.putExtra("ident", getIDent);
                                ee.putExtra("id_sensus", getIDsensus);
                                ee.putExtra("set", true);
                                startActivity(ee);
                                break;
                            case 1:
                                Intent lr = new Intent(getApplicationContext(), list_data_luas_real.class);
                                lr.putExtra("ident", getIDent);
                                lr.putExtra("id_sensus", getIDsensus);
                                startActivity(lr);
                                break;
                            case 2:
                                Intent pe = new Intent(getApplicationContext(), list_data.class);
                                pe.putExtra("ident", getIDent);
                                pe.putExtra("id_sensus", getIDsensus);
                                startActivity(pe);
                                break;
                            case 3:
                                kirim = "catat1";
                                if (st_idel != null && !st_idel.isEmpty() && !st_idel.equals("null")
                                        && st_luasl != null && !st_luasl.isEmpty() && !st_luasl.equals("null")
                                        && st_reall != null && !st_reall.isEmpty() && !st_reall.equals("null")) {

                                    if (cLas == 0 && cReals == 0) {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 1 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        if (idLuas != cR) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Pencatatan 1 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {
                                            new netcheck().execute(getIDsensus);
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Pencatatan 1 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                            .show();
                                    if (cLas != 0 || cReals != 0) {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 1 " + getIDobj + " belum terkirim", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    if (idProd == 0 && idEstP == 0 && idEstL == 0) {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 2 " + getIDobj + " belum terkirim", Toast.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 2 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                                break;
                            case 4:
                                kirim = "catat2";
                                if (st_idel != null && !st_idel.isEmpty() && !st_idel.equals("null")
                                        && st_luasl != null && !st_luasl.isEmpty() && !st_luasl.equals("null")
                                        && st_reall != null && !st_reall.isEmpty() && !st_reall.equals("null")) {
                                    if (cLas == 0 && cReals == 0) {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 1 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                .show();

                                        if (st_prodl != null && !st_prodl.isEmpty() && !st_prodl.equals("null")
                                                && st_estPl != null && !st_estPl.isEmpty() && !st_estPl.equals("null")
                                                && st_estLL != null &&
                                                !st_estLL.isEmpty() && !st_estLL.equals("null")) {
                                            if (idProd != 0 && idEstP != 0 && idEstL != 0) {
                                                if (idLuas != cP || idLuas != cEL || idLuas != cEP) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Pencatatan 2 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                                            .show();
                                                } else {
                                                    new netcheck().execute(getIDsensus);
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(),
                                                        "Pencatatan 2 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Pencatatan 2 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    } else {
                                        if (idLuas != cR) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Pencatatan 1 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Pencatatan 1 " + getIDobj + " belum terkirim", Toast.LENGTH_SHORT)
                                                    .show();

                                            if (idProd == 0 && idEstP == 0 && idEstL == 0) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Pencatatan 2 " + getIDobj + " belum terkirim", Toast.LENGTH_SHORT)
                                                        .show();
                                            } else {
                                                Toast.makeText(getApplicationContext(),
                                                        "Pencatatan 2 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Pencatatan 1 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                            .show();

                                    if (cLas != 0 || cReals != 0) {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 1 " + getIDobj + " belum terkirim", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    if (idProd == 0 && idEstP == 0 && idEstL == 0) {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 2 " + getIDobj + " belum terkirim", Toast.LENGTH_SHORT)
                                                .show();
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 2 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                }
                                break;
                            case 5:
                                kirim = "sebaris";
                                if (st_idel != null && !st_idel.isEmpty() && !st_idel.equals("null")
                                        && st_luasl != null && !st_luasl.isEmpty() && !st_luasl.equals("null")
                                        && st_reall != null && !st_reall.isEmpty() && !st_reall.equals("null")) {
                                    if (cLas == 0 && cReals == 0) {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 1 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                .show();

                                        if (st_prodl != null && !st_prodl.isEmpty() && !st_prodl.equals("null")
                                                && st_estPl != null && !st_estPl.isEmpty() && !st_estPl.equals("null")
                                                && st_estLL != null &&
                                                !st_estLL.isEmpty() && !st_estLL.equals("null")) {
                                            if (idProd == 0 && idEstP == 0 && idEstL == 0) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Pencatatan 2 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Pencatatan 2 " + getIDobj + " kosong", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    } else {
                                        if (idLuas != cR) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Pencatatan 1 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {
                                            if (st_prodl != null && !st_prodl.isEmpty() && !st_prodl.equals("null")
                                                    && st_estPl != null && !st_estPl.isEmpty() && !st_estPl.equals("null")
                                                    && st_estLL != null &&
                                                    !st_estLL.isEmpty() && !st_estLL.equals("null")) {
                                                if (idProd != 0 && idEstP != 0 && idEstL != 0) {
                                                    if (idLuas != cP || idLuas != cEL || idLuas != cEP) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "Pencatatan 2 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                                                .show();
                                                    } else {
                                                        new netcheck().execute(getIDsensus);
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Pencatatan 2 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                            .show();

                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(),
                                                        "Pencatatan 2 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Pencatatan 1 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                            .show();

                                    if (cLas != 0 && cReals != 0) {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 1 " + getIDobj + " belum terkirim", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    if (st_prodl != null && !st_prodl.isEmpty() && !st_prodl.equals("null")
                                            && st_estPl != null && !st_estPl.isEmpty() && !st_estPl.equals("null")
                                            && st_estLL != null &&
                                            !st_estLL.isEmpty() && !st_estLL.equals("null")) {
                                        if (idProd == 0 && idEstP == 0 && idEstL == 0) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Pencatatan 2 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                    .show();

                                            finish();
                                            startActivity(getIntent());
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 2 " + getIDobj + " kosong", Toast.LENGTH_SHORT)
                                                .show();

                                    }
                                }
                                break;
                            case 6:
                                SQLiteDatabase db = dbhelper.getWritableDatabase();
                                db.execSQL("delete from tbl_identitas where id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelLuas + " where id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelProd + " where id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelReal + " where id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelEstLuas + " where id_sensus = '" + getIDsensus + "'");
                                db.execSQL("delete from " + tabelEstProd + " where id_sensus = '" + getIDsensus + "'");
                                ShowSQLiteDBdata();
                                break;
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

//        LISTVIEW.setSelected(true);
//        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
//                CheckBox cb = (CheckBox) arg1.findViewById(R.id.checkBox1);
////                cb.performClick();
//                model = dbhelper.getAllIdentitas(id_obj);
//                model.moveToPosition(arg2);
//                getIDsensus = dbhelper.getIDsensus(model);
//
//                if (pilih_ArrayList.contains(getIDsensus)) {
//                    pilih_ArrayList.remove(getIDsensus);
//                    cb.setChecked(false);
//                } else {
//                    pilih_ArrayList.add(getIDsensus);
//                    cb.setChecked(true);
//                }
//
//                Log.d("array ", pilih_ArrayList.size() + "");
//                mUploadButton.setEnabled(!(pilih_ArrayList.size() == 0));
//                hapusButton.setEnabled(!(pilih_ArrayList.size() == 0));
//                model.close();
//            }
//        });
    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

//    public String cekKirim(String idSENSUS) {
//        String success;
//        String url = "http://smallholder.inobu.org/cekID.php?" + "id_sensus=";
//
//        JSONParser jParser = new JSONParser();
//        JSONObject json = jParser.getJSONFromUrl(url + idSENSUS);
//
//        try {
//            success = json.getString("success");
//            Log.e("error", "nilai sukses=" + success);
//            Log.e("error", url + idSENSUS);
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            Log.e("erro", "tidak bisa ambil data 1");
//        }
//    }

    private class checkID extends AsyncTask<String, String, String> {
        private ProgressDialog nDialog;
        String success;
        String url = "http://smallholder.inobu.org/cekID.php?" + "id_sensus=";
        String idSENSUS;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(view_data.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            idSENSUS = arg0[0];
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(url + idSENSUS);

            try {
                success = json.getString("success");
                Log.e("error", "nilai sukses=" + success);
                Log.e("error", url + idSENSUS);

            } catch (Exception e) {
                // TODO: handle exception
                Log.e("erro", "tidak bisa ambil data 1");
            }
            return success;
        }

        @Override
        protected void onPostExecute(String result) {
            nDialog.dismiss();
            if (success.equals("1")) {
                Toast.makeText(getApplicationContext(),
                        "Ada id yang sama tidak bisa dikirim", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Pengiriman berhasil", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private class netcheck extends AsyncTask<String, String, Boolean> {
        String idSENSUS;
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            nDialog = new ProgressDialog(view_data.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            idSENSUS = params[0];
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url
                            .openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            nDialog.dismiss();

            if (th == true) {
                if (kirim == "semua") {
                    for (int i = 0; i < pilih_ArrayList.size(); i++) {
                        getIDobj = dbhelper.instantSelect("nama", "tbl_identitas", "id_sensus", pilih_ArrayList.get(i).toString());
                        st_idel = dbhelper.instantSelect("status", "tbl_identitas", "id_sensus", pilih_ArrayList.get(i).toString());
                        st_luasl = dbhelper.instantSelect("status", tabelLuas, "id_sensus", pilih_ArrayList.get(i).toString());
                        st_reall = dbhelper.instantSelect("status", tabelReal, "id_sensus", pilih_ArrayList.get(i).toString());
                        st_prodl = dbhelper.instantSelect("status", tabelProd, "id_sensus", pilih_ArrayList.get(i).toString());
                        st_estPl = dbhelper.instantSelect("status", tabelEstProd, "id_sensus", pilih_ArrayList.get(i).toString());
                        st_estLL = dbhelper.instantSelect("status", tabelEstLuas, "id_sensus", pilih_ArrayList.get(i).toString());

                        idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + pilih_ArrayList.get(i).toString());
                        idProd = dbhelper.count(tabelProd + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status = 0");
                        idEstP = dbhelper.count(tabelEstProd + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status = 0");
                        idEstL = dbhelper.count(tabelEstLuas + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status = 0");

                        cLas = dbhelper.count(tabelLuas + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status = 0");
                        cReals = dbhelper.count(tabelReal + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status = 0");

                        cR = dbhelper.count(tabelLuas + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status_realisasi = 1");
                        cP = dbhelper.count(tabelLuas + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status_prod = 1");
                        cEP = dbhelper.count(tabelLuas + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status_est_prod = 1");
                        cEL = dbhelper.count(tabelLuas + " where id_sensus = " + pilih_ArrayList.get(i).toString() + " and status_est_luas_area = 1");

                        if (st_idel != null && !st_idel.isEmpty() && !st_idel.equals("null")
                                && st_luasl != null && !st_luasl.isEmpty() && !st_luasl.equals("null")
                                && st_reall != null && !st_reall.isEmpty() && !st_reall.equals("null")) {

                            if (cLas == 0 && cReals == 0) {
                                Toast.makeText(getApplicationContext(),
                                        "Pencatatan 1 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                        .show();

                                if (st_prodl != null && !st_prodl.isEmpty() && !st_prodl.equals("null")
                                        && st_estPl != null && !st_estPl.isEmpty() && !st_estPl.equals("null")
                                        && st_estLL != null && !st_estLL.isEmpty() && !st_estLL.equals("null")) {
                                    if (idProd != 0 && idEstP != 0 && idEstL != 0) {
                                        if (idLuas != cP || idLuas != cEL || idLuas != cEP) {
                                            Toast.makeText(getApplicationContext(),
                                                    "Pencatatan 2 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {
                                            new sentProdBt().execute(pilih_ArrayList.get(i).toString());
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "Pencatatan 2 " + getIDobj + " sudah terkirim", Toast.LENGTH_SHORT)
                                                .show();

                                        Log.d("tag pencatatan 2 sudah kirim", Pilihcount + "");
                                        if (kirim == "catat1" || kirim == "sebaris" || pilih_ArrayList.size() == Pilihcount) {
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    }
                                } else {
                                    Log.d("status ", "tidak lengkap");
                                    Toast.makeText(getApplicationContext(),
                                            "Pencatatan 2 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                            .show();

                                    Pilihcount++;
                                    Log.d("tag semua pencatatan 2", Pilihcount + "");
                                    if (kirim == "catat1" || kirim == "sebaris" || pilih_ArrayList.size() == Pilihcount) {
                                        finish();
                                        startActivity(getIntent());
                                    }

                                }
                            } else {
                                if (idLuas != cR) {
                                    Log.d("status ", "tidak lengkap " + idLuas + " cr " + cR);
                                    Toast.makeText(getApplicationContext(),
                                            "Pencatatan 1 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                            .show();

                                    Pilihcount++;
                                    if (kirim == "catat1" || kirim == "sebaris" || pilih_ArrayList.size() == Pilihcount) {
                                        Log.d("tag semua akan mati", Pilihcount + "");
                                        finish();
                                        startActivity(getIntent());
                                    }

                                    Log.d("tag semua belum lengkap", Pilihcount + "");
                                } else {
                                    Log.d("tag all run narsum" + pilih_ArrayList.get(i).toString(), Pilihcount + "");
                                    new sentnarasumberBt().execute(pilih_ArrayList.get(i).toString());
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Pencatatan 1 " + getIDobj + " belum lengkap", Toast.LENGTH_SHORT)
                                    .show();

                            Pilihcount++;
                            if (kirim == "catat1" || kirim == "sebaris" || pilih_ArrayList.size() == Pilihcount) {
                                Log.d("tag semua pencatan1", Pilihcount + "");
                                finish();
                                startActivity(getIntent());
                            }
                        }
                    }
                } else if (kirim == "catat1") {
                    new sentnarasumberBt().execute(idSENSUS);
                } else if (kirim == "sebaris") {
                    new sentnarasumberBt().execute(idSENSUS);
                } else if (kirim == "catat2") {
                    new sentProdBt().execute(idSENSUS);
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Pengiriman data gagal, cek koneksi", Toast.LENGTH_SHORT)
                        .show();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    class sentnarasumberBt extends AsyncTask<String, Void, String> {
        String success = "2";
        String idSENSUS;
        String namanya;

        @Override
        protected String doInBackground(String... params) {
            idSENSUS = params[0];
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_identitas WHERE id_sensus = " + idSENSUS + "  and status = 0 ORDER BY create_date", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    namanya = model.getString(model.getColumnIndex("nama"));
                    String telp = model.getString(model.getColumnIndex("telp"));

                    String kelompok_tani = model.getString(model.getColumnIndex("kelompok_tani"));
                    if (id_obj != 1) {
                        kelompok_tani = "-";
                    }

                    String kode_propinsi = model.getString(model.getColumnIndex("kode_propinsi"));
                    String kode_kabupaten = kode_propinsi + model.getString(model.getColumnIndex("kode_kabupaten"));
                    String kode_kecamatan = kode_kabupaten + model.getString(model.getColumnIndex("kode_kecamatan"));
                    String kode_desa = kode_kecamatan + model.getString(model.getColumnIndex("kode_desa"));
                    String no_urut = model.getString(model.getColumnIndex("no_urut"));
                    String id_manbun = model.getString(model.getColumnIndex("id_manbun"));
                    if (id_obj != 1) {
                        id_manbun = "9999";
                    }

                    String tgl_catat = model.getString(model.getColumnIndex("tgl_catat"));

                    Log.d("query", id_sensus + "," + id_obj + "," + namanya + "," + telp + "," + kelompok_tani + "," +
                            kode_propinsi + "," + kode_kabupaten + "," + kode_kecamatan + "," + kode_desa + "," + String.format("%04d", Integer.parseInt(no_urut.toString())) + "," +
                            id_manbun + "," + tgl_catat + "");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_obj_sensus", id_obj + ""));
                    nameValuePairs.add(new BasicNameValuePair("nama", namanya));
                    nameValuePairs.add(new BasicNameValuePair("no_telp", telp));

                    if (id_obj == 1) {
                        nameValuePairs.add(new BasicNameValuePair("kelompok_tani", kelompok_tani));
                    }

                    nameValuePairs.add(new BasicNameValuePair("kode_propinsi", kode_propinsi));
                    nameValuePairs.add(new BasicNameValuePair("kode_kabkot", kode_kabupaten));
                    nameValuePairs.add(new BasicNameValuePair("kode_kecamatan", kode_kecamatan));
                    nameValuePairs.add(new BasicNameValuePair("kode_desa", kode_desa));
                    nameValuePairs.add(new BasicNameValuePair("no_urut", String.format("%04d", Integer.parseInt(no_urut.toString()))));
                    nameValuePairs.add(new BasicNameValuePair("id_manbun", id_manbun));

                    if (id_obj == 1) {
                        nameValuePairs.add(new BasicNameValuePair("tgl_catat", tgl_catat));
                    }

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json;

//                    if (id_obj == 1) {
                    json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_identitas,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " narasumber");
                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_identitas", "status", Integer.parseInt(RowID), 1);
                        } else {
                            gagal(idSENSUS);
                        }
                    } catch (Exception z) {
                        z.printStackTrace();
                    }

                } while (model.moveToNext());
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(view_data.this);
            pDialog.setMessage("Kirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            pDialog.dismiss();

            if (Integer.parseInt(result) == 1) {
                Toast.makeText(getApplicationContext(),
                        "Data identitas " + objek + ", " + namanya + "  terkirim!", Toast.LENGTH_LONG).show();
                new sentLuasKebunBt().execute(idSENSUS);
            } else if (Integer.parseInt(result) == 2) {
                Toast.makeText(getApplicationContext(),
                        "Data identitas " + objek + ", " + namanya + "  sudah terkirim!", Toast.LENGTH_LONG).show();
                new sentLuasKebunBt().execute(idSENSUS);
            } else if (Integer.parseInt(result) == 99) {
                Toast.makeText(getApplicationContext(),
                        "ID Sensus " + idSENSUS + " dari " + namanya + " sudah ada", Toast.LENGTH_LONG)
                        .show();

                Log.d("tag 99", Pilihcount + "");
                Pilihcount++;
                Log.d("tag semua pencatatan 2", Pilihcount + "");
                if (kirim == "catat1" || kirim == "sebaris" || pilih_ArrayList.size() == Pilihcount) {
                    finish();
                    startActivity(getIntent());
                }

//                if (st_idel != null && !st_idel.isEmpty() && !st_idel.equals("null")
//                        && st_luasl != null && !st_luasl.isEmpty() && !st_luasl.equals("null")
//                        && st_reall != null && !st_reall.isEmpty() && !st_reall.equals("null")) {
//                    new sentLuasKebunBt().execute(idSENSUS);
//                }else{
//                    Toast.makeText(getApplicationContext(),
//                            "Silahkan ubah ID Sensus dari " + namanya + " sudah ada", Toast.LENGTH_LONG)
//                            .show();
//                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data identitas " + objek + ", " + namanya + "  gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    class sentLuasKebunBt extends AsyncTask<String, Void, String> {
        //        ProgressDialog pDialog;
        String success = "0";
        String idSENSUS;
        String namanya;

        @Override
        protected String doInBackground(String... params) {
            idSENSUS = params[0];
            Double tbm = 0.0, tm = 0.0, ttm = 0.0, tbm_inti = 0.0, tm_inti = 0.0, ttm_inti = 0.0, tbm_plasma = 0.0, tm_plasma = 0.0, ttm_plasma = 0.0;

            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT a.*, b.id_obj_sensus, b.nama  FROM " + tabelLuas + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + " and a.status = 0", null);

            if (model.moveToFirst()) {
                do {
                    namanya = model.getString(model.getColumnIndex("nama"));
                    String RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    if (id_obj == 1) {
                        tbm = model.getDouble(model.getColumnIndex("tbm"));
                        tm = model.getDouble(model.getColumnIndex("tm"));
                        ttm = model.getDouble(model.getColumnIndex("ttm"));

                        Log.d("query", id_sensus + "," + id_komoditas + "," + tbm + "," + tm + "," + ttm);
                    } else {
                        tbm_inti = model.getDouble(model.getColumnIndex("tbm_inti"));
                        tm_inti = model.getDouble(model.getColumnIndex("tm_inti"));
                        ttm_inti = model.getDouble(model.getColumnIndex("ttm_inti"));
                        tbm_plasma = model.getDouble(model.getColumnIndex("tbm_plasma"));
                        tm_plasma = model.getDouble(model.getColumnIndex("tm_plasma"));
                        ttm_plasma = model.getDouble(model.getColumnIndex("ttm_plasma"));

                        Log.d("query", id_sensus + "," + id_komoditas + "," + tbm_inti + "," + tm_inti + "," + ttm_inti
                                + "," + tbm_plasma + "," + tm_plasma + "," + ttm_plasma);
                    }
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));

                    if (id_obj == 1) {
                        nameValuePairs.add(new BasicNameValuePair("tbm", tbm.toString()));
                        nameValuePairs.add(new BasicNameValuePair("tm", tm.toString()));
                        nameValuePairs.add(new BasicNameValuePair("ttm", ttm.toString()));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("tbm_inti", tbm_inti.toString()));
                        nameValuePairs.add(new BasicNameValuePair("tm_inti", tm_inti.toString()));
                        nameValuePairs.add(new BasicNameValuePair("ttm_inti", ttm_inti.toString()));
                        nameValuePairs.add(new BasicNameValuePair("tbm_plasma", tbm_plasma.toString()));
                        nameValuePairs.add(new BasicNameValuePair("tm_plasma", tm_plasma.toString()));
                        nameValuePairs.add(new BasicNameValuePair("ttm_plasma", ttm_plasma.toString()));
                    }
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json;
                    if (id_obj == 1) {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_luas_kebun_petani,
                                "POST", nameValuePairs);
                    } else {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_luas_kebun_perusahaan,
                                "POST", nameValuePairs);
                    }

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " luas kebun");
                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus(tabelLuas, "status", Integer.parseInt(RowID), 1);
                        } else {
                            gagal(idSENSUS);
                        }
                    } catch (Exception z) {
                        z.printStackTrace();
                    }

                } while (model.moveToNext());
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(view_data.this);
            pDialog.setMessage("Kirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            pDialog.dismiss();

            if (Integer.parseInt(result) == 1) {
                new sentRealisasiBt().execute(idSENSUS);
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun " + objek + " " + namanya + " terkirim!", Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(result) == 2) {
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun " + objek + " " + namanya + "  sudah terkirim!", Toast.LENGTH_LONG).show();
                new sentLuasKebunBt().execute(idSENSUS);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun " + objek + " " + namanya + "  gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(view_data.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentRealisasiBt extends AsyncTask<String, Void, String> {
        //        ProgressDialog pDialog;
        String success = "2";
        String idSENSUS;
        String namanya;

        @Override
        protected String doInBackground(String... params) {
            idSENSUS = params[0];
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT a.*, b.id_obj_sensus, b.nama  FROM " + tabelReal + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + " and a.status = 0", null);

            if (model.moveToFirst()) {
                do {
                    namanya = model.getString(model.getColumnIndex("nama"));
                    RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String bulan = model.getString(model.getColumnIndex("bulan"));
                    Double jumlah_prod = model.getDouble(model.getColumnIndex("jumlah_prod"));
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String satuan = model.getString(model.getColumnIndex("satuan"));
                    Double nilai_jual = model.getDouble(model.getColumnIndex("nilai_jual"));
                    Double harga = model.getDouble(model.getColumnIndex("harga"));
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + bulan + "," + jumlah_prod + "," + nilai_jual + "," + harga + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("bulan", bulan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod.toString()));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("satuan", satuan));
                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", nilai_jual.toString()));
                    nameValuePairs.add(new BasicNameValuePair("harga", harga.toString()));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json;
                    if (id_obj == 1) {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_realisasi_petani,
                                "POST", nameValuePairs);
                    } else {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_realisasi_perusahaan,
                                "POST", nameValuePairs);
                    }

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " realisasi");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus(tabelReal, "status", Integer.parseInt(RowID), 1);
                        } else {
                            gagal(idSENSUS);
                        }
                    } catch (Exception z) {
                        z.printStackTrace();
                    }

                } while (model.moveToNext());
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(view_data.this);
            pDialog.setMessage("Kirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            pDialog.dismiss();

            if (Integer.parseInt(result) == 1) {
                Toast.makeText(getApplicationContext(),
                        "Data pencatatan 1 " + objek + " " + namanya + " terkirim!", Toast.LENGTH_LONG).show();


                if (kirim == "catat1") {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    Log.d("realisasiBT semua", idSENSUS);
                    st_prodl = dbhelper.instantSelect("status", tabelProd, "id_sensus", idSENSUS);
                    st_estPl = dbhelper.instantSelect("status", tabelEstProd, "id_sensus", idSENSUS);
                    st_estLL = dbhelper.instantSelect("status", tabelEstLuas, "id_sensus", idSENSUS);

                    idProd = dbhelper.count(tabelProd + " where id_sensus = " + idSENSUS + " and status = 0");
                    idEstP = dbhelper.count(tabelEstProd + " where id_sensus = " + idSENSUS + " and status = 0");
                    idEstL = dbhelper.count(tabelEstLuas + " where id_sensus = " + idSENSUS + " and status = 0");
                    idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + idSENSUS + "");

                    cP = dbhelper.count(tabelLuas + " where id_sensus = " + idSENSUS + " and status_prod = 1");
                    cEP = dbhelper.count(tabelLuas + " where id_sensus = " + idSENSUS + " and status_est_prod = 1");
                    cEL = dbhelper.count(tabelLuas + " where id_sensus = " + idSENSUS + " and status_est_luas_area = 1");

                    if (st_prodl != null && !st_prodl.isEmpty() && !st_prodl.equals("null")
                            && st_estPl != null && !st_estPl.isEmpty() && !st_estPl.equals("null")
                            && st_estLL != null && !st_estLL.isEmpty() && !st_estLL.equals("null")) {
                        if (idProd != 0 && idEstP != 0 && idEstL != 0) {
                            if (idLuas != cP || idLuas != cEL || idLuas != cEP) {
                                Toast.makeText(getApplicationContext(),
                                        "Pencatatan 2 " + objek + "  " + namanya + " belum lengkap", Toast.LENGTH_SHORT)
                                        .show();

                                Pilihcount++;
                                Log.d("tag semua loose", Pilihcount + "");
                                if (kirim == "catat1" || kirim == "sebaris" || pilih_ArrayList.size() == Pilihcount) {
                                    Log.d("tag semua", "loose");
                                    finish();
                                    startActivity(getIntent());
                                }
                            } else {
                                new sentProdBt().execute(idSENSUS);
                            }
                        }
                    } else {
                        Log.d("status ", "tidak lengkap");
                        Toast.makeText(getApplicationContext(),
                                "Pencatatan 2 " + objek + "  " + namanya + "belum lengkap", Toast.LENGTH_SHORT)
                                .show();

                        Pilihcount++;
                        Log.d("tag semua loose bebas", Pilihcount + "");
                        if (kirim == "catat1" || kirim == "sebaris" || pilih_ArrayList.size() == Pilihcount) {
                            Log.d("tag semua", "loose bebas");
                            finish();
                            startActivity(getIntent());
                        }
                    }
                }

            } else if (Integer.parseInt(result) == 2) {
                Toast.makeText(getApplicationContext(),
                        "Data luas realisasi " + objek + "  " + namanya + "  sudah terkirim!", Toast.LENGTH_LONG).show();
                if (kirim == "catat1") {
                    Toast.makeText(getApplicationContext(),
                            "Data pencatatan 1 " + objek + "  " + namanya + " terkirim!", Toast.LENGTH_LONG).show();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                } else {
                    new sentProdBt().execute(idSENSUS);
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data realisasi " + objek + "  " + namanya + " gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(view_data.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentProdBt extends AsyncTask<String, Void, String> {
        //        ProgressDialog pDialog;
        String success = "2";
        String idSENSUS;
        String namanya;

        @Override
        protected String doInBackground(String... params) {
            idSENSUS = params[0];
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT a.*, b.id_obj_sensus, b.nama  FROM " + tabelProd + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + " and a.status = 0", null);

            if (model.moveToFirst()) {
                do {
                    namanya = model.getString(model.getColumnIndex("nama"));
                    RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tgl_pendataan = model.getString(model.getColumnIndex("tgl_pendataan"));
                    Double jumlah_prod = model.getDouble(model.getColumnIndex("jumlah_prod"));
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String satuan = model.getString(model.getColumnIndex("satuan"));
                    Double dijual = model.getDouble(model.getColumnIndex("dijual"));
                    Double disimpan = model.getDouble(model.getColumnIndex("disimpan"));
                    Double konsumsi = model.getDouble(model.getColumnIndex("konsumsi"));
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tgl_pendataan + "," + jumlah_prod + "," + wujud_prod
                            + "," + dijual + "," + disimpan + "," + konsumsi + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tgl_pendataan", tgl_pendataan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod.toString()));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("satuan", satuan));
                    nameValuePairs.add(new BasicNameValuePair("dijual", dijual.toString()));
                    nameValuePairs.add(new BasicNameValuePair("disimpan", disimpan.toString()));
                    nameValuePairs.add(new BasicNameValuePair("konsumsi", konsumsi.toString()));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json;
                    if (id_obj == 1) {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_produksi_petani,
                                "POST", nameValuePairs);
                    } else {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_produksi_perusahaan,
                                "POST", nameValuePairs);
                    }

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " produksi");
                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus(tabelProd, "status", Integer.parseInt(RowID), 1);
                        } else {
                            gagal(idSENSUS);
                        }
                    } catch (Exception z) {
                        z.printStackTrace();
                    }

                } while (model.moveToNext());
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(view_data.this);
            pDialog.setMessage("Kirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            pDialog.dismiss();

            if (Integer.parseInt(result) == 1) {
                new sentEstLuasKebunBt().execute(idSENSUS);

                Toast.makeText(getApplicationContext(),
                        "Data produksi " + objek + "  " + namanya + " terkirim!", Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(result) == 2) {
                Toast.makeText(getApplicationContext(),
                        "Data produksi " + objek + " " + namanya + " sudah terkirim!", Toast.LENGTH_LONG).show();
                new sentEstLuasKebunBt().execute(idSENSUS);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data produksi " + objek + "  " + namanya + " gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(view_data.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentEstLuasKebunBt extends AsyncTask<String, Void, String> {
        String success = "2";
        String idSENSUS;
        String namanya;

        @Override
        protected String doInBackground(String... params) {
            idSENSUS = params[0];
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT a.*, b.id_obj_sensus, b.nama FROM " + tabelEstLuas + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + " and a.status = 0", null);

            if (model.moveToFirst()) {
                do {
                    namanya = model.getString(model.getColumnIndex("nama"));
                    RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    Double tbm = 0.0, tm = 0.0, ttm = 0.0, tbm_inti = 0.0, tm_inti = 0.0, ttm_inti = 0.0, tbm_plasma = 0.0, tm_plasma = 0.0, ttm_plasma = 0.0;
                    if (id_obj == 1) {
                        tbm = model.getDouble(model.getColumnIndex("tbm"));
                        tm = model.getDouble(model.getColumnIndex("tm"));
                        ttm = model.getDouble(model.getColumnIndex("ttm"));

                        Log.d("query", id_sensus + "," + id_komoditas + "," + tbm + "," + tm + "," + ttm);
                    } else {
                        tbm_inti = model.getDouble(model.getColumnIndex("tbm_inti"));
                        tm_inti = model.getDouble(model.getColumnIndex("tm_inti"));
                        ttm_inti = model.getDouble(model.getColumnIndex("ttm_inti"));
                        tbm_plasma = model.getDouble(model.getColumnIndex("tbm_plasma"));
                        tm_plasma = model.getDouble(model.getColumnIndex("tm_plasma"));
                        ttm_plasma = model.getDouble(model.getColumnIndex("ttm_plasma"));

                        Log.d("query", id_sensus + "," + id_komoditas + "," + tbm_inti + "," + tm_inti + "," + ttm_inti
                                + "," + tbm_plasma + "," + tm_plasma + "," + ttm_plasma);
                    }
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));

                    if (id_obj == 1) {
                        nameValuePairs.add(new BasicNameValuePair("tbm", tbm.toString()));
                        nameValuePairs.add(new BasicNameValuePair("tm", tm.toString()));
                        nameValuePairs.add(new BasicNameValuePair("ttm", ttm.toString()));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("tbm_inti", tbm_inti.toString()));
                        nameValuePairs.add(new BasicNameValuePair("tm_inti", tm_inti.toString()));
                        nameValuePairs.add(new BasicNameValuePair("ttm_inti", ttm_inti.toString()));
                        nameValuePairs.add(new BasicNameValuePair("tbm_plasma", tbm_plasma.toString()));
                        nameValuePairs.add(new BasicNameValuePair("tm_plasma", tm_plasma.toString()));
                        nameValuePairs.add(new BasicNameValuePair("ttm_plasma", ttm_plasma.toString()));
                    }
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json;
                    if (id_obj == 1) {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_luas_kebun_petani,
                                "POST", nameValuePairs);
                    } else {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_luas_kebun_perusahaan,
                                "POST", nameValuePairs);
                    }

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " estimasi luas kebun");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus(tabelEstLuas, "status", Integer.parseInt(RowID), 1);
                        } else {
                            gagal(idSENSUS);
                        }
                    } catch (Exception z) {
                        z.printStackTrace();
                    }

                } while (model.moveToNext());
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(view_data.this);
            pDialog.setMessage("Kirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            pDialog.dismiss();

            if (Integer.parseInt(result) == 1) {
                new sentEstProdBt().execute(idSENSUS);
                Toast.makeText(getApplicationContext(),
                        "Data estimasi luas kebun " + objek + "  " + namanya + " terkirim!", Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(result) == 2) {
                new sentEstProdBt().execute(idSENSUS);
                Toast.makeText(getApplicationContext(),
                        "Data estimasi luas kebun " + objek + "  " + namanya + "  sudah terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data estimasi luas kebun " + objek + "  " + namanya + "  gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
//                finish();
            }
        }
    }

    class sentEstProdBt extends AsyncTask<String, Void, String> {
        //        ProgressDialog pDialog;
        String success = "3";
        String idSENSUS;
        String namanya;

        @Override
        protected String doInBackground(String... params) {
            idSENSUS = params[0];
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT a.*, b.id_obj_sensus, b.nama FROM " + tabelEstProd + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + "  and a.status = 0", null);

            if (model.moveToFirst()) {
                do {
                    namanya = model.getString(model.getColumnIndex("nama"));
                    RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tgl_pendataan = model.getString(model.getColumnIndex("tgl_pendataan"));
                    Double jumlah_prod = model.getDouble(model.getColumnIndex("jumlah_prod"));
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String satuan = model.getString(model.getColumnIndex("satuan"));
                    Double nilai_jual = model.getDouble(model.getColumnIndex("nilai_jual"));
                    Double harga = model.getDouble(model.getColumnIndex("harga"));
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tgl_pendataan + "," + jumlah_prod + "," + wujud_prod
                            + "," + nilai_jual + "," + harga + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tgl_pendataan", tgl_pendataan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod.toString()));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("satuan", satuan));
                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", nilai_jual.toString()));
                    nameValuePairs.add(new BasicNameValuePair("harga", harga.toString()));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json;
                    if (id_obj == 1) {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_produksi_pertanian,
                                "POST", nameValuePairs);
                    } else {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_produksi_perusahaan,
                                "POST", nameValuePairs);
                    }

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " estimasi produksi");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus(tabelEstProd, "status", Integer.parseInt(RowID), 1);
                        } else {
                            gagal(idSENSUS);
                        }
                    } catch (Exception z) {
                        z.printStackTrace();
                    }

                } while (model.moveToNext());
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(view_data.this);
            pDialog.setMessage("Kirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            pDialog.dismiss();

            if (Integer.parseInt(result) == 1) {
                Toast.makeText(getApplicationContext(),
                        "Data estimasi produksi " + objek + " " + namanya + " terkirim!", Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(result) == 3) {
                Toast.makeText(getApplicationContext(),
                        "Data estimasi produksi " + objek + " " + namanya + " sudah terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data estimasi produksi " + objek + " " + namanya + " gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
            }


            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_list_identitas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
//            case R.id.unggahSemua:
//                kirim = "semua";
//                new netcheck().execute();
//                break;
            case R.id.tambah:
                intent = new Intent(view_data.this, edit_identitas.class);
                intent.putExtra("id_sensus", id_sensus);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
                break;
        }
        return false;
    }

    void gagal(String idSENS) {
        dbhelper.updatebyid_sensus("tbl_identitas", "status", idSENS, 0);
        dbhelper.updatebyid_sensus(tabelEstLuas, "status", idSENS, 0);
        dbhelper.updatebyid_sensus(tabelEstProd, "status", idSENS, 0);
        dbhelper.updatebyid_sensus(tabelLuas, "status", idSENS, 0);
        dbhelper.updatebyid_sensus(tabelProd, "status", idSENS, 0);
        dbhelper.updatebyid_sensus(tabelReal, "status", idSENS, 0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),
                submenu.class);
        intent.putExtra("id_obj_pencatatan", id_obj);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private class HapusExe extends AsyncTask<Long, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            nDialog = new ProgressDialog(view_data.this);
            nDialog.setTitle("Sedang proses");
            nDialog.setMessage("Menunggu..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... longs) {
            if (pilih_ArrayList.size() > 0) {
                for (int i = 0; i < pilih_ArrayList.size(); i++) {
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    db.execSQL("delete from tbl_identitas where id_sensus = '" + pilih_ArrayList.get(i).toString() + "'");
                    db.execSQL("delete from " + tabelLuas + " where id_sensus = '" + pilih_ArrayList.get(i).toString() + "'");
                    db.execSQL("delete from " + tabelProd + " where id_sensus = '" + pilih_ArrayList.get(i).toString() + "'");
                    db.execSQL("delete from " + tabelReal + " where id_sensus = '" + pilih_ArrayList.get(i).toString() + "'");
                    db.execSQL("delete from " + tabelEstLuas + " where id_sensus = '" + pilih_ArrayList.get(i).toString() + "'");
                    db.execSQL("delete from " + tabelEstProd + " where id_sensus = '" + pilih_ArrayList.get(i).toString() + "'");
                    db.close();
                }
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            nDialog.dismiss();
            if (th == true) {
                ShowSQLiteDBdata();
                thumbnailsselection = new boolean[count];
                pilih_ArrayList.clear();
                mUploadButton.setEnabled(!(pilih_ArrayList.size() == 0));
                hapusButton.setEnabled(!(pilih_ArrayList.size() == 0));

                Toast.makeText(getApplicationContext(),
                        "Data sudah dihapus", Toast.LENGTH_SHORT)
                        .show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "Data gagal dihapus", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }

    public void goListData() {
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
        alert.setTitle("Perhatian");
        alert.setCancelable(false);
        alert.setMessage("Yakin ingin menghapus data?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new HapusExe().execute();

            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert.show();

    }

    public class SQLiteListAdapter extends BaseAdapter {

        Context context;
        private ArrayList<String> userID;
        private ArrayList<String> UserName;
        private ArrayList<String> User_PhoneNumber;
        private ArrayList<String> User_Kelompoktani;
        private ArrayList<String> User_obj;
        private ArrayList<String> User_Manbun;
        private ArrayList<String> Status1;
        private ArrayList<String> Status2;

        public SQLiteListAdapter(
                Context context2,
                ArrayList<String> id,
                ArrayList<String> name,
                ArrayList<String> phone,
                ArrayList<String> kelompoktani,
                ArrayList<String> objek,
                ArrayList<String> manbun,
                ArrayList<String> status1,
                ArrayList<String> status2
        ) {

            this.context = context2;
            this.userID = id;
            this.UserName = name;
            this.User_PhoneNumber = phone;
            this.User_Kelompoktani = kelompoktani;
            this.User_obj = objek;
            this.User_Manbun = manbun;
            this.Status1 = status1;
            this.Status2 = status2;
        }

        public int getCount() {
            // TODO Auto-generated method stub
            return userID.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(int position, View child, ViewGroup parent) {

            Holder holder;

            LayoutInflater layoutInflater;
            String obj = User_obj.get(position);
            if (child == null) {
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                child = layoutInflater.inflate(R.layout.listviewdatalayout, null);

                holder = new Holder();

                holder.textviewname = (TextView) child.findViewById(R.id.textViewNAME);
                holder.textviewphone_number = (TextView) child.findViewById(R.id.textViewTELEPON);
                holder.textviewkelompoktani = (TextView) child.findViewById(R.id.textViewKELOMPOK_TANI);
                holder.textviewManbun = (TextView) child.findViewById(R.id.textViewManbun);
                holder.textviewStatus1 = (TextView) child.findViewById(R.id.tvStatusCat1);
                holder.textviewStatus2 = (TextView) child.findViewById(R.id.tvStatusCat2);
                holder.labelManbun = (TextView) child.findViewById(R.id.labelManbun);
                holder.layoutManbun = (LinearLayout) child.findViewById(R.id.layoutManbun);
                holder.checkbox = (CheckBox) child.findViewById(R.id.checkBox1);

                if (Integer.parseInt(obj) != 1) {
                    holder.textviewManbun.setVisibility(View.GONE);
                    holder.layoutManbun.setVisibility(View.GONE);
                    holder.labelManbun.setVisibility(View.GONE);
                }

                child.setTag(holder);
            } else {
                holder = (Holder) child.getTag();
            }
            holder.textviewname.setText(UserName.get(position));
            holder.textviewphone_number.setText(User_PhoneNumber.get(position));
            holder.textviewkelompoktani.setText(User_Kelompoktani.get(position));
            holder.textviewManbun.setText(User_Manbun.get(position));
            holder.textviewStatus1.setText(Status1.get(position));
            holder.textviewStatus2.setText(Status2.get(position));
            holder.checkbox.setId(position);
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    CheckBox cb = (CheckBox) v;
                    cb.setChecked(false);

                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }

                    model = dbhelper.getAllIdentitas(id_obj);
                    model.moveToPosition(id);
                    getIDsensus = dbhelper.getIDsensus(model);

                    if (pilih_ArrayList.contains(getIDsensus)) {
                        pilih_ArrayList.remove(getIDsensus);
                    } else {
                        pilih_ArrayList.add(getIDsensus);
                    }

                    Log.d("array ", pilih_ArrayList.size() + "");
                    Log.d("id_sensus ", getIDsensus + "");
                    mUploadButton.setEnabled(!(pilih_ArrayList.size() == 0));
                    hapusButton.setEnabled(!(pilih_ArrayList.size() == 0));
                    model.close();
                }
            });
            holder.checkbox.setChecked(thumbnailsselection[position]);
            return child;
        }

        public class Holder {
            TextView textviewname;
            TextView textviewphone_number;
            TextView textviewkelompoktani;
            TextView textviewManbun;
            TextView textviewStatus1;
            TextView textviewStatus2;
            TextView labelManbun;
            CheckBox checkbox;
            LinearLayout layoutManbun;
        }
    }
}