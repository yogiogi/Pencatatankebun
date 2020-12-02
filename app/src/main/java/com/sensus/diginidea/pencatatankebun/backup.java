package com.sensus.diginidea.pencatatankebun;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.sensus.diginidea.pencatatankebun.dbPackage.DbHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.content.ContentValues.TAG;

public class backup extends Activity {
    Button btbackup;
    Button btRestore;
    String RowID;
    TextView shapeIDs;
    ProgressDialog pDialog;
    Cursor model = null;
    DbHelper dbhelper;
    String tabelLuas, tabelReal, tabelProd, tabelEstLuas, tabelEstProd;
    int id_obj;
    String objek;
    File directory;
    String csvFile = "myData.xls";
    File file;
    WritableSheet sheet1, sheet2;
    WritableWorkbook workbook;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.backup);

//        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }

        dbhelper = new DbHelper(this);
        btbackup = (Button) findViewById(R.id.btBackup);
        btRestore = (Button) findViewById(R.id.btRestore);

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

        btbackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                File sd = Environment.getExternalStorageDirectory();
                directory = new File(sd.getAbsolutePath());
                //create directory if not exist
                if (!directory.isDirectory()) {
                    directory.mkdirs();
                }

                file = new File(directory, csvFile);
                if (file.exists()) {
                    file.delete();
                } else {
                    file = new File(directory, csvFile);
                }

                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale("en", "EN"));
                try {
                    workbook = Workbook.createWorkbook(file, wbSettings);
                    //Excel sheet name. 0 represents first sheet
                    sheet1 = workbook.createSheet("identitas", 0);
                    sheet2 = workbook.createSheet("Komoditas", 1);
                    new netcheck().execute();

                    workbook.write();
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }


