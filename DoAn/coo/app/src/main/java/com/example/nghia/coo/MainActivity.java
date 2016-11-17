package com.example.nghia.coo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    TextView facebookName;
    TextView facebookEmail;

    Uri photoUrl;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    CircleImageView image_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        String name="null",email="null";

        final SQLite db = new SQLite(this,"Shopping.sqlite",null,1);
        db.QueryData("CREATE TABLE IF NOT EXISTS Schedule(Id INTEGER PRIMARY KEY AUTOINCREMENT,Name VARCHAR,DateTime VARCHAR,Image TEXT,User VARCHAR,Key VARCHAR)");
        db.QueryData("CREATE TABLE IF NOT EXISTS Shopping(Id INTEGER PRIMARY KEY AUTOINCREMENT,Name VARCHAR,Ration VARCHAR,Implement TEXT)");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

                name = user.getDisplayName();
                email = user.getEmail();

                photoUrl = user.getPhotoUrl();
                //  String uid = user.getUid();

            mDatabase.child(user.getUid().toString()).child("User").child("Name").setValue(name, new DatabaseReference.CompletionListener(){
                @Override
                public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference){
                    if(databaseError==null)
                    {
                        //Toast.makeText(MainActivity.this, "luu ten thanh cong", Toast.LENGTH_LONG).show();

                    }
                }
            });
            mDatabase.child(user.getUid().toString()).child("User").child("Image").setValue(String.valueOf(photoUrl), new DatabaseReference.CompletionListener(){
                @Override
                public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference){
                    if(databaseError==null)
                    {
                       // Toast.makeText(MainActivity.this, "luu hinh thanh cong", Toast.LENGTH_LONG).show();

                    }
                }
            });

        } else {
            goLoginScreen();
              }
    //      if (AccessToken.getCurrentAccessToken() == null) {
     //      goLoginScreen();

     //   }
    /*    mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(user.getUid().toString()).child("User").child("Name").setValue(name, new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference){
                if(databaseError==null)
                {
                    Toast.makeText(MainActivity.this, "luu ten thanh cong", Toast.LENGTH_LONG).show();

                }
            }
        });
        mDatabase.child(user.getUid().toString()).child("User").child("Image").setValue(String.valueOf(photoUrl), new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference){
                if(databaseError==null)
                {
                    Toast.makeText(MainActivity.this, "luu hinh thanh cong", Toast.LENGTH_LONG).show();

                }
            }
        });*/
        setContentView(R.layout.activity_main);
        HomeFragment fragment = new HomeFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    //    Bundle b = getIntent().getExtras();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        facebookName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        facebookEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        image_profile= (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        facebookName.setText(name);
        facebookEmail.setText(email);
        Picasso.with(this).load(photoUrl).into(image_profile);
       // String a=new String(Profile.getCurrentProfile().getName());
        //if(a!=null)
        //facebookName.setText("asd");
        //Email.setText(b.getCharSequence("email"));
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/



    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();

        goLoginScreen();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    public void setActionBarTitle(String title){
        toolbar.setTitle(title);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            HomeFragment fragment = new HomeFragment();
            //fragment.getActivity().setTitle("Home");
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
            // Handle the camera action
        } else if (id == R.id.nav_recipe) {
            RecipeFragment fragment = new RecipeFragment();
           // fragment.getActivity().setTitle("My Recipes");
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_shopping) {

            ShoppingFragment fragment = new ShoppingFragment();
          //  fragment.getActivity().setTitle("Shopping List");
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_planner) {
            PlannerFragment fragment = new PlannerFragment();
           // fragment.getActivity().setTitle("Meal Planner");
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
