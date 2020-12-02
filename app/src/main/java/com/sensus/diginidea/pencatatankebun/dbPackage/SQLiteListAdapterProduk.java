package com.sensus.diginidea.pencatatankebun.dbPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sensus.diginidea.pencatatankebun.R;

import java.util.ArrayList;

public class SQLiteListAdapterProduk extends BaseAdapter {

    Context context;
    ArrayList<String> id_sensus;
    ArrayList<String> Komoditas;
    ArrayList<Double> Jumlah_prod;
    ArrayList<String> Wujud_prod;
    ArrayList<Double> Dijual;
    ArrayList<Double> Disimpan;
    ArrayList<Double> Konsumsi;
    ArrayList<String> KETERANGAN;
    ArrayList<String> TVKG;

    public SQLiteListAdapterProduk(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> komoditas,
            ArrayList<Double> jml_prod,
            ArrayList<String> wujud_prod,
            ArrayList<Double> dijual,
            ArrayList<Double> disimpan,
            ArrayList<Double> konsumsi,
            ArrayList<String> keterangan,
            ArrayList<String> tvKg

    ) {

        this.context = context2;
        this.id_sensus = id;
        this.Komoditas = komoditas;
        this.Jumlah_prod = jml_prod;
        this.Wujud_prod = wujud_prod;
        this.Dijual = dijual;
        this.Disimpan = disimpan;
        this.Konsumsi = konsumsi;
        this.KETERANGAN = keterangan;
        this.TVKG =tvKg;
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
            child = layoutInflater.inflate(R.layout.listviewdatalayoutproduk, null);

            holder = new Holder();

            holder.textviewkomoditas = (TextView) child.findViewById(R.id.textViewKOMODITAS);
            holder.textviewjumlah = (TextView) child.findViewById(R.id.textViewJMLPROD);
            holder.textviewtwujud = (TextView) child.findViewById(R.id.textViewWUJUD);
            holder.textviewtdijual = (TextView) child.findViewById(R.id.textViewDIJUAL);
            holder.textviewdisimpan = (TextView) child.findViewById(R.id.textViewDISIMPAN);
            holder.textviewkonsumsi = (TextView) child.findViewById(R.id.textViewKONSUMSI);
            holder.textviewketerangan = (TextView) child.findViewById(R.id.textViewKET);
            holder.textviewkg = (TextView) child.findViewById(R.id.tvkg);
            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }

        holder.textviewkomoditas.setText(Komoditas.get(position));
        holder.textviewjumlah.setText(String.format("%.2f", Jumlah_prod.get(position)));
        holder.textviewtwujud.setText(Wujud_prod.get(position));
        holder.textviewtdijual.setText(String.format("%.2f", Dijual.get(position)));
        holder.textviewdisimpan.setText(String.format("%.2f", Disimpan.get(position)));
        holder.textviewkonsumsi.setText(String.format("%.2f", Konsumsi.get(position)));
        holder.textviewketerangan.setText(KETERANGAN.get(position));
        holder.textviewkg.setText(TVKG.get(position));
        return child;
    }

    public class Holder {
        TextView textviewkomoditas;
        TextView textviewjumlah;
        TextView textviewtwujud;
        TextView textviewtdijual;
        TextView textviewdisimpan;
        TextView textviewkonsumsi;
        TextView textviewketerangan;
        TextView textviewkg;
    }
}