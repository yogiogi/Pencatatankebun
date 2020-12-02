package com.sensus.diginidea.pencatatankebun.listData;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sensus.diginidea.pencatatankebun.JSONParser;
import com.sensus.diginidea.pencatatankebun.MainActivity;
import com.sensus.diginidea.pencatatankebun.R;
import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.slidingItems.SlidingTabLayout;
import com.sensus.diginidea.pencatatankebun.view_data;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * Created by Yogi on 9/25/2017.
 */

public class list_data extends AppCompatActivity {
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private ActionTabsViewPagerAdapterView myViewPageAdapter;

    String id_sensus;
    DbHelper dbhelper;
    TextView lbNamaPetani, jml_luas, statusKirim;
    int id_obj;
    String tabel, tabelReal, tabelProd, tabelEstP, tabelEstL;
    Cursor model = null;
    int idLuas = 0, idLuasan = 0, idRealisasi = 0, idReal = 0, idProd = 0, idEstP = 0, idEstL = 0;
    int cR = 0, cP = 0, cEP = 0, cEL = 0;

    String kirimapa = "";
    String LINK, RowID;

    String st_ide = "", st_luas = "", st_real = "";
    String st_prod, st_estp, st_estl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_data);

        statusKirim = (TextView) findViewById(R.id.txStatusKirim);
        lbNamaPetani = (TextView) findViewById(R.id.lbNamaPetani);
        jml_luas = (TextView) findViewById(R.id.txJml_luas);

        Bundle extras = getIntent().getExtras();
        dbhelper = new DbHelper(this);

        Log.d("Response", extras + "");
        if (extras != null) {
            id_sensus = getIntent().getExtras().getString("id_sensus");
            Log.d("id_sensus", id_sensus + "");
        }

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
        setTitle("LIST DATA PENCATATAN 2");

        lbNamaPetani.setText(dbhelper.instantSelect("nama", "tbl_identitas", "id_sensus", id_sensus));
        jml_luas.setText(dbhelper.count(tabel + " where id_sensus = '" + id_sensus + "'") + "");
        st_prod = dbhelper.instantSelect("status", tabelProd, "id_sensus", id_sensus);
        st_estp = dbhelper.instantSelect("status", tabelEstP, "id_sensus", id_sensus);
        st_estl = dbhelper.instantSelect("status", tabelEstL, "id_sensus", id_sensus);

        st_ide = dbhelper.instantSelect("status", "tbl_identitas", "id_sensus", id_sensus);
        st_luas = dbhelper.instantSelect("status", tabel, "id_sensus", id_sensus);
        st_real = dbhelper.instantSelect("status", tabelReal, "id_sensus", id_sensus);

        idLuas = dbhelper.count(tabel + " where id_sensus = " + id_sensus + "");
        idReal = dbhelper.count(tabel + " where id_sensus = " + id_sensus + " and status_realisasi = 1");

        idLuasan = dbhelper.count(tabel + " where id_sensus = " + id_sensus + " and status = 0");
        idRealisasi = dbhelper.count(tabelReal + " where id_sensus = " + id_sensus + " and status = 0");

        idProd = dbhelper.count(tabelProd + " where id_sensus = " + id_sensus + " and status = 0");
        idEstP = dbhelper.count(tabelEstP + " where id_sensus = " + id_sensus + " and status = 0");
        idEstL = dbhelper.count(tabelEstL + " where id_sensus = " + id_sensus + " and status = 0");

        cR = dbhelper.count(tabel + " where id_sensus = " + id_sensus + " and status_realisasi = 1");
        cP = dbhelper.count(tabel + " where id_sensus = " + id_sensus + " and status_prod = 1");
        cEP = dbhelper.count(tabel + " where id_sensus = " + id_sensus + " and status_est_prod = 1");
        cEL = dbhelper.count(tabel + " where id_sensus = " + id_sensus + " and status_est_luas_area = 1");

        String cat = "";
        if (st_prod != null && !st_prod.isEmpty() && !st_prod.equals("null")
                && st_estp != null && !st_estp.isEmpty() && !st_estp.equals("null")
                && st_estl != null && !st_estl.isEmpty() && !st_estl.equals("null")) {
            if (idProd == 0 && idEstP == 0 && idEstP == 0) {
                statusKirim.setText("Terkirim");
            } else {
                statusKirim.setText("Belum Terkirim");

                if (idLuas != cP || idLuas != cEL || idLuas != cEP) {
                    if (idLuas != cP) {
                        cat = "Produksi, ";
                    }
                    if (idLuas != cEL) {
                        cat += "Estimasi Luas Area, ";
                    }
                    if (idLuas != cEP) {
                        cat += "Estimasi Produksi, ";
                    }

                    cat += "belum lengkap";
                    statusKirim.setText(cat);
                }

            }
        } else {
            statusKirim.setVisibility(View.GONE);
        }

        list_data_luasan mainFragment = new list_data_luasan();
        mainFragment.onDataPassed(id_sensus);

        slidingTabLayout = (SlidingTabLayout)

                findViewById(R.id.tab);

        viewPager = (ViewPager)

                findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new

                list_data_produksi());
        fragments.add(new

                list_data_est_produk());
        fragments.add(new

                list_data_est_luas_area());

        myViewPageAdapter = new

                ActionTabsViewPagerAdapterView(getFragmentManager(),

                fragments);
        viewPager.setAdapter(myViewPageAdapter);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);

        LINK = dbhelper.instantSelect("setURL", "tb_setting", "id_set", "1");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public interface OnDataPassedListener {
        void onDataPassed(String text);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menulist, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.upload:
//                if (st_luas != null && !st_luas.isEmpty() && !st_luas.equals("null")
//                        && st_real != null && !st_real.isEmpty() && !st_real.equals("null")) {
//                    if (idLuasan == 0 && idRealisasi == 0) {
//                        kirimData();
//                    } else {
//                        if (idLuas != cR) {
//                            Toast.makeText(this, "Pencatatan 1 belum lengkap", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(this, "Pencatatan 1 belum terkirim", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                } else {
//                    statusKirim.setVisibility(View.GONE);
//                }
//
////                if (st_ide != null && !st_ide.isEmpty() && !st_ide.equals("null")
////                        && st_luas != null && !st_luas.isEmpty() && !st_luas.equals("null")
////                        && st_real != null && !st_real.isEmpty() && !st_real.equals("null")) {
////                    if (Integer.parseInt(st_ide) == 1 && Integer.parseInt(st_luas) == 1 && Integer.parseInt(st_real) == 1) {
////                    } else {
////                        Toast.makeText(this, "Pencatatan 1 belum terkirim", Toast.LENGTH_SHORT).show();
////                    }
////                } else {
////                    if (idLuas != cR) {
////                        Toast.makeText(this, "Pencatatan 1 belum lengkap", Toast.LENGTH_SHORT).show();
////                    }
////                }
//                break;
//        }
//        return false;
//    }

    public void kirimData() {
        if (st_prod != null && !st_prod.isEmpty() && !st_prod.equals("null")
                && st_estp != null && !st_estp.isEmpty() && !st_estp.equals("null")
                && st_estl != null && !st_estl.isEmpty() && !st_estl.equals("null")) {
            if (idProd == 0 && idEstP == 0 && idEstP == 0) {
                Toast.makeText(this, "Pencatatan 2 sudah terkirim", Toast.LENGTH_SHORT).show();
            } else {
                if (idLuas != cP || idLuas != cEL || idLuas != cEP) {
                    Toast.makeText(this, "Pencatatan 2 belum lengkap", Toast.LENGTH_SHORT).show();
                } else {
                    new netcheck().execute();
                }
            }
        }
        {
            statusKirim.setVisibility(View.GONE);
        }

//        if (st_prod != null && !st_prod.isEmpty() && !st_prod.equals("null")
//                && st_estp != null && !st_estp.isEmpty() && !st_estp.equals("null")
//                && st_estl != null && !st_estl.isEmpty() && !st_estl.equals("null")) {
//            if (Integer.parseInt(st_prod) == 1 && Integer.parseInt(st_estp) == 1 && Integer.parseInt(st_estl) == 1) {
//            } else {
//                new netcheck().execute();
//            }
//        } else {
//            if (idLuas != cP && idLuas != cEP && idLuas != cEP) {
//                Toast.makeText(this, "Pencatatan 2 masih kosong", Toast.LENGTH_SHORT).show();
//            } else {
//                if (idLuas != cP) {
//                    Toast.makeText(this, "Pencatatan 2 belum lengkap", Toast.LENGTH_SHORT).show();
//                } else if (idLuas != cEP) {
//                    Toast.makeText(this, "Pencatatan 2 belum lengkap", Toast.LENGTH_SHORT).show();
//                } else if (idLuas != cEL) {
//                    Toast.makeText(this, "Pencatatan 2 belum lengkap", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }


        if (idLuas != idReal) {
            Toast.makeText(this, "Pencatatan 1 belum lengkap", Toast.LENGTH_SHORT).show();
        } else {
//                new netcheck().execute();
        }
    }

    private class netcheck extends AsyncTask<Long, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(list_data.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... longs) {
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
                if (id_obj == 1) {
                    new sentProdPetani().execute();
                } else {
                    new sentProdPerusahaan().execute();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Gagal, Masalah jaringan", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    class sentProdPetani extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_produksi_petani WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
//                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tgl_pendataan = model.getString(model.getColumnIndex("tgl_pendataan"));
                    String jumlah_prod = Double.valueOf(model.getString(model.getColumnIndex("jumlah_prod"))).longValue() + "";
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String dijual = Double.valueOf(model.getString(model.getColumnIndex("dijual"))).longValue() + "";
                    String disimpan = Double.valueOf(model.getString(model.getColumnIndex("disimpan"))).longValue() + "";
                    String konsumsi = Double.valueOf(model.getString(model.getColumnIndex("konsumsi"))).longValue() + "";
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tgl_pendataan + "," + jumlah_prod + "," + wujud_prod
                            + "," + dijual + "," + disimpan + "," + konsumsi + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tgl_pendataan", tgl_pendataan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("dijual", dijual));
                    nameValuePairs.add(new BasicNameValuePair("disimpan", disimpan));
                    nameValuePairs.add(new BasicNameValuePair("konsumsi", konsumsi));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_produksi_petani,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " prod petani");
                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_produksi_petani", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data.this);
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
                new sentEstLuasKebunPetani().execute();
                Toast.makeText(getApplicationContext(),
                        "Data produksi petani terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data produksi petani gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentProdPerusahaan extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_produksi_perusahaan WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
//                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tgl_pendataan = model.getString(model.getColumnIndex("tgl_pendataan"));
                    String jumlah_prod = Double.valueOf(model.getString(model.getColumnIndex("jumlah_prod"))).longValue() + "";
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String dijual = Double.valueOf(model.getString(model.getColumnIndex("dijual"))).longValue() + "";
                    String disimpan = Double.valueOf(model.getString(model.getColumnIndex("disimpan"))).longValue() + "";
                    String konsumsi = Double.valueOf(model.getString(model.getColumnIndex("konsumsi"))).longValue() + "";
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tgl_pendataan + "," + jumlah_prod + "," + wujud_prod
                            + "," + dijual + "," + disimpan + "," + konsumsi + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tgl_pendataan", tgl_pendataan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("dijual", dijual));
                    nameValuePairs.add(new BasicNameValuePair("disimpan", disimpan));
                    nameValuePairs.add(new BasicNameValuePair("konsumsi", konsumsi));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_produksi_perusahaan,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " prod perusahaan");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_produksi_perusahaan", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data.this);
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
                new sentEstLuasKebunPerusahaan().execute();
                Toast.makeText(getApplicationContext(),
                        "Data produksi perusahaan terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data produksi perusahaan gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentEstLuasKebunPetani extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_est_luas_kebun_petani WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
//                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    String jenis_tanaman = model.getString(model.getColumnIndex("jenis_tanaman"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tbm = Double.valueOf(model.getString(model.getColumnIndex("tbm"))).longValue() + "";
                    String tm = Double.valueOf(model.getString(model.getColumnIndex("tm"))).longValue() + "";
                    String ttm = Double.valueOf(model.getString(model.getColumnIndex("ttm"))).longValue() + "";
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tbm + "," + tm + "," + ttm + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
//                    nameValuePairs.add(new BasicNameValuePair("jenis_tanaman", jenis_tanaman));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tbm", tbm));
                    nameValuePairs.add(new BasicNameValuePair("tm", tm));
                    nameValuePairs.add(new BasicNameValuePair("ttm", ttm));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_luas_kebun_petani,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " estimasi luas kebun petani");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_est_luas_kebun_petani", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data.this);
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
                new sentEstProdPetani().execute();
                Toast.makeText(getApplicationContext(),
                        "Data estimasi luas kebun petani terkirim!", Toast.LENGTH_LONG).show();
                new sentEstProdPetani().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data estimasi luas kebun petani gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
            }

            Intent intent = new Intent(list_data.this, MainActivity.class);
            startActivity(intent);
        }
    }

    class sentEstLuasKebunPerusahaan extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_est_luas_kebun_perusahaan WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
