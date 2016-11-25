package com.example.nghia.coo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class ViewRecipeActivity extends AppCompatActivity {

    Recipe curRecipe=new Recipe();
    UserKey curUserKey=new UserKey();

    TextView name,time,ration,material,date;
    ListView listview;
    ImageView cover;
    ImageButton ava;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference mDatabase;
    String curName,curAvatar;
    Button addshopping,setschedule,buttonname,addfollow,following;
    ItemImplementAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        curRecipe=(Recipe)getIntent().getSerializableExtra("TheRecipe");
        curUserKey=(UserKey)getIntent().getSerializableExtra("UserKey");
        Mapping();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        name.setText(curRecipe.namerecipe);
        time.setText(curRecipe.time);
        ration.setText(curRecipe.ration);
        material.setText(curRecipe.material);
        if(curUserKey.user.equals(user.getUid()))
            addfollow.setVisibility(View.INVISIBLE);
        else
            LoadFollow();
        int mon=curRecipe.cal.getMonth()+1;
        int ye=curRecipe.cal.getYear()+1900;
        date.setText(curRecipe.cal.getHours()+":"+curRecipe.cal.getMinutes()+"  "+curRecipe.cal.getDate()+"/"+mon+"/"+ye);
        LoadImageName();
        final SQLite db = new SQLite(this,"Shopping.sqlite",null,1);
        adapter = new ItemImplementAdapter(this, R.layout.layout_item_implement, curRecipe.implement);
        int totalItemsHeight=0;
        listview.setAdapter(adapter);
        for (int itemPos = 0; itemPos < adapter.getCount(); itemPos++) {
            View item = adapter.getView(itemPos, null, listview);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }
        totalItemsHeight+=listview.getDividerHeight() *(adapter.getCount() - 1);
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, totalItemsHeight);
        listview.setLayoutParams(lparams);
        Picasso.with(this).load(curRecipe.image).into(cover);
        addshopping.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.QueryData("CREATE TABLE IF NOT EXISTS Shopping(Id INTEGER PRIMARY KEY AUTOINCREMENT,Name VARCHAR,Ration VARCHAR,Implement TEXT)");
                db.QueryData("INSERT INTO Shopping VALUES(null,'"+curRecipe.namerecipe+"','"+curRecipe.ration+"','"+curRecipe.material+"')");
                Toast.makeText(ViewRecipeActivity.this, R.string.addedshopping, Toast.LENGTH_SHORT).show();


            }
        });
        setschedule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goSetSchedule();

            }
        });
        ava.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(curUserKey.user.equals(user.getUid()))
                {
                    goWallUser();
                }else
                    goAnotherWall(curName,curAvatar,curUserKey.user);

            }
        });
        buttonname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(curUserKey.user.equals(user.getUid()))
                {
                    goWallUser();
                }else
                goAnotherWall(curName,curAvatar,curUserKey.user);

            }
        });
        addfollow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               following.setVisibility(View.VISIBLE);
                addfollow.setVisibility(View.INVISIBLE);
                mDatabase.child("User").child(user.getUid()).child("Follow").child(curUserKey.user).setValue(curUserKey.user, new DatabaseReference.CompletionListener(){
                    @Override
                    public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference){
                        if(databaseError==null)
                        {
                            Toast.makeText(ViewRecipeActivity.this, getString(R.string.followed), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
        following.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               // addfollow.setVisibility(View.VISIBLE);
               // following.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void goWallUser(){
        Intent intent = new Intent(this,UserWallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void goAnotherWall(String name,String avatar,String user){
        Intent intent = new Intent(this,AnotherWallActivity.class);
        intent.putExtra("ID",user);
        intent.putExtra("ImageCover",avatar);
        intent.putExtra("NameWall",name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void LoadFollow(){
        final DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference();
        mDatabase2.child("User").child(user.getUid()).child("Follow").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(curUserKey.user.equals(dataSnapshot.getValue().toString()))
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
    private void LoadImageName(){
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference();
        mDatabase1.child("User").child(curUserKey.user).child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                buttonname.setText(dataSnapshot.getValue().toString());
                curName=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase1.child("User").child(curUserKey.user).child("Image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Picasso.with(ViewRecipeActivity.this).load(dataSnapshot.getValue().toString()).into(ava);
                curAvatar=dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void goSetSchedule() {
        Intent intent = new Intent(this,SetScheduleActivity.class);
        intent.putExtra("UserKey",curUserKey);
        intent.putExtra("Image",curRecipe.image);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void Mapping(){

        name=(TextView) findViewById(R.id.textNameView);
        date=(TextView) findViewById(R.id.textViewDatePublish);
        time=(TextView) findViewById(R.id.editTextTimeView);
        ration=(TextView) findViewById(R.id.editTextRationView);
        material=(TextView) findViewById(R.id.editTextMaterial);
        cover=(ImageView) findViewById(R.id.coverRecipeView);
        listview=(ListView) findViewById(R.id.listViewImplement);
        addshopping=(Button) findViewById(R.id.buttonaddshopping);
        buttonname=(Button) findViewById(R.id.button3Name);
        addfollow=(Button) findViewById(R.id.buttonAddFollow);
        following=(Button) findViewById(R.id.buttonFollowing);
        ava=(ImageButton) findViewById(R.id.imageButtonAva);
        setschedule=(Button) findViewById(R.id.buttonPlannerRecipe);
    }
}
