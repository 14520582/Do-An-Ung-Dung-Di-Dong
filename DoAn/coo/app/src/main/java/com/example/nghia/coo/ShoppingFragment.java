package com.example.nghia.coo;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
       // super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        View view= inflater.inflate(R.layout.fragment_shopping, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.shopping));
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        //Toast.makeText(getActivity(),user.getUid(),Toast.LENGTH_SHORT);
        mDatabase.child(user.getUid()).child("Recipes").orderByChild("namerecipe").equalTo("h").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(getActivity(),dataSnapshot.getKey().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem mSearchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                shoppingAdapter.getFilter().filter(newText.toString());
                return false;
            }
        });
    }



}
