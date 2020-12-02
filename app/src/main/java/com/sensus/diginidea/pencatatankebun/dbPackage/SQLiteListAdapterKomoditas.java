package com.sensus.diginidea.pencatatankebun.dbPackage;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sensus.diginidea.pencatatankebun.R;

import java.util.ArrayList;

public class SQLiteListAdapterKomoditas extends BaseAdapter {

    Context context;
    private ArrayList<String> userID;
    private ArrayList<String> UserName;
    private ArrayList<String> Status;

    public SQLiteListAdapterKomoditas(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> name,
            ArrayList<String> status
    ) {

        this.context = context2;
        this.userID = id;
        this.UserName = name;
        this.Status = status;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return userID.size();
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
            child = layoutInflater.inflate(R.layout.layout_real_menu_list, null);

            holder = new Holder();
            holder.textviewid = (TextView) child.findViewById(R.id.textViewSensus);
            holder.textviewname = (TextView) child.findViewById(R.id.textViewNAME);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }

        holder.textviewname.setText(UserName.get(position));
        holder.textviewid.setText(userID.get(position));
        if (Status.get(position) == "ok") {
            holder.textviewid.setTextColor(Color.RED);
            holder.textviewname.setTextColor(Color.RED);
        } else {
            holder.textviewid.setTextColor(Color.WHITE);
            holder.textviewname.setTextColor(Color.WHITE);
        }
        return child;
    }

    public class Holder {
        TextView textviewid;
        TextView textviewname;
    }
}