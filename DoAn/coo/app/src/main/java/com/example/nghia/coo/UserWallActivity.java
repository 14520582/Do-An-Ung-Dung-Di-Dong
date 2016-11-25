package com.example.nghia.coo;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
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

public class UserWallActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wall);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Mapping();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        photoUrl=user.getPhotoUrl();
        nameprofile=user.getDisplayName();
        Picasso.with(this).load(photoUrl).into(avatar);
        nameuser.setText(nameprofile);
        rpAdapter = new RecipeAdapterUser(this,R.layout.introrecipeuser,listrecipe,listUserKey,listname,listimage,listtruefalse);
        LoadData();

        listview.setAdapter(rpAdapter);
    }
    private void Mapping(){
        listview=(ListView) findViewById(R.id.listViewWallUser);
        avatar=(ImageView) findViewById(R.id.imageViewWallUser);
        nameuser=(TextView) findViewById(R.id.textViewWallUser);
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
                listname.add(0,nameprofile);
                final String rec=dataSnapshot.getKey();
                final int pos=listrecipe.size();
                listtruefalse.add(0,Boolean.FALSE);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
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
        return true;
       // getMenuInflater().inflate(R.menu.main, menu);
       // MenuInflater inflater=getMenuInflater();
      //  inflater.inflate(R.menu.menu_search,menu);
     /*   MenuItem item =menu.getItem(R.id.action_search);
        SearchView searchView=(SearchView) item.getActionView();
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
        });*/

    }
}