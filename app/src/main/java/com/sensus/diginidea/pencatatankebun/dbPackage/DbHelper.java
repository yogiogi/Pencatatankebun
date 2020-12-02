package com.sensus.diginidea.pencatatankebun.dbPackage;

//import java.util.ArrayList;

import java.util.Date;
import java.util.HashSet;
//import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
//import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "sensus_kebun.db";
    // tasks table name
    private static final String TABLE_IDENTITAS = "tbl_identitas";
    private static final String TABLE_LUAS_KEBUN_PETANI = "tbl_luas_kebun_petani";
    private static final String TABLE_LUAS_KEBUN_PERUSAHAAN = "tbl_luas_kebun_perusahaan";
    private static final String TABLE_REALISASI_PETANI = "tbl_realisasi_petani";
    private static final String TABLE_REALISASI_PERUSAHAAN = "tbl_realisasi_perusahaan";
    private static final String TABLE_PRODUKSI_PETANI = "tbl_produksi_petani";
    private static final String TABLE_PRODUKSI_PERUSAHAAN = "tbl_produksi_perusahaan";
    private static final String TABLE_EST_LUAS_KEBUN_PETANI = "tbl_est_luas_kebun_petani";
    private static final String TABLE_EST_LUAS_KEBUN_PERUSAHAAN = "tbl_est_luas_kebun_perusahaan";
    private static final String TABLE_EST_PRODUKSI_PETANI = "tbl_est_produksi_kebun_petani";
    private static final String TABLE_EST_PRODUKSI_PERUSAHAAN = "tbl_est_produksi_kebun_perusahaan";
    private static final String TABLE_MST_MANBUN = "mst_manbun";
    private static final String TABLE_MST_JENISTANAMAN = "mst_jenis_tanaman";
    private static final String TABLE_MST_WUJUDPRODUKSI = "mst_wujud_produksi";
    private static final String TABLE_MST_KOMODITAS = "mst_komoditas";
    private static final String TABLE_MST_OBJEK_SENSUS = "objek_sensus";
    private static final String TABLE_MST_PROPINSI = "mst_propinnsi";
    private static final String TABLE_MST_KABKOT = "mst_kabkot";
    private static final String TABLE_MST_KECAMATAN = "mst_kecamatan";
    private static final String TABLE_MST_DESA = "mst_desa";
    private static final String TABLE_USER = "tbl_user";
    private static final String TABLE_SETTING = "tb_setting";
    private static final String TABLE_SATUAN = "tb_satuan";

    public static final String KEY_SETURL = "setURL";

    //untuk table provinsi
    private static final String KEY_KODEPROP = "kode_propinsi";
    private static final String KEY_NAMAPROP = "nama_propinsi";
    public static final String KEY_ID = "id";

    // untuk tabel IDENTITAS
    public static final String KEY_IDSENSUS = "id_sensus";
    public static final String KEY_IDOBJSENSUS = "id_obj_sensus";
    public static final String KEY_NAMAOBJSENSUS = "objek_sensus";
    public static final String KEY_NAMA = "nama";
    public static final String KEY_TELP = "telp";
    public static final String KEY_KELTANI = "kelompok_tani";
    public static final String KEY_KODEKABKOT = "kode_kabupaten";
    public static final String KEY_KODEKABKOT2 = "kode_kabupaten2";
    public static final String KEY_NAMAKABKOT = "nama_kab_kot";
    public static final String KEY_KODEKECAMATAN = "kode_kecamatan";
    public static final String KEY_KODEKECAMATAN2 = "kode_kecamatan2";
    public static final String KEY_NAMAKECAMATAN = "nama_kecamatan";
    public static final String KEY_KODEDESA = "kode_desa";
    public static final String KEY_KODEDESA2 = "kode_desa2";
    public static final String KEY_NAMADESA = "nama_desa";
    public static final String KEY_NOURUT = "no_urut";
    public static final String KEY_IDMANBUN = "id_manbun";
    public static final String KEY_TGLCATAT = "tgl_catat";
    private static final String KEY_STATUSREAL = "status_realisasi"; //terkirim, belum terkirim


    //untuk tabel luas_kebun_pekebun
    public static final String KEY_IDKOMODITAS = "id_komoditas";
    public static final String KEY_IDTANAMAN = "id_tanaman";
    public static final String KEY_KOMODITAS = "komoditas";
    public static final String KEY_TBM = "tbm";
    public static final String KEY_TM = "tm";
    public static final String KEY_TTM = "ttm";
    public static final String KEY_KET = "keterangan";

    //untuk tabel luas_kebun_perusahaan
    public static final String KEY_TBMINTI = "tbm_inti";
    public static final String KEY_TMINTI = "tm_inti";
    public static final String KEY_TTMINTI = "ttm_inti";
    public static final String KEY_TBMPLASMA = "tbm_plasma";
    public static final String KEY_TMPLASMA = "tm_plasma";
    public static final String KEY_TTMPLASMA = "ttm_plasma";

    //untuk real_produk_perbulan petani
    private static final String KEY_BULAN = "bulan";
    private static final String KEY_JML_PROD = "jumlah_prod";
    public static final String KEY_WUJUDPROD = "wujud_produksi";
    public static final String KEY_IDWUJUDPROD = "id_wujud_produksi";
    private static final String KEY_NILAIJUAL = "nilai_jual";
    private static final String KEY_HARGA = "harga";
    public static final String KEY_SATUAN = "satuan";
    public static final String KEY_KONVERSI = "konversi";

    //untuk real_produk_perbulan perusahaan
    private static final String KEY_TGLPENDATAAN = "tgl_pendataan";
    private static final String KEY_DIJUAL = "dijual";
    private static final String KEY_DISIMPAN = "disimpan";
    private static final String KEY_KONSUMSI = "konsumsi";

    private static final String KEY_DESKRIPSI = "deskripsi";

    private static final String KEY_USERNAME = "user";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ENUMID = "enumID";

    public static final String KEY_JENISTANAMAN = "jenis_tanaman";

    public String url_sen_identitas = "/sen_identitas_tes.php";
    public String url_sen_identitas_perusahaan = "/sen_identitas_perusahaan.php";

    public String url_sen_luas_kebun_petani = "/sen_luas_kebun_petani.php";
    public String url_sen_luas_kebun_perusahaan = "/sen_luas_kebun_perusahaan.php";
    public String url_sen_produksi_petani = "/sen_produksi_petani.php";
    public String url_sen_produksi_perusahaan = "/sen_produksi_perusahaan.php";
    public String url_sen_realisasi_petani = "/sen_realisasi_petani.php";
    public String url_sen_realisasi_perusahaan = "/sen_realisasi_perusahaan.php";
    public String url_sen_est_produksi_perusahaan = "/sen_est_produksi_perusahaan.php";
    public String url_sen_est_produksi_pertanian = "/sen_est_produksi_pertanian.php";
    public String url_sen_est_luas_kebun_petani = "/sen_est_luas_kebun_petani.php";
    public String url_sen_est_luas_kebun_perusahaan = "/sen_est_luas_kebun_perusahaan.php";

