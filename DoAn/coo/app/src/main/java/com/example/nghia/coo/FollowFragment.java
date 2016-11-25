package com.example.nghia.coo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class FollowFragment extends Fragment {
    private DatabaseReference mDatabase;
    View view;
    ListView listView;

    ArrayList<String> listuser=new ArrayList<String>();
    ArrayList<String> listname=new ArrayList<String>();
    ArrayList<String> listimage=new ArrayList<String>();
    FollowAdapter rpAdapter=null;
    public FollowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view= inflater.inflate(R.layout.fragment_follow, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.follow));
        listView=(ListView) view.findViewById(R.id.listFollowingUser);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        rpAdapter = new FollowAdapter(this.getContext(),R.layout.introfollow,listimage,listname,listuser);
        LoadData();
        listView.setAdapter(rpAdapter);
        return view;
    }
    private void LoadData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("User").child(user.getUid()).child("Follow").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listuser.add(0,dataSnapshot.getKey());

                listname.add(0,"");
                listimage.add(0,"");
                mDatabase.child("User").child(dataSnapshot.getKey()).child("Name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listname.add(0,dataSnapshot.getValue().toString());

                        listname.remove(listname.size()-1);
                        rpAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                mDatabase.child("User").child(dataSnapshot.getKey()).child("Image").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listimage.add(0,dataSnapshot.getValue().toString());

                        listimage.remove(listimage.size()-1);
                        rpAdapter.notifyDataSetChanged();
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

}
