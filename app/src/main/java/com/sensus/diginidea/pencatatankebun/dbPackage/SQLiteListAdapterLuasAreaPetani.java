package com.sensus.diginidea.pencatatankebun.dbPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sensus.diginidea.pencatatankebun.R;

import java.util.ArrayList;

public class SQLiteListAdapterLuasAreaPetani extends BaseAdapter {

    Context context;
    //    ArrayList<String> sensusID;
    ArrayList<String> KOMODITAS;
    ArrayList<Double> TBM;
    ArrayList<Double> TM;
    ArrayList<Double> TTM;
    ArrayList<Double> JML;
    ArrayList<String> KETERANGAN;

    public SQLiteListAdapterLuasAreaPetani(
            Context context2,
//            ArrayList<String> id_sensus,
            ArrayList<String> komoditas,
            ArrayList<Double> tbm,
            ArrayList<Double> tm,
            ArrayList<Double> ttm,
            ArrayList<Double> jml,
            ArrayList<String> keterangan
    ) {

        this.context = context2;
//        this.sensusID = id_sensus;
        this.KOMODITAS = komoditas;
        this.TBM = tbm;
        this.TM = tm;
        this.TTM = ttm;
        this.JML = jml;
        this.KETERANGAN = keterangan;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return KOMODITAS.size();
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
            child = layoutInflater.inflate(R.layout.listviewdatalayoutpetani, null);

            holder = new Holder();
//            holder.textviewsensusid = (TextView) child.findViewById(R.id.textViewSENSUS);
            holder.textviewkomoditas = (TextView) child.findViewById(R.id.textViewKOMODITAS);
            holder.textviewtbm = (TextView) child.findViewById(R.id.textViewTBM);
            holder.textviewtm = (TextView) child.findViewById(R.id.textViewTM);
            holder.textviewttm = (TextView) child.findViewById(R.id.textViewTTM);
            holder.textviewjumlah = (TextView) child.findViewById(R.id.textViewJMLINTI);
            holder.textviewketerangan = (TextView) child.findViewById(R.id.textViewKeterangan);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
//        holder.textviewsensusid.setText(sensusID.get(position));
        holder.textviewkomoditas.setText(KOMODITAS.get(position));
        holder.textviewtbm.setText(String.format("%.2f", TBM.get(position)));
        holder.textviewtm.setText(String.format("%.2f", TM.get(position)));
        holder.textviewttm.setText(String.format("%.2f", TTM.get(position)));
        holder.textviewjumlah.setText(String.format("%.2f", JML.get(position)));
        holder.textviewketerangan.setText(KETERANGAN.get(position));

        return child;
    }

    public class Holder {
        //        TextView textviewsensusid;
        TextView textviewkomoditas;
        TextView textviewtbm;
        TextView textviewtm;
        TextView textviewttm;
        TextView textviewjumlah;
        TextView textviewketerangan;
    }

}