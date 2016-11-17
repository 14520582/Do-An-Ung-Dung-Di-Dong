package com.example.nghia.coo;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingFragment extends Fragment {
    private ListView listview;
    ListShoppingAdapter shoppingAdapter=null;
    ArrayList<ShoppingObject> arrayList=new ArrayList<ShoppingObject>();
    public ShoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shopping, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.shopping));
        final SQLite db = new SQLite(getActivity(),"Shopping.sqlite",null,1);
        listview=(ListView) view.findViewById(R.id.shoppingout);
        shoppingAdapter = new ListShoppingAdapter(this.getContext(),R.layout.layout_list_shopping,arrayList);
        Cursor cur =db.GetData("SELECT * FROM Shopping");
        while(cur.moveToNext()) {
            ShoppingObject tem = new ShoppingObject(cur.getInt(0), cur.getString(1), cur.getString(2), cur.getString(3));
            arrayList.add(tem);
            shoppingAdapter.notifyDataSetChanged();
        }

        listview.setAdapter(shoppingAdapter);
        return view;
    }

}
