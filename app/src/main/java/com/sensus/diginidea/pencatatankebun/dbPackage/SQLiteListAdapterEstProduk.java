package com.sensus.diginidea.pencatatankebun.dbPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sensus.diginidea.pencatatankebun.R;

import java.util.ArrayList;

public class SQLiteListAdapterEstProduk extends BaseAdapter {

    Context context;
    ArrayList<String> id_sensus;
    ArrayList<String> Komoditas;
    ArrayList<Double> Jumlah_prod;
    ArrayList<String> Wujud_prod;
    ArrayList<Double> Nilai_jual;
    ArrayList<Double> Harga;
    ArrayList<String> KETERANGAN;
    ArrayList<String> TVKG;

    public SQLiteListAdapterEstProduk(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> komoditas,
            ArrayList<Double> jml_prod,
            ArrayList<String> wujud_prod,
            ArrayList<Double> nilai_jual,
            ArrayList<Double> harga,
            ArrayList<String> keterangan,
            ArrayList<String> tvkg
    ) {

        this.context = context2;
        this.id_sensus = id;
        this.Komoditas = komoditas;
        this.Jumlah_prod = jml_prod;
        this.Wujud_prod = wujud_prod;
        this.Nilai_jual = nilai_jual;
        this.Harga = harga;
        this.KETERANGAN = keterangan;
        this.TVKG=tvkg;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return id_sensus.size();
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

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listviewdatalayoutestproduk, null);

            holder = new Holder();

            holder.textviewkomoditas = (TextView) child.findViewById(R.id.textViewKOMODITAS);
            holder.textviewjumlah = (TextView) child.findViewById(R.id.textViewJMLPROD);
            holder.textviewtwujud = (TextView) child.findViewById(R.id.textViewWUJUD);
            holder.textviewtnilai = (TextView) child.findViewById(R.id.textViewNILAIJUAL);
            holder.textviewharga = (TextView) child.findViewById(R.id.textViewHARGA);
            holder.textviewketerangan = (TextView) child.findViewById(R.id.textViewKET);
            holder.textviewkg = (TextView) child.findViewById(R.id.tvkg);
//            holder.textviewkonversi = (TextView) child.findViewById(R.id.textViewKONVERSI);
//            holder.textviewsatkonversi = (TextView) child.findViewById(R.id.tvSa);
            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.textviewkomoditas.setText(Komoditas.get(position));
        holder.textviewjumlah.setText(String.format("%.2f", Jumlah_prod.get(position)));
        holder.textviewtwujud.setText(Wujud_prod.get(position));
        holder.textviewtnilai.setText(String.format("%.0f", Nilai_jual.get(position)));
        holder.textviewharga.setText(String.format("%.0f", Harga.get(position)));
        holder.textviewketerangan.setText(KETERANGAN.get(position));
        holder.textviewkg.setText(TVKG.get(position));
        return child;
    }

    public class Holder {
        TextView textviewkomoditas;
        TextView textviewjumlah;
        TextView textviewtwujud;
        TextView textviewtnilai;
        TextView textviewharga;
        TextView textviewketerangan;
        TextView textviewkg;
    }

}