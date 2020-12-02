package com.sensus.diginidea.pencatatankebun;

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
import com.sensus.diginidea.pencatatankebun.edit.real_produk_perbulan;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class real_menu_list extends AppCompatActivity {
    //    String[] daftar;
    ListView ListView01;
    TextView txJml_luas, txPemilik;
    Menu menu;
    protected Cursor cursor;
    DbHelper dbcenter;
    String id_sensus;
    int id_obj;
    String tabel;
    SQLiteDatabase db;
    int cKmoditas;
    Cursor model = null;
    String LINK;

    private ArrayList<String> ID_ArrayList = new ArrayList<String>();
    private ArrayList<String> NAME_ArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_menu_list);

        txJml_luas = (TextView) findViewById(R.id.txJml_luas);
        ListView01 = (ListView) findViewById(R.id.lsView);
        ListView01.setScrollingCacheEnabled(false);

        txPemilik = (TextView) findViewById(R.id.txPemilik);
        dbcenter = new DbHelper(this);
        LINK = dbcenter.instantSelect("setURL", "tb_setting", "id_set", "1");

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            Log.i("Value", extras.getString("id_sensus"));
            Log.d("Response", extras + "");

            id_sensus = extras.getString("id_sensus");
            Log.d("id_sensus real menu", id_sensus + "");
        }
        id_obj = Integer.parseInt(dbcenter.instantSelect("id_obj_sensus", "tbl_identitas", "id_sensus", id_sensus));
        txPemilik.setText(dbcenter.instantSelect("nama", "tbl_identitas", "id_sensus", id_sensus));

        if (id_obj == 1) {
            tabel = "tbl_luas_kebun_petani";
        } else {
            tabel = "tbl_luas_kebun_perusahaan";
        }

        int jmlomoditas = dbcenter.count(tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + "");
        txJml_luas.setText(jmlomoditas + "");

        cKmoditas = dbcenter.count(tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_realisasi = 0");
        if (cKmoditas == 0) {
            Toast.makeText(real_menu_list.this, "Komoditas sudah selesai diinput", Toast.LENGTH_SHORT).show();
            AlertsaveData();
        }

        RefreshList();
    }

    public void AlertsaveData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(real_menu_list.this);
