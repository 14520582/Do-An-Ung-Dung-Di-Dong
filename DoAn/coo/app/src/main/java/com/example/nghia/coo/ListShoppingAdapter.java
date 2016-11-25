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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nghia on 15/11/2016.
 */

public class ListShoppingAdapter extends BaseAdapter implements Filterable {
    Context context;
    int mLayout;
    List<ShoppingObject> arrayShoppingObject;
    List<ShoppingObject> arrayShoppingObjectSource;
    public ListShoppingAdapter(Context context, int mLayout, List<ShoppingObject> arrayimplement) {
        this.context = context;
        this.mLayout = mLayout;
        this.arrayShoppingObject = arrayimplement;
        this.arrayShoppingObjectSource=arrayimplement;
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
            rowview.setTag(holder);
        }
        else{
            holder=(ListShoppingAdapter.ViewHolder) rowview.getTag();
        }
        final int pos=arrayShoppingObject.get(position).id;
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
        holder.name.setText(arrayShoppingObject.get(position).Name);
        holder.ration.setText(arrayShoppingObject.get(position).Ration);
        return rowview;
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter(){
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                arrayShoppingObject = (ArrayList<ShoppingObject>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<ShoppingObject> FilteredArrList = new ArrayList<ShoppingObject>();
                if (arrayShoppingObjectSource == null) {
                    arrayShoppingObjectSource=new ArrayList<ShoppingObject>(arrayShoppingObject);
                }


                if (constraint == null || constraint.length() == 0) {
                    results.count = arrayShoppingObjectSource.size();
                    results.values = arrayShoppingObjectSource;

                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < arrayShoppingObjectSource.size(); i++) {
                        String data = arrayShoppingObjectSource.get(i).Name;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new ShoppingObject(arrayShoppingObjectSource.get(i).id,arrayShoppingObjectSource.get(i).Name,arrayShoppingObjectSource.get(i).Ration,arrayShoppingObjectSource.get(i).listimplement));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
        }
}
