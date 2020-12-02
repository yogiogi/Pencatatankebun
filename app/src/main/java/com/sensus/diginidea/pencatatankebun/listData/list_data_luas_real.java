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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yogi on 9/25/2017.
 */

public class list_data_luas_real extends AppCompatActivity {
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private LuasRealViewPagerAdapterView myViewPageAdapter;

    String id_sensus;
    DbHelper dbhelper;
    TextView lbNamaPetani, jml_luas, statusKirim;
    int id_obj;
    String tabelIde, tabel, tabelReal;
    Cursor model = null;
    String st_luas, st_real;
    String kirimapa = "";
    String LINK, RowID;
    int idLuas = 0;
    int cR = 0;
    int cLuasan, cRealisasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_data);

        Bundle extras = getIntent().getExtras();
        dbhelper = new DbHelper(this);

        Log.d("Response", extras + "");
        if (extras != null) {
            id_sensus = getIntent().getExtras().getString("id_sensus");
            Log.d("id_sensus", id_sensus + "");
        }

        tabelIde = "tbl_identitas";
        id_obj = Integer.parseInt(dbhelper.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));
        if (id_obj == 1) {
            tabel = "tbl_luas_kebun_petani";
            tabelReal = "tbl_realisasi_petani";
        } else {
            tabel = "tbl_luas_kebun_perusahaan";
            tabelReal = "tbl_realisasi_perusahaan";
        }
        setTitle("DATA PENCATATAN 1");

        lbNamaPetani = (TextView) findViewById(R.id.lbNamaPetani);
        jml_luas = (TextView) findViewById(R.id.txJml_luas);
        statusKirim = (TextView) findViewById(R.id.txStatusKirim);

        lbNamaPetani.setText(dbhelper.instantSelect("nama", "tbl_identitas", "id_sensus", id_sensus));
        jml_luas.setText(dbhelper.count(tabel + " where id_sensus = '" + id_sensus + "'") + "");

        String st_luas = dbhelper.instantSelect("status", tabel, "id_sensus", id_sensus);
        String st_real = dbhelper.instantSelect("status", tabelReal, "id_sensus", id_sensus);
        cR = dbhelper.count(tabel + " where id_sensus = " + id_sensus + " and status_realisasi = 1");

        idLuas = dbhelper.count(tabel + " where id_sensus = " + id_sensus);


        cLuasan = dbhelper.count(tabel + " where id_sensus = " + id_sensus + " and status = 0");
        cRealisasi = dbhelper.count(tabelReal + " where id_sensus = " + id_sensus + " and status = 0");

        if (st_luas != null && !st_luas.isEmpty() && !st_luas.equals("null")
                && st_real != null && !st_real.isEmpty() && !st_real.equals("null")) {
            if (cLuasan == 0 && cRealisasi == 0) {
                statusKirim.setText("Terkirim");
            } else {
                statusKirim.setText("Belum Terkirim");
                if (idLuas != cR) {
                    statusKirim.setText("Realisasi belum lengkap");
                }
            }
        } else {
            statusKirim.setVisibility(View.GONE);
        }

        list_data_luasan mainFragment = new list_data_luasan();
        mainFragment.onDataPassed(id_sensus);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        fragments = new ArrayList<Fragment>();
        fragments.add(new list_data_luasan());
        fragments.add(new list_data_real_produk());

        myViewPageAdapter = new LuasRealViewPagerAdapterView(getFragmentManager(),
                fragments);
        viewPager.setAdapter(myViewPageAdapter);

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
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
//                kirimData();
//                break;
//        }
//        return false;
//    }

    public void kirimData() {
        if (idLuas != cR) {
            Toast.makeText(this, "ada data komoditas yang masih kosong", Toast.LENGTH_SHORT).show();
        } else {
            new netcheck().execute();
        }
    }

    private class netcheck extends AsyncTask<Long, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(list_data_luas_real.this);
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
                if (cLuasan != 0 & cRealisasi != 0) {
                    new sentnarasumber().execute();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Gagal, Masalah jaringan", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    class sentnarasumber extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;
        String getTimeOn;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_identitas WHERE id_sensus = " + id_sensus + "  and status = 0 ORDER BY create_date", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    id_obj_sensus = model.getString(model.getColumnIndex("id_obj_sensus"));
                    String nama = model.getString(model.getColumnIndex("nama"));
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
//                    if (Integer.parseInt(id_obj_sensus) != 1) {
//                        tgl_catat = "NULL";
//                    }

                    Log.d("query", id_sensus + "," + id_obj + "," + nama + "," + telp + "," + kelompok_tani + "," +
                            kode_propinsi + "," + kode_kabupaten + "," + kode_kecamatan + "," + kode_desa + "," + String.format("%04d", Integer.parseInt(no_urut.toString())) + "," +
                            id_manbun + "," + tgl_catat + "");

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_obj_sensus", id_obj + ""));
                    nameValuePairs.add(new BasicNameValuePair("nama", nama));
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
//                    nameValuePairs.add(new BasicNameValuePair("id_pengawas", "9999"));

                    if (id_obj == 1) {
                        nameValuePairs.add(new BasicNameValuePair("tgl_catat", tgl_catat));
                    }

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json;

                    if (id_obj == 1) {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_identitas,
                                "POST", nameValuePairs);
                    } else {
                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_identitas_perusahaan,
                                "POST", nameValuePairs);
                    }

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " narasumber");
                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_identitas", "status", Integer.parseInt(RowID), 1);
                        } else {
                            gagal(id_sensus);
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
            pDialog = new ProgressDialog(list_data_luas_real.this);
            pDialog.setMessage("Kirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            // super.onPostExecute(result);
            pDialog.dismiss();
            String objek = "";

            if (id_obj == 1) {
                objek = "pekebun";
            } else {
                objek = "PBS/PBN";
            }

            if (Integer.parseInt(result) == 1) {
                if (id_obj == 1) {
                    new sentLuasKebunPetani().execute();
                } else {
                    new sentLuasKebunPerusahaan().execute();
                }
                Toast.makeText(getApplicationContext(),
                        "Data identitas " + objek + " terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data identitas " + objek + " gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data_luas_real.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentRealisasiPetani extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_realisasi_petani WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
//                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String bulan = model.getString(model.getColumnIndex("bulan"));
                    String jumlah_prod = Double.valueOf(model.getString(model.getColumnIndex("jumlah_prod"))).longValue() + "";
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String nilai_jual = Double.valueOf(model.getString(model.getColumnIndex("nilai_jual"))).longValue() + "";
                    String harga = Double.valueOf(model.getString(model.getColumnIndex("harga"))).longValue() + "";
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + bulan + "," + jumlah_prod + "," + nilai_jual + "," + harga + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("bulan", bulan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", nilai_jual));
                    nameValuePairs.add(new BasicNameValuePair("harga", harga));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_realisasi_petani,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " realisasi petani");

                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_realisasi_petani", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data_luas_real.this);
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
                        "Data realisasi petani terkirim!", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data realisasi petani gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data_luas_real.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentRealisasiPerusahaan extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_realisasi_perusahaan WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String bulan = model.getString(model.getColumnIndex("bulan"));
                    String jumlah_prod = Double.valueOf(model.getString(model.getColumnIndex("jumlah_prod"))).longValue() + "";
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
                    String nilai_jual = Double.valueOf(model.getString(model.getColumnIndex("nilai_jual"))).longValue() + "";
                    String harga = model.getString(model.getColumnIndex("harga"));
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + bulan + "," + jumlah_prod + "," + nilai_jual + "," + harga + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("bulan", bulan));
                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", jumlah_prod));
                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", nilai_jual));
                    nameValuePairs.add(new BasicNameValuePair("harga", harga));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_realisasi_perusahaan,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " realisasi peusahaan");
                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_realisasi_perusahaan", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data_luas_real.this);
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
                        "Data realisasi perusahaan terkirim!", Toast.LENGTH_LONG).show();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data realisasi perusahaan gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data_luas_real.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentLuasKebunPetani extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_luas_kebun_petani WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    String jenis_tanaman = model.getString(model.getColumnIndex("jenis_tanaman"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tbm = model.getDouble(model.getColumnIndex("tbm")) + "";
                    String tm = model.getDouble(model.getColumnIndex("tm")) + "";
                    String ttm = model.getDouble(model.getColumnIndex("ttm")) + "";
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
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_luas_kebun_petani,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " luas kebun petani");
                        if (Integer.parseInt(success) == 1) {
                            dbhelper.updateStatus("tbl_luas_kebun_petani", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data_luas_real.this);
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
                new sentRealisasiPetani().execute();
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun petani terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun petani gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data_luas_real.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    class sentLuasKebunPerusahaan extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbhelper.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_luas_kebun_perusahaan WHERE id_sensus = " + id_sensus + "", null);

            if (model.moveToFirst()) {
                do {
                    RowID = model.getString(model.getColumnIndex("id"));
                    id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    String jenis_tanaman = model.getString(model.getColumnIndex("jenis_tanaman"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String tbm_inti = model.getDouble(model.getColumnIndex("tbm_inti")) + "";
                    String tm_inti = model.getDouble(model.getColumnIndex("tm_inti")) + "";
                    String ttm_inti = model.getDouble(model.getColumnIndex("ttm_inti")) + "";
                    String tbm_plasma = model.getDouble(model.getColumnIndex("tbm_plasma")) + "";
                    String tm_plasma = model.getDouble(model.getColumnIndex("tm_plasma")) + "";
                    String ttm_plasma = model.getDouble(model.getColumnIndex("ttm_plasma")) + "";
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tbm_inti + "," + tm_inti + "," + ttm_inti
                            + "," + tbm_plasma + "," + tm_plasma + "," + ttm_plasma + "," + keterangan);

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
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_luas_kebun_perusahaan,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " luas kebun perusahaan");

                        dbhelper.updateStatus("tbl_luas_kebun_perusahaan", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(list_data_luas_real.this);
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
                        "Data luas kebun perusahaan terkirim!", Toast.LENGTH_LONG).show();
                new sentRealisasiPerusahaan().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun perusahaan gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(list_data_luas_real.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    void gagal(String idSENS) {
        dbhelper.updatebyid_sensus("tbl_identitas", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_est_luas_kebun_petani", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_est_luas_kebun_perusahaan", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_est_produksi_kebun_produksi", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_est_produksi_kebun_perusahaan", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_luas_kebun_perusahaan", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_luas_kebun_petani", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_produksi_perusahaan", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_produksi_petani", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_realisasi_perusahaan", "status", idSENS, 0);
        dbhelper.updatebyid_sensus("tbl_realisasi_petani", "status", idSENS, 0);
    }
}
