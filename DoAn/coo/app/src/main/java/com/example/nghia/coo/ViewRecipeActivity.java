package com.example.nghia.coo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ViewRecipeActivity extends AppCompatActivity {

    Recipe curRecipe=new Recipe();
    private LinearLayout mLayout;
    TextView name,time,ration,material;
    ListView listview;
    ImageView cover;
    Button addshopping;
    ItemImplementAdapter adapter=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        curRecipe=(Recipe)getIntent().getSerializableExtra("TheRecipe");
        Mapping();

        name.setText(curRecipe.namerecipe);
        time.setText(curRecipe.time);
        ration.setText(curRecipe.ration);
        material.setText(curRecipe.material);
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
                Cursor cur =db.GetData("SELECT * FROM Shopping");
                while(cur.moveToNext()){
                    Toast.makeText(ViewRecipeActivity.this, cur.getString(1), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void Mapping(){
        mLayout = (LinearLayout) findViewById(R.id.linearlayout1View);
        name=(TextView) findViewById(R.id.textNameView);
        time=(TextView) findViewById(R.id.editTextTimeView);
        ration=(TextView) findViewById(R.id.editTextRationView);
        material=(TextView) findViewById(R.id.editTextMaterial);
        cover=(ImageView) findViewById(R.id.coverRecipeView);
        listview=(ListView) findViewById(R.id.listViewImplement);
        addshopping=(Button) findViewById(R.id.buttonaddshopping);
    }
}
