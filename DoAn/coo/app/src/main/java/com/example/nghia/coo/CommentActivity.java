package com.example.nghia.coo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;



public class CommentActivity extends AppCompatActivity {
    ListView listView;
    ImageButton send;
    EditText boxtext;
    ArrayList<Comment> arrayList=new ArrayList<Comment>();
    String currecipe;
    ArrayList<String> arrayMessage=new ArrayList<String>();
    ArrayList<String> arrayName=new ArrayList<String>();
    ArrayList<String> arrayAvatar=new ArrayList<String>();
    CommentApdater adapter=null;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_comment);
        currecipe=(String) getIntent().getSerializableExtra("Key");
        listView=(ListView) findViewById(R.id.listmessage);
        send=(ImageButton) findViewById(R.id.post);
        boxtext=(EditText) findViewById(R.id.textBox);

        adapter=new CommentApdater(CommentActivity.this,R.layout.layout_a_comment,arrayMessage,arrayName,arrayAvatar);
        LoadData();
       // listView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boxtext.getText().length()!=0){
                   // arrayName.add(user.getDisplayName());
                  //  arrayAvatar.add(user.getPhotoUrl().toString());
                  //  arrayMessage.add(boxtext.getText().toString());
                    updateData();
                    pushComment();

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    boxtext.setText("");
                  //  GetData();

                    adapter.notifyDataSetChanged();
                }
            }
        });
        listView.setAdapter(adapter);

    }
    private void pushComment(){
        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("Comment").child(currecipe).push().setValue(new Comment(user.getUid(),boxtext.getText().toString()));
    }
    private void updateData(){
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Recipes").child(currecipe).child("countcomment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i=dataSnapshot.getValue(Integer.class)+1;
                mDatabase.child("Recipes").child(currecipe).child("countcomment").setValue(i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void LoadData(){
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Comment").child(currecipe).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               // Toast.makeText(CommentActivity.this,dataSnapshot.getValue(Comment.class).comment,Toast.LENGTH_SHORT).show();
                      Iterator iterator= dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                   // Toast.makeText(CommentActivity.this,((DataSnapshot)iterator.next()).getValue().toString(),Toast.LENGTH_SHORT).show();
                    arrayMessage.add(((DataSnapshot)iterator.next()).getValue().toString());
                    String userid=((DataSnapshot)iterator.next()).getValue().toString();
                   // Toast.makeText(CommentActivity.this,arrayMessage.get(arrayMessage.size()-1),Toast.LENGTH_SHORT).show();
                    arrayName.add("");
                    arrayAvatar.add("");
                    final int pos=arrayAvatar.size()-1;
                   // Toast.makeText(CommentActivity.this,pos+"",Toast.LENGTH_SHORT).show();
                    mDatabase.child("User").child(userid).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            arrayName.set(pos,dataSnapshot.getValue().toString());
                          //  Toast.makeText(CommentActivity.this,pos+"",Toast.LENGTH_SHORT).show();
                         //   arrayName.remove(pos);
                           // Toast.makeText(CommentActivity.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mDatabase.child("User").child(userid).child("Image").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            arrayAvatar.set(pos,dataSnapshot.getValue().toString());
                           // listView.setAdapter(adapter);
                         //   arrayAvatar.remove(pos);
                          //  Toast.makeText(CommentActivity.this,pos+"",Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                           // Toast.makeText(CommentActivity.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                   // adapter.notifyDataSetChanged();
                   // adapter.notifyDataSetChanged();
                }
              //  Comment cm=dataSnapshot.getValue(Comment.class);
               //     Toast.makeText(CommentActivity.this,((DataSnapshot)iterator.next()).getValue(Comment.class).comment,Toast.LENGTH_SHORT).show();
                   // arrayMessage.add(((DataSnapshot)iterator.next()).getValue(Comment.class).comment);
           /*     arrayMessage.add(cm.comment);
                    mDatabase.child("User").child(cm.usercomment).child("Name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            arrayName.add(dataSnapshot.getValue().toString());
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mDatabase.child("User").child(cm.usercomment).child("Image").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            arrayAvatar.add(dataSnapshot.getValue().toString());
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                adapter.notifyDataSetChanged();*/
              //  }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
              /*  Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    //  Toast.makeText(CommentActivity.this,((DataSnapshot)iterator.next()).getValue().toString(),Toast.LENGTH_SHORT).show();
                    arrayMessage.add(((DataSnapshot) iterator.next()).getValue().toString());
                    String userid = ((DataSnapshot) iterator.next()).getValue().toString();
                    mDatabase.child("User").child(userid).child("Name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            arrayName.add(dataSnapshot.getValue().toString());
                            Toast.makeText(CommentActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mDatabase.child("User").child(userid).child("Image").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            arrayAvatar.add(dataSnapshot.getValue().toString());
                            Toast.makeText(CommentActivity.this, dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                }*/
              //  Iterator iterator = dataSnapshot.getChildren().iterator();
               // while (iterator.hasNext()) {
               // Toast.makeText(CommentActivity.this,"change",Toast.LENGTH_SHORT).show();}
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
       /* for(int i=0;i<arrayList.size();i++){
            arrayAvatar.add("");
            arrayName.add("");
            mDatabase.child("User").child(arrayList.get(i).usercomment).child("Name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arrayName.add(dataSnapshot.getValue().toString());
                   // Toast.makeText(CommentActivity.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                    arrayName.remove(0);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

                mDatabase.child("User").child(arrayList.get(i).usercomment).child("Image").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        arrayAvatar.add(dataSnapshot.getValue().toString());
                     //   Toast.makeText(CommentActivity.this,dataSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show();
                        arrayAvatar.remove(0);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }*/
    }
}