//	private static final String[] COLUMNS = { KEY_IDKK, KEY_NARA, KEY_DESA, KEY_KEC, KEY_KAB, KEY_KTP, KEY_HP, KEY_NAMAKK, KEY_JMLKK,
//		KEY_TMPLHR, KEY_TGLLHR, KEY_PDDK, KEY_SUKU, KEY_PKJUTAMA, KEY_PKJLAIN, KEY_AGTORG, KEY_TGLINPUT, KEY_WKTINPUT};

//	private SQLiteDatabase dbase;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql_identitas = "CREATE TABLE IF NOT EXISTS tbl_identitas (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " id_obj_sensus INTEGER," +
                " nama VARCHAR," +
                " telp VARCHAR," +
                " kelompok_tani VARCHAR," +
                " kode_propinsi VARCHAR(2)," +
                " kode_kabupaten VARCHAR(2)," +
                " kode_kecamatan VARCHAR(3)," +
                " kode_desa VARCHAR(3)," +
                " no_urut INTEGER," +
                " id_manbun INTEGER," +
                " tgl_catat VARCHAR," +
                " status INTEGER DEFAULT 0," +
                " create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(sql_identitas);

        String sql_luas_kpetani = "CREATE TABLE IF NOT EXISTS tbl_luas_kebun_petani (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " jenis_tanaman INTEGER," +
                " id_komoditas INTEGER," +
                " tbm DOUBLE," +
                " tm DOUBLE," +
                " ttm DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0," +
                " status_realisasi BOOLEAN DEFAULT 0," +
                " status_prod BOOLEAN DEFAULT 0, " +
                " status_est_prod BOOLEAN DEFAULT 0, " +
                " status_est_luas_area BOOLEAN DEFAULT 0)";
        db.execSQL(sql_luas_kpetani);

        String sql_luas_kperusahaan = "CREATE TABLE IF NOT EXISTS tbl_luas_kebun_perusahaan (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " jenis_tanaman INTEGER," +
                " id_komoditas INTEGER," +
                " tbm_inti DOUBLE," +
                " tm_inti DOUBLE," +
                " ttm_inti DOUBLE," +
                " tbm_plasma DOUBLE," +
                " tm_plasma DOUBLE," +
                " ttm_plasma DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0," +
                " status_realisasi BOOLEAN DEFAULT 0," +
                " status_prod BOOLEAN DEFAULT 0, " +
                " status_est_prod BOOLEAN DEFAULT 0, " +
                " status_est_luas_area BOOLEAN DEFAULT 0)";
        db.execSQL(sql_luas_kperusahaan);

        String sql_realisasi_petani = "CREATE TABLE IF NOT EXISTS tbl_realisasi_petani (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " id_komoditas INTEGER," +
                " bulan INTEGER," +
                " jumlah_prod DOUBLE," +
                " wujud_produksi VARCHAR(15)," +
                " satuan VARCHAR(15)," +
                " nilai_jual DOUBLE," +
                " harga DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0)";
        db.execSQL(sql_realisasi_petani);

        String sql_realisasi_perusahaan = "CREATE TABLE IF NOT EXISTS tbl_realisasi_perusahaan (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " id_komoditas INTEGER," +
                " bulan INTEGER," +
                " jumlah_prod DOUBLE," +
                " wujud_produksi VARCHAR(15)," +
                " satuan VARCHAR(15)," +
                " nilai_jual DOUBLE," +
                " harga DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0)";
        db.execSQL(sql_realisasi_perusahaan);

        String sql_produksi_petani = "CREATE TABLE IF NOT EXISTS tbl_produksi_petani (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " id_komoditas INTEGER," +
                " tgl_pendataan DATETIME ," +
                " jumlah_prod DOUBLE," +
                " wujud_produksi VARCHAR(15)," +
                " satuan VARCHAR(15)," +
                " dijual DOUBLE," +
                " disimpan DOUBLE," +
                " konsumsi DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0)";
        db.execSQL(sql_produksi_petani);

        String sql_produksi_perusahaan = "CREATE TABLE IF NOT EXISTS tbl_produksi_perusahaan (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " id_komoditas INTEGER," +
                " tgl_pendataan DATETIME ," +
                " jumlah_prod DOUBLE," +
                " wujud_produksi VARCHAR(15)," +
                " satuan VARCHAR(15)," +
                " dijual DOUBLE," +
                " disimpan DOUBLE," +
                " konsumsi DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0)";
        db.execSQL(sql_produksi_perusahaan);

        String sql_est_luas_petani = "CREATE TABLE IF NOT EXISTS tbl_est_luas_kebun_petani (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " jenis_tanaman INTEGER," +
                " id_komoditas INTEGER," +
                " tbm DOUBLE," +
                " tm DOUBLE," +
                " ttm DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0)";
        db.execSQL(sql_est_luas_petani);

        String sql_est_produksi_petani = "CREATE TABLE IF NOT EXISTS tbl_est_produksi_kebun_petani (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " id_komoditas INTEGER," +
                " tgl_pendataan TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                " jumlah_prod DOUBLE," +
                " wujud_produksi VARCHAR(15)," +
                " satuan VARCHAR(15)," +
                " nilai_jual DOUBLE," +
                " harga DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0)";
        db.execSQL(sql_est_produksi_petani);

        String sql_est_luas_perusahaan = "CREATE TABLE IF NOT EXISTS tbl_est_luas_kebun_perusahaan (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " jenis_tanaman INTEGER," +
                " id_komoditas INTEGER," +
                " tbm_inti DOUBLE," +
                " tm_inti DOUBLE," +
                " ttm_inti DOUBLE," +
                " tbm_plasma DOUBLE," +
                " tm_plasma DOUBLE," +
                " ttm_plasma DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0)";
        db.execSQL(sql_est_luas_perusahaan);

        String sql_est_produksi_perusahaan = "CREATE TABLE IF NOT EXISTS tbl_est_produksi_kebun_perusahaan (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_sensus VARCHAR(15)," +
                " id_komoditas INTEGER," +
                " tgl_pendataan TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                " jumlah_prod DOUBLE," +
                " wujud_produksi VARCHAR(15)," +
                " satuan VARCHAR(15)," +
                " nilai_jual DOUBLE," +
                " harga DOUBLE," +
                " keterangan TEXT," +
                " status INTEGER DEFAULT 0)";
        db.execSQL(sql_est_produksi_perusahaan);

        String sql_mst_manbun = "CREATE TABLE IF NOT EXISTS mst_manbun (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_manbun VARCHAR(10)," +
                " nama VARCHAR(50)," +
                " telp VARCHAR(15)," +
                " kode_kecamatan VARCHAR(15))";
        db.execSQL(sql_mst_manbun);

        String sql_user = "CREATE TABLE IF NOT EXISTS tb_user (" +
                " id_user INTEGER PRIMARY KEY AUTOINCREMENT," +
                " user VARCHAR(50)," +
                " password VARCHAR(50)," +
                " enumID INTEGER)";
        db.execSQL(sql_user);

        String sql_mst_wujud_produksi = "CREATE TABLE IF NOT EXISTS mst_wujud_produksi (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_komoditas INTEGER," +
                " wujud_produksi VARCHAR(25)," +
                " konversi DECIMAL DEFAULT 1)";
        db.execSQL(sql_mst_wujud_produksi);

        String sql_mst_komoditas = "CREATE TABLE IF NOT EXISTS mst_komoditas (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_tanaman INTEGER," +
                " id_komoditas INTEGER," +
                " komoditas VARCHAR(25))";
        db.execSQL(sql_mst_komoditas);

        String sql_jenis = "CREATE TABLE IF NOT EXISTS mst_jenis_tanaman (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " jenis_tanaman VARCHAR(200))";
        db.execSQL(sql_jenis);

        String sql_objek_sensus = "CREATE TABLE IF NOT EXISTS mst_objek_sensus (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_obj_sensus INTEGER," +
                " objek_sensus VARCHAR(25))";
        db.execSQL(sql_objek_sensus);

        String sql_prop = "CREATE TABLE IF NOT EXISTS mst_propinsi (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kode_propinsi VARCHAR(2)," +
                " nama_propinsi VARCHAR(50))";
        db.execSQL(sql_prop);

        String sql_kabkot = "CREATE TABLE IF NOT EXISTS mst_kabkot (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kode_propinsi VARCHAR(2)," +
                " kode_kabupaten VARCHAR(4)," +
                " kode_kabupaten2 VARCHAR(4)," +
                " nama_kab_kot VARCHAR(50))";
        db.execSQL(sql_kabkot);

        String sql_kec = "CREATE TABLE IF NOT EXISTS mst_kecamatan (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kode_kabupaten INTEGER(4)," +
                " kode_kecamatan VARCHAR(3)," +
                " kode_kecamatan2 VARCHAR(3)," +
                " nama_kecamatan VARCHAR(50))";
        db.execSQL(sql_kec);

        String sql_desa = "CREATE TABLE IF NOT EXISTS mst_desa (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " kode_kecamatan VARCHAR(3)," +
                " kode_desa VARCHAR(10)," +
                " kode_desa2 VARCHAR(10)," +
                " nama_desa VARCHAR(50))";
        db.execSQL(sql_desa);

        String sql_bulan = "CREATE TABLE IF NOT EXISTS mst_bulan (" +
                " id INTEGER PRIMARY KEY," +
                " bulan VARCHAR(10))";
        db.execSQL(sql_bulan);

        String sql_kosong = "CREATE TABLE IF NOT EXISTS tb_kosong (" +
                " id_kosong INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_kk INTEGER(11)," +
                " shape_id VARCHAR(50)," +
                " deskripsi VARCHAR(250))";
        db.execSQL(sql_kosong);

        String sql_setting = "CREATE TABLE IF NOT EXISTS tb_setting (" +
                " id_set INTEGER PRIMARY KEY AUTOINCREMENT," +
                " setURL VARCHAR(200))";
        db.execSQL(sql_setting);

        String sql_satuan = "CREATE TABLE IF NOT EXISTS tb_satuan (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " id_komoditas INTEGER," +
                " satuan VARCHAR(200))";
        db.execSQL(sql_satuan);
    }

    public void delete(String mst) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(mst, null, null);
        db.close();
    }

    public void insertURL() {
        ContentValues val = new ContentValues();
        val.put(KEY_SETURL, "http://smallholder.inobu.org/");
        getWritableDatabase().insert(TABLE_SETTING, null, val);
    }

    public void insertCount(String name) {
        ContentValues val = new ContentValues();
        val.put(KEY_USERNAME, name);

        getWritableDatabase().insert(TABLE_USER, null, val);
    }

    public void insertUser(String username, String password, String enumid) {
        ContentValues val = new ContentValues();
        val.put(KEY_USERNAME, username);
        val.put(KEY_PASSWORD, password);
        val.put(KEY_ENUMID, enumid);

        getWritableDatabase().insert(TABLE_USER, null, val);
    }

    public void updateStatus(String tabel, String column, int id, int status) {
        ContentValues val = new ContentValues();
        val.put(column, status);

        getWritableDatabase().update(tabel, val, "id = " + id, null);
        close();
    }

    public void updatebyid_sensus(String tabel, String column, String id, int status) {
        ContentValues val = new ContentValues();
        val.put(column, status);

        getWritableDatabase().update(tabel, val, "id_sensus= '" + id + "'", null);
        close();
    }

    public boolean insertIdentitas(String id_sensus, int id_obj_sensus, String nama, String telp, String kelompok_tani,
                                   String propinsi, String kabupaten, String kecamatan, String desa, String nourut,
                                   String id_manbun, String tgl_catat) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_IDOBJSENSUS, id_obj_sensus);
        val.put(KEY_NAMA, nama);
        val.put(KEY_TELP, telp);
        val.put(KEY_KELTANI, kelompok_tani);
        val.put(KEY_KODEPROP, propinsi);
        val.put(KEY_KODEKABKOT, kabupaten);
        val.put(KEY_KODEKECAMATAN, kecamatan);
        val.put(KEY_KODEDESA, desa);
        val.put(KEY_NOURUT, nourut);
        val.put(KEY_IDMANBUN, id_manbun);
        val.put(KEY_TGLCATAT, tgl_catat);

        getWritableDatabase().insert(TABLE_IDENTITAS, null, val);
        return true;
    }

    public boolean updateIdentitas(String idROW, String id_sensus, int id_obj_sensus, String nama, String telp, String kelompok_tani,
                                   String propinsi, String kabupaten, String kecamatan, String desa, String nourut,
                                   String id_manbun, String tgl_catat) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_IDOBJSENSUS, id_obj_sensus);
        val.put(KEY_NAMA, nama);
        val.put(KEY_TELP, telp);
        val.put(KEY_KELTANI, kelompok_tani);
        val.put(KEY_KODEPROP, propinsi);
        val.put(KEY_KODEKABKOT, kabupaten);
        val.put(KEY_KODEKECAMATAN, kecamatan);
        val.put(KEY_KODEDESA, desa);
        val.put(KEY_NOURUT, nourut);
        val.put(KEY_IDMANBUN, id_manbun);
        val.put(KEY_TGLCATAT, tgl_catat);

        getWritableDatabase().update(TABLE_IDENTITAS, val, "id=" + idROW, null);
        return true;
    }

    public boolean updateManbun(String id_manbun, String no_telp) {
        ContentValues val = new ContentValues();
        val.put(KEY_TELP, no_telp);
        getWritableDatabase().update(TABLE_MST_MANBUN, val, "id_manbun=" + id_manbun, null);
        return true;
    }

    public boolean insertLuasKPetani(String id_sensus, int id_jenis, int id_komoditas, Double tbm, Double tm, Double ttm, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_JENISTANAMAN, id_jenis);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_TBM, tbm);
        val.put(KEY_TM, tm);
        val.put(KEY_TTM, ttm);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_LUAS_KEBUN_PETANI, null, val);

        return true;
    }

    public boolean updateLuasKPerusahaanID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);

        getWritableDatabase().update(TABLE_LUAS_KEBUN_PERUSAHAAN, val, "id_sensus='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateLuasKPetanID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);

        getWritableDatabase().update(TABLE_LUAS_KEBUN_PETANI, val, "id_sensus='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateLuasKPetani(long id, int id_jenis, int id_komoditas, Double tbm, Double tm, Double ttm, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_JENISTANAMAN, id_jenis);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_TBM, tbm);
        val.put(KEY_TM, tm);
        val.put(KEY_TTM, ttm);
        val.put(KEY_KET, keterangan);

        getWritableDatabase().update(TABLE_LUAS_KEBUN_PETANI, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertLuasKPerusahaan(String id_sensus, int id_jenis, int id_komoditas, Double tbm_inti, Double tm_inti, Double ttm_inti,
                                         Double tbm_plasma, Double tm_plasma, Double ttm_plasma, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_JENISTANAMAN, id_jenis);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_TBMINTI, tbm_inti);
        val.put(KEY_TMINTI, tm_inti);
        val.put(KEY_TTMINTI, ttm_inti);
        val.put(KEY_TBMPLASMA, tbm_plasma);
        val.put(KEY_TMPLASMA, tm_plasma);
        val.put(KEY_TTMPLASMA, ttm_plasma);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_LUAS_KEBUN_PERUSAHAAN, null, val);
        return true;
    }

    public boolean updateLuasKPerusahaan(long id, int id_jenis, int id_komoditas, Double tbm_inti, Double tm_inti, Double ttm_inti,
                                         Double tbm_plasma, Double tm_plasma, Double ttm_plasma, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_JENISTANAMAN, id_jenis);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_TBMINTI, tbm_inti);
        val.put(KEY_TMINTI, tm_inti);
        val.put(KEY_TTMINTI, ttm_inti);
        val.put(KEY_TBMPLASMA, tbm_plasma);
        val.put(KEY_TMPLASMA, tm_plasma);
        val.put(KEY_TTMPLASMA, ttm_plasma);
        val.put(KEY_KET, keterangan);

        getWritableDatabase().update(TABLE_LUAS_KEBUN_PERUSAHAAN, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertRealisasiPetani(String id_sensus, int id_komoditas, int bulan, Double jumlah_prod, String wujud_produksi,
                                         String satuan, double nilai_jual, double harga, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_BULAN, bulan);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_NILAIJUAL, nilai_jual);
        val.put(KEY_HARGA, harga);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_REALISASI_PETANI, null, val);

        return true;
    }

    public boolean updateRealisasiKebunID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        getWritableDatabase().update(TABLE_REALISASI_PETANI, val, "id_sensus ='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateRealisasiPerusahaanID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        getWritableDatabase().update(TABLE_REALISASI_PERUSAHAAN, val, "id_sensus ='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateRealisasiPetani(long id, int id_komoditas, int bulan, Double jumlah_prod, String wujud_produksi,
                                         String satuan, double nilai_jual, double harga, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_BULAN, bulan);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_NILAIJUAL, nilai_jual);
        val.put(KEY_HARGA, harga);
        val.put(KEY_KET, keterangan);

        getWritableDatabase().update(TABLE_REALISASI_PETANI, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertRealisasiPerusahaan(String id_sensus, int id_komoditas, int bulan, Double jumlah_prod, String wujud_produksi,
                                             String satuan, double nilai_jual, double harga, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_BULAN, bulan);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_NILAIJUAL, nilai_jual);
        val.put(KEY_HARGA, harga);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_REALISASI_PERUSAHAAN, null, val);
        return true;
    }

    public boolean updateRealisasiPerusahaan(long id, int id_komoditas, int bulan, Double jumlah_prod, String wujud_produksi,
                                             String satuan, double nilai_jual, double harga, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_BULAN, bulan);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_NILAIJUAL, nilai_jual);
        val.put(KEY_HARGA, harga);
        val.put(KEY_KET, keterangan);

        getWritableDatabase().update(TABLE_REALISASI_PERUSAHAAN, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertProduksiPetani(String id_sensus, int id_komoditas, Double jumlah_prod, String wujud_produksi, String satuan,
                                        String tgl_pendataan, Double dijual, Double disimpan, Double konsumsi, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_TGLPENDATAAN, tgl_pendataan);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_DIJUAL, dijual);
        val.put(KEY_DISIMPAN, disimpan);
        val.put(KEY_KONSUMSI, konsumsi);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_PRODUKSI_PETANI, null, val);

        return true;
    }

    public boolean updateEstLuasKebunPerusahaanID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        getWritableDatabase().update(TABLE_EST_LUAS_KEBUN_PERUSAHAAN, val, "id_sensus ='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateEstLuasKebunPetaniID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        getWritableDatabase().update(TABLE_EST_LUAS_KEBUN_PETANI, val, "id_sensus ='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateEstProduksiPerusahaanID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        getWritableDatabase().update(TABLE_EST_PRODUKSI_PERUSAHAAN, val, "id_sensus ='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateEstProduksiPetaniID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        getWritableDatabase().update(TABLE_EST_PRODUKSI_PETANI, val, "id_sensus ='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateProduksiPerusahaanID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        getWritableDatabase().update(TABLE_PRODUKSI_PERUSAHAAN, val, "id_sensus ='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateProduksiPetaniID(String id_sensus, String id_sensuslama) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        getWritableDatabase().update(TABLE_PRODUKSI_PETANI, val, "id_sensus ='" + id_sensuslama + "'", null);
        return true;
    }

    public boolean updateProduksiPetani(long id, int id_komoditas, Double jumlah_prod, String wujud_produksi, String satuan,
                                        String tgl_pendataan, Double dijual, Double disimpan, Double konsumsi, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_TGLPENDATAAN, tgl_pendataan);
        val.put(KEY_DIJUAL, dijual);
        val.put(KEY_DISIMPAN, disimpan);
        val.put(KEY_KONSUMSI, konsumsi);
        val.put(KEY_KET, keterangan);

        getWritableDatabase().update(TABLE_PRODUKSI_PETANI, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertProduksiPerusahaan(String id_sensus, int id_komoditas, Double jumlah_prod, String wujud_produksi, String satuan,
                                            String tgl_pendataan, Double dijual, Double disimpan, Double konsumsi, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_TGLPENDATAAN, tgl_pendataan);
        val.put(KEY_DIJUAL, dijual);
        val.put(KEY_DISIMPAN, disimpan);
        val.put(KEY_KONSUMSI, konsumsi);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_PRODUKSI_PERUSAHAAN, null, val);

        return true;
    }

    public boolean updateProduksiPerusahaan(long id, int id_komoditas, Double jumlah_prod, String wujud_produksi, String satuan,
                                            String tgl_pendataan, Double dijual, Double disimpan, Double konsumsi, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_TGLPENDATAAN, tgl_pendataan);
        val.put(KEY_DIJUAL, dijual);
        val.put(KEY_DISIMPAN, disimpan);
        val.put(KEY_KONSUMSI, konsumsi);
        val.put(KEY_KET, keterangan);

        getWritableDatabase().update(TABLE_PRODUKSI_PERUSAHAAN, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertEstLuasPetani(String id_sensus, int id_jenis, int id_komoditas, Double tbm, Double tm, Double ttm, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_JENISTANAMAN, id_jenis);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_TBM, tbm);
        val.put(KEY_TM, tm);
        val.put(KEY_TTM, ttm);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_EST_LUAS_KEBUN_PETANI, null, val);

        return true;
    }

    public boolean updateEstLuasPetani(long id, int id_jenis, int id_komoditas, Double tbm, Double tm, Double ttm, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_JENISTANAMAN, id_jenis);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_TBM, tbm);
        val.put(KEY_TM, tm);
        val.put(KEY_TTM, ttm);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().update(TABLE_EST_LUAS_KEBUN_PETANI, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertEstLuasPerusahaan(String id_sensus, int id_jenis, int id_komoditas, Double tbm_inti, Double tm_inti, Double ttm_inti,
                                           Double tbm_plasma, Double tm_plasma, Double ttm_plasma, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_JENISTANAMAN, id_jenis);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_TBMINTI, tbm_inti);
        val.put(KEY_TMINTI, tm_inti);
        val.put(KEY_TTMINTI, ttm_inti);
        val.put(KEY_TBMPLASMA, tbm_plasma);
        val.put(KEY_TMPLASMA, tm_plasma);
        val.put(KEY_TTMPLASMA, ttm_plasma);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_EST_LUAS_KEBUN_PERUSAHAAN, null, val);

        return true;
    }

    public boolean updateEstLuasPerusahaan(long id, int id_jenis, int id_komoditas, Double tbm_inti, Double tm_inti, Double ttm_inti,
                                           Double tbm_plasma, Double tm_plasma, Double ttm_plasma, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_JENISTANAMAN, id_jenis);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_TBMINTI, tbm_inti);
        val.put(KEY_TMINTI, tm_inti);
        val.put(KEY_TTMINTI, ttm_inti);
        val.put(KEY_TBMPLASMA, tbm_plasma);
        val.put(KEY_TMPLASMA, tm_plasma);
        val.put(KEY_TTMPLASMA, ttm_plasma);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().update(TABLE_EST_LUAS_KEBUN_PERUSAHAAN, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertEstProdPetani(String id_sensus, int id_komoditas, Double jumlah_prod, String wujud_produksi, String satuan,
                                       Double nilai_jual, Double harga, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_NILAIJUAL, nilai_jual);
        val.put(KEY_HARGA, harga);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_EST_PRODUKSI_PETANI, null, val);
        return true;
    }

    public boolean updateEstProdPetani(long id, int id_komoditas, Double jumlah_prod, String wujud_produksi, String satuan,
                                       Double nilai_jual, Double harga, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_NILAIJUAL, nilai_jual);
        val.put(KEY_HARGA, harga);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().update(TABLE_EST_PRODUKSI_PETANI, val, "id='" + id + "'", null);
        return true;
    }

    public boolean insertEstProdPerusahaan(String id_sensus, int id_komoditas, Double jumlah_prod, String wujud_produksi, String satuan,
                                           Double nilai_jual, Double harga, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDSENSUS, id_sensus);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_NILAIJUAL, nilai_jual);
        val.put(KEY_HARGA, harga);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().insert(TABLE_EST_PRODUKSI_PERUSAHAAN, null, val);
        return true;
    }

    public boolean updateEstProdPerusahaan(long id, int id_komoditas, Double jumlah_prod, String wujud_produksi, String satuan,
                                           Double nilai_jual, Double harga, String keterangan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_JML_PROD, jumlah_prod);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        val.put(KEY_SATUAN, satuan);
        val.put(KEY_NILAIJUAL, nilai_jual);
        val.put(KEY_HARGA, harga);
        val.put(KEY_KET, keterangan);
        getWritableDatabase().update(TABLE_EST_PRODUKSI_PERUSAHAAN, val, "id='" + id + "'", null);
        return true;
    }

    public void insertSatuan(String id_komoditas, String satuan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_SATUAN, satuan);
        getWritableDatabase().insert(TABLE_SATUAN, null, val);
    }

