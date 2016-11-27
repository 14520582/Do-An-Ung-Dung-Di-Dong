package com.example.nghia.coo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nghia on 26/11/2016.
 */

public class CommentApdater extends BaseAdapter {
    Context context;
    int mLayout;
    List<String> arrayComment;
    List<String> arrayName;
    List<String> arrayAvatar;

    public CommentApdater(Context context, int mLayout, List<String> arrayComment, List<String> arrayName, List<String> arrayAvatar) {
        this.context = context;
        this.mLayout = mLayout;
        this.arrayComment = arrayComment;
        this.arrayName = arrayName;
        this.arrayAvatar = arrayAvatar;
    }

    @Override
    public int getCount() {
        return arrayComment.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder {
        CircleImageView avatar;
        TextView nameuser,message;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = convertView;
        CommentApdater.ViewHolder holder=new CommentApdater.ViewHolder();
        if(rowview==null){
            rowview=inflater.inflate(mLayout,null);
            holder.avatar=(CircleImageView) rowview.findViewById(R.id.user_comment);
            holder.nameuser=(TextView) rowview.findViewById(R.id.namecomment);
            holder.message=(TextView) rowview.findViewById(R.id.message);
            rowview.setTag(holder);
        }
        else{
            holder = (ViewHolder) rowview.getTag();
        }
       // holder.nameuser.setText(arrayName.get(position));
     //   if(arrayAvatar.get(position)==null){
     //       Toast.makeText(context,"null",Toast.LENGTH_SHORT).show();
     //   }else
     //       Toast.makeText(context,"not null"+arrayAvatar.get(position),Toast.LENGTH_SHORT).show();
        holder.message.setText(arrayComment.get(position));
        holder.nameuser.setText(arrayName.get(position));
        //Picasso.with(context).load(arrayAvatar.get(position)).into(holder.avatar);
       // Toast.makeText(context,arrayName.get(position)+position,Toast.LENGTH_SHORT).show();
        if(arrayAvatar.get(position).length()!=0) {
            Picasso.with(context).load(arrayAvatar.get(position)).into(holder.avatar);

        }
        return  rowview;
    }
}
