package com.sensus.diginidea.pencatatankebun;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;
import com.sensus.diginidea.pencatatankebun.edit.est_luas_areal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    Button pekebun, perusahaan;
    DbHelper db;
    private static final String JSON_WUJUDPRODUKSI = "http://smallholder.inobu.org/sen_wujud.php";
    private static final String JSON_KOMODITAS = "http://smallholder.inobu.org/sen_get.php";
    private static final String JSON_KABKOT = "http://smallholder.inobu.org/sen_get_kabkot.php";
    private static final String JSON_KECAMATAN = "http://smallholder.inobu.org/sen_get_kecamatan.php";
    private static final String JSON_DESA = "http://smallholder.inobu.org/sen_get_desa.php";
    private static final String JSON_PROPINSI = "http://smallholder.inobu.org/sen_get_propinsi.php";
    private static final String JSON_MANBUN = "http://smallholder.inobu.org/sen_get_manbun.php";
    String status = null;
    boolean berhasil = false;
    String outFileName;

    int cKomoditas = 0;
    int cDesa = 0;
    int cKecamatan = 0;
    int cManBun = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
//    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

//        String intStorageDirectory = getFilesDir().toString();
//        File folder = new File(intStorageDirectory, "sensusTani");
//        folder.mkdirs();

        File database = getApplicationContext().getDatabasePath(
                "sensus_kebun.db");

        db = new DbHelper(this);
        if (!database.exists()) {
            unduhPertamaData();
        }

        pekebun = (Button) findViewById(R.id.btPekebun);
        perusahaan = (Button) findViewById(R.id.btPerusahaan);

        cKomoditas = db.count("mst_komoditas");
        cDesa = db.count("mst_desa");
        cKecamatan = db.count("mst_kecamatan");
        cManBun = db.count("mst_manbun");

        pekebun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (cKomoditas != 0 && cDesa != 0 && cKecamatan != 0 && cManBun != 0) {
                Intent intent = null;
                intent = new Intent(MainActivity.this, submenu.class);
                intent.putExtra("id_obj_pencatatan", 1);
                startActivity(intent);
//                } else {
//                    Toast.makeText(MainActivity.this, "Data belum lengkap terunduh", Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "Tidak bisa masuk", Toast.LENGTH_LONG).show();
//                }
            }
        });

        perusahaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (cKomoditas != 0 && cDesa != 0 && cKecamatan != 0 && cManBun != 0) {
                Intent intent = null;
                intent = new Intent(MainActivity.this, submenu.class);
                intent.putExtra("id_obj_pencatatan", 2);
                startActivity(intent);
//                } else {
//
//       Toast.makeText(MainActivity.this, "Data belum lengkap terunduh", Toast.LENGTH_LONG).show();
//                    Toast.makeText(MainActivity.this, "Tidak bisa masuk", Toast.LENGTH_LONG).show();
//                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.unduh:
                new netcheck().execute();
                break;
            case R.id.tambah:
//                Intent intent = null;
//                intent = new Intent(MainActivity.this, backup.class);
//                startActivity(intent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }

                final String inFileName = "/data/data/com.sensus.diginidea.pencatatankebun/databases/sensus_kebun.db";
                File dbFile = new File(inFileName);
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(dbFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                outFileName = Environment.getExternalStorageDirectory() + "/sensus_kebun.db";

                // Open the empty db as the output stream
                OutputStream output = null;
                try {
                    output = new FileOutputStream(outFileName);

                    // Transfer bytes from the inputfile to the outputfile
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        output.write(buffer, 0, length);
                    }

                    // Close the streams
                    output.flush();
                    output.close();
                    fis.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, "backup :/sensus_kebun.db berhasil", Toast.LENGTH_LONG).show();
                goListData();
                break;
        }
        return false;
    }

    public void goListData() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Perhatian");
        alert.setCancelable(false);
        alert.setMessage("Backup berhasil, lihat file backup sensus_kebun.db ?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(outFileName); // a directory
                intent.setDataAndType(uri, "*/*");
                startActivity(Intent.createChooser(intent, "Open folder"));
            }
        });
        alert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alert.show();
    }

    private class netcheck extends AsyncTask<Long, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(MainActivity.this);
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
                unduhData();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Gagal, Masalah jaringan", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    void unduhData() {
        if (db.count("tb_setting") == 0) {
            db.delete("tb_setting");
            Log.d("delete", "tb_setting");
            db.insertURL();
        }

        if (db.count("mst_jenis_tanaman") == 0) {
            db.delete("mst_jenis_tanaman");
            Log.d("delete", "mst_jenis_tanaman");
            insertJenisTanaman();
        }

        db.delete("mst_komoditas");
        Log.d("delete", "mst_komoditas");
        getJSON(JSON_KOMODITAS);

        db.delete("mst_wujud_produksi");
        Log.d("delete", "mst_wujud_produksi");
        db.insertSatuan("0", "KG");
        db.insertSatuan("2", "KG");
        getJSON(JSON_WUJUDPRODUKSI);


        if (db.count("mst_bulan") == 0) {
            db.delete("mst_bulan");
            Log.d("delete", "mst_bulan");
            insertBulan();
        }

        db.delete("mst_manbun");
        Log.d("delete", "mst_manbun");
        getJSON(JSON_MANBUN);

        if (db.count("mst_kabkot") == 0) {
            db.delete("mst_kabkot");
            Log.d("delete", "mst_kabkot");
            insertKabKot();
        }

        db.delete("mst_kecamatan");
        Log.d("delete", "mst_kecamatan");
        getJSON(JSON_KECAMATAN);

        db.delete("mst_desa");
        Log.d("delete", "mst_desa");
        getJSON(JSON_DESA);
    }

    void unduhPertamaData() {
        if (db.count("tb_setting") == 0) {
            db.delete("tb_setting");
            Log.d("delete", "tb_setting");
            db.insertURL();
        }

        if (db.count("mst_jenis_tanaman") == 0) {
            db.delete("mst_jenis_tanaman");
            Log.d("delete", "mst_jenis_tanaman");
            insertJenisTanaman();
        }

        if (db.count("mst_komoditas") == 0) {
            db.delete("mst_komoditas");
            Log.d("delete", "mst_komoditas");
            insertKomoditas();
            Toast.makeText(getApplicationContext(),
                    "Unduh data Komoditas  berhasil", Toast.LENGTH_SHORT)
                    .show();
        }

        if (db.count("tb_satuan") == 0) {
            db.delete("tb_satuan");
            Log.d("delete", "tb_satuan");
            db.insertSatuan("2", "Butir");
            db.insertSatuan("2", "KG");
            db.insertSatuan("0", "KG");
        }

        if (db.count("mst_wujud_produksi") == 0) {
            db.delete("mst_wujud_produksi");
            Log.d("delete", "mst_twujud_produksi");
            insertWujudProduksi();
            db.updatewujudprod("2", "Buah Kelapa", 0.2);
            db.updatewujudprod("2", "Kopra", 1.0);
            db.updatewujudprod("1", "Lateks", 0.3);
            db.updatewujudprod("1", "Sit Angin", 0.75);
            db.updatewujudprod("1", "Slab Tipis", 0.5);
            db.updatewujudprod("1", "Lump Segar", 0.5);

            Toast.makeText(getApplicationContext(),
                    "Unduh data wujud berhasil", Toast.LENGTH_SHORT)
                    .show();
        }

        if (db.count("mst_bulan") == 0) {
            db.delete("mst_bulan");
            Log.d("delete", "mst_bulan");
            insertBulan();
        }

        if (db.count("mst_manbun") == 0) {
            db.delete("mst_manbun");
            Log.d("delete", "mst_manbun");
            insertManbun();
            Toast.makeText(getApplicationContext(),
                    "Unduh data manbun berhasil", Toast.LENGTH_SHORT)
                    .show();

        }

        if (db.count("mst_kabkot") == 0) {
            db.delete("mst_kabkot");
            Log.d("delete", "mst_kabkot");
            insertKabKot();

            Toast.makeText(getApplicationContext(),
                    "Unduh data kabupaten/kota berhasil", Toast.LENGTH_SHORT)
                    .show();
        }

        if (db.count("mst_kecamatan") == 0) {
            db.delete("mst_kecamatan");
            Log.d("delete", "mst_kecamatan");
            insertKecataman();

            Toast.makeText(getApplicationContext(),
                    "Unduh data kecamatan berhasil", Toast.LENGTH_SHORT)
                    .show();
        }

        if (db.count("mst_desa") == 0) {
            db.delete("mst_desa");
            Log.d("delete", "mst_desa");
            insertDesa();

            Toast.makeText(getApplicationContext(),
                    "Unduh data desa berhasil", Toast.LENGTH_SHORT)
                    .show();
        }
    }