//    public void updateSatuan(String id_komoditas, String wujud, String satuan) {
//        ContentValues val = new ContentValues();
//        val.put(KEY_WUJUDPROD, wujud);
//        getWritableDatabase().update(TABLE_SATUAN, val, "id_komoditas ='" + id_komoditas + "' and satuan = " + satuan + "'", null);
//    }

    public void insertwujudprod(String id_komoditas, String wujud_produksi) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_WUJUDPROD, wujud_produksi);
        getWritableDatabase().insert(TABLE_MST_WUJUDPRODUKSI, null, val);
    }

    public void updatewujudprod(String id_komoditas, String wujud_produksi, Double konversi) {
        ContentValues val = new ContentValues();
        val.put(KEY_KONVERSI, konversi);
//        val.put(KEY_SATUAN, satuan);
        getWritableDatabase().update(TABLE_MST_WUJUDPRODUKSI, val, "id_komoditas ='" + id_komoditas + "' and wujud_produksi = '" + wujud_produksi + "'", null);
    }

    public void insertMstKomoditas(String id_tanaman, String id_komoditas, String komoditas) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDTANAMAN, id_tanaman);
        val.put(KEY_IDKOMODITAS, id_komoditas);
        val.put(KEY_KOMODITAS, komoditas);
        getWritableDatabase().insert(TABLE_MST_KOMODITAS, null, val);
    }

    public void insertJenisTanaman(String jenisTanaman) {
        ContentValues val = new ContentValues();
        val.put(KEY_JENISTANAMAN, jenisTanaman);
        getWritableDatabase().insert(TABLE_MST_JENISTANAMAN, null, val);
    }

    public void insertProp(String kode_propinsi, String propinsi) {
        ContentValues val = new ContentValues();
        val.put(KEY_KODEPROP, kode_propinsi);
        val.put(KEY_NAMAPROP, propinsi);
        getWritableDatabase().insert(TABLE_MST_PROPINSI, null, val);
    }

    public void updateProp(long id, String kode_propinsi, String propinsi) {
        ContentValues val = new ContentValues();
        val.put(KEY_KODEPROP, kode_propinsi);
        val.put(KEY_NAMAPROP, propinsi);
        getWritableDatabase().update(TABLE_MST_PROPINSI, val, "id='" + id + "'", null);
    }

    public void insertKab(String kode_propinsi, String kode_kabupaten, String kode_kabupaten2, String nama_kab_kot) {
        ContentValues val = new ContentValues();
        val.put(KEY_KODEPROP, kode_propinsi);
        val.put(KEY_KODEKABKOT, kode_kabupaten);
        val.put(KEY_KODEKABKOT2, kode_kabupaten2);
        val.put(KEY_NAMAKABKOT, nama_kab_kot);
        getWritableDatabase().insert(TABLE_MST_KABKOT, null, val);
    }

    public void updateKab(long id, String kode_propinsi, String kode_kabupaten, String nama_kab_kot) {
        ContentValues val = new ContentValues();
        val.put(KEY_KODEPROP, kode_propinsi);
        val.put(KEY_KODEKABKOT, kode_kabupaten);
        val.put(KEY_NAMAKABKOT, nama_kab_kot);
        getWritableDatabase().update(TABLE_MST_KABKOT, val, "id='" + id + "'", null);
    }

    public void insertbulan(int id, String bulan) {
        ContentValues val = new ContentValues();
        val.put("id", id);
        val.put(KEY_BULAN, bulan);
        getWritableDatabase().insert("mst_bulan", null, val);
    }

    public void insertManbun(String id_manbun, String nama, String noTelp, String kode_kecamatan) {
        ContentValues val = new ContentValues();
        val.put(KEY_IDMANBUN, id_manbun);
        val.put(KEY_NAMA, nama);
        val.put(KEY_TELP, noTelp);
        val.put(KEY_KODEKECAMATAN, kode_kecamatan);
        getWritableDatabase().insert(TABLE_MST_MANBUN, null, val);
    }

    public void insertKec(String kode_kabupaten, String kode_kecamatan, String kode_kecamatan2, String nama_kecamatan) {
        ContentValues val = new ContentValues();
        val.put(KEY_KODEKABKOT, kode_kabupaten);
        val.put(KEY_KODEKECAMATAN, kode_kecamatan);
        val.put(KEY_KODEKECAMATAN2, kode_kecamatan2);
        val.put(KEY_NAMAKECAMATAN, nama_kecamatan);
        getWritableDatabase().insert(TABLE_MST_KECAMATAN, null, val);
    }

    public void updateKec(long id, int kode_kabupaten, String kode_kecamatan, String kode_kecamatan2, String nama_kecamatan) {
        ContentValues val = new ContentValues();
        val.put(KEY_KODEKABKOT, kode_kabupaten);
        val.put(KEY_KODEKECAMATAN, kode_kecamatan);
        val.put(KEY_KODEKECAMATAN2, kode_kecamatan2);
        val.put(KEY_NAMAKECAMATAN, nama_kecamatan);
        getWritableDatabase().update(TABLE_MST_KECAMATAN, val, "id='" + id + "'", null);
    }

    public void insertDesa(String kode_kecamatan, String kode_desa, String kode_desa2, String nama_desa) {
        ContentValues val = new ContentValues();
        val.put(KEY_KODEKECAMATAN, kode_kecamatan);
        val.put(KEY_KODEDESA, kode_desa);
        val.put(KEY_KODEDESA2, kode_desa2);
        val.put(KEY_NAMADESA, nama_desa);
        getWritableDatabase().insert(TABLE_MST_DESA, null, val);
    }

    public void updateDesa(long id, String kode_kecamatan, String kode_desa, String kode_desa2, String nama_desa) {
        ContentValues val = new ContentValues();
        val.put(KEY_KODEKECAMATAN, kode_kecamatan);
        val.put(KEY_KODEDESA, kode_desa);
        val.put(KEY_KODEDESA, kode_desa2);
        val.put(KEY_NAMADESA, nama_desa);
        getWritableDatabase().update(TABLE_MST_DESA, val, "id='" + id + "'", null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_IDENTITAS);
//		onCreate(db);
//
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUAS_KEBUN_PETANI);
//		onCreate(db);
//
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LUAS_KEBUN_PERUSAHAAN);
//		onCreate(db);
//
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REALISASI_PETANI);
//		onCreate(db);
//
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REALISASI_PERUSAHAAN);
//		onCreate(db);
//
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUKSI_PETANI);
//		onCreate(db);
//
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUKSI_PERUSAHAAN);
//		onCreate(db);
//
//		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EST_LUAS_KEBUN_PETANI);
//		onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EST_LUAS_KEBUN_PERUSAHAAN);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EST_PRODUKSI_PETANI);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EST_PRODUKSI_PERUSAHAAN);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_MANBUN);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_KOMODITAS);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_OBJEK_SENSUS);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_PROPINSI);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_KABKOT);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_KECAMATAN);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MST_DESA);
//        onCreate(db);
//
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
//        onCreate(db);
    }

    public Cursor getSelectedKomoditas(String tabel, String id_sensus) {
        return (getReadableDatabase().rawQuery(
                "select a.id_komoditas, b.komoditas  from " + tabel + " a inner join mst_komoditas b on a.id_komoditas = b.id_komoditas where id_sensus = " + id_sensus + "", null));
    }

    public Cursor getAllIdentitas(int id_obj) {
        return (getReadableDatabase().rawQuery(
                "SELECT id, id_sensus, nama, id_obj_sensus, status FROM tbl_identitas WHERE id_obj_sensus = " + id_obj + " ORDER BY create_date", null));
    }

    public Cursor getAllIdentitasSensus(String id_sensus) {
        return (getReadableDatabase().rawQuery(
                "SELECT id, id_sensus FROM tbl_identitas where id_sensus = " + id_sensus + " ORDER BY create_date", null));
    }

    public Cursor getAllEstLuas_area(String tableName, String id_sensus) {
        Log.d("komoditas", "SELECT a.id, a.id_sensus, b.komoditas, b.id_komoditas FROM " + tableName + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas where a.id_sensus = " + id_sensus);
        return (getReadableDatabase().rawQuery(
                "SELECT a.id, a.id_sensus, b.komoditas, b.id_komoditas FROM " + tableName + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas where a.id_sensus = " + id_sensus + "", null));
    }

    public Cursor getAllReal(String tableName, String id_sensus, String nama_komo, String bulan) {
        Log.d("komoditas", "SELECT a.id, a.id_sensus, b.komoditas, a.id_komoditas, a.bulan FROM " + tableName + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas INNER JOIN mst_bulan c ON a.bulan = c.id where a.id_sensus = " + id_sensus + " and b.komoditas like '%" + nama_komo + "%' and c.bulan like '%" + bulan + "%'");
        return (getReadableDatabase().rawQuery(
                "SELECT a.id, a.id_sensus, b.komoditas, a.id_komoditas, a.bulan as bulan FROM " + tableName + " a INNER JOIN mst_komoditas b ON a.id_komoditas = b.id_komoditas INNER JOIN mst_bulan c ON a.bulan = c.id where a.id_sensus = " + id_sensus + " and b.komoditas like '%" + nama_komo + "%' and c.bulan like '%" + bulan + "%'", null));
    }

    public Cursor getAllProduksi(String tableName, String id_sensus, String id_komo) {
        Log.d("komoditas", "SELECT id, id_sensus, id_komoditas FROM " + tableName + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komo + "%'");
        return (getReadableDatabase().rawQuery(
                "SELECT id, id_sensus, id_komoditas FROM " + tableName + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komo + "%'", null));
//        SELECT * FROM " + tabelProduksi + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komoditas + "%'
    }

    public Cursor getDataLuasan(String tableName, String id_sensus, String id_komoditas) {
        return (getReadableDatabase().rawQuery(
                "SELECT * FROM " + tableName + " where id_sensus='" + id_sensus + "' and id_komoditas like '%" + id_komoditas + "%'", null));
    }

    // dari tabel kk
    public String getIDent(Cursor c) {
        return (c.getString(0));
    }

    public String getIDsensus(Cursor c) {
        return (c.getString(1));
    }

    public String getIDobj(Cursor c) {
        return (c.getString(2));
    }

    public String getIDStatus(Cursor c) {
        return (c.getString(3));
    }

//    public Cursor InstantSelect(String idKK) {
//        Log.d("idKK Response", idKK + "");
//        return (getReadableDatabase().rawQuery("SELECT * FROM tb_lahan WHERE id_kk = '" + idKK + "' ORDER BY id_lahan",
//                null));
//    }
//
//    public Cursor getAllLahan(String idKK) {
//        Log.d("idKK Response", idKK + "");
//        return (getReadableDatabase().rawQuery("SELECT * FROM tb_lahan WHERE id_kk = '" + idKK + "' and status = '0' ORDER BY id_lahan",
//                null));
//    }
//
//    public Cursor getAlltoLahan(String idKK, int idLahan) {
////		Log.d("idKK Response", idKK+"");
//        return (getReadableDatabase().rawQuery("SELECT * FROM tb_lahan WHERE id_kk = '" + idKK + "' and id_lahan=" + idLahan + " ORDER BY id_lahan",
//                null));
//    }
//
//    public Cursor getAllLahanKosong() {
//        return (getReadableDatabase().rawQuery("select * from tb_lahan where status_lahan is null and lama_lahan ='' and tahun_tanam_prod='0'",
//                null));
//    }
//
//    public Cursor getAlltoLhn(String idKK, String shapeid) {
//        Log.d("idKK Response", idKK + " " + shapeid);
//        return (getReadableDatabase().rawQuery("select * from tb_lahan where id_kk='" + idKK + "' and shape_id='" + shapeid + "' order by id_lahan",
//                null));
//    }

//    public Cursor getOneContact(long id) {
//        return (getReadableDatabase().query("tb_kk", null, "id_kk=" + id, null, null, null, null));
//    }

//	public Cursor getURL()
//	{
//	      return (getReadableDatabase().query("tb_setting", null, "id_set= 1", null, null, null, null));
//	}

    public String instant(String field_name, String table_name, String select, String value) {
        String row = "";
        try {
            String selectQuery = "SELECT " + field_name + " FROM " + table_name + " where " + select + " = " + value + "";
            Log.d("selectQuery", selectQuery);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getString(0);
        } catch (Throwable e) {

        }
        return row;
    }

    public String instantSelect(String field_name, String table_name, String select, String value) {
        String row = null;
        try {
            String selectQuery = "SELECT " + field_name + " FROM " + table_name + " where " + select + " = '" + value + "'";
            Log.d("selectQuery", selectQuery);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getString(0);
        } catch (Throwable e) {

        }
        return row;
    }

    public String instantSelectLike(String field_name, String table_name, String select, String value) {
        String row = null;
        try {
            String selectQuery = "SELECT " + field_name + " FROM " + table_name + " where " + select + " like '%" + value + "%'";
            Log.d("selectQuery", selectQuery);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getString(0);
        } catch (Throwable e) {

        }
        return row;
    }

//    public int getid() {
//        int row = 0;
//        String selectQuery = "SELECT MAX(id_kk) FROM tb_kk";
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//
//        row = cursor.getInt(0);
//        return row;
//    }

    public int getEnumID() {
        int row = 0;
        String selectQuery = "SELECT MAX(enumID) FROM tb_user";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        row = cursor.getInt(0);
        return row;
    }

    public Set<String> getAllDesa(String kec) {
        Set<String> set = new HashSet<String>();

        String selectQuery = null;

        selectQuery = "SELECT b.kode_desa, b.nama_desa FROM mst_kecamatan a" +
                " INNER JOIN mst_desa b ON a.kode_kecamatan = b.kode_kecamatan" +
                " WHERE a.nama_kecamatan = '" + kec + "' ORDER BY b.nama_desa ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                set.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return set;
    }

    public Set<String> getAllKec(String kab) {
        Set<String> set = new HashSet<String>();

        String selectQuery = "SELECT b.kode_kecamatan, b.nama_kecamatan" +
                " FROM mst_kabkot a " +
                " inner join mst_kecamatan b on a.kode_kabupaten = b.kode_kabupaten" +
                " WHERE a.nama_kab_kot = '" + kab + "' ORDER BY b.nama_kecamatan ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                set.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return set;
    }

    public Set<String> getAllKab(String prov) {
        Set<String> set = new HashSet<String>();

        String selectQuery = "select kode_kabupaten, nama_kab_kot" +
                " from mst_kabkot " +
                " where kode_propinsi = '" + prov + "' order by nama_kab_kot asc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                set.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return set;
    }

//    public Set<String> getAllProv() {
//        Set<String> set = new HashSet<String>();
//        String selectQuery = "select kode_prov, nama_provinsi from mst_prov order by nama_provinsi asc";
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                set.add(cursor.getString(1));
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//
//        return set;
//    }

    public Set<String> getAll(String value, String TableName, String orderBy) {
        Set<String> set = new HashSet<String>();
        String selectQuery = "select " + value + " from " + TableName + " order by " + orderBy + " asc";
        Log.d("query", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                set.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return set;
    }

    public int countURL() {
        int row = 0;
        try {
            String selectQuery = "SELECT COUNT(*) from tb_setting";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getInt(0);
        } catch (Throwable e) {

        }
        return row;
    }

    public int count(String tableName) {
        int row = 0;
        try {
            String selectQuery = "SELECT COUNT(*) from " + tableName + "";
            Log.d("query", selectQuery);

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getInt(0);
        } catch (Throwable e) {

        }
        return row;
    }

    //dari tabel kk
    public String getIdSensus(Cursor c) {
        return (c.getString(0));
    }

    //dari tabel kk
    public String getEnumIDnara(Cursor c) {
        return (c.getString(1));
    }

    public String getNama(Cursor c) {
        return (c.getString(2));
    }

    public String getDesa(Cursor c) {
        return (c.getString(3));
    }

    public String getKec(Cursor c) {
        return (c.getString(4));
    }

    public String getKab(Cursor c) {
        return (c.getString(5));
    }

    public String getProv(Cursor c) {
        return (c.getString(6));
    }

    public String getKTP(Cursor c) {
        return (c.getString(7));
    }

    public String getHP(Cursor c) {
        return (c.getString(8));
    }

    public String getnamaKK(Cursor c) {
        return (c.getString(9));
    }

    public String getJmlKK(Cursor c) {
        return (c.getString(10));
    }

    public String getTempatLhr(Cursor c) {
        return (c.getString(11));
    }

    public String gettanggalLhr(Cursor c) {
        return (c.getString(12));
    }

    public String getpendidikan(Cursor c) {
        return (c.getString(13));
    }

    public String getsuku(Cursor c) {
        return (c.getString(14));
    }

    public String getpekerjaanutama(Cursor c) {
        return (c.getString(15));
    }

    public String getpekerjaanlain(Cursor c) {
        return (c.getString(16));
    }

    public String getagt_org(Cursor c) {
        return (c.getString(17));
    }

    public String gettglinput(Cursor c) {
        return (c.getString(18));
    }

    public String getStatus(Cursor c) {
        return (c.getString(19));
    }


//    //Declare Lahan
//    public String getidlahan(Cursor c) {
//        return (c.getString(0));
//    }
//
//    public String getidkklahan(Cursor c) {
//        return (c.getString(1));
//    }
//
//    public String getEnumID(Cursor c) {
//        return (c.getString(2));
//    }
//
//    public String getshape_ID(Cursor c) {
//        return (c.getString(3));
//    }
//
//    public String getstatus_lahan(Cursor c) {
//        return (c.getString(4));
//    }
//
//    public String getlama_lahan(Cursor c) {
//        return (c.getString(5));
//    }
//
//    public String getluas_prod(Cursor c) {
//        return (c.getString(6));
//    }
//
//    public String getluas_nonprod(Cursor c) {
//        return (c.getString(7));
//    }
//
//    public String gettahun_tanam(Cursor c) {
//        return (c.getString(8));
//    }
//
//    public String gettahun_tanamnon(Cursor c) {
//        return (c.getString(9));
//    }
//
//    public String getpernah_panen(Cursor c) {
//        return (c.getString(10));
//    }
//
//    public String getsiklus_panen(Cursor c) {
//        return (c.getString(11));
//    }
//
//    public String getberat_buah(Cursor c) {
//        return (c.getString(12));
//    }
//
//    public String getprod_perpanen(Cursor c) {
//        return (c.getString(13));
//    }
//
//    public String getasal_bibitchk(Cursor c) {
//        return (c.getString(14));
//    }
//
//    public String getasal_bibit(Cursor c) {
//        return (c.getString(15));
//    }
//
//    public String getjenis_bibit(Cursor c) {
//        return (c.getString(16));
//    }
//
//    public String gethibrida(Cursor c) {
//        return (c.getString(17));
//    }
//
//    public String getasal_pupukchk(Cursor c) {
//        return (c.getString(18));
//    }
//
//    public String getasal_pupuk(Cursor c) {
//        return (c.getString(19));
//    }
//
//    public String getjenis_pupuk(Cursor c) {
//        return (c.getString(20));
//    }
//
//    public String getpenggunaan_pupukchk(Cursor c) {
//        return (c.getString(21));
//    }
//
//    public String getpenggunaan_pupuk(Cursor c) {
//        return (c.getString(22));
//    }
//
//    public String getcampur_pupuk(Cursor c) {
//        return (c.getString(23));
//    }
//
//    public String getbahan_pupuk(Cursor c) {
//        return (c.getString(24));
//    }
//
//    public String gettutupan_lahan(Cursor c) {
//        return (c.getString(25));
//    }
//
//    public String getlokasi_jualchk(Cursor c) {
//        return (c.getString(26));
//    }
//
//    public String getlokasi_jual(Cursor c) {
//        return (c.getString(27));
//    }
//
//    public String getStatusKirim_lahan(Cursor c) {
//        return (c.getString(28));
//    }
//

    //------------------------
    public String getOneDesa(String iddesa) {
        String row = null;
        try {
            String selectQuery = "SELECT nama_desa FROM mst_desa WHERE kode_desa ='" + iddesa + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getString(0);
        } catch (Throwable e) {

        }
        return row;
    }

    public String getOneKec(String idkec) {
        String row = null;
        try {
            String selectQuery = "SELECT nama_kecamatan FROM mst_kecamatan WHERE kode_kecamatan ='" + idkec + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getString(0);
        } catch (Throwable e) {

        }
        return row;
    }

    public String getOneKab(String idkab) {
        String row = null;
        try {
            String selectQuery = "SELECT nama_kab_kot FROM mst_kabkot WHERE kode_kabupaten ='" + idkab + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getString(0);
        } catch (Throwable e) {

        }
        return row;
    }

    public String getOneProv(String idprov) {
        String row = null;
        try {
            String selectQuery = "SELECT nama_provinsi FROM mst_prov WHERE kode_prov ='" + idprov + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            row = cursor.getString(0);
        } catch (Throwable e) {

        }
        return row;
    }

//    public String getNarasumber(int id_kk) {
//        String row = null;
//        try {
//            String selectQuery = "select b.narasumber, a.id_lahan, a.shape_id, a.status_lahan from tb_lahan a left join tb_kk b on a.id_kk=b.id_kk where id_kk='" + id_kk + "'";
//
//            SQLiteDatabase db = this.getWritableDatabase();
//            Cursor cursor = db.rawQuery(selectQuery, null);
//            if (cursor != null) {
//                cursor.moveToFirst();
//            }
//
//            row = cursor.getString(0);
//        } catch (Throwable e) {
//
//        }
//        return row;
//    }

//	public int getCount(String idcount)
//	{
//		Integer row=null;
//		try{
//			String selectQuery = "SELECT count FROM tb_count WHERE name ='"+idcount+"'";
//
//			SQLiteDatabase db = this.getWritableDatabase();
//			Cursor cursor = db.rawQuery(selectQuery, null);
//			if (cursor != null) {
//	            cursor.moveToFirst();
//	        }
//
//			row = cursor.getInt(0);
//		}catch (Throwable e) {
//
//		}
//		return row;
//	}

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

//    public int getCountLahan(int idkk) {
//        String countQuery = "SELECT * FROM tb_lahan where id_kk=" + idkk + " and status=0";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        int cnt = cursor.getCount();
//        cursor.close();
//        return cnt;
//    }

//    public int getLahanKosong() {
//        String countQuery = "select * from tb_lahan where status_lahan is null and lama_lahan ='' and tahun_tanam_prod='0'";
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        int cnt = cursor.getCount();
//        cursor.close();
//        return cnt;
//    }

    public void resetTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();
    }

    public int getstatusKK(String idkk) {
        int status = 0;
        try {
            String selectQuery = "select status from tb_kk where id_kk='" + idkk + "'";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor != null) {
                cursor.moveToFirst();
            }

            status = cursor.getInt(0);
        } catch (Throwable e) {

        }
        return status;
    }

}
