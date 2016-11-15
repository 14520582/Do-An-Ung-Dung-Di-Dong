package com.example.nghia.coo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nghia on 15/11/2016.
 */

public class ListShoppingAdapter extends BaseAdapter {
    Context context;
    int mLayout;
    List<ShoppingObject> arrayShoppingObject;
    public ListShoppingAdapter(Context context, int mLayout, List<ShoppingObject> arrayimplement) {
        this.context = context;
        this.mLayout = mLayout;
        this.arrayShoppingObject = arrayimplement;
    }
    @Override
    public int getCount() {
        return arrayShoppingObject.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayShoppingObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        TextView name,ration;
        ListView listimpl;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;
        ListShoppingAdapter.ViewHolder holder=new ListShoppingAdapter.ViewHolder();
        if(rowview==null){
            rowview=inflater.inflate(mLayout,null);
            holder.name=(TextView) rowview.findViewById(R.id.textNameShopping);
            holder.ration=(TextView) rowview.findViewById(R.id.textRationShopping);
            holder.listimpl=(ListView) rowview.findViewById(R.id.listviewShopping);
        }
        else{
            holder=(ListShoppingAdapter.ViewHolder) rowview.getTag();
        }
        String temp="";
        for(int i=0;i<arrayShoppingObject.get(position).listimplement.size();i++)
        {
            temp+=arrayShoppingObject.get(position).listimplement.get(i);
        }
        holder.name.setText(arrayShoppingObject.get(position).Name);
        holder.ration.setText(arrayShoppingObject.get(position).Ration);
        int totalItemsHeight=0;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayShoppingObject.get(position).listimplement);
        holder.listimpl.setAdapter(adapter);
        for (int itemPos = 0; itemPos < adapter.getCount(); itemPos++) {
            View item = adapter.getView(itemPos, null, holder.listimpl);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }
        totalItemsHeight+=holder.listimpl.getDividerHeight() *(adapter.getCount() - 1);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, totalItemsHeight);
        holder.listimpl.setLayoutParams(lparams);
        return rowview;
    }
}
