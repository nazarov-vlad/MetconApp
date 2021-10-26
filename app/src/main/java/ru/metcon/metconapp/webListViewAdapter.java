package ru.metcon.metconapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class webListViewAdapter extends ArrayAdapter<JSONArray> {
    int listLayout;
    ArrayList< JSONArray> usersList;
    Context context;

    public webListViewAdapter(Context context, int listLayout , int field, ArrayList<JSONArray> usersList) {
        super(context, listLayout, field, usersList);
        this.context = context;
        this.listLayout=listLayout;
        this.usersList = usersList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(listLayout, null, false);
        TextView Cod1 = listViewItem.findViewById(R.id.textViewCod1);
        TextView Name1 = listViewItem.findViewById(R.id.textViewName1);
        TextView Name2 = listViewItem.findViewById(R.id.textViewName2);
        TextView Name3 = listViewItem.findViewById(R.id.textViewName3);
        TextView Name4 = listViewItem.findViewById(R.id.textViewName4);
        try{
            Cod1.setText(usersList.get(position).getString(0));
            Name1.setText(usersList.get(position).getString(1));
            Name2.setText(usersList.get(position).getString(2));
            Name3.setText(usersList.get(position).getString(3));
            Name4.setText(usersList.get(position).getString(4));
        }catch (JSONException je){
            je.printStackTrace();
        }
        return listViewItem;
    }
}