//        alert.setTitle("PERHATIAN");
        alert.setCancelable(false);
        alert.setMessage("KIRIM DATA KE SERVER");
        alert.setPositiveButton("KIRIM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new netcheck().execute();
            }
        });

        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(real_menu_list.this,
                        submenu.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            }
        });
        alert.show();
    }

    public void RefreshList() {
        db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("select a.id_komoditas as id, b.komoditas as komoditas from " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_realisasi = 0", null);
        Log.d("query", "select a.id_komoditas, b.komoditas  from " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_realisasi = 0");

        ID_ArrayList.clear();
        NAME_ArrayList.clear();

        if (cursor.moveToFirst()) {
            do {
                ID_ArrayList.add(cursor.getString(cursor.getColumnIndex("id")));
                NAME_ArrayList.add(cursor.getString(cursor.getColumnIndex("komoditas")));
            } while (cursor.moveToNext());
        }

        SQLiteListAdapterNama ListAdapter = new SQLiteListAdapterNama(real_menu_list.this,
                NAME_ArrayList
        );

        ListView01.setAdapter(ListAdapter);
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                Log.d("dua", arg2 + "");
                cursor = db.rawQuery("select a.id_komoditas, komoditas as id, b.komoditas as komoditas from " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + " and status_realisasi = 0", null);
                cursor.moveToPosition(arg2);
                String selection = dbcenter.getEnumIDnara(cursor);
                Intent i = new Intent(getApplicationContext(), real_produk_perbulan.class);
                i.putExtra("id_sensus", id_sensus);
                i.putExtra("komoditas", selection);
                i.putExtra("wujud_produksi", "");
                i.putExtra("posisi", 1);
                startActivity(i);
            }
        });
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), submenu.class);
        intent.putExtra("id_obj_pencatatan", id_obj);
        startActivity(intent);
    }


    private class netcheck extends AsyncTask<Long, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(real_menu_list.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(false);
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
                new sentnarasumber().execute();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Error in Network Connection", Toast.LENGTH_SHORT)
                        .show();

                Intent intent = new Intent(getApplicationContext(),
                        submenu.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            }
        }
    }

    class sentnarasumber extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success = "0";
        String getTimeOn;
        String nama;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbcenter.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_identitas WHERE id_sensus = " + id_sensus + " ORDER BY create_date and status = 0", null);

            if (model.moveToFirst()) {
                do {
                    String status = model.getString(model.getColumnIndex("status"));

                    if (Integer.parseInt(status) != 1) {
                        String RowID = model.getString(model.getColumnIndex("id"));
                        id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                        nama = model.getString(model.getColumnIndex("nama"));
                        String telp = model.getString(model.getColumnIndex("telp"));

                        String kelompok_tani = model.getString(model.getColumnIndex("kelompok_tani"));
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
//                        nameValuePairs.add(new BasicNameValuePair("id_pengawas", "9999"));

                        if (id_obj == 1) {
                            nameValuePairs.add(new BasicNameValuePair("tgl_catat", tgl_catat));
                        }

                        JSONParser jsonParser = new JSONParser();
                        JSONObject json;

//                        if (id_obj == 1) {
                        json = jsonParser.makeHttpRequest(LINK + dbcenter.url_sen_identitas,
                                "POST", nameValuePairs);
//                        } else {
//                            json = jsonParser.makeHttpRequest(LINK + dbcenter.url_sen_identitas_perusahaan,
//                                    "POST", nameValuePairs);
//                        }

                        try {
                            success = json.getString("success");
                            Log.d("Request Ok", success + " narasumber");
                            if (Integer.parseInt(success) == 1) {
                                dbcenter.updateStatus("tbl_identitas", "status", Integer.parseInt(RowID), 1);

                            } else if (Integer.parseInt(success) == 99) {
                                Toast.makeText(getApplicationContext(),
                                        "ID Sensus " + id_sensus + " dari " + nama + " sudah ada", Toast.LENGTH_LONG)
                                        .show();

                                Intent intent = new Intent(real_menu_list.this,
                                        submenu.class);
                                intent.putExtra("id_obj_pencatatan", id_obj);
                                startActivity(intent);
                            }
                        } catch (Exception z) {
                            z.printStackTrace();
                        }
                    }
                } while (model.moveToNext());
            }

            return success;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(real_menu_list.this);
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
            } else if (Integer.parseInt(result) == 99) {
                Toast.makeText(getApplicationContext(),
                        "ID Sensus " + id_sensus + " dari " + nama + " sudah ada", Toast.LENGTH_LONG)
                        .show();

                Toast.makeText(getApplicationContext(),
                        "Silahkan cek ulang", Toast.LENGTH_SHORT)
                        .show();

                Intent intent = new Intent(getApplicationContext(),
                        submenu.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data identitas " + objek + " gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(getApplicationContext(),
                        submenu.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            }
        }
    }

    class sentLuasKebunPetani extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbcenter.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_luas_kebun_petani WHERE id_sensus = " + id_sensus + " and status = 0", null);

            if (model.moveToFirst()) {
                do {
                    String RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    Double tbm = model.getDouble(model.getColumnIndex("tbm"));
                    Double tm = model.getDouble(model.getColumnIndex("tm"));
                    Double ttm = model.getDouble(model.getColumnIndex("ttm"));
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tbm + "," + tm + "," + ttm + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tbm", tbm.toString()));
                    nameValuePairs.add(new BasicNameValuePair("tm", tm.toString()));
                    nameValuePairs.add(new BasicNameValuePair("ttm", ttm.toString()));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbcenter.url_sen_luas_kebun_petani,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " luas kebun petani");
                        if (Integer.parseInt(success) == 1) {
                            dbcenter.updateStatus("tbl_luas_kebun_petani", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(real_menu_list.this);
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
                        "Data luas kebun pekebun terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun pekebun gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(getApplicationContext(),
                        submenu.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            }
        }
    }

    class sentLuasKebunPerusahaan extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbcenter.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_luas_kebun_perusahaan WHERE id_sensus = " + id_sensus + " and status = 0", null);

            if (model.moveToFirst()) {
                do {
                    String RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    Double tbm_inti = model.getDouble(model.getColumnIndex("tbm_inti"));
                    Double tm_inti = model.getDouble(model.getColumnIndex("tm_inti"));
                    Double ttm_inti = model.getDouble(model.getColumnIndex("ttm_inti"));
                    Double tbm_plasma = model.getDouble(model.getColumnIndex("tbm_plasma"));
                    Double tm_plasma = model.getDouble(model.getColumnIndex("tm_plasma"));
                    Double ttm_plasma = model.getDouble(model.getColumnIndex("ttm_plasma"));
                    String keterangan = model.getString(model.getColumnIndex("keterangan"));

                    Log.d("query", id_sensus + "," + id_komoditas + "," + tbm_inti + "," + tm_inti + "," + ttm_inti
                            + "," + tbm_plasma + "," + tm_plasma + "," + ttm_plasma + "," + keterangan);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
                    nameValuePairs.add(new BasicNameValuePair("tbm_inti", tbm_inti.toString()));
                    nameValuePairs.add(new BasicNameValuePair("tm_inti", tm_inti.toString()));
                    nameValuePairs.add(new BasicNameValuePair("ttm_inti", ttm_inti.toString()));
                    nameValuePairs.add(new BasicNameValuePair("tbm_plasma", tbm_plasma.toString()));
                    nameValuePairs.add(new BasicNameValuePair("tm_plasma", tm_plasma.toString()));
                    nameValuePairs.add(new BasicNameValuePair("ttm_plasma", ttm_plasma.toString()));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbcenter.url_sen_luas_kebun_perusahaan,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " luas kebun perusahaan");

                        dbcenter.updateStatus("tbl_luas_kebun_perusahaan", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(real_menu_list.this);
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
                new sentRealisasiPerusaahaan().execute();
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun perusahaan terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data luas kebun perusahaan gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
                Intent intent = new Intent(getApplicationContext(),
                        submenu.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            }
        }
    }

    class sentRealisasiPetani extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbcenter.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_realisasi_petani WHERE id_sensus = " + id_sensus + "  and status = 0", null);

            if (model.moveToFirst()) {
                do {
                    String RowID = model.getString(model.getColumnIndex("id"));
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
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbcenter.url_sen_realisasi_petani,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " realisasi petani");

                        if (Integer.parseInt(success) == 1) {
                            dbcenter.updateStatus("tbl_realisasi_petani", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(real_menu_list.this);
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
                        "Data pencatatan 1 pekebun terkirim!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data realisasi pekebun gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
            }
            Intent intent = new Intent(getApplicationContext(),
                    submenu.class);
            intent.putExtra("id_obj_pencatatan", id_obj);
            startActivity(intent);
        }
    }

    class sentRealisasiPerusaahaan extends AsyncTask<Object, Void, String> {
        ProgressDialog pDialog;
        String success;

        @Override
        protected String doInBackground(Object... objects) {
            SQLiteDatabase db = dbcenter.getWritableDatabase();
            model = db.rawQuery("SELECT * FROM tbl_realisasi_perusahaan WHERE id_sensus = " + id_sensus + " and status = 0", null);

            if (model.moveToFirst()) {
                do {
                    String RowID = model.getString(model.getColumnIndex("id"));
                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
                    String bulan = model.getString(model.getColumnIndex("bulan"));
                    Double jumlah_prod = model.getDouble(model.getColumnIndex("jumlah_prod"));
                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
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
                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", nilai_jual.toString()));
                    nameValuePairs.add(new BasicNameValuePair("harga", harga.toString()));
                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));

                    JSONParser jsonParser = new JSONParser();
                    JSONObject json = jsonParser.makeHttpRequest(LINK + dbcenter.url_sen_realisasi_perusahaan,
                            "POST", nameValuePairs);

                    try {
                        success = json.getString("success");
                        Log.d("Request Ok", success + " realisasi peusahaan");
                        if (Integer.parseInt(success) == 1) {
                            dbcenter.updateStatus("tbl_realisasi_perusahaan", "status", Integer.parseInt(RowID), 1);
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
            pDialog = new ProgressDialog(real_menu_list.this);
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
            } else {
                Toast.makeText(getApplicationContext(),
                        "Data realisasi perusahaan gagal terkirim", Toast.LENGTH_SHORT)
                        .show();
            }

            Intent intent = new Intent(getApplicationContext(),
                    submenu.class);
            intent.putExtra("id_obj_pencatatan", id_obj);
            startActivity(intent);
        }
    }


}