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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nghia on 21/11/2016.
 */

public class FollowAdapter extends BaseAdapter {
    Context context;
    int mLayout;
    List<String> arrayImage;
    List<String> arrayName;
    List<String> arrayUser;

    public FollowAdapter(Context context, int mLayout, List<String> arrayImage, List<String> arrayName, List<String> arrayUser) {
        this.context = context;
        this.mLayout = mLayout;
        this.arrayImage = arrayImage;
        this.arrayName = arrayName;
        this.arrayUser = arrayUser;
    }

    @Override
    public int getCount() {
        return arrayUser.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        CircleImageView avatar;
        Button buttonremove,buttonname;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;
        FollowAdapter.ViewHolder holder=new FollowAdapter.ViewHolder();
        if(rowview==null){
            rowview=inflater.inflate(mLayout,null);
            holder.buttonremove=(Button) rowview.findViewById(R.id.buttonunfollow);
            holder.buttonname=(Button) rowview.findViewById(R.id.buttonNameFollow);
            holder.avatar=(CircleImageView) rowview.findViewById(R.id.user_following);
            rowview.setTag(holder);
        }else{
            holder = (ViewHolder) rowview.getTag();
        }
        final String nameuser =arrayName.get(position);
        final String imageuser =arrayImage.get(position);
        final String userid=arrayUser.get(position);
        holder.buttonname.setText(arrayName.get(position));
        if(arrayImage.get(position).length()!=0) {
            Picasso.with(context).load(arrayImage.get(position)).into(holder.avatar);

        }
        holder.avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAnotherWall(nameuser,imageuser,userid);
            }
        });
        holder.buttonname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAnotherWall(nameuser,imageuser,userid);
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
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase.child("User").child(user.getUid()).child("Follow").child(userid).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Toast.makeText(context,context.getString(R.string.unfollowed),Toast.LENGTH_SHORT).show();
                        FollowFragment fragment = new FollowFragment();
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
                builder.setMessage(context.getString(R.string.confirmunfollow)).setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();
            }
        });
        return rowview;
    }
    private void goAnotherWall(String name,String avatar,String user){
        Intent intent = new Intent(context,AnotherWallActivity.class);
        intent.putExtra("ID",user);
        intent.putExtra("ImageCover",avatar);
        intent.putExtra("NameWall",name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
