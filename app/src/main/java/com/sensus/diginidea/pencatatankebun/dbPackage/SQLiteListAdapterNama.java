package com.sensus.diginidea.pencatatankebun.dbPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sensus.diginidea.pencatatankebun.R;

import java.util.ArrayList;

public class SQLiteListAdapterNama extends BaseAdapter {

    Context context;
//    private ArrayList<String> userID;
    private ArrayList<String> UserName;

    public SQLiteListAdapterNama(
            Context context2,
//            ArrayList<String> id,
            ArrayList<String> name
    ) {

        this.context = context2;
//        this.userID = id;
        this.UserName = name;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return UserName.size();
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
//            holder.textviewid = (TextView) child.findViewById(R.id.textViewSensus);
            holder.textviewname = (TextView) child.findViewById(R.id.textViewNAME);

            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }

        holder.textviewname.setText(UserName.get(position));
//        holder.textviewid.setText(userID.get(position));

        return child;
    }

    public class Holder {
//        TextView textviewid;
        TextView textviewname;
    }
}