//                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    String jenis_tanaman = model.getString(model.getColumnIndex("jenis_tanaman"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tbm_inti = Double.valueOf(model.getString(model.getColumnIndex("tbm_inti"))).longValue() + "";
                    String tm_inti = Double.valueOf(model.getString(model.getColumnIndex("tm_inti"))).longValue() + "";
                    String ttm_inti = Double.valueOf(model.getString(model.getColumnIndex("ttm_inti"))).longValue() + "";
                    String tbm_plasma = Double.valueOf(model.getString(model.getColumnIndex("tbm_plasma"))).longValue() + "";
                    String tm_plasma = Double.valueOf(model.getString(model.getColumnIndex("tm_plasma"))).longValue() + "";
                    String ttm_plasma = Double.valueOf(model.getString(model.getColumnIndex("ttm_plasma"))).longValue() + "";
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tbm_inti + "," + tm_inti + "," + ttm_inti
                            + "," + ttm_inti + "," + tbm_plasma + "," + tm_plasma + "," + ttm_plasma + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
//                    nameValuePairs.add(new BasicNameValuePair("jenis_tanaman", jenis_tanaman));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tbm_inti", tbm_inti));
                    nameValuePairs.add(new BasicNameValuePair("tm_inti", tm_inti));
                    nameValuePairs.add(new BasicNameValuePair("ttm_inti", ttm_inti));
                    nameValuePairs.add(new BasicNameValuePair("tbm_plasma", tbm_plasma));
                    nameValuePairs.add(new BasicNameValuePair("tm_plasma", tm_plasma));
                    nameValuePairs.add(new BasicNameValuePair("ttm_plasma", ttm_plasma));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_luas_kebun_perusahaan,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " estimasi luas kebun perusahaan");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_est_luas_kebun_perusahaan", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data.this);
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
                new sentEstProdPerusahaan().execute();
                Toast.makeText(getApplicationContext(),
                        "Data estimasi luas kebun perusahaan terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data estimasi luas kebun perusahaan gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
            }

            Intent intent = new Intent(list_data.this, MainActivity.class);
            startActivity(intent);
        }
    }

    class sentEstProdPetani extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_est_produksi_kebun_petani WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
