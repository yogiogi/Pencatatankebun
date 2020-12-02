package com.sensus.diginidea.pencatatankebun.dbPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sensus.diginidea.pencatatankebun.R;

import java.util.ArrayList;

public class SQLiteListAdapterLuasAreaPerusahaan extends BaseAdapter {

    Context context;
    //    ArrayList<String> id_sensus;
    ArrayList<String> Komoditas;
    ArrayList<Double> TBMINTI;
    ArrayList<Double> TMINTI;
    ArrayList<Double> TTMINTI;
    ArrayList<Double> JMLINTI;
    ArrayList<Double> TBMPLASMA;
    ArrayList<Double> TMPLASMA;
    ArrayList<Double> TTMPLASMA;
    ArrayList<Double> JMLPLASMA;
    ArrayList<String> KETERANGAN;

    public SQLiteListAdapterLuasAreaPerusahaan(
            Context context2,
//            ArrayList<String> id,
            ArrayList<String> komoditas,
            ArrayList<Double> tbminti,
            ArrayList<Double> tminti,
            ArrayList<Double> ttminti,
            ArrayList<Double> jmlinti,
            ArrayList<Double> tbmplasma,
            ArrayList<Double> tmplasma,
            ArrayList<Double> ttmplasma,
            ArrayList<Double> jmlplasma,
            ArrayList<String> keterangan

    ) {

        this.context = context2;
//        this.userID = id;
        this.Komoditas = komoditas;
        this.TBMINTI = tbminti;
        this.TMINTI = tminti;
        this.TTMINTI = ttminti;
        this.JMLINTI = jmlinti;
        this.TBMPLASMA = tbmplasma;
        this.TMPLASMA = tmplasma;
        this.TTMPLASMA = ttmplasma;
        this.JMLPLASMA = jmlplasma;
        this.KETERANGAN = keterangan;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Komoditas.size();
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
            child = layoutInflater.inflate(R.layout.listviewdatalayoutperusahaan, null);

            holder = new Holder();

            holder.textviewkomoditas = (TextView) child.findViewById(R.id.textViewKOMODITAS);
            holder.textviewtbmi = (TextView) child.findViewById(R.id.textViewTBM);
            holder.textviewtmi = (TextView) child.findViewById(R.id.textViewTM);
            holder.textviewttmi = (TextView) child.findViewById(R.id.textViewTTM);
            holder.textviewjmlinti = (TextView) child.findViewById(R.id.textViewJMLINTI);
            holder.textviewtbmp = (TextView) child.findViewById(R.id.textViewTBMPLASMA);
            holder.textviewtmp = (TextView) child.findViewById(R.id.textViewTMPLASMA);
            holder.textviewttmp = (TextView) child.findViewById(R.id.textViewTTMPLASMA);
            holder.textviewjmlplasma = (TextView) child.findViewById(R.id.textViewJMLPLASMA);
            holder.textviewket = (TextView) child.findViewById(R.id.textViewKeterangan);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.textviewkomoditas.setText(Komoditas.get(position));
        holder.textviewtbmi.setText(String.format("%.2f", TBMINTI.get(position)));
        holder.textviewtmi.setText(String.format("%.2f", TMINTI.get(position)));
        holder.textviewttmi.setText(String.format("%.2f", TTMINTI.get(position)));
        holder.textviewjmlinti.setText(String.format("%.2f", JMLINTI.get(position)));
        holder.textviewtbmp.setText(String.format("%.2f", TBMPLASMA.get(position)));
        holder.textviewtmp.setText(String.format("%.2f", TMPLASMA.get(position)));
        holder.textviewttmp.setText(String.format("%.2f", TTMPLASMA.get(position)));
        holder.textviewjmlplasma.setText(String.format("%.2f", JMLPLASMA.get(position)));
        holder.textviewket.setText(KETERANGAN.get(position));
        return child;
    }

    public class Holder {
        TextView textviewkomoditas;
        TextView textviewtbmi;
        TextView textviewtmi;
        TextView textviewttmi;
        TextView textviewjmlinti;
        TextView textviewtbmp;
        TextView textviewtmp;
        TextView textviewttmp;
        TextView textviewjmlplasma;
        TextView textviewket;
    }
}