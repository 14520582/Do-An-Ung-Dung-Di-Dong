package com.example.nghia.coo;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnotherWallActivity extends AppCompatActivity {
    private RecyclerView listview;
    private DatabaseReference mDatabase;
    ArrayList<Recipe> listrecipe=new ArrayList<Recipe>();
    ArrayList<UserKey> listUserKey=new ArrayList<UserKey>();
    ArrayList<String> listname=new ArrayList<String>();
    ArrayList<String> listimage=new ArrayList<String>();
    ArrayList<Boolean> listtruefalse=new ArrayList<Boolean>();
    HomeReAdapter rpAdapter=null;
    ImageView avatar,back;
    TextView nameuser;
    Button addfollow,following;
    String curName,curAvatar,curUser;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_another_wall);
        curUser=(String)getIntent().getSerializableExtra("ID");
        curAvatar=(String) getIntent().getSerializableExtra("ImageCover");
        curName=(String) getIntent().getSerializableExtra("NameWall");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        LoadFollow();
        Mapping();
        Picasso.with(this).load(curAvatar).into(avatar);
        nameuser.setText(curName);
        listview.setLayoutManager(new LinearLayoutManager(this));
        rpAdapter = new HomeReAdapter(listrecipe,listUserKey,listname,listimage,listtruefalse);
        LoadData();
        LoadBackground();
        addfollow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                following.setVisibility(View.VISIBLE);
                addfollow.setVisibility(View.INVISIBLE);
                mDatabase.child("User").child(user.getUid()).child("Follow").child(curUser).setValue(curUser, new DatabaseReference.CompletionListener(){
                    @Override
                    public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference){
                        if(databaseError==null)
                        {
                            Toast.makeText(AnotherWallActivity.this, getString(R.string.followed), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
      //  Toast.makeText(AnotherWallActivity.this, curUser, Toast.LENGTH_LONG).show();
        listview.setAdapter(rpAdapter);
        listview.addItemDecoration(new ItemDivider(this));
    }
    private void LoadFollow(){
        final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.child("User").child(user.getUid()).child("Follow").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(curUser.equals(dataSnapshot.getValue().toString()))
                {
                    addfollow.setVisibility(View.INVISIBLE);
                    following.setVisibility(View.VISIBLE);
                    mDatabase2.removeEventListener(this);

                }
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
    private void LoadBackground(){
        mDatabase.child("User").child(curUser).child("Cover").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                Picasso.with(AnotherWallActivity.this).load(dataSnapshot.getValue().toString()).into(back);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void Mapping(){
        listview=(RecyclerView) findViewById(R.id.listViewAnother);
        addfollow=(Button) findViewById(R.id.button3Nonfollowing);
        following=(Button) findViewById(R.id.button4Following);
        avatar=(ImageView) findViewById(R.id.imageViewAnotherUser);
        nameuser=(TextView) findViewById(R.id.textViewAnotherUser);
        back=(ImageView) findViewById(R.id.imageAnotherBack);
        //  searchview=(SearchView) view.findViewById(R.id.searchView);
    }
    private void LoadData(){

        mDatabase.child("Recipes").orderByChild("userid").equalTo(curUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Recipe rp= dataSnapshot.getValue(Recipe.class);
                UserKey tm=new UserKey(curUser,dataSnapshot.getKey());
               //  Toast.makeText(AnotherWallActivity.this, rp.namerecipe.toString(), Toast.LENGTH_LONG).show();
                listrecipe.add(0,new Recipe(rp.namerecipe,rp.image,rp.material,rp.time,rp.ration,rp.implement,rp.cal,rp.userid,rp.like,rp.countcomment));
                final String rec=dataSnapshot.getKey();
                final int pos=listrecipe.size();
                listtruefalse.add(0,Boolean.FALSE);
                listUserKey.add(0,tm);
                listname.add(0,curName);
                listimage.add(0,curAvatar);
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, 825*listrecipe.size());
                listview.setLayoutParams(lparams);
                mDatabase.child("User").child(user.getUid()).child("Liked").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        if(rec.equals(dataSnapshot.getValue().toString())) {
                            listtruefalse.set(listrecipe.size()-pos, Boolean.TRUE);
                            //  Toast.makeText(getActivity(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();

                            mDatabase.removeEventListener(this);
                            rpAdapter.notifyDataSetChanged();
                        }


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
       // getMenuInflater().inflate(R.menu.main, menu);
      //  MenuInflater inflater=getMenuInflater();
       // inflater.inflate(R.menu.menu_search,menu);
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
        return true;
    }
}
