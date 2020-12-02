package com.sensus.diginidea.pencatatankebun;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sensus.diginidea.pencatatankebun.edit.est_luas_areal;
import com.sensus.diginidea.pencatatankebun.edit.est_produksi;
import com.sensus.diginidea.pencatatankebun.edit.produksi;

/**
 * Created by Yogi on 9/25/2017.
 */

public class estimasi extends AppCompatActivity {
    Button btProduksi, btEstProduksi, btEstLuasAreal;
    String id_sensus, id_objek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estimasi);

        btProduksi = (Button) findViewById(R.id.btProduksi);
        btEstProduksi = (Button) findViewById(R.id.btEstProduksi);
        btEstLuasAreal = (Button) findViewById(R.id.btEstLuasAreal);

        Bundle extras = getIntent().getExtras();
        Log.d("Response", extras + "");
        if (extras != null) {
            id_sensus = getIntent().getExtras().getString("id_sensus");
            id_objek = getIntent().getExtras().getString("id_obj_pencatatan");
            Log.d("id_sensus", id_sensus + "");
        }

        btProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(estimasi.this, produksi.class);
                intent.putExtra("id_sensus", id_sensus);
                startActivity(intent);
            }
        });

        btEstProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(estimasi.this, est_produksi.class);
                intent.putExtra("id_sensus", id_sensus);
                startActivity(intent);
            }
        });

        btEstLuasAreal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(estimasi.this, est_luas_areal.class);
                intent.putExtra("id_sensus", id_sensus);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = null;
        intent = new Intent(estimasi.this, submenu.class);
        intent.putExtra("id_obj_pencatatan", id_objek);
        startActivity(intent);
    }

    public void AlertCekTanggal() {
        AlertDialog.Builder alert = new AlertDialog.Builder(estimasi.this);
        alert.setTitle("Perhatian");
        alert.setCancelable(false);
        alert.setMessage("Tidak dapat diisi, belum bulannya");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(estimasi.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
        alert.show();
    }
}
