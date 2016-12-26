package com.example.nghia.coo;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class UserWallActivity extends AppCompatActivity {
    private RecyclerView listview;
    ImageView img;
    private static int RESULT_LOAD_IMAGE = 1;
    private DatabaseReference mDatabase;
    ArrayList<Recipe> listrecipe=new ArrayList<Recipe>();
    ArrayList<UserKey> listUserKey=new ArrayList<UserKey>();
    ArrayList<String> listname=new ArrayList<String>();
    ArrayList<String> listimage=new ArrayList<String>();
    ArrayList<Boolean> listtruefalse=new ArrayList<Boolean>();
    MyRecipeAdapter rpAdapter=null;
    ImageView avatar;
    ImageButton buttonedit,save;
    TextView nameuser;
    Uri photoUrl;
    String nameprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wall);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Mapping();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        photoUrl=user.getPhotoUrl();
        nameprofile=user.getDisplayName();
        Picasso.with(this).load(photoUrl).into(avatar);
        nameuser.setText(nameprofile);
        listview.setLayoutManager(new LinearLayoutManager(this));
        rpAdapter = new MyRecipeAdapter(listrecipe,listUserKey,listname,listimage,listtruefalse);
        LoadData();

        listview.setAdapter(rpAdapter);
        listview.addItemDecoration(new ItemDivider(this));
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
                                Toast.makeText(UserWallActivity.this,"Saved",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }
    private void Mapping(){
        listview=(RecyclerView) findViewById(R.id.listViewWallUser);
        avatar=(ImageView) findViewById(R.id.imageViewWallUser);
        nameuser=(TextView) findViewById(R.id.textViewWallUser);
        img=(ImageView) findViewById(R.id.imageBackground);
        buttonedit=(ImageButton) findViewById(R.id.imageButtonEdit);
        save=(ImageButton) findViewById(R.id.imageButtonSave);
        //  searchview=(SearchView) view.findViewById(R.id.searchView);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImageURI = data.getData();

            Picasso.with(this).load(selectedImageURI).noPlaceholder().centerCrop().fit()
                    .into((ImageView) findViewById(R.id.imageBackground));
            save.setVisibility(View.VISIBLE);
        }

    }
    private void LoadBackground(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("User").child(user.getUid()).child("Cover").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                Picasso.with(UserWallActivity.this).load(dataSnapshot.getValue().toString()).into(img);
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