//                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tgl_pendataan = model.getString(model.getColumnIndex("tgl_pendataan"));
                    String jumlah_prod = Double.valueOf(model.getString(model.getColumnIndex("jumlah_prod"))).longValue() + "";
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String nilai_jual = Double.valueOf(model.getString(model.getColumnIndex("nilai_jual"))).longValue() + "";
                    String harga = Double.valueOf(model.getString(model.getColumnIndex("harga"))).longValue() + "";
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tgl_pendataan + "," + jumlah_prod + "," + wujud_prod
                            + "," + nilai_jual + "," + harga + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tgl_pendataan", tgl_pendataan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", nilai_jual));
                    nameValuePairs.add(new BasicNameValuePair("harga", harga));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_produksi_pertanian,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " estimasi produksi petani");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_est_produksi_kebun_petani", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data.this);
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
                        "Data estimasi produksi petani terkirim!", Toast.LENGTH_LONG).show();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data estimasi produksi petani gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentEstProdPerusahaan extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_est_produksi_kebun_perusahaan WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
//                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tgl_pendataan = model.getString(model.getColumnIndex("tgl_pendataan"));
                    String jumlah_prod = Double.valueOf(model.getString(model.getColumnIndex("jumlah_prod"))).longValue() + "";
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String nilai_jual = Double.valueOf(model.getString(model.getColumnIndex("nilai_jual"))).longValue() + "";
                    String harga = Double.valueOf(model.getString(model.getColumnIndex("harga"))).longValue() + "";
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tgl_pendataan + "," + jumlah_prod + "," + wujud_prod
                            + "," + nilai_jual + "," + harga + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tgl_pendataan", tgl_pendataan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", nilai_jual));
                    nameValuePairs.add(new BasicNameValuePair("harga", harga));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_produksi_perusahaan,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " estimasi produksi perusahaan");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_est_produksi_kebun_perusahaan", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data.this);
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
                        "Data estimasi produksi perusahaan terkirim!", Toast.LENGTH_LONG).show();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data estimasi produksi perusahaan gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

}