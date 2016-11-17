package com.example.nghia.coo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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
        Button remove;

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
            holder.remove=(Button) rowview.findViewById(R.id.removeShopping);
            holder.remove.setTag(position);
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
        final int pos=arrayShoppingObject.get(position).id;
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
        final SQLite db = new SQLite(context,"Shopping.sqlite",null,1);
        holder.remove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                db.QueryData("DELETE FROM Shopping WHERE Id="+pos);
                                ShoppingFragment fragment = new ShoppingFragment();
                                android.support.v4.app.FragmentTransaction fragmentTransaction =
                                        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                fragmentTransaction.commit();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }


                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.confirmshopping)).setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();
            }
        });
        return rowview;
    }
}
