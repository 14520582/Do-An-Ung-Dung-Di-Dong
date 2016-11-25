package com.example.nghia.coo;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {
    View view;
    private ListView listview;
    private DatabaseReference mDatabase;
    ArrayList<Recipe> listrecipe=new ArrayList<Recipe>();
    ArrayList<UserKey> listUserKey=new ArrayList<UserKey>();
    ArrayList<String> listname=new ArrayList<String>();
    ArrayList<String> listimage=new ArrayList<String>();
    ArrayList<Boolean> listtruefalse=new ArrayList<Boolean>();
    RecipeAdapterUser rpAdapter=null;
    ImageView avatar;
    TextView nameuser;
    Uri photoUrl;
    String nameprofile;
    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        view= inflater.inflate(R.layout.fragment_recipe, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.recipe));
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Mapping();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        photoUrl=user.getPhotoUrl();
        nameprofile=user.getDisplayName();
        Picasso.with(getActivity()).load(photoUrl).into(avatar);
        nameuser.setText(nameprofile);
        rpAdapter = new RecipeAdapterUser(this.getContext(),R.layout.introrecipeuser,listrecipe,listUserKey,listname,listimage,listtruefalse);
        LoadData();

        listview.setAdapter(rpAdapter);

        return view;
    }
    private void Mapping(){
        listview=(ListView) view.findViewById(R.id.listView);
        avatar=(ImageView) view.findViewById(R.id.imageViewUser);
        nameuser=(TextView) view.findViewById(R.id.textViewUser);
      //  searchview=(SearchView) view.findViewById(R.id.searchView);
    }
    private void LoadData(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase.child("Recipes").orderByChild("userid").equalTo(user.getUid().toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Recipe rp= dataSnapshot.getValue(Recipe.class);
                UserKey tm=new UserKey(user.getUid().toString(),dataSnapshot.getKey());
              // Toast.makeText(getApplicationContext(), rp.toString(), Toast.LENGTH_LONG).show();
                listrecipe.add(0,new Recipe(rp.namerecipe,rp.image,rp.material,rp.time,rp.ration,rp.implement,rp.cal,rp.userid,rp.like));
                listUserKey.add(0,tm);
                final String rec=dataSnapshot.getKey();
                final int pos=listrecipe.size();
                listtruefalse.add(0,Boolean.FALSE);
                listname.add(0,nameprofile);
                listimage.add(0,photoUrl.toString());
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, 825*listrecipe.size());
                listview.setLayoutParams(lparams);
                mDatabase.child("User").child(user.getUid()).child("Liked").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        if(rec.equals(dataSnapshot.getValue().toString())) {
                            listtruefalse.set(listrecipe.size()-pos, Boolean.TRUE);
                            //  Toast.makeText(getActivity(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                            // listtruefalse.remove(listtruefalse.size()-1);
                            mDatabase.removeEventListener(this);
                            rpAdapter.notifyDataSetChanged();
                        }

                        // Toast.makeText(getActivity(),listtruefalse.size()+"",Toast.LENGTH_SHORT).show();

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
                rpAdapter.notifyDataSetChanged();
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
                rpAdapter.getFilter().filter(newText.toString());
                return false;
            }
        });
    }
}
