package com.example.nghia.coo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nghia on 10/11/2016.
 */

public class RecipeAdapter extends BaseAdapter implements Filterable {
    Context context;
    int mLayout;
    List<Recipe> arrayRecipe;
    List<Recipe> arrayRecipeSource;
    List<UserKey> arrayUserKey;
    List<UserKey> arrayUserKeySource;
    List<String> arrayName;
    List<String> arrayNameSource;
    List<String> arrayImage;
    List<String> arrayImageSource;
    List<Boolean> arrayTrueFalse;
    List<Boolean> arrayTrueFalseSource;
    Recipe curRecipe;
    UserKey curUserKey;
    FirebaseUser userid = FirebaseAuth.getInstance().getCurrentUser();

    public RecipeAdapter(Context context, int mLayout, List<Recipe> arrayRecipe,List<UserKey> arrayUserKey,List<String> arrayName,List<String> arrayImage,List<Boolean> arrayTrueFalse) {
        this.context = context;
        this.mLayout = mLayout;
        this.arrayRecipe = arrayRecipe;
        this.arrayUserKey=arrayUserKey;
        this.arrayName = arrayName;
        this.arrayImage = arrayImage;
        this.arrayRecipeSource=arrayRecipe;
        this.arrayUserKeySource=arrayUserKey;
        this.arrayNameSource = arrayName;
        this.arrayImageSource = arrayImage;
        this.arrayTrueFalse=arrayTrueFalse;
        this.arrayTrueFalseSource=arrayTrueFalse;


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
        TextView name,time,ration,date,textlike,textcomment;
        Button buttonView,buttonname,buttonboth,buttonlike,buttonunlike,buttoncomment1,buttoncomment2;
        ImageButton avatar;


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
            holder.textcomment=(TextView) rowview.findViewById(R.id.textViewCountComment);
            holder.buttonView=(Button) rowview.findViewById(R.id.buttonView);
            holder.buttonname=(Button) rowview.findViewById(R.id.buttonName);
            holder.buttonlike=(Button) rowview.findViewById(R.id.buttonLike);
            holder.buttonunlike=(Button) rowview.findViewById(R.id.buttonUnlike);
            holder.buttonboth=(Button) rowview.findViewById(R.id.buttonBothLike);
            holder.buttoncomment1=(Button) rowview.findViewById(R.id.buttonComment1);
            holder.buttoncomment2=(Button) rowview.findViewById(R.id.buttonComment2);
            holder.textlike=(TextView)  rowview.findViewById(R.id.textViewLike);
            holder.date=(TextView) rowview.findViewById(R.id.textViewDate);
            holder.avatar=(ImageButton) rowview.findViewById(R.id.imageAvatar);
            holder.buttonView.setTag(position);
            rowview.setTag(holder);
        }else{
            holder=(ViewHolder) rowview.getTag();
        }
        holder.name.setText(arrayRecipe.get(position).namerecipe);
        holder.time.setText(arrayRecipe.get(position).time);
        final int pos=position;
        holder.ration.setText(arrayRecipe.get(position).ration);
        final int mon=arrayRecipe.get(position).cal.getMonth()+1;
        final int ye=arrayRecipe.get(position).cal.getYear()+1900;
        holder.date.setText(arrayRecipe.get(position).cal.getHours()+":"+arrayRecipe.get(position).cal.getMinutes()+"  "+arrayRecipe.get(position).cal.getDate()+"/"+mon+"/"+ye);
        holder.buttonname.setText(arrayName.get(pos));
        final String nameUser=arrayName.get(position);
        final String name =arrayRecipe.get(position).namerecipe;
        final  String image=arrayRecipe.get(position).image;
        final String material=arrayRecipe.get(position).material;
        final String time=arrayRecipe.get(position).time;

        final String ration=arrayRecipe.get(position).ration;
        final String user = arrayUserKey.get(position).user;
        final String key = arrayUserKey.get(position).key;
        final String imageUser=arrayImage.get(position);
        final Date cal=arrayRecipe.get(position).cal;
        final int like=arrayRecipe.get(position).like;
        final int cm =arrayRecipe.get(position).countcomment;
        final ArrayList<String> imp =new ArrayList<String>(arrayRecipe.get(position).implement);