//    void unduhPertamaData() {
//        if (db.count("tb_setting") == 0) {
//            db.delete("tb_setting");
//            Log.d("delete", "tb_setting");
//            db.insertURL();
//        }
//
//        if (db.count("mst_jenis_tanaman") == 0) {
//            db.delete("mst_jenis_tanaman");
//            Log.d("delete", "mst_jenis_tanaman");
//            insertJenisTanaman();
//        }
//
//        if (db.count("mst_komoditas") == 0) {
//            db.delete("mst_komoditas");
//            Log.d("delete", "mst_komoditas");
//            getJSON(JSON_KOMODITAS);
//        }
//
//        if (db.count("mst_wujud_produksi") == 0) {
//            db.delete("mst_wujud_produksi");
//            Log.d("delete", "mst_twujud_produksi");
//            db.insertSatuan("0", "KG");
//            db.insertSatuan("2", "KG");
//            getJSON(JSON_WUJUDPRODUKSI);
//        }
//
//        if (db.count("mst_bulan") == 0) {
//            db.delete("mst_bulan");
//            Log.d("delete", "mst_bulan");
//            insertBulan();
//        }
//
//        if (db.count("mst_manbun") == 0) {
//            db.delete("mst_manbun");
//            Log.d("delete", "mst_manbun");
//            getJSON(JSON_MANBUN);
//        }
//
//        if (db.count("mst_kabkot") == 0) {
//            db.delete("mst_kabkot");
//            Log.d("delete", "mst_kabkot");
//            insertKabKot();
//        }
//
//        if (db.count("mst_kecamatan") == 0) {
//            db.delete("mst_kecamatan");
//            Log.d("delete", "mst_kecamatan");
//            getJSON(JSON_KECAMATAN);
//        }
//
//        if (db.count("mst_desa") == 0) {
//            db.delete("mst_desa");
//            Log.d("delete", "mst_desa");
//            getJSON(JSON_DESA);
//        }
//    }

    public void insertJenisTanaman() {
        db.insertJenisTanaman("TANAMAN TAHUNAN");
        db.insertJenisTanaman("TANAMAN SEMUSIM DAN REMPAH");
    }

    public void insertKabKot() {
        db.insertKab("62", "6201", "01", "KOTAWARINGIN BARAT");
        db.insertKab("62", "6210", "10", "PULANG PISAU");
    }

    public void insertBulan() {
        db.insertbulan(0, "Semua Bulan");
        db.insertbulan(1, "JANUARI");
        db.insertbulan(2, "FEBRUARI");
        db.insertbulan(3, "MARET");
        db.insertbulan(4, "APRIL");
        db.insertbulan(5, "MEI");
        db.insertbulan(6, "JUNI");
        db.insertbulan(7, "JULI");
        db.insertbulan(8, "AGUSTUS");
        db.insertbulan(9, "SEPTEMBER");
    }

    private void getJSON(final String url) {
        class GetJSON extends AsyncTask<String, Void, Boolean> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this, "SILAHKAN TUNGGU...", "MENGUNDUH DATA ", true, false);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                String result = "";
                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    result = sb.toString();

                } catch (Exception e) {
                    return false;
                }

                if (result.length() == 5) {
                } else {
                    //parse json data
                    try {
                        JSONArray jArray = new JSONArray(result);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json_data = jArray.getJSONObject(i);

                            switch (uri) {
                                case JSON_KOMODITAS:
                                    db.insertMstKomoditas(json_data.getString("id_tanaman"), json_data.getString("id_komoditas"), json_data.getString("komoditas"));
                                    status = "KOMODITAS";
                                    if (json_data.getString("komoditas").equals("Kelapa")) {
                                        db.insertSatuan(json_data.getString("id_komoditas"), "Butir");

//                                    } else if (json_data.getString("komoditas").equals("Karet")) {
//                                        db.insertSatuan(json_data.getString("id_komoditas"), 0, "KG");
                                    }
                                    break;
                                case JSON_WUJUDPRODUKSI:
                                    db.insertwujudprod(json_data.getString("id_komoditas"), json_data.getString("wujud_produksi"));
                                    String komoditas = db.instantSelect("komoditas", "mst_komoditas", "id_komoditas", json_data.getString("id_komoditas"));
                                    String id_wujud = db.instantSelect("id_wujud_produksi", "mst_wujud_produksi", "id_wujud_produksi", json_data.getString("wujud_produksi"));
                                    status = "WUJUD PRODUKSI";

                                    if (komoditas.equals("Kelapa")) {
                                        db.updatewujudprod(json_data.getString("id_komoditas"), "Buah Kelapa", 0.2);
                                        db.updatewujudprod(json_data.getString("id_komoditas"), "Kopra", 1.0);
                                    } else if (komoditas.equals("Karet")) {
                                        db.updatewujudprod(json_data.getString("id_komoditas"), "Lateks", 0.3);
                                        db.updatewujudprod(json_data.getString("id_komoditas"), "Sit Angin", 0.75);
                                        db.updatewujudprod(json_data.getString("id_komoditas"), "Slab Tipis", 0.5);
                                        db.updatewujudprod(json_data.getString("id_komoditas"), "Lump Segar", 0.5);
                                    }

                                    break;
                                case JSON_DESA:
                                    db.insertDesa(json_data.getString("kode_kecamatan"), json_data.getString("kode_desa"), json_data.getString("kode_desa2"), json_data.getString("nama_desa"));
                                    status = "DESA";
                                    break;
                                case JSON_KECAMATAN:
                                    db.insertKec(json_data.getString("kode_kabkot"), json_data.getString("kode_kecamatan"), json_data.getString("kode_kecamatan2"), json_data.getString("nama_kecamatan"));
                                    status = "KECAMATAN";
                                    break;
                                case JSON_MANBUN:
                                    db.insertManbun(json_data.getString("id_manbun"), json_data.getString("nama"), json_data.getString("no_telp"), json_data.getString("kode_kecamatan"));
                                    status = "MANBUN";
                                    break;
                            }
                        }

                        return true;
                    } catch (JSONException e) {
                        Log.e("log_tag", "Error parsing data " + e.toString());
                    }
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
                loading.dismiss();

                if (s) {
                    berhasil = true;
                    Toast.makeText(getApplicationContext(),
                            "Unduh data " + status + " berhasil", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    berhasil = false;
                    Toast.makeText(getApplicationContext(),
                            "Unduh data gagal", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute(url);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void insertDesa() {
        db.insertDesa("6201040", "6201040001", "001", "BABUAL BABOTI");
        db.insertDesa("6201040", "6201040002", "002", "TEMPAYUNG");
        db.insertDesa("6201040", "6201040003", "003", "SAKABULIN");
        db.insertDesa("6201040", "6201040004", "004", "KINJIL");
        db.insertDesa("6201040", "6201040005", "005", "KOTAWARINGIN HILIR");
        db.insertDesa("6201040", "6201040006", "006", "RIAM DURIAN");
        db.insertDesa("6201040", "6201040007", "007", "DAWAK");
        db.insertDesa("6201040", "6201040008", "008", "KOTAWARINGIN HULU");
        db.insertDesa("6201040", "6201040009", "009", "LALANG");
        db.insertDesa("6201040", "6201040010", "010", "RUNGUN");
        db.insertDesa("6201040", "6201040011", "011", "KONDANG");
        db.insertDesa("6201040", "6201040012", "012", "SUKAMULYA");
        db.insertDesa("6201040", "6201040013", "013", "SUKAJAYA");
        db.insertDesa("6201040", "6201040015", "015", "SUKA MAKMUR");
        db.insertDesa("6201040", "6201040018", "018", "IPUH BANGUN JAYA");
        db.insertDesa("6201040", "6201040019", "019", "SUMBER MUKTI");
        db.insertDesa("6201040", "6201040020", "020", "PALIH BARU");
        db.insertDesa("6201050", "6201050001", "001", "TANJUNG PUTRI");
        db.insertDesa("6201050", "6201050002", "002", "KUMPAI BATU BAWAH");
        db.insertDesa("6201050", "6201050003", "003", "KUMPAI BATU ATAS");
        db.insertDesa("6201050", "6201050004", "004", "PASIR PANJANG");
        db.insertDesa("6201050", "6201050005", "005", "MENDAWAI");
        db.insertDesa("6201050", "6201050006", "006", "MENDAWAI SEBERANG");
        db.insertDesa("6201050", "6201050007", "007", "RAJA");
        db.insertDesa("6201050", "6201050008", "008", "SIDOREJO");
        db.insertDesa("6201050", "6201050009", "009", "MADUREJO");
        db.insertDesa("6201050", "6201050010", "010", "BARU");
        db.insertDesa("6201050", "6201050011", "011", "RAJA SEBERANG");
        db.insertDesa("6201050", "6201050012", "012", "RANGDA");
        db.insertDesa("6201050", "6201050013", "013", "KENAMBUI");
        db.insertDesa("6201050", "6201050014", "014", "RUNTU");
        db.insertDesa("6201050", "6201050015", "015", "UMPANG");
        db.insertDesa("6201050", "6201050016", "016", "NATAI RAYA");
        db.insertDesa("6201050", "6201050017", "017", "MEDANGSARI");
        db.insertDesa("6201050", "6201050018", "018", "NATAI BARU");
        db.insertDesa("6201050", "6201050019", "019", "TANJUNG TERANTANG");
        db.insertDesa("6201050", "6201050020", "020", "SULUNG");
        db.insertDesa("6201060", "6201060001", "001", "SUNGAI CABANG");
        db.insertDesa("6201060", "6201060002", "002", "TELUK PULAI");
        db.insertDesa("6201060", "6201060003", "003", "SUNGAI SEKONYER");
        db.insertDesa("6201060", "6201060004", "004", "KUBU");
        db.insertDesa("6201060", "6201060005", "005", "SUNGAI BAKAU");
        db.insertDesa("6201060", "6201060006", "006", "TELUK BOGAM");
        db.insertDesa("6201060", "6201060007", "007", "KERAYA");
        db.insertDesa("6201060", "6201060008", "008", "SEBUAI");
        db.insertDesa("6201060", "6201060009", "009", "SUNGAI KAPITAN");
        db.insertDesa("6201060", "6201060010", "010", "KUMAI HILIR");
        db.insertDesa("6201060", "6201060011", "011", "BATU BELAMAN");
        db.insertDesa("6201060", "6201060012", "012", "SUNGAI TENDANG");
        db.insertDesa("6201060", "6201060013", "013", "CANDI");
        db.insertDesa("6201060", "6201060014", "014", "KUMAI HULU");
        db.insertDesa("6201060", "6201060015", "015", "SUNGAI BEDAUN");
        db.insertDesa("6201060", "6201060016", "016", "SABUAI TIMUR");
        db.insertDesa("6201060", "6201060031", "031", "BUMI HARJO");
        db.insertDesa("6201060", "6201060032", "032", "PANGKALAN SATU");
        db.insertDesa("6201061", "6201061001", "001", "PANGKALAN BANTENG");
        db.insertDesa("6201061", "6201061002", "002", "MULYA JADI");
        db.insertDesa("6201061", "6201061003", "003", "AMIN JAYA");
        db.insertDesa("6201061", "6201061004", "004", "NATAI KERBAU");
        db.insertDesa("6201061", "6201061005", "005", "KARANGMULYA");
        db.insertDesa("6201061", "6201061006", "006", "MARGA MULYA");
        db.insertDesa("6201061", "6201061007", "007", "ARGA MULYA");
        db.insertDesa("6201061", "6201061008", "008", "KEBUN AGUNG");
        db.insertDesa("6201061", "6201061009", "009", "SIDO MULYO");
        db.insertDesa("6201061", "6201061010", "010", "SIMPANG BERAMBAI");
        db.insertDesa("6201061", "6201061011", "011", "SUNGAI HIJAU");
        db.insertDesa("6201061", "6201061012", "012", "SUNGAI BENGKUANG");
        db.insertDesa("6201061", "6201061013", "013", "SUNGAI KUNING");
        db.insertDesa("6201061", "6201061014", "014", "SUNGAI PAKIT");
        db.insertDesa("6201061", "6201061015", "015", "BERAMBAI MAKMUR");
        db.insertDesa("6201061", "6201061016", "016", "KARANG SARI");
        db.insertDesa("6201061", "6201061017", "017", "SUNGAI PULAU");
        db.insertDesa("6201062", "6201062001", "001", "PURBASARI");
        db.insertDesa("6201062", "6201062002", "002", "SUNGAI RANGIT JAYA");
        db.insertDesa("6201062", "6201062003", "003", "SUMBER AGUNG");
        db.insertDesa("6201062", "6201062004", "004", "LADA MANDALA JAYA");
        db.insertDesa("6201062", "6201062005", "005", "MAKARTI JAYA");
        db.insertDesa("6201062", "6201062006", "006", "PANDU SENJAYA");
        db.insertDesa("6201062", "6201062007", "007", "PANGKALAN TIGA");
        db.insertDesa("6201062", "6201062008", "008", "KADIPI ATAS");
        db.insertDesa("6201062", "6201062009", "009", "PANGKALAN DEWA");
        db.insertDesa("6201062", "6201062010", "010", "PANGKALAN DURIN");
        db.insertDesa("6201062", "6201062011", "011", "SUNGAI MELAWEN");
        db.insertDesa("6201070", "6201070001", "001", "NANGA MUA");
        db.insertDesa("6201070", "6201070002", "002", "PANGKUT");
        db.insertDesa("6201070", "6201070003", "003", "SUKARAMI");
        db.insertDesa("6201070", "6201070004", "004", "GANDIS");
        db.insertDesa("6201070", "6201070005", "005", "KERABU");
        db.insertDesa("6201070", "6201070006", "006", "SAMBI");
        db.insertDesa("6201070", "6201070007", "007", "PENYOMBAAN");
        db.insertDesa("6201070", "6201070008", "008", "PANDAU");
        db.insertDesa("6201070", "6201070009", "009", "RIAM");
        db.insertDesa("6201070", "6201070010", "010", "PENAHAN");
        db.insertDesa("6201070", "6201070011", "011", "SUNGAI DAU");
        db.insertDesa("6210010", "6210010001", "001", "CEMANTAN");
        db.insertDesa("6210010", "6210010002", "002", "PAPUYU III SEI PUDAK");
        db.insertDesa("6210010", "6210010003", "003", "KIAPAK");
        db.insertDesa("6210010", "6210010004", "004", "PAPUYU II SEI BARUNAI");
        db.insertDesa("6210010", "6210010005", "005", "PAPUYU I SEI PASANAN");
        db.insertDesa("6210010", "6210010006", "006", "SEI RUNGUN");
        db.insertDesa("6210010", "6210010007", "007", "BAHAUR HILIR");
        db.insertDesa("6210010", "6210010008", "008", "BAHAUR TENGAH");
        db.insertDesa("6210010", "6210010009", "009", "BAHAUR HULU");
        db.insertDesa("6210010", "6210010010", "010", "TANJUNG PERAWAN");
        db.insertDesa("6210010", "6210010011", "011", "BAHAUR BATU RAYA");
        db.insertDesa("6210010", "6210010012", "012", "BAHAUR HULU PERMAI");
        db.insertDesa("6210010", "6210010013", "013", "BAHAUR BASANTAN");
        db.insertDesa("6210011", "6210011001", "001", "PADURAN SEBANGAU");
        db.insertDesa("6210011", "6210011002", "002", "PADURAN MULYA");
        db.insertDesa("6210011", "6210011003", "003", "MEKAR JAYA");
        db.insertDesa("6210011", "6210011004", "004", "SEBANGAU PERMAI");
        db.insertDesa("6210011", "6210011005", "005", "SEBANGAU JAYA");
        db.insertDesa("6210011", "6210011006", "006", "SEBANGAU MULYA");
        db.insertDesa("6210011", "6210011007", "007", "SEI BAKAU");
        db.insertDesa("6210011", "6210011008", "008", "SEI HAMBAWANG");
        db.insertDesa("6210020", "6210020001", "001", "DANDANG");
        db.insertDesa("6210020", "6210020002", "002", "TALIO");
        db.insertDesa("6210020", "6210020003", "003", "GADABUNG");
        db.insertDesa("6210020", "6210020004", "004", "BELANTI SIAM");
        db.insertDesa("6210020", "6210020005", "005", "PANGKOH HILIR");
        db.insertDesa("6210020", "6210020006", "006", "TALIO MUARA");
        db.insertDesa("6210020", "6210020007", "007", "TALIO HULU");
        db.insertDesa("6210020", "6210020008", "008", "PANGKOH SARI");
        db.insertDesa("6210020", "6210020009", "009", "KANTAN MUARA");
        db.insertDesa("6210020", "6210020010", "010", "PANGKOH HULU");
        db.insertDesa("6210020", "6210020011", "011", "SANGGANG");
        db.insertDesa("6210020", "6210020012", "012", "PANTIK");
        db.insertDesa("6210020", "6210020013", "013", "MULYASARI");
        db.insertDesa("6210020", "6210020014", "014", "KANTAN DALAM");
        db.insertDesa("6210020", "6210020015", "015", "KANTAN ATAS");
        db.insertDesa("6210020", "6210020016", "016", "KARYA BERSAMA");
        db.insertDesa("6210030", "6210030001", "001", "GANDANG");
        db.insertDesa("6210030", "6210030002", "002", "GARANTUNG");
        db.insertDesa("6210030", "6210030003", "003", "MALIKU BARU");
        db.insertDesa("6210030", "6210030004", "004", "BADIRIH");
        db.insertDesa("6210030", "6210030005", "005", "TAHAI JAYA");
        db.insertDesa("6210030", "6210030006", "006", "TAHAI BARU");
        db.insertDesa("6210030", "6210030007", "007", "KANAMIT");
        db.insertDesa("6210030", "6210030008", "008", "PURWODADI");
        db.insertDesa("6210030", "6210030009", "009", "WONO AGUNG");
        db.insertDesa("6210030", "6210030010", "010", "KANAMIT BARAT");
        db.insertDesa("6210030", "6210030011", "011", "SEI BARU TEWU");
        db.insertDesa("6210030", "6210030012", "012", "SIDODADI");
        db.insertDesa("6210030", "6210030013", "013", "KANAMIT JAYA");
        db.insertDesa("6210030", "6210030014", "014", "GANDANG BARAT");
        db.insertDesa("6210030", "6210030015", "15", "MALIKU MULYA");
        db.insertDesa("6210040", "6210040001", "001", "BUNTOI");
        db.insertDesa("6210040", "6210040002", "002", "MINTIN");
        db.insertDesa("6210040", "6210040003", "003", "MANTAREN II");
        db.insertDesa("6210040", "6210040004", "004", "MANTAREN I");
        db.insertDesa("6210040", "6210040005", "005", "PULANG PISAU");
        db.insertDesa("6210040", "6210040006", "006", "ANJIR PULANG PISAU");
        db.insertDesa("6210040", "6210040007", "007", "GOHONG");
        db.insertDesa("6210040", "6210040008", "008", "KALAWA");
        db.insertDesa("6210040", "6210040009", "009", "HANJAK MAJU");
        db.insertDesa("6210040", "6210040010", "010", "BERENG");
        db.insertDesa("6210041", "6210041001", "001", "GARUNG");
        db.insertDesa("6210041", "6210041002", "002", "HENDA");
        db.insertDesa("6210041", "6210041003", "003", "SIMPUR");
        db.insertDesa("6210041", "6210041004", "004", "SAKAKAJANG");
        db.insertDesa("6210041", "6210041005", "005", "JABIREN");
        db.insertDesa("6210041", "6210041006", "006", "PILANG");
        db.insertDesa("6210041", "6210041007", "007", "TUMBANG NUSA");
        db.insertDesa("6210041", "6210041008", "008", "TANJUNG TARUNA");
        db.insertDesa("6210050", "6210050001", "001", "TANJUNG SANGGALANG");
        db.insertDesa("6210050", "6210050002", "002", "PENDA BARANIA");
        db.insertDesa("6210050", "6210050003", "003", "BUKIT RAWI");
        db.insertDesa("6210050", "6210050004", "004", "TUWUNG");
        db.insertDesa("6210050", "6210050005", "005", "SIGI");
        db.insertDesa("6210050", "6210050006", "006", "PETUK LITI");
        db.insertDesa("6210050", "6210050007", "007", "BUKIT LITI");
        db.insertDesa("6210050", "6210050008", "008", "BAHU PALAWA");
        db.insertDesa("6210050", "6210050009", "009", "PAMARUNAN");
        db.insertDesa("6210050", "6210050010", "010", "BALUKON");
        db.insertDesa("6210050", "6210050011", "011", "BUKIT BAMBA");
        db.insertDesa("6210050", "6210050012", "012", "TAHAWA");
        db.insertDesa("6210050", "6210050013", "013", "PARAHANGAN");
        db.insertDesa("6210050", "6210050014", "014", "BERENG RAMBANG");
        db.insertDesa("6210060", "6210060001", "001", "MANEN PADURAN");
        db.insertDesa("6210060", "6210060002", "002", "MANEN KALEKA");
        db.insertDesa("6210060", "6210060003", "003", "LAWANG URU");
        db.insertDesa("6210060", "6210060004", "004", "HURUNG");
        db.insertDesa("6210060", "6210060005", "005", "HANUA");
        db.insertDesa("6210060", "6210060006", "006", "RAMANG");
        db.insertDesa("6210060", "6210060007", "007", "TAMBAK");
        db.insertDesa("6210060", "6210060008", "008", "PAHAWAN");
        db.insertDesa("6210060", "6210060009", "009", "GOHA");
        db.insertDesa("6210060", "6210060010", "010", "BAWAN");
        db.insertDesa("6210060", "6210060011", "011", "TUMBANG TARUSAN");
        db.insertDesa("6210060", "6210060012", "012", "PANDAWEI");
        db.insertDesa("6210060", "6210060013", "013", "PANGI");
        db.insertDesa("6210060", "6210060014", "014", "TANGKAHEN");
        db.insertDesa("6210060", "6210060015", "015", "KASALI BARU");
    }//private static final String JSON_DESA = "http://smallholder.inobu.org/sen_get_desa.php";

    //    private static final String JSON_WUJUDPRODUKSI = "http://smallholder.inobu.org/sen_wujud.php";
    public void insertWujudProduksi() {
        db.insertwujudprod("1", "Lateks");
        db.insertwujudprod("2", "Buah Kelapa");
        db.insertwujudprod("3", "Buah Basah");
        db.insertwujudprod("4", "Bunga Basah");
        db.insertwujudprod("5", "Lada Kering");
        db.insertwujudprod("6", "Gelondong Mete");
        db.insertwujudprod("7", "TBS");
        db.insertwujudprod("8", "Buah Basah");
        db.insertwujudprod("9", "Nira");
        db.insertwujudprod("10", "Daun Basah");
        db.insertwujudprod("1", "Sit Angin");
        db.insertwujudprod("1", "Slab Tipis");
        db.insertwujudprod("1", "Lump Segar");
        db.insertwujudprod("2", "Kopra");
        db.insertwujudprod("3", "Kopi Beras");
        db.insertwujudprod("4", "Bunga Kering");
        db.insertwujudprod("5", "Lada Basah");
        db.insertwujudprod("5", "Lada Kering");
        db.insertwujudprod("5", "Lada Hitam");
        db.insertwujudprod("5", "Lada Putih");
        db.insertwujudprod("7", "Minyak Sawit (CPO)");
        db.insertwujudprod("7", "Inti Sawit (KPO)");
        db.insertwujudprod("8", "Biji Kering");
        db.insertwujudprod("9", "Gula Merah");
        db.insertwujudprod("10", "Minyak Nilam");
        db.insertwujudprod("12", "Buah Kering");
        db.insertwujudprod("12", "Serat Berbiji");
        db.insertwujudprod("14", "Buah");
        db.insertwujudprod("14", "Inti Biji");
        db.insertwujudprod("15", "Nira");
        db.insertwujudprod("15", "Gula Merah");
        db.insertwujudprod("16", "Buah Basah");
        db.insertwujudprod("16", "Biji Kering");
        db.insertwujudprod("17", "Batang Sagu");
        db.insertwujudprod("17", "Tepung Sagu");
        db.insertwujudprod("18", "Kapas Berbiji");
        db.insertwujudprod("18", "Serat");
        db.insertwujudprod("19", "Daun Basah");
        db.insertwujudprod("19", "Minyak Sereh Wangi");
        db.insertwujudprod("20", "Batang");
        db.insertwujudprod("20", "Hablur");
        db.insertwujudprod("21", "Daun Basah");
        db.insertwujudprod("21", "Kerosok/Daun Kering");
        db.insertwujudprod("22", "Buah Segar");
        db.insertwujudprod("23", "Buah Basah");
        db.insertwujudprod("24", "Kulit Basah");
        db.insertwujudprod("24", "Kulit kering");
        db.insertwujudprod("25", "Daun Basah");
        db.insertwujudprod("25", "Daun Kering");
        db.insertwujudprod("6", "Kacang Mete");
        db.insertwujudprod("1", "Kadar Karet Kering");
    }

    //    private static final String JSON_KOMODITAS = "http://smallholder.inobu.org/sen_get.php";
    public void insertKomoditas() {
        db.insertMstKomoditas("1", "1", "Karet");
        db.insertMstKomoditas("1", "2", "Kelapa");
        db.insertMstKomoditas("1", "3", "Kopi");
        db.insertMstKomoditas("1", "4", "Cengkeh");
        db.insertMstKomoditas("1", "5", "Lada");
        db.insertMstKomoditas("1", "6", "Jambu Mete");
        db.insertMstKomoditas("1", "7", "Kelapa Sawit");
        db.insertMstKomoditas("1", "8", "Coklat/Kakao");
        db.insertMstKomoditas("1", "9", "Aren");
        db.insertMstKomoditas("2", "10", "Nilam");
        db.insertMstKomoditas("1", "12", "Kapuk");
        db.insertMstKomoditas("1", "14", "Kemiri Sunan");
        db.insertMstKomoditas("1", "15", "Lontar/Siwalan");
        db.insertMstKomoditas("1", "16", "Pinang");
        db.insertMstKomoditas("1", "17", "Sagu");
        db.insertMstKomoditas("2", "18", "Kapas");
        db.insertMstKomoditas("2", "19", "Sereh Wangi");
        db.insertMstKomoditas("2", "20", "Tebu");
        db.insertMstKomoditas("2", "21", "Tembakau");
        db.insertMstKomoditas("1", "22", "Asam Jawa");
        db.insertMstKomoditas("1", "23", "Cabe Jamu/Cabe Jawa");
        db.insertMstKomoditas("1", "24", "Cassiavera/Kayu Manis");
        db.insertMstKomoditas("1", "25", "Gambir");
    }

    //    private static final String JSON_KABKOT = "http://smallholder.inobu.org/sen_get_kabkot.php";
    public void insertKabupaten() {
        db.insertKab("62", "6201", "01", "KOTAWARINGIN BARAT");
        db.insertKab("62", "6202", "02", "KOTAWARINGIN TIMUR");
        db.insertKab("62", "6203", "03", "KAPUAS");
        db.insertKab("62", "6204", "04", "BARITO SELATAN");
        db.insertKab("62", "6205", "05", "BARITO UTARA");
        db.insertKab("62", "6206", "06", "SUKAMARA");
        db.insertKab("62", "6207", "07", "LAMANDAU");
        db.insertKab("62", "6208", "08", "SERUYAN");
        db.insertKab("62", "6209", "09", "KATINGAN");
        db.insertKab("62", "6210", "10", "PULANG PISAU");
        db.insertKab("62", "6211", "11", "GUNUNG MAS");
        db.insertKab("62", "6212", "12", "BARITO TIMUR");
        db.insertKab("62", "6213", "13", "MURUNG RAYA");
        db.insertKab("62", "6271", "71", "PALANGKA RAYA");
    }

    //    private static final String JSON_KECAMATAN = "http://smallholder.inobu.org/sen_get_kecamatan.php";
    public void insertKecataman() {
        db.insertKec("6201", "6201040", "040", "KOTAWARINGIN LAMA");
        db.insertKec("6201", "6201050", "050", "ARUT SELATAN");
        db.insertKec("6201", "6201060", "060", "KUMAI");
        db.insertKec("6201", "6201061", "061", "PANGKALAN BANTENG");
        db.insertKec("6201", "6201062", "062", "PANGKALAN LADA");
        db.insertKec("6201", "6201070", "070", "ARUT UTARA");
        db.insertKec("6210", "6210010", "010", "KAHAYAN KUALA");
        db.insertKec("6210", "6210011", "011", "SEBANGAU KUALA");
        db.insertKec("6210", "6210020", "020", "PANDIH BATU");
        db.insertKec("6210", "6210030", "030", "MALIKU");
        db.insertKec("6210", "6210040", "040", "KAHAYAN HILIR");
        db.insertKec("6210", "6210041", "041", "JABIREN RAYA");
        db.insertKec("6210", "6210050", "050", "KAHAYAN TENGAH");
        db.insertKec("6210", "6210060", "060", "BANAMA TINGANG");
    }

    //    private static final String JSON_MANBUN = "http://smallholder.inobu.org/sen_get_manbun.php";
    public void insertManbun() {
        db.insertManbun("1", "Cuak Ardani, A.Md", "-", "6210060");
        db.insertManbun("2", "Eko Novianto, S.P", "-", "6210050");
        db.insertManbun("3", "Mambang, S.P", "-", "6210041");
        db.insertManbun("4", "Kristina, S.PKP", "-", "6210040");
        db.insertManbun("6", "Harijanti", "-", "6210020");
        db.insertManbun("7", "Abdah Attamimi, S.Pt", "-", "6210010");
        db.insertManbun("8", "Supriyono, S.Pt", "-", "6210030");
        db.insertManbun("9", "Prenteli", "-", "6210060");
        db.insertManbun("10", "Erik Wanda", "-", "6210060");
        db.insertManbun("11", "Aisupran, A.Md", "-", "6210050");
        db.insertManbun("12", "Viktor", "-", "6210050");
        db.insertManbun("13", "Asro Laelani, S.P", "-", "6210041");
        db.insertManbun("14", "Via Erika, S.P", "-", "6210041");
        db.insertManbun("15", "Rajakino, S.Tp", "-", "6210040");
        db.insertManbun("16", "Heni Purwandari, S.P", "-", "6210040");
        db.insertManbun("17", "Ruswati, S.Pt", "-", "6210030");
        db.insertManbun("18", "Robiatun", "-", "6210030");
        db.insertManbun("19", "Siti Chusnul Khotimah, S.P", "82155582158", "6210020");
        db.insertManbun("20", "Sukmi Nurhayati", "85249230573", "6210020");
        db.insertManbun("21", "Asmir Nirwan H.Y, S.Pt", "82255117817", "6210010");
        db.insertManbun("22", "Primadona, S.P", "85284339942", "6210010");
        db.insertManbun("23", "Tengang", "-", "6210011");
        db.insertManbun("24", "Tokoh Ananda Aribrata", "-", "6210011");
        db.insertManbun("25", "Puji Santoso", "-", "6210011");
        db.insertManbun("27", "Sunardi, S.St", "-", "6201070");
        db.insertManbun("28", "Kasihono, S.Pkp", "-", "6201070");
        db.insertManbun("29", "Heru Hiswara, S.Tp", "-", "6201070");
        db.insertManbun("30", "Frans Kely, S.Tp", "-", "6201070");
        db.insertManbun("31", "Sri Indah Sukawati, S.P", "-", "6201070");
        db.insertManbun("32", "Sartono, S.Pkp", "-", "6201062");
        db.insertManbun("33", "Suyatna", "-", "6201062");
        db.insertManbun("34", "Aina", "-", "6201062");
        db.insertManbun("35", "Muhamad Mundzir, S.Pt", "-", "6201062");
        db.insertManbun("36", "Dina Ekawati, S", "-", "6201062");
        db.insertManbun("37", "Turhan, A.Md", "-", "6201062");
        db.insertManbun("38", "Painten, S.St", "-", "6201062");
        db.insertManbun("39", "Suyadi, A.Md", "-", "6201062");
        db.insertManbun("40", "M. Ardiansyah. S.P", "-", "6201040");
        db.insertManbun("41", "Sumarwoto, S.St", "-", "6201040");
        db.insertManbun("42", "Fajar Lazuardi, S.P", "-", "6201040");
        db.insertManbun("43", "Mildayanti", "-", "6201040");
        db.insertManbun("44", "M.Nur Wasik AM", "-", "6201040");
        db.insertManbun("45", "Ir. Titik Setyowati", "-", "6201040");
        db.insertManbun("46", "Edy Kusumanto, S.St", "-", "6201040");
        db.insertManbun("47", "Nur Indah Y, S.P", "-", "6201040");
        db.insertManbun("48", "Harmoko M. Marasad", "-", "6201040");
        db.insertManbun("49", "Yayan Syahlani", "-", "6201040");
        db.insertManbun("50", "Obella Ovidius", "-", "6201040");
        db.insertManbun("51", "Nurhidayah, S.St", "-", "6201050");
        db.insertManbun("52", "Mohammad Najar, S.P", "-", "6201050");
        db.insertManbun("53", "Sumardi, S.St", "-", "6201050");
        db.insertManbun("54", "Furnandi, S.St", "-", "6201050");
        db.insertManbun("55", "Rusiana, S.St", "-", "6201050");
        db.insertManbun("56", "Suharmiyati, S.P", "-", "6201050");
        db.insertManbun("57", "Sumarsono, S.St", "-", "6201050");
        db.insertManbun("58", "Muliadi, S.St", "-", "6201050");
        db.insertManbun("59", "Tukiman, S.St", "-", "6201050");
        db.insertManbun("60", "Manto, S.St", "-", "6201050");
        db.insertManbun("61", "Juliaty R. Purba, A.Md", "-", "6201050");
        db.insertManbun("62", "Agus Pitawati, S.P", "-", "6201050");
        db.insertManbun("63", "Ninik Herwiyati, S.P", "-", "6201050");
        db.insertManbun("64", "Saharani B", "-", "6201050");
        db.insertManbun("65", "Supangat, A.Md", "-", "6201050");
        db.insertManbun("66", "Cony Susanti, S.P", "-", "6201050");
        db.insertManbun("67", "Mursiti", "-", "6201050");
        db.insertManbun("68", "Nurasih Budiyanti", "-", "6201050");
        db.insertManbun("69", "Jumarno, S.Pkp", "-", "6201060");
        db.insertManbun("70", "Suprayitno, A.Md", "-", "6201060");
        db.insertManbun("71", "Fitriadi", "-", "6201060");
        db.insertManbun("72", "Dewi Mislianti, S.P", "-", "6201060");
        db.insertManbun("73", "Suprapti", "-", "6201060");
        db.insertManbun("74", "Rasidah", "-", "6201060");
        db.insertManbun("75", "Roida Tua Lumban Batu, S.TP", "-", "6201060");
        db.insertManbun("76", "Cawarman Purba, S.P", "-", "6201060");
        db.insertManbun("77", "Happy M.Fauzi, S.P", "-", "6201060");
        db.insertManbun("78", "Jono, S.Pkp", "-", "6201061");
        db.insertManbun("79", "Iswanta, S.Pkp", "-", "6201061");
        db.insertManbun("80", "Luluk Khoiriyah, S.P", "-", "6201061");
        db.insertManbun("81", "Waluyo, S.P", "-", "6201061");
        db.insertManbun("82", "Mishadi, S.Pkp", "-", "6201061");
        db.insertManbun("83", "Painem, S.St", "-", "6201061");
        db.insertManbun("84", "Edi Teguh Prasojo", "-", "6201061");
        db.insertManbun("85", "Sabni, A.Md", "-", "6201061");
        db.insertManbun("86", "Yohanes Sudarmo, A.Md", "-", "6201061");
        db.insertManbun("87", "Coeri", "-", "6201061");
        db.insertManbun("88", "Kamiyono, S.Pkp", "-", "6201061");
        db.insertManbun("89", "Dhandun Baratha, S.P", "-", "6201061");
        db.insertManbun("90", "Mis Meri, S.P", "-", "6201061");
        db.insertManbun("91", "Ponco Suhendriyo, S.St", "-", "6201070");
        db.insertManbun("92", "Elbert, S.Sti", "-", "6201050");
        db.insertManbun("93", "Rusmin", "-", "6201062");
        db.insertManbun("94", "Gandung Restu Irianto", "-", "6201062");
        db.insertManbun("95", "Tulus Widodo", "-", "6201062");
        db.insertManbun("96", "Rinda Rupel", "-", "6201061");
        db.insertManbun("97", "Jaeli", "-", "6201060");
        db.insertManbun("98", "Wahyono", "-", "6201060");
        db.insertManbun("8888", "test", "11111", "6210011");
        db.insertManbun("9999", "", "-", "-");
    }

}