package com.example.nghia.coo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nghia on 14/11/2016.
 */

public class ItemImplementAdapter extends BaseAdapter {
    Context context;
    int mLayout;
    ArrayList<String> arrayImplement;
    public ItemImplementAdapter(Context context, int mLayout, ArrayList<String> arrayimplement) {
        this.context = context;
        this.mLayout = mLayout;
        this.arrayImplement = arrayimplement;
    }
    @Override
    public int getCount() {
        return arrayImplement.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayImplement.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        TextView step,intent;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;
        ItemImplementAdapter.ViewHolder holder=new ItemImplementAdapter.ViewHolder();
        if(rowview==null){
            rowview=inflater.inflate(mLayout,null);
            holder.step=(TextView) rowview.findViewById(R.id.textViewStep);
            holder.intent=(TextView) rowview.findViewById(R.id.textViewImplement);
        }
        else{
            holder=(ViewHolder) rowview.getTag();
        }
        final int next= position+1;
        holder.step.setText(context.getString(R.string.step)+" "+next);
        holder.intent.setText(arrayImplement.get(position));
        return rowview;
    }
}