        if(like==0){
            holder.textlike.setVisibility(View.INVISIBLE);
        }else
        {
            holder.textlike.setVisibility(View.VISIBLE);
            holder.textlike.setText(like+"");
        }
        if(cm==0){
            holder.textcomment.setVisibility(View.INVISIBLE);
        }else
        {
            holder.textcomment.setVisibility(View.VISIBLE);
            if(cm>1)
            holder.textcomment.setText(cm+" comments");
            else
                holder.textcomment.setText(cm+" comment");
        }
        holder.buttoncomment2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goCommentScreen(key);
               // Toast.makeText(context,key,Toast.LENGTH_SHORT).show();

            }
        });
        Picasso.with(context).load(arrayRecipe.get(position).image).into(holder.cover);
        if(arrayImage.get(position).length()!=0) {
            Picasso.with(context).load(arrayImage.get(pos)).into(holder.avatar);

        }
        holder.buttonunlike.setVisibility( (arrayTrueFalse.get(pos) ? View.VISIBLE : View.INVISIBLE ) );
        holder.buttonlike.setVisibility( (arrayTrueFalse.get(pos) ?  View.INVISIBLE:View.VISIBLE  ) );
        holder.buttonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                curRecipe = new Recipe(name,image,material,time,ration,imp,cal,user,like,cm);
                curUserKey=new UserKey(user,key);

                goViewRecipeScreen(curRecipe,curUserKey);
            }
        });
        holder.buttonunlike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                setEditMode(false,pos);
                unlike(key);
                setCountUnLike(key,pos);

            }
        });
        holder.buttonlike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setEditMode(true,pos);
                like(key);
                setCountLike(key,pos);

            }
        });
        holder.buttonboth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    if(arrayTrueFalse.get(pos)){
                        setEditMode(false,pos);
                        unlike(key);
                    setCountUnLike(key,pos);}
                    else{
                        setEditMode(true,pos);
                        like(key);
                    setCountLike(key,pos);}
            }
        });
        holder.buttonname.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(imageUser.length()!=0&&nameUser.length()!=0){
                if(user.equals(userid.getUid()))
                {
                    goWallUser();
                }else
                {
                    curUserKey=new UserKey(user,key);
                    goAnotherWall(nameUser,imageUser,curUserKey.user);}}
            }
        });
        holder.avatar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(imageUser.length()!=0&&nameUser.length()!=0){
                if(user.equals(userid.getUid()))
                {
                    goWallUser();
                }else{
                    curUserKey=new UserKey(user,key);
                    goAnotherWall(nameUser,imageUser,curUserKey.user);}}
            }
        });

        return rowview;
    }
    public void setEditMode(boolean value,int po){
        this.arrayTrueFalse.set(po,value);


    }
    private void goWallUser(){
        Intent intent = new Intent(context,UserWallActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    private void goAnotherWall(String name,String avatar,String user){
        Intent intent = new Intent(context,AnotherWallActivity.class);
        intent.putExtra("ID",user);
        intent.putExtra("ImageCover",avatar);
        intent.putExtra("NameWall",name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
    private void unlike(String recipe){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(userid.getUid()).child("Liked").child(recipe).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null)
                {}
            }
        });
    }
    private void setCountLike(String recipe,int po){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String recipe1=recipe;
        mDatabase.child("Recipes").child(recipe).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k=dataSnapshot.getValue(Integer.class)+1;

                mDatabase.child("Recipes").child(recipe1).child("like").setValue(k);
               // mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        arrayRecipe.get(po).like+=1;
        this.notifyDataSetChanged();

    }
    private void goCommentScreen(String key){
        Intent intent = new Intent(context, CommentActivity.class);

        intent.putExtra("Key",key);
        //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    private void setCountUnLike(String recipe,int po){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String recipe1=recipe;
        mDatabase.child("Recipes").child(recipe).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k=dataSnapshot.getValue(Integer.class)-1;

                mDatabase.child("Recipes").child(recipe1).child("like").setValue(k);
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        arrayRecipe.get(po).like-=1;
        this.notifyDataSetChanged();
    }
    private void like(String recipe){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(userid.getUid()).child("Liked").child(recipe).setValue(recipe, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            }
        });
    }
    private void goViewRecipeScreen(Recipe currecipe,UserKey curuserkey) {
        Intent intent = new Intent(context, ViewRecipeActivity.class);
        intent.putExtra("TheRecipe",currecipe);
        intent.putExtra("UserKey",curuserkey);

      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter(){
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                RecipeFull currecipefull = (RecipeFull)results.values;
                arrayRecipe=currecipefull.currecipe;
                arrayUserKey=currecipefull.curuserkey;
                arrayImage=currecipefull.curimage;
                arrayName=currecipefull.curname;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                ArrayList<Recipe> FilteredArrList = new ArrayList<Recipe>();
                ArrayList<UserKey> FilteredArrList1 = new ArrayList<UserKey>();
                ArrayList<String> FilteredArrList2 = new ArrayList<String>();
                ArrayList<String> FilteredArrList3 = new ArrayList<String>();
                ArrayList<Boolean> FilteredArrList6 = new ArrayList<Boolean>();

                //RecipeFull FilteredArrList4;
                if (arrayRecipeSource == null) {
                    arrayRecipeSource=new ArrayList<Recipe>(arrayRecipe);
                }


                if (constraint == null || constraint.length() == 0) {
                    results.count = arrayRecipeSource.size();
                    RecipeFull FilteredArrList5=new RecipeFull((ArrayList<Recipe>) arrayRecipeSource,(ArrayList<UserKey>) arrayUserKeySource,( ArrayList<String>) arrayNameSource,(ArrayList<String>) arrayImageSource,(ArrayList<Boolean>) arrayTrueFalseSource);
                    results.values = FilteredArrList5;

                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < arrayRecipeSource.size(); i++) {
                        String data = arrayRecipeSource.get(i).namerecipe;
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(new Recipe(arrayRecipeSource.get(i).namerecipe,arrayRecipeSource.get(i).image,arrayRecipeSource.get(i).material,arrayRecipeSource.get(i).time,arrayRecipeSource.get(i).ration,arrayRecipeSource.get(i).implement,arrayRecipeSource.get(i).cal,arrayRecipeSource.get(i).userid,arrayRecipeSource.get(i).like,arrayRecipeSource.get(i).countcomment));
                            FilteredArrList1.add(new UserKey(arrayUserKeySource.get(i).user,arrayUserKeySource.get(i).key));
                            FilteredArrList2.add(arrayNameSource.get(i));
                            FilteredArrList3.add(arrayImageSource.get(i));
                            FilteredArrList6.add(arrayTrueFalseSource.get(i));

                        }
                    }
                    // set the Filtered result to return
                    RecipeFull FilteredArrList4=new RecipeFull(FilteredArrList,FilteredArrList1,FilteredArrList2,FilteredArrList3,FilteredArrList6);
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList4;

                }
                return results;
            }
        };
        return filter;
    }

}
