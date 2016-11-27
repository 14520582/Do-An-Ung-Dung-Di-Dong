package com.example.nghia.coo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    Button addRecipe;
    View view;
    private DatabaseReference mDatabase;
    private ListView listview;
    ImageView imagebutton;
    ArrayList<UserKey> listUserKey =new ArrayList<UserKey>();
    ArrayList<Recipe> listrecipe=new ArrayList<Recipe>();
    ArrayList<String> listname=new ArrayList<String>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> listimage=new ArrayList<String>();
    ArrayList<Boolean> listtruefalse=new ArrayList<Boolean>();
    RecipeAdapter rpAdapter=null;


    public HomeFragment () {

        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.home));


        Mapping();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goAddRecipeScreen();
            }
        });

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goAddRecipeScreen();
            }
        });

        rpAdapter = new RecipeAdapter(this.getContext(),R.layout.introrecipe,listrecipe,listUserKey,listname,listimage,listtruefalse);
      /* mDatabase.child("Recipes").child("-KXKadRVowx0fZBCOpv_").child("like").orderByChild("liker").equalTo(user.getUid()).removeEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });*/
        LoadData();
      //  int totalItemsHeight=0;
        listview.setAdapter(rpAdapter);





        return view;
    }

    private void LoadData(){
       final DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Recipes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // Iterator iterator=dataSnapshot.

              //  Iterator iterator=dataSnapshot.getChildren().iterator();
                //((DataSnapshot)iterator.next()).ge
             /*   while (iterator.hasNext()){
                    Recipe rp =new Recipe();
                    rp.cal=((DataSnapshot)iterator.next()).getValue(Date.class);
                    rp.countcomment=((DataSnapshot)iterator.next()).getValue(Integer.class);
                    rp.image=((DataSnapshot)iterator.next()).getValue().toString();
                    rp.implement=(ArrayList<String>)((DataSnapshot)iterator.next()).getValue();
                    rp.like=((DataSnapshot)iterator.next()).getValue(Integer.class);
                    rp.material=((DataSnapshot)iterator.next()).getValue().toString();
                    rp.namerecipe=((DataSnapshot)iterator.next()).getValue().toString();
                    rp.ration=((DataSnapshot)iterator.next()).getValue().toString();
                    rp.time=((DataSnapshot)iterator.next()).getValue().toString();
                    rp.userid=((DataSnapshot)iterator.next()).getValue().toString();
                   // Toast.makeText(getActivity(),((DataSnapshot)iterator.next()).getValue().toString(),Toast.LENGTH_SHORT).show();
                  //  Toast.makeText(getActivity(),((DataSnapshot)iterator.next()).getValue().toString(),Toast.LENGTH_SHORT).show();
                    listrecipe.add(0,rp);
                  //  Toast.makeText(getActivity(),listrecipe.get(0).namerecipe,Toast.LENGTH_SHORT).show();

                }*/
               Recipe rp= dataSnapshot.getValue(Recipe.class);
                listrecipe.add(0,rp);
                UserKey tm=new UserKey(rp.userid,dataSnapshot.getKey());

                final String rec=dataSnapshot.getKey();
                final int pos=listrecipe.size();
                listtruefalse.add(0,Boolean.FALSE);
                listUserKey.add(0,tm);
                listname.add(0,"");
                listimage.add(0,"");

               // Toast.makeText(getActivity(), rp.namerecipe,Toast.LENGTH_SHORT).show();
              //  Toast.makeText(getActivity(),dataSnapshot.getKey(),Toast.LENGTH_SHORT).show();
                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, 825*listrecipe.size());

                listview.setLayoutParams(lparams);
              //  LoadImage(tm.user);

                mDatabase1.child("User").child(user.getUid()).child("Liked").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                        if(rec.equals(dataSnapshot.getValue().toString())) {
                            listtruefalse.set(listrecipe.size()-pos, Boolean.TRUE);
                           // Toast.makeText(getActivity(),"true",Toast.LENGTH_SHORT).show();
                            // listtruefalse.remove(listtruefalse.size()-1);
                           // rpAdapter.notifyDataSetChanged();
                            mDatabase1.removeEventListener(this);

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
                mDatabase1.child("User").child(tm.user).child("Name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listname.set(listrecipe.size()-pos,dataSnapshot.getValue().toString());
                      //  Toast.makeText(getActivity(),t,Toast.LENGTH_SHORT).show();
                      //  listname.remove(listname.size()-1);

                      //  rpAdapter.notifyDataSetChanged();

                      //   Toast.makeText(getActivity(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabase1.child("User").child(tm.user).child("Image").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listimage.set(listrecipe.size()-pos,dataSnapshot.getValue().toString());

                      //  listimage.remove(listimage.size()-1);
                        rpAdapter.notifyDataSetChanged();

                       //  Toast.makeText(getActivity(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




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
    private  void LoadImage(String importuser){
        //Toast.makeText(getActivity(),importuser, Toast.LENGTH_SHORT).show();

        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();

        mDatabase1.child("User").child(importuser).child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listname.add(0,dataSnapshot.getValue().toString());
                //  Toast.makeText(getActivity(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
               // listname.remove(listname.size()-1);


                 Toast.makeText(getActivity(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase1.child("User").child(importuser).child("Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listimage.add(0,dataSnapshot.getValue().toString());

               // listimage.remove(listimage.size()-1);
              //  rpAdapter.notifyDataSetChanged();

                 Toast.makeText(getActivity(),dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void Mapping(){
        listview=(ListView) view.findViewById(R.id.listViewHome);




        addRecipe=(Button) view.findViewById(R.id.buttonAddRecipe);
        imagebutton=(ImageView) view.findViewById(R.id.imageViewButton);
    }
    private void goAddRecipeScreen() {
        Intent intent = new Intent(this.getActivity(),AddRecipeActivity.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
