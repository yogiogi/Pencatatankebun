package com.sensus.diginidea.pencatatankebun.dbPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sensus.diginidea.pencatatankebun.R;

import java.util.ArrayList;

public class SQLiteListAdapterEstLuas extends BaseAdapter {

    Context context;
//    ArrayList<String> id_sensus;
    ArrayList<String> Komoditas;
    ArrayList<String> TBM;
    ArrayList<String> TM;
    ArrayList<String> TTM;
//    ArrayList<String> TBMPLASMA;
//    ArrayList<String> TMPLASMA;
//    ArrayList<String> TTMPLASMA;
//    ArrayList<String> KETERANGAN;

    public SQLiteListAdapterEstLuas(
            Context context2,
//            ArrayList<String> id,
            ArrayList<String> komoditas,
            ArrayList<String> tbm,
            ArrayList<String> tm,
            ArrayList<String> ttm
//            ArrayList<String> tbmplasma,
//            ArrayList<String> tmplasma,
//            ArrayList<String> ttmplasma,
//            ArrayList<String> keterangan

    ) {

        this.context = context2;
//        this.id_sensus = id;
        this.Komoditas = komoditas;
        this.TBM = tbm;
        this.TM = tm;
        this.TTM = ttm;
//        this.TBMPLASMA = tbmplasma;
//        this.TMPLASMA = tmplasma;
//        this.TTMPLASMA = ttmplasma;
//        this.KETERANGAN = keterangan;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return TBM.size();
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

            holder.textviewkomoditas = (TextView) child.findViewById(R.id.textViewKOMODITAS);
            holder.textviewtbm = (TextView) child.findViewById(R.id.textViewTBM);
            holder.textviewtm = (TextView) child.findViewById(R.id.textViewTM);
            holder.textviewttm= (TextView) child.findViewById(R.id.textViewTTM);
//            holder.textviewtbmplasma = (TextView) child.findViewById(R.id.textViewHARGA);
//            holder.textviewtmplasma= (TextView) child.findViewById(R.id.textViewHARGA);
//            holder.textviewttmplasma = (TextView) child.findViewById(R.id.textViewHARGA);
//            holder.textviewketerangan= (TextView) child.findViewById(R.id.textViewKeterangan);
            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.textviewkomoditas.setText(Komoditas.get(position));
        holder.textviewtbm.setText(TBM.get(position));
        holder.textviewtm.setText(TM.get(position));
        holder.textviewttm.setText(TTM.get(position));
//        holder.textviewtbmplasma.setText(TTMPLASMA.get(position));
//        holder.textviewtmplasma.setText(TMPLASMA.get(position));
//        holder.textviewtbmplasma.setText(TBMPLASMA.get(position));
//        holder.textviewketerangan.setText(KETERANGAN.get(position));

        return child;
    }

    public class Holder {
        TextView textviewkomoditas;
        TextView textviewtbm;
        TextView textviewtm;
        TextView textviewttm;
//        TextView textviewtbmplasma;
//        TextView textviewtmplasma;
//        TextView textviewttmplasma;
//        TextView textviewketerangan;
    }

}