//                exportDB();
            }

        });

        btRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


            }

        });
    }

    private class netcheck extends AsyncTask<Long, String, Boolean> {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(backup.this);
            nDialog.setTitle("");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(Long... longs) {

            try {
                // column and row
                sheet1.addCell(new Label(0, 0, "id_sensus"));
                sheet1.addCell(new Label(1, 0, "Objek_sensus"));
                sheet1.addCell(new Label(2, 0, "Nama Pemilik"));
                sheet1.addCell(new Label(3, 0, "Telp"));
                sheet1.addCell(new Label(4, 0, "Kelompok_tani"));
                sheet1.addCell(new Label(5, 0, "Propinsi"));
                sheet1.addCell(new Label(6, 0, "Kabupaten"));
                sheet1.addCell(new Label(7, 0, "Kecamatan"));
                sheet1.addCell(new Label(8, 0, "Desa"));
                sheet1.addCell(new Label(9, 0, "No Urut"));
                sheet1.addCell(new Label(10, 0, "Nama Manbun"));
                sheet1.addCell(new Label(11, 0, "Telp ManBun"));
                sheet1.addCell(new Label(12, 0, "Tanggal Pencatatan"));

                SQLiteDatabase db = dbhelper.getWritableDatabase();
                model = db.rawQuery("select a.id, a.id_sensus, (case when a.id_obj_sensus = 1 then 'pekebun' else 'perusahaan' end) as 'objek_sensus', " +
                        "a.nama, a.telp, a.kelompok_tani, " +
                        "(case when a.kode_propinsi= 62 then 'KALIMANTAN TENGAH' end) as 'Propinsi', " +
                        "b.nama_kab_kot, c.nama_kecamatan, d.nama_desa, a.no_urut, e.nama as 'ManBun', e.telp as 'telp_manbun', a.tgl_catat " +
                        "from tbl_identitas a left join mst_kabkot b on a.kode_kabupaten = b.kode_kabupaten2 " +
                        "inner join mst_kecamatan c on '62'||a.kode_kabupaten||a.kode_kecamatan = c.kode_kecamatan " +
                        "inner join mst_desa d on '62'||a.kode_kabupaten||a.kode_kecamatan||a.kode_desa = d.kode_desa " +
                        "left join mst_manbun e on a.id_manbun = e.id_manbun order by a.create_date", null);
                Log.d("query", "select a.id, a.id_sensus, (case when a.id_obj_sensus = 1 then 'pekebun' else 'perusahaan' end) as 'objek_sensus', " +
                        "a.nama, a.telp, a.kelompok_tani, " +
                        "(case when a.kode_propinsi= 62 then 'KALIMANTAN TENGAH' end) as 'Propinsi', " +
                        "b.nama_kab_kot, c.nama_kecamatan, d.nama_desa, a.no_urut, e.nama as 'ManBun', e.telp as 'telp_manbun', a.tgl_catat " +
                        "from tbl_identitas a left join mst_kabkot b on a.kode_kabupaten = b.kode_kabupaten2 " +
                        "inner join mst_kecamatan c on '62'||a.kode_kabupaten||a.kode_kecamatan = c.kode_kecamatan " +
                        "inner join mst_desa d on '62'||a.kode_kabupaten||a.kode_kecamatan||a.kode_dessa = d.kode_desa " +
                        "left join mst_manbun e on a.id_manbun = e.id_manbun order by a.create_date");

                if (model.moveToFirst()) {
                    do {
                        RowID = model.getString(model.getColumnIndex("id"));
                        String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                        String objek__sensus = model.getString(model.getColumnIndex("objek_sensus"));
                        String nama = model.getString(model.getColumnIndex("nama"));
                        String telp = model.getString(model.getColumnIndex("telp"));

                        String kelompok_tani = model.getString(model.getColumnIndex("kelompok_tani"));
                        if (id_obj != 1) {
                            kelompok_tani = "-";
                        }

                        String propinsi = model.getString(model.getColumnIndex("Propinsi"));
                        String kabupaten = model.getString(model.getColumnIndex("nama_kab_kot"));
                        String kecamatan = model.getString(model.getColumnIndex("nama_kecamatan"));
                        String desa = model.getString(model.getColumnIndex("nama_desa"));
                        String no_urut = model.getString(model.getColumnIndex("no_urut"));
                        String manbun = model.getString(model.getColumnIndex("ManBun"));
                        if (id_obj != 1) {
                            manbun = "-";
                        }
                        String telp_manbun = model.getString(model.getColumnIndex("telp_manbun"));

                        String tgl_catat = model.getString(model.getColumnIndex("tgl_catat"));

                        Log.d("query", id_sensus + "," + id_obj + "," + nama + "," + telp + "," + kelompok_tani + "," +
                                propinsi + "," + kabupaten + "," + kecamatan + "," + desa + "," + String.format("%04d", Integer.parseInt(no_urut.toString())) + "," +
                                manbun + "," + tgl_catat + "");

                        int i = model.getPosition() + 1;
                        sheet2.addCell(new Label(0, i, id_sensus));
                        sheet2.addCell(new Label(1, i, objek__sensus));
                        sheet2.addCell(new Label(2, i, nama));
                        sheet2.addCell(new Label(3, i, telp));
                        sheet2.addCell(new Label(4, i, kelompok_tani));
                        sheet2.addCell(new Label(5, i, propinsi));
                        sheet2.addCell(new Label(6, i, kabupaten));
                        sheet2.addCell(new Label(7, i, kecamatan));
                        sheet2.addCell(new Label(8, i, desa));
                        sheet2.addCell(new Label(9, i, no_urut));
                        sheet2.addCell(new Label(10, i, manbun));
                        sheet2.addCell(new Label(11, i, telp_manbun));
                        sheet2.addCell(new Label(11, i, tgl_catat));
                    } while (model.moveToNext());
                }

                //closing cursor
                model.close();
                Toast.makeText(getApplication(),
                        "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
//                success = "1";

                //file path
                sheet2.addCell(new Label(0, 0, "id_sensus"));
                sheet2.addCell(new Label(1, 0, "Komoditas"));
                sheet2.addCell(new Label(2, 0, "Jenis Tanaman"));
                sheet2.addCell(new Label(3, 0, "TBM"));
                sheet2.addCell(new Label(4, 0, "TM"));
                sheet2.addCell(new Label(5, 0, "TTM"));
                sheet2.addCell(new Label(6, 0, "Keterangan"));
                sheet2.addCell(new Label(7, 0, "Status Kirim"));

                db = dbhelper.getWritableDatabase();
                model = db.rawQuery("select a.id, a.id_sensus, b.komoditas, c.jenis_tanaman, a.tbm, a.tm, a.ttm, a.keterangan, case when a.status = 1 then 'terkirim' else 'belum_terkirim' end as status_kirim " +
                        "from tbl_luas_kebun_petani a " +
                        "inner join mst_komoditas b on a.id_komoditas = b.id_komoditas " +
                        "inner join mst_jenis_tanaman c on a.jenis_tanaman = c.id", null);
                Log.d("query", "select a.id, a.id_sensus, b.komoditas, c.jenis_tanaman, a.tbm, a.tm, a.ttm, a.keterangan, case when a.status = 1 then 'terkirim' else 'belum_terkirim' end as status_kirim " +
                        "from tbl_luas_kebun_petani a " +
                        "inner join mst_komoditas b on a.id_komoditas = b.id_komoditas " +
                        "inner join mst_jenis_tanaman c on a.jenis_tanaman = c.id");

                if (model.moveToFirst()) {
                    do {
                        RowID = model.getString(model.getColumnIndex("id"));
                        String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
                        String komoditass = model.getString(model.getColumnIndex("komoditas"));
                        String jenis = model.getString(model.getColumnIndex("jenis_tanaman"));
                        String tbm = model.getString(model.getColumnIndex("tbm"));
                        String tm = model.getString(model.getColumnIndex("tm"));
                        String ttm = model.getString(model.getColumnIndex("ttm"));
                        String keterangan = model.getString(model.getColumnIndex("keterangan"));

                        int i = model.getPosition() + 1;
                        sheet2.addCell(new Label(0, i, id_sensus));
                        sheet2.addCell(new Label(1, i, komoditass));
                        sheet2.addCell(new Label(2, i, jenis));
                        sheet2.addCell(new Label(3, i, tbm));
                        sheet2.addCell(new Label(4, i, ttm));
                        sheet2.addCell(new Label(5, i, keterangan));
                    } while (model.moveToNext());
                }

                //closing cursor
                model.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        @Override
        protected void onPostExecute(Boolean th) {
            nDialog.dismiss();
            if (th == true) {
//                new luaskebunbackup().execute();
                Toast.makeText(getApplicationContext(),
                        "Berhasil backup identitas", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Gagal, Backup ", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
//
//    private class luaskebunbackup extends AsyncTask<Long, String, Boolean> {
//        private ProgressDialog nDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            nDialog = new ProgressDialog(backup.this);
//            nDialog.setTitle("");
//            nDialog.setMessage("Loading..");
//            nDialog.setIndeterminate(false);
//            nDialog.setCancelable(true);
//            nDialog.show();
//        }
//
//        @Override
//        protected Boolean doInBackground(Long... longs) {
//
//            try {
//
//                workbook.write();
//                workbook.close();
//                Toast.makeText(getApplication(),
//                        "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
////                success = "1";
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean th) {
//            nDialog.dismiss();
//            if (th == true) {
//                Toast.makeText(getApplicationContext(),
//                        "Berhasil backup", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Gagal, Backup ", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        finish();
    }

    //    class sentLuasKebunBt extends AsyncTask<String, Void, String> {
//        //        ProgressDialog pDialog;
//        String success = "2";
//        String idSENSUS;
//
//        @Override
//        protected String doInBackground(String... params) {
//            idSENSUS = params[0];
//            Double tbm = 0.0, tm = 0.0, ttm = 0.0, tbm_inti = 0.0, tm_inti = 0.0, ttm_inti = 0.0, tbm_plasma = 0.0, tm_plasma = 0.0, ttm_plasma = 0.0;
//
//            SQLiteDatabase db = dbhelper.getWritableDatabase();
//            model = db.rawQuery("select a.id, a.id_sensus, b.komoditas, c.jenis_tanaman, a.tbm, a.tm, a.ttm, a.keterangan, case when a.status = 1 then 'terkirim' else 'belum_terkirim' end as status_kirim " +
//                    "from tbl_luas_kebun_petani a " +
//                    "inner join mst_komoditas b on a.id_komoditas = b.id_komoditas " +
//                    "inner join mst_jenis_tanaman c on a.jenis_tanaman = c.id", null);
//
//            try {
//                //file path
//                File file = new File(directory, csvFile);
//                WorkbookSettings wbSettings = new WorkbookSettings();
//                wbSettings.setLocale(new Locale("en", "EN"));
//                WritableWorkbook workbook;
//                workbook = Workbook.createWorkbook(file, wbSettings);
//                //Excel sheet name. 0 represents first sheet
//                WritableSheet sheet = workbook.createSheet("luas kebun", 0);
//                // column and row
//                sheet.addCell(new Label(0, 0, "id_sensus"));
//                sheet.addCell(new Label(1, 0, "Objek_sensus"));
//                sheet.addCell(new Label(2, 0, "Nama Pemilik"));
//                sheet.addCell(new Label(3, 0, "Telp"));
//                sheet.addCell(new Label(4, 0, "Kelompok_tani"));
//                sheet.addCell(new Label(5, 0, "Propinsi"));
//                sheet.addCell(new Label(6, 0, "Kabupaten"));
//                sheet.addCell(new Label(7, 0, "Kecamatan"));
//                sheet.addCell(new Label(8, 0, "Desa"));
//                sheet.addCell(new Label(9, 0, "No Urut"));
//                sheet.addCell(new Label(10, 0, "Nama Manbun"));
//                sheet.addCell(new Label(11, 0, "Telp ManBun"));
//                sheet.addCell(new Label(12, 0, "Tanggal Pencatatan"));
//
//                if (model.moveToFirst()) {
//                    do {
//                        String RowID = model.getString(model.getColumnIndex("id"));
//                        String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                        String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
//                        if (id_obj == 1) {
//                            tbm = model.getDouble(model.getColumnIndex("tbm"));
//                            tm = model.getDouble(model.getColumnIndex("tm"));
//                            ttm = model.getDouble(model.getColumnIndex("ttm"));
//
//                            Log.d("query", id_sensus + "," + id_komoditas + "," + tbm + "," + tm + "," + ttm);
//                        } else {
//                            tbm_inti = model.getDouble(model.getColumnIndex("tbm_inti"));
//                            tm_inti = model.getDouble(model.getColumnIndex("tm_inti"));
//                            ttm_inti = model.getDouble(model.getColumnIndex("ttm_inti"));
//                            tbm_plasma = model.getDouble(model.getColumnIndex("tbm_plasma"));
//                            tm_plasma = model.getDouble(model.getColumnIndex("tm_plasma"));
//                            ttm_plasma = model.getDouble(model.getColumnIndex("ttm_plasma"));
//
//                            Log.d("query", id_sensus + "," + id_komoditas + "," + tbm_inti + "," + tm_inti + "," + ttm_inti
//                                    + "," + tbm_plasma + "," + tm_plasma + "," + ttm_plasma);
//                        }
//                        String keterangan = model.getString(model.getColumnIndex("keterangan"));
//
//                        int i = model.getPosition() + 1;
//                        sheet.addCell(new Label(0, i, id_sensus));
//                        sheet.addCell(new Label(1, i, objek__sensus));
//                        sheet.addCell(new Label(2, i, nama));
//                        sheet.addCell(new Label(3, i, telp));
//                        sheet.addCell(new Label(4, i, kelompok_tani));
//                        sheet.addCell(new Label(5, i, propinsi));
//                        sheet.addCell(new Label(6, i, kabupaten));
//                        sheet.addCell(new Label(7, i, kecamatan));
//                        sheet.addCell(new Label(8, i, desa));
//                        sheet.addCell(new Label(9, i, no_urut));
//                        sheet.addCell(new Label(10, i, manbun));
//                        sheet.addCell(new Label(11, i, telp_manbun));
//                        sheet.addCell(new Label(11, i, tgl_catat));
//
//                    } while (model.moveToNext());
//                }
//
//                if (model.moveToFirst()) {
//                    do {
//                        RowID = model.getString(model.getColumnIndex("id"));
//                        String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                        String objek__sensus = model.getString(model.getColumnIndex("objek_sensus"));
//                        String nama = model.getString(model.getColumnIndex("nama"));
//                        String telp = model.getString(model.getColumnIndex("telp"));
//
//                        String kelompok_tani = model.getString(model.getColumnIndex("kelompok_tani"));
//                        if (id_obj != 1) {
//                            kelompok_tani = "-";
//                        }
//
//                        String propinsi = model.getString(model.getColumnIndex("Propinsi"));
//                        String kabupaten = model.getString(model.getColumnIndex("nama_kab_kot"));
//                        String kecamatan = model.getString(model.getColumnIndex("nama_kecamatan"));
//                        String desa = model.getString(model.getColumnIndex("nama_desa"));
//                        String no_urut = model.getString(model.getColumnIndex("no_urut"));
//                        String manbun = model.getString(model.getColumnIndex("ManBun"));
//                        if (id_obj != 1) {
//                            manbun = "-";
//                        }
//                        String telp_manbun = model.getString(model.getColumnIndex("telp_manbun"));
//
//                        String tgl_catat = model.getString(model.getColumnIndex("tgl_catat"));
//
//                        Log.d("query", id_sensus + "," + id_obj + "," + nama + "," + telp + "," + kelompok_tani + "," +
//                                propinsi + "," + kabupaten + "," + kecamatan + "," + desa + "," + String.format("%04d", Integer.parseInt(no_urut.toString())) + "," +
//                                manbun + "," + tgl_catat + "");
//
//                    } while (model.moveToNext());
//                }
//
//                //closing cursor
//                model.close();
//                workbook.write();
//                workbook.close();
//                Toast.makeText(getApplication(),
//                        "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();
//                success = "1";
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return success;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(backup.this);
//            pDialog.setMessage("Kirim data...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // super.onPostExecute(result);
//            pDialog.dismiss();
//
//            if (Integer.parseInt(result) == 1) {
//                new backup.sentRealisasiBt().execute(idSENSUS);
//                Toast.makeText(getApplicationContext(),
//                        "Data luas kebun " + objek + " terkirim!", Toast.LENGTH_LONG).show();
//            } else if (Integer.parseInt(result) == 2) {
//                Toast.makeText(getApplicationContext(),
//                        "Data luas kebun " + objek + " sudah terkirim!", Toast.LENGTH_LONG).show();
//                new backup.sentLuasKebunBt().execute(idSENSUS);
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Data luas kebun " + objek + " gagal terkirim", Toast.LENGTH_SHORT)
//                        .show();
//                Intent intent = new Intent(backup.this, MainActivity.class);
//                startActivity(intent);
//            }
//        }
//    }
//
//    class sentRealisasiBt extends AsyncTask<String, Void, String> {
//        //        ProgressDialog pDialog;
//        String success = "2";
//        String idSENSUS;
//
//        @Override
//        protected String doInBackground(String... params) {
//            idSENSUS = params[0];
//            SQLiteDatabase db = dbhelper.getWritableDatabase();
//            model = db.rawQuery("SELECT a.*, b.id_obj_sensus  FROM " + tabelReal + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
//                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + " and a.status = 0", null);
//
//            if (model.moveToFirst()) {
//                do {
//                    RowID = model.getString(model.getColumnIndex("id"));
//                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
//                    String bulan = model.getString(model.getColumnIndex("bulan"));
//                    Double jumlah_prod = model.getDouble(model.getColumnIndex("jumlah_prod"));
//                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
//                    Double nilai_jual = model.getDouble(model.getColumnIndex("nilai_jual"));
//                    Double harga = model.getDouble(model.getColumnIndex("harga"));
//                    String keterangan = model.getString(model.getColumnIndex("keterangan"));
//
//                    Log.d("query", id_sensus + "," + id_komoditas + "," + bulan + "," + jumlah_prod + "," + nilai_jual + "," + harga + "," + keterangan);
//
//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
//                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
//                    nameValuePairs.add(new BasicNameValuePair("bulan", bulan));
//                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", String.format("%.2f", jumlah_prod)));
//                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
//                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", String.format("%.2f", nilai_jual)));
//                    nameValuePairs.add(new BasicNameValuePair("harga", String.format("%.0f", harga)));
//                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
//
//                    JSONParser jsonParser = new JSONParser();
//                    JSONObject json;
//                    if (id_obj == 1) {
//                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_realisasi_petani,
//                                "POST", nameValuePairs);
//                    } else {
//                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_realisasi_perusahaan,
//                                "POST", nameValuePairs);
//                    }
//
//                    try {
//                        success = json.getString("success");
//                        Log.d("Request Ok", success + " realisasi");
//
//                        if (Integer.parseInt(success) == 1) {
//                            dbhelper.updateStatus(tabelReal, "status", Integer.parseInt(RowID), 1);
//                        } else {
//                            gagal(id_sensus);
//                        }
//                    } catch (Exception z) {
//                        z.printStackTrace();
//                    }
//
//                } while (model.moveToNext());
//            }
//
//            return success;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(backup.this);
//            pDialog.setMessage("Kirim data...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // super.onPostExecute(result);
//            pDialog.dismiss();
//
//            if (Integer.parseInt(result) == 1) {
//                Toast.makeText(getApplicationContext(),
//                        "Data realisasi " + objek + " terkirim!", Toast.LENGTH_LONG).show();
//
//                st_prodl = dbhelper.instantSelect("status", tabelProd, "id_sensus", idSENSUS);
//                st_estPl = dbhelper.instantSelect("status", tabelEstProd, "id_sensus", idSENSUS);
//                st_estLL = dbhelper.instantSelect("status", tabelEstLuas, "id_sensus", idSENSUS);
//
//                idLuas = dbhelper.count(tabelLuas + " where id_sensus = " + idSENSUS);
//                idProd = dbhelper.count(tabelProd + " where id_sensus = " + idSENSUS + " and status = 0");
//                idEstP = dbhelper.count(tabelEstProd + " where id_sensus = " + idSENSUS + " and status = 0");
//                idEstL = dbhelper.count(tabelEstLuas + " where id_sensus = " + idSENSUS + " and status = 0");
//
//                cP = dbhelper.count(tabelLuas + " where id_sensus = " + idSENSUS + " and status_prod = 1");
//                cEP = dbhelper.count(tabelLuas + " where id_sensus = " + idSENSUS + " and status_est_prod = 1");
//                cEL = dbhelper.count(tabelLuas + " where id_sensus = " + idSENSUS + " and status_est_luas_area = 1");
//
//                if (kirim == "catat1") {
//                    Toast.makeText(getApplicationContext(),
//                            "Data pencatatan 1 " + objek + " terkirim!", Toast.LENGTH_LONG).show();
//
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//                } else {
//                    if (st_prodl != null && !st_prodl.isEmpty() && !st_prodl.equals("null")
//                            && st_estPl != null && !st_estPl.isEmpty() && !st_estPl.equals("null")
//                            && st_estLL != null && !st_estLL.isEmpty() && !st_estLL.equals("null")) {
//                        if (idProd != 0 && idEstP != 0 && idEstP != 0) {
//                            if (idLuas != cP || idLuas != cEL || idLuas != cEP) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Pencatatan 2 belum lengkap", Toast.LENGTH_SHORT)
//                                        .show();
//                            } else {
//                                new backup.sentProdBt().execute(idSENSUS);
//                            }
//                        }
//                    } else {
//                        Log.d("status ", "tidak lengkap");
//                        Toast.makeText(getApplicationContext(),
//                                "Pencatatan 2 belum lengkap", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                }
//
//            } else if (Integer.parseInt(result) == 2) {
//                Toast.makeText(getApplicationContext(),
//                        "Data luas realisasi " + objek + " sudah terkirim!", Toast.LENGTH_LONG).show();
//                if (kirim == "catat1") {
//                    Toast.makeText(getApplicationContext(),
//                            "Data pencatatan 1 " + objek + " terkirim!", Toast.LENGTH_LONG).show();
//
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//                } else {
//                    new backup.sentProdBt().execute(idSENSUS);
//                }
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Data realisasi " + objek + " gagal terkirim", Toast.LENGTH_SHORT)
//                        .show();
//                Intent intent = new Intent(backup.this, MainActivity.class);
//                startActivity(intent);
//            }
//        }
//    }
//
//    class sentProdBt extends AsyncTask<String, Void, String> {
//        //        ProgressDialog pDialog;
//        String success = "2";
//        String idSENSUS;
//
//        @Override
//        protected String doInBackground(String... params) {
//            idSENSUS = params[0];
//            SQLiteDatabase db = dbhelper.getWritableDatabase();
//            model = db.rawQuery("SELECT a.*, b.id_obj_sensus  FROM " + tabelProd + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
//                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + " and a.status = 0", null);
//
//            if (model.moveToFirst()) {
//                do {
//                    RowID = model.getString(model.getColumnIndex("id"));
//                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
//                    String tgl_pendataan = model.getString(model.getColumnIndex("tgl_pendataan"));
//                    Double jumlah_prod = model.getDouble(model.getColumnIndex("jumlah_prod"));
//                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
//                    Double dijual = model.getDouble(model.getColumnIndex("dijual"));
//                    Double disimpan = model.getDouble(model.getColumnIndex("disimpan"));
//                    Double konsumsi = model.getDouble(model.getColumnIndex("konsumsi"));
//                    String keterangan = model.getString(model.getColumnIndex("keterangan"));
//
//                    Log.d("query", id_sensus + "," + id_komoditas + "," + tgl_pendataan + "," + jumlah_prod + "," + wujud_prod
//                            + "," + dijual + "," + disimpan + "," + konsumsi + "," + keterangan);
//
//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
//                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
//                    nameValuePairs.add(new BasicNameValuePair("tgl_pendataan", tgl_pendataan));
//                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", String.format("%.2f", jumlah_prod)));
//                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
//                    nameValuePairs.add(new BasicNameValuePair("dijual", String.format("%.2f", dijual)));
//                    nameValuePairs.add(new BasicNameValuePair("disimpan", String.format("%.2f", disimpan)));
//                    nameValuePairs.add(new BasicNameValuePair("konsumsi", String.format("%.2f", konsumsi)));
//                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
//
//                    JSONParser jsonParser = new JSONParser();
//                    JSONObject json;
//                    if (id_obj == 1) {
//                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_produksi_petani,
//                                "POST", nameValuePairs);
//                    } else {
//                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_produksi_perusahaan,
//                                "POST", nameValuePairs);
//                    }
//
//                    try {
//                        success = json.getString("success");
//                        Log.d("Request Ok", success + " produksi");
//                        if (Integer.parseInt(success) == 1) {
//                            dbhelper.updateStatus(tabelProd, "status", Integer.parseInt(RowID), 1);
//                        } else {
//                            gagal(id_sensus);
//                        }
//                    } catch (Exception z) {
//                        z.printStackTrace();
//                    }
//
//                } while (model.moveToNext());
//            }
//
//            return success;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(backup.this);
//            pDialog.setMessage("Kirim data...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // super.onPostExecute(result);
//            pDialog.dismiss();
//
//            if (Integer.parseInt(result) == 1) {
//                new backup.sentEstLuasKebunBt().execute(idSENSUS);
//
//                Toast.makeText(getApplicationContext(),
//                        "Data produksi " + objek + " terkirim!", Toast.LENGTH_LONG).show();
//            } else if (Integer.parseInt(result) == 2) {
//                new backup.sentEstLuasKebunBt().execute(idSENSUS);
//
//                Toast.makeText(getApplicationContext(),
//                        "Data produksi " + objek + " sudah terkirim!", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Data produksi " + objek + " gagal terkirim", Toast.LENGTH_SHORT)
//                        .show();
//                Intent intent = new Intent(backup.this, MainActivity.class);
//                startActivity(intent);
//            }
//        }
//    }
//
//    class sentEstLuasKebunBt extends AsyncTask<String, Void, String> {
//        String success = "2";
//        String idSENSUS;
//
//        @Override
//        protected String doInBackground(String... params) {
//            idSENSUS = params[0];
//            SQLiteDatabase db = dbhelper.getWritableDatabase();
//            model = db.rawQuery("SELECT a.*, b.id_obj_sensus  FROM " + tabelEstLuas + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
//                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + " and a.status = 0", null);
//
//            if (model.moveToFirst()) {
//                do {
//                    RowID = model.getString(model.getColumnIndex("id"));
//                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
//                    Double tbm = 0.0, tm = 0.0, ttm = 0.0, tbm_inti = 0.0, tm_inti = 0.0, ttm_inti = 0.0, tbm_plasma = 0.0, tm_plasma = 0.0, ttm_plasma = 0.0;
//                    if (id_obj == 1) {
//                        tbm = model.getDouble(model.getColumnIndex("tbm"));
//                        tm = model.getDouble(model.getColumnIndex("tm"));
//                        ttm = model.getDouble(model.getColumnIndex("ttm"));
//
//                        Log.d("query", id_sensus + "," + id_komoditas + "," + tbm + "," + tm + "," + ttm);
//                    } else {
//                        tbm_inti = model.getDouble(model.getColumnIndex("tbm_inti"));
//                        tm_inti = model.getDouble(model.getColumnIndex("tm_inti"));
//                        ttm_inti = model.getDouble(model.getColumnIndex("ttm_inti"));
//                        tbm_plasma = model.getDouble(model.getColumnIndex("tbm_plasma"));
//                        tm_plasma = model.getDouble(model.getColumnIndex("tm_plasma"));
//                        ttm_plasma = model.getDouble(model.getColumnIndex("ttm_plasma"));
//
//                        Log.d("query", id_sensus + "," + id_komoditas + "," + tbm_inti + "," + tm_inti + "," + ttm_inti
//                                + "," + tbm_plasma + "," + tm_plasma + "," + ttm_plasma);
//                    }
//                    String keterangan = model.getString(model.getColumnIndex("keterangan"));
//
//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
//                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
//
//                    if (id_obj == 1) {
//                        nameValuePairs.add(new BasicNameValuePair("tbm", String.format("%.2f", tbm)));
//                        nameValuePairs.add(new BasicNameValuePair("tm", String.format("%.2f", tm)));
//                        nameValuePairs.add(new BasicNameValuePair("ttm", String.format("%.2f", ttm)));
//                    } else {
//                        nameValuePairs.add(new BasicNameValuePair("tbm_inti", String.format("%.2f", tbm_inti)));
//                        nameValuePairs.add(new BasicNameValuePair("tm_inti", String.format("%.2f", tm_inti)));
//                        nameValuePairs.add(new BasicNameValuePair("ttm_inti", String.format("%.2f", ttm_inti)));
//                        nameValuePairs.add(new BasicNameValuePair("tbm_plasma", String.format("%.2f", tbm_plasma)));
//                        nameValuePairs.add(new BasicNameValuePair("tm_plasma", String.format("%.2f", tm_plasma)));
//                        nameValuePairs.add(new BasicNameValuePair("ttm_plasma", String.format("%.2f", ttm_plasma)));
//                    }
//                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
//
//                    JSONParser jsonParser = new JSONParser();
//                    JSONObject json;
//                    if (id_obj == 1) {
//                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_luas_kebun_petani,
//                                "POST", nameValuePairs);
//                    } else {
//                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_luas_kebun_perusahaan,
//                                "POST", nameValuePairs);
//                    }
//
//
//                    try {
//                        success = json.getString("success");
//                        Log.d("Request Ok", success + " estimasi luas kebun");
//
//                        if (Integer.parseInt(success) == 1) {
//                            dbhelper.updateStatus(tabelEstLuas, "status", Integer.parseInt(RowID), 1);
//                        } else {
//                            gagal(id_sensus);
//                        }
//                    } catch (Exception z) {
//                        z.printStackTrace();
//                    }
//
//                } while (model.moveToNext());
//            }
//
//            return success;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(backup.this);
//            pDialog.setMessage("Kirim data...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // super.onPostExecute(result);
//            pDialog.dismiss();
//
//            if (Integer.parseInt(result) == 1) {
//                new backup.sentEstProdBt().execute(idSENSUS);
//                Toast.makeText(getApplicationContext(),
//                        "Data estimasi luas kebun " + objek + " terkirim!", Toast.LENGTH_LONG).show();
//            } else if (Integer.parseInt(result) == 2) {
//                new backup.sentEstProdBt().execute(idSENSUS);
//                Toast.makeText(getApplicationContext(),
//                        "Data estimasi luas kebun " + objek + " sudah terkirim!", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Data estimasi luas kebun " + objek + " gagal terkirim", Toast.LENGTH_SHORT)
//                        .show();
////                finish();
//            }
//        }
//    }
//
//    class sentEstProdBt extends AsyncTask<String, Void, String> {
//        //        ProgressDialog pDialog;
//        String success = "3";
//        String idSENSUS;
//
//        @Override
//        protected String doInBackground(String... params) {
//            idSENSUS = params[0];
//            SQLiteDatabase db = dbhelper.getWritableDatabase();
//            model = db.rawQuery("SELECT a.*, b.id_obj_sensus  FROM " + tabelEstProd + " a inner join tbl_identitas b on a.id_sensus = b.id_sensus " +
//                    " WHERE b.id_obj_sensus = " + id_obj + " and a.id_sensus = " + idSENSUS + "  and a.status = 0", null);
//
//            if (model.moveToFirst()) {
//                do {
//                    RowID = model.getString(model.getColumnIndex("id"));
//                    String id_sensus = model.getString(model.getColumnIndex("id_sensus"));
//                    String id_komoditas = model.getString(model.getColumnIndex("id_komoditas"));
//                    String tgl_pendataan = model.getString(model.getColumnIndex("tgl_pendataan"));
//                    Double jumlah_prod = model.getDouble(model.getColumnIndex("jumlah_prod"));
//                    String wujud_prod = model.getString(model.getColumnIndex("wujud_produksi"));
//                    Double nilai_jual = model.getDouble(model.getColumnIndex("nilai_jual"));
//                    Double harga = model.getDouble(model.getColumnIndex("harga"));
//                    String keterangan = model.getString(model.getColumnIndex("keterangan"));
//
//                    Log.d("query", id_sensus + "," + id_komoditas + "," + tgl_pendataan + "," + jumlah_prod + "," + wujud_prod
//                            + "," + nilai_jual + "," + harga + "," + keterangan);
//
//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                    nameValuePairs.add(new BasicNameValuePair("id_sensus", id_sensus));
//                    nameValuePairs.add(new BasicNameValuePair("id_komoditas", id_komoditas));
//                    nameValuePairs.add(new BasicNameValuePair("tgl_pendataan", tgl_pendataan));
//                    nameValuePairs.add(new BasicNameValuePair("jumlah_prod", String.format("%.2f", jumlah_prod)));
//                    nameValuePairs.add(new BasicNameValuePair("wujud_prod", wujud_prod));
//                    nameValuePairs.add(new BasicNameValuePair("nilai_jual", String.format("%.2f", nilai_jual)));
//                    nameValuePairs.add(new BasicNameValuePair("harga", String.format("%.0f", harga)));
//                    nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
//
//                    JSONParser jsonParser = new JSONParser();
//                    JSONObject json;
//                    if (id_obj == 1) {
//                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_produksi_pertanian,
//                                "POST", nameValuePairs);
//                    } else {
//                        json = jsonParser.makeHttpRequest(LINK + dbhelper.url_sen_est_produksi_perusahaan,
//                                "POST", nameValuePairs);
//                    }
//
//                    try {
//                        success = json.getString("success");
//                        Log.d("Request Ok", success + " estimasi produksi");
//
//                        if (Integer.parseInt(success) == 1) {
//                            dbhelper.updateStatus(tabelEstProd, "status", Integer.parseInt(RowID), 1);
//                        } else {
//                            gagal(id_sensus);
//                        }
//                    } catch (Exception z) {
//                        z.printStackTrace();
//                    }
//
//                } while (model.moveToNext());
//            }
//
//            return success;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(backup.this);
//            pDialog.setMessage("Kirim data...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(false);
//            pDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // super.onPostExecute(result);
//            pDialog.dismiss();
//
//            if (Integer.parseInt(result) == 1) {
//                Toast.makeText(getApplicationContext(),
//                        "Data estimasi produksi " + objek + " terkirim!", Toast.LENGTH_LONG).show();
//            } else if (Integer.parseInt(result) == 3) {
//                Toast.makeText(getApplicationContext(),
//                        "Data estimasi produksi " + objek + " sudah terkirim!", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Data estimasi produksi " + objek + " gagal terkirim", Toast.LENGTH_SHORT)
//                        .show();
//            }
//
//
//            Intent intent = getIntent();
//            finish();
//            startActivity(intent);
//        }
//    }

}
