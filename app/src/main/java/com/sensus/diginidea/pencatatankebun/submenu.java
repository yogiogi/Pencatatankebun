package com.sensus.diginidea.pencatatankebun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.sensus.diginidea.pencatatankebun.edit.edit_identitas;

/**
 * Created by Yogi on 9/25/2017.
 */

public class submenu extends AppCompatActivity {
    Button btIdentitas, btViewData, btInputProduksi;
    int id_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submenu);

        btIdentitas = (Button) findViewById(R.id.btIdentitas);
        btViewData = (Button) findViewById(R.id.btViewData);
        btInputProduksi = (Button) findViewById(R.id.btInputProduksi);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_obj = getIntent().getExtras().getInt("id_obj_pencatatan");
            if (id_obj == 1) {
                setTitle("PENCATATAN PEKEBUN");
                btIdentitas.setText("INPUT IDENTITAS PEKEBUN");
            } else {
                setTitle("PENCATATAN PERUSAHAAN");
                btIdentitas.setText("INPUT IDENTITAS PERUSAHAAN");
            }
        }

        btIdentitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(submenu.this, edit_identitas.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            }
        });

        btInputProduksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(submenu.this, view_data_estimasi.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            }
        });

        btViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(submenu.this, view_data.class);
                intent.putExtra("id_obj_pencatatan", id_obj);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(submenu.this,
                MainActivity.class);
        startActivity(intent);
    }
}