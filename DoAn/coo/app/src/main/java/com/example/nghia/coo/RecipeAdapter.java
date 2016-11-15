package com.example.nghia.coo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nghia on 10/11/2016.
 */

public class RecipeAdapter extends BaseAdapter {
    Context context;
    int mLayout;
    List<Recipe> arrayRecipe;
    Recipe curRecipe;
    public RecipeAdapter(Context context, int mLayout, List<Recipe> arrayRecipe) {
        this.context = context;
        this.mLayout = mLayout;
        this.arrayRecipe = arrayRecipe;
    }

    @Override
    public int getCount() {
        return arrayRecipe.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayRecipe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        ImageView cover;
        TextView name,time,ration;
        Button buttonView;

    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;
        ViewHolder holder=new ViewHolder();
        if(rowview==null){
            rowview=inflater.inflate(mLayout,null);
            holder.cover=(ImageView) rowview.findViewById(R.id.imageCover);
            holder.name=(TextView) rowview.findViewById(R.id.name);
            holder.ration=(TextView) rowview.findViewById(R.id.Ration);
            holder.time=(TextView) rowview.findViewById(R.id.Time);
            holder.buttonView=(Button) rowview.findViewById(R.id.buttonView);
            holder.buttonView.setTag(position);
            rowview.setTag(holder);
        }else{
            holder=(ViewHolder) rowview.getTag();
        }
        holder.name.setText(arrayRecipe.get(position).namerecipe);
        holder.time.setText(arrayRecipe.get(position).time);
        holder.ration.setText(arrayRecipe.get(position).ration);
        final String name =arrayRecipe.get(position).namerecipe;
        final  String image=arrayRecipe.get(position).image;
        final String material=arrayRecipe.get(position).material;
        final String time=arrayRecipe.get(position).time;
        final String ration=arrayRecipe.get(position).ration;
        final ArrayList<String> imp =new ArrayList<String>(arrayRecipe.get(position).implement);

        Picasso.with(context).load(arrayRecipe.get(position).image).into(holder.cover);


        holder.buttonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                curRecipe = new Recipe(name,image,material,time,ration,imp);
              //  Toast.makeText(convertView.getContext(), curRecipe.namerecipe, Toast.LENGTH_SHORT).show();
                goViewRecipeScreen(curRecipe);
            }
        });
        return rowview;
    }
    private void goViewRecipeScreen(Recipe currecipe) {
        Intent intent = new Intent(context, ViewRecipeActivity.class);
        intent.putExtra("TheRecipe",currecipe);
      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
