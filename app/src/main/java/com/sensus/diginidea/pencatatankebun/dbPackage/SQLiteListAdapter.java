package com.sensus.diginidea.pencatatankebun.dbPackage;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sensus.diginidea.pencatatankebun.Country;
import com.sensus.diginidea.pencatatankebun.R;

public class SQLiteListAdapter extends BaseAdapter {

    Context context;
    private ArrayList<String> userID;
    private ArrayList<String> UserName;
    private ArrayList<String> User_PhoneNumber;
    private ArrayList<String> User_Kelompoktani;
    private ArrayList<String> User_obj;
    private ArrayList<String> User_Manbun;
    private ArrayList<String> Status1;
    private ArrayList<String> Status2;

    public SQLiteListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> name,
            ArrayList<String> phone,
            ArrayList<String> kelompoktani,
            ArrayList<String> objek,
            ArrayList<String> manbun,
            ArrayList<String> status1,
            ArrayList<String> status2
    )
    {

        this.context = context2;
        this.userID = id;
        this.UserName = name;
        this.User_PhoneNumber = phone;
        this.User_Kelompoktani = kelompoktani;
        this.User_obj = objek;
        this.User_Manbun = manbun;
        this.Status1 = status1;
        this.Status2 = status2;
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
        String obj = User_obj.get(position);
        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.listviewdatalayout, null);

            holder = new Holder();

            holder.textviewname = (TextView) child.findViewById(R.id.textViewNAME);
            holder.textviewphone_number = (TextView) child.findViewById(R.id.textViewTELEPON);
            holder.textviewkelompoktani = (TextView) child.findViewById(R.id.textViewKELOMPOK_TANI);
            holder.textviewManbun = (TextView) child.findViewById(R.id.textViewManbun);
            holder.textviewStatus1 = (TextView) child.findViewById(R.id.tvStatusCat1);
            holder.textviewStatus2 = (TextView) child.findViewById(R.id.tvStatusCat2);
            holder.labelManbun = (TextView) child.findViewById(R.id.labelManbun);
            holder.layoutManbun = (LinearLayout) child.findViewById(R.id.layoutManbun);

            if (Integer.parseInt(obj) != 1) {
                holder.textviewManbun.setVisibility(View.GONE);
                holder.layoutManbun.setVisibility(View.GONE);
                holder.labelManbun.setVisibility(View.GONE);
            }

            child.setTag(holder);
        } else {

            holder = (Holder) child.getTag();
        }
        holder.textviewname.setText(UserName.get(position));
        holder.textviewphone_number.setText(User_PhoneNumber.get(position));
        holder.textviewkelompoktani.setText(User_Kelompoktani.get(position));
        holder.textviewManbun.setText(User_Manbun.get(position));
        holder.textviewStatus1.setText(Status1.get(position));
        holder.textviewStatus2.setText(Status2.get(position));
        return child;
    }

    public class Holder {
        TextView textviewname;
        TextView textviewphone_number;
        TextView textviewkelompoktani;
        TextView textviewManbun;
        TextView textviewStatus1;
        TextView textviewStatus2;
        TextView labelManbun;
        LinearLayout layoutManbun;
    }
}