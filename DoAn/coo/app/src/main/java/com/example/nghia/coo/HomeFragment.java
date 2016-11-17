package com.example.nghia.coo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

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
  //  private SearchView searchview;
    private DatabaseReference mDatabase;
    private ListView listview;
    ArrayList<UserKey> listUserKey =new ArrayList<UserKey>();
    ArrayList<Recipe> listrecipe=new ArrayList<Recipe>();
    RecipeAdapter rpAdapter=null;
    boolean ready =false;

    public HomeFragment (){

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_home, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.home));
        Mapping();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goAddRecipeScreen();
            }
        });

        rpAdapter = new RecipeAdapter(this.getContext(),R.layout.introrecipe,listrecipe,listUserKey);
       // String a= new String (listID.get(0));

      //  Toast.makeText(getActivity(), listID.size()+"", Toast.LENGTH_LONG).show();
        LoadData();

        listview.setAdapter(rpAdapter);


        return view;
    }
    private void LoadData(){
     //   for(int i=0;i<listID.size();i++){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       // Toast.makeText(getActivity(), listID.get(0), Toast.LENGTH_LONG).show();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()){
                    final String user=dsp.getKey();
                    mDatabase.child(dsp.getKey()).child("Recipes").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Recipe rp= dataSnapshot.getValue(Recipe.class);
                            UserKey tm=new UserKey(user,dataSnapshot.getKey());
                            // Toast.makeText(getApplicationContext(), dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                            listrecipe.add(0,new Recipe(rp.namerecipe,rp.image,rp.material,rp.time,rp.ration,rp.implement));
                            listUserKey.add(0,tm);
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void Mapping(){
        listview=(ListView) view.findViewById(R.id.listViewHome);
      //  searchview=(SearchView) view.findViewById(R.id.searchView1);
        addRecipe=(Button) view.findViewById(R.id.buttonAddRecipe);
    }
    private void goAddRecipeScreen() {
        Intent intent = new Intent(this.getActivity(),AddRecipeActivity.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
