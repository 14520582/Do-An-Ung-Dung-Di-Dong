package com.example.nghia.coo;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {
    View view;
    private ListView listview;
    ImageView img;
    private static int RESULT_LOAD_IMAGE = 1;
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
    ImageButton buttonedit,save;
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
       final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        photoUrl=user.getPhotoUrl();
        nameprofile=user.getDisplayName();
        Picasso.with(getActivity()).load(photoUrl).into(avatar);
        nameuser.setText(nameprofile);
        rpAdapter = new RecipeAdapterUser(this.getContext(),R.layout.introrecipeuser,listrecipe,listUserKey,listname,listimage,listtruefalse);
        LoadData();

        listview.setAdapter(rpAdapter);
        buttonedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        LoadBackground();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://cookman-fe463.appspot.com");
                StorageReference mountainsRef = storageRef.child(user.getUid()+".png");
                mountainsRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                img.setDrawingCacheEnabled(true);
                img.buildDrawingCache();
                Bitmap bitmap = img.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mDatabase.child("User").child(user.getUid()).child("Cover").setValue(String.valueOf(taskSnapshot.getDownloadUrl()), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                Toast.makeText(getActivity(),"Saved",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
            }
        });
        return view;
    }
    private void Mapping(){
        listview=(ListView) view.findViewById(R.id.listView);
        avatar=(ImageView) view.findViewById(R.id.imageViewUser);
        nameuser=(TextView) view.findViewById(R.id.textViewUser);
        img=(ImageView) view.findViewById(R.id.imageBackground1);
        buttonedit=(ImageButton) view.findViewById(R.id.imageButtonEdit1);
        save=(ImageButton) view.findViewById(R.id.imageButtonSave1);
      //  searchview=(SearchView) view.findViewById(R.id.searchView);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImageURI = data.getData();

            Picasso.with(getActivity()).load(selectedImageURI).noPlaceholder().centerCrop().fit()
                    .into((ImageView) view.findViewById(R.id.imageBackground1));
            save.setVisibility(View.VISIBLE);
        }

    }
    private void LoadBackground(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("User").child(user.getUid()).child("Cover").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                Picasso.with(getActivity()).load(dataSnapshot.getValue().toString()).into(img);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void LoadData(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase.child("Recipes").orderByChild("userid").equalTo(user.getUid().toString()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Recipe rp= dataSnapshot.getValue(Recipe.class);
                UserKey tm=new UserKey(user.getUid().toString(),dataSnapshot.getKey());
              // Toast.makeText(getApplicationContext(), rp.toString(), Toast.LENGTH_LONG).show();
                listrecipe.add(0,new Recipe(rp.namerecipe,rp.image,rp.material,rp.time,rp.ration,rp.implement,rp.cal,rp.userid,rp.like,rp.countcomment));
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
