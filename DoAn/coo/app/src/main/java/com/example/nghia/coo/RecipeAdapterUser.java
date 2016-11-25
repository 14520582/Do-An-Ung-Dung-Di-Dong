package com.example.nghia.coo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

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
 * Created by nghia on 18/11/2016.
 */

public class RecipeAdapterUser extends BaseAdapter implements Filterable {
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
    public RecipeAdapterUser(Context context, int mLayout, List<Recipe> arrayRecipe,List<UserKey> arrayUserKey,List<String> arrayName,List<String> arrayImage,List<Boolean> arrayTrueFalse) {
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
        TextView name,time,ration,date,textlike;
        Button buttonView,buttonname,buttonremove,buttonboth,buttonlike,buttonunlike,buttoncomment1,buttoncomment2;
        ImageButton avatar;

    }
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;
        RecipeAdapterUser.ViewHolder holder=new RecipeAdapterUser.ViewHolder();
        if(rowview==null){
            rowview=inflater.inflate(mLayout,null);
            holder.cover=(ImageView) rowview.findViewById(R.id.imageCoverUser);
            holder.name=(TextView) rowview.findViewById(R.id.nameUser);
            holder.ration=(TextView) rowview.findViewById(R.id.RationUser);
            holder.time=(TextView) rowview.findViewById(R.id.TimeUser);
            holder.buttonView=(Button) rowview.findViewById(R.id.buttonViewUser);
            holder.date=(TextView) rowview.findViewById(R.id.textViewDateUser);
            holder.buttonlike=(Button) rowview.findViewById(R.id.buttonLikeUser);
            holder.buttonunlike=(Button) rowview.findViewById(R.id.buttonUnlikeUser);
            holder.buttonboth=(Button) rowview.findViewById(R.id.buttonBothLikeUser);
            holder.buttoncomment1=(Button) rowview.findViewById(R.id.buttonComment1User);
            holder.buttoncomment2=(Button) rowview.findViewById(R.id.buttonComment2User);
            holder.textlike=(TextView)  rowview.findViewById(R.id.textViewLikeUser);
            holder.buttonname=(Button) rowview.findViewById(R.id.buttonNameUser);
            holder.buttonremove=(Button) rowview.findViewById(R.id.buttonRemoveRecipe);
            holder.avatar=(ImageButton) rowview.findViewById(R.id.imageAvatarUser);
            holder.buttonView.setTag(position);
            rowview.setTag(holder);
        }else{
            holder=(RecipeAdapterUser.ViewHolder) rowview.getTag();
        }
        holder.name.setText(arrayRecipe.get(position).namerecipe);
        holder.time.setText(arrayRecipe.get(position).time);
        holder.ration.setText(arrayRecipe.get(position).ration);
        holder.buttonname.setText(arrayName.get(position));
        final int mon=arrayRecipe.get(position).cal.getMonth()+1;
        final int ye=arrayRecipe.get(position).cal.getYear()+1900;
        holder.date.setText(arrayRecipe.get(position).cal.getHours()+":"+arrayRecipe.get(position).cal.getMinutes()+"  "+arrayRecipe.get(position).cal.getDate()+"/"+mon+"/"+ye);
        final String name =arrayRecipe.get(position).namerecipe;
        final  String image=arrayRecipe.get(position).image;
        final String material=arrayRecipe.get(position).material;
        final Date cal=arrayRecipe.get(position).cal;
        final String time=arrayRecipe.get(position).time;
        final String ration=arrayRecipe.get(position).ration;
        final String user = arrayUserKey.get(position).user;
        final String key = arrayUserKey.get(position).key;
        final int like=arrayRecipe.get(position).like;
        final ArrayList<String> imp =new ArrayList<String>(arrayRecipe.get(position).implement);
        final int pos=position;
        Picasso.with(context).load(arrayRecipe.get(position).image).into(holder.cover);
        if(arrayImage.get(position).length()!=0)
            Picasso.with(context).load(arrayImage.get(position)).into(holder.avatar);
        if(like==0){
            holder.textlike.setVisibility(View.INVISIBLE);
        }else
        {
            holder.textlike.setVisibility(View.VISIBLE);
            holder.textlike.setText(like+"");
        }
        holder.buttonunlike.setVisibility( (arrayTrueFalse.get(pos) ? View.VISIBLE : View.INVISIBLE ) );
        holder.buttonlike.setVisibility( (arrayTrueFalse.get(pos) ?  View.INVISIBLE:View.VISIBLE  ) );
        holder.buttonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                curRecipe = new Recipe(name,image,material,time,ration,imp,cal,user,like);
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
        holder.buttonremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


                                mDatabase.child("Recipes").child(key).removeValue(new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                                Toast.makeText(context,context.getString(R.string.removerecipe),Toast.LENGTH_SHORT).show();
                                                RecipeFragment fragment = new RecipeFragment();
                                                android.support.v4.app.FragmentTransaction fragmentTransaction =
                                                        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                                fragmentTransaction.commit();


                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }


                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.confirmremoverecipe)).setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();
            }
        });
        return rowview;
    }
    public void setEditMode(boolean value,int po){
        this.arrayTrueFalse.set(po,value);


    }
    private void unlike(String recipe){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(userid.getUid()).child("Liked").child(recipe).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError==null){}

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

                //   int count=(int)dataSnapshot.getValue()+1;
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
    private void setCountUnLike(String recipe,int po){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final String recipe1=recipe;
        mDatabase.child("Recipes").child(recipe).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int k=dataSnapshot.getValue(Integer.class)-1;

                //  int count=(int)dataSnapshot.getValue()-1;
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
                            FilteredArrList.add(new Recipe(arrayRecipeSource.get(i).namerecipe,arrayRecipeSource.get(i).image,arrayRecipeSource.get(i).material,arrayRecipeSource.get(i).time,arrayRecipeSource.get(i).ration,arrayRecipeSource.get(i).implement,arrayRecipeSource.get(i).cal,arrayRecipeSource.get(i).userid,arrayRecipeSource.get(i).like));
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
