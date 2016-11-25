package com.example.nghia.coo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddRecipeActivity extends AppCompatActivity {
    CircleImageView buttonedit;
    private static int RESULT_LOAD_IMAGE = 1;
    private DatabaseReference mDatabase;
    private LinearLayout mLayout;
    private Button mButton,dStep;
    private EditText editText1,editTextName,editTextTime,editTextRation,editTextMaterial;
    private FloatingActionButton myFab;
    int step=1;
    private ImageView cover;
    Toolbar toolbar;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Mapping();

        final  StorageReference storageRef = storage.getReferenceFromUrl("gs://cookman-fe463.appspot.com");

        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmpty())
                {DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                Calendar calendar= Calendar.getInstance();
                                StorageReference mountainsRef = storageRef.child("cover"+calendar.getTimeInMillis()+".png");
                                cover.setDrawingCacheEnabled(true);
                                cover.buildDrawingCache();
                                Bitmap bitmap = cover.getDrawingCache();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] data = baos.toByteArray();

                                UploadTask uploadTask = mountainsRef.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Recipe rp=new Recipe();
                                        rp.time=editTextTime.getText().toString();
                                        rp.ration=editTextRation.getText().toString();
                                        rp.material=editTextMaterial.getText().toString();
                                        rp.namerecipe=editTextName.getText().toString();
                                        rp.image=String.valueOf(downloadUrl);
                                        rp.cal=new Date();
                                        rp.userid=user.getUid();
                                        rp.like=0;
                                        getStringfromLayout(rp.implement);
                                        mDatabase.child("Recipes").push().setValue(rp, new DatabaseReference.CompletionListener(){
                                            @Override
                                            public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference){
                                                if(databaseError==null)
                                                {
                                                    Toast.makeText(AddRecipeActivity.this,getString(R.string.uploadsucess),Toast.LENGTH_SHORT).show();
                                                    goMainScreen();
                                                }
                                            }
                                        });
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }


                    }
                };

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivity.this);
                    builder.setMessage(getString(R.string.confirmupload)).setPositiveButton(R.string.yes, dialogClickListener)
                            .setNegativeButton(R.string.no, dialogClickListener).show();



                }

                else
                    Toast.makeText(AddRecipeActivity.this, getString(R.string.isEmpty), Toast.LENGTH_LONG).show();
            }
        });
        buttonedit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View arg0) {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step++;
                mLayout.addView(createNewTextView(""+step));
                mLayout.addView(createNewEditText());


            }
        });
        dStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(step>1){

                mLayout.removeViewAt(6+step*2-2);
                    mLayout.removeViewAt(6+step*2-2);
                step--;}
            }
        });

    }
    private void goMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void getStringfromLayout(ArrayList<String> al){
        int count=mLayout.getChildCount();


        int j=0;
        for(int i=7;i<count;i+=2)
        {
            EditText txt=(EditText) mLayout.getChildAt(i);

            al.add(txt.getText().toString());


            j++;
        }
    }
    private boolean isEmpty(){
        if(editTextRation.getText().toString().trim().length()==0||editTextName.getText().toString().trim().length()==0
                ||editTextTime.getText().toString().trim().length()==0
                ||editTextMaterial.getText().toString().trim().length()==0)
            return true;
        else
            return false;

    }
    private void Mapping(){
        mLayout = (LinearLayout) findViewById(R.id.linearlayout1);
        buttonedit=(CircleImageView) findViewById(R.id.edit_cover);
        editText1= (EditText) findViewById(R.id.recipeText);
        dStep=(Button) findViewById(R.id.deleteStep);
        mButton = (Button) findViewById(R.id.addrecpieButton);
        myFab = (FloatingActionButton) findViewById(R.id.up);
        editTextName=(EditText) findViewById(R.id.editTextName);
        editTextMaterial=(EditText) findViewById(R.id.editTextMaterial);
        editTextTime=(EditText) findViewById(R.id.editTextTime);
        editTextRation=(EditText) findViewById(R.id.editTextRation);
        cover =(ImageView) findViewById(R.id.coverRecipe);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
    }
    private TextView createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(getString(R.string.step) +" "+ step);
        return textView;
    }
    private EditText createNewEditText() {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(editText1.getContext());
        editText.setLayoutParams(lparams);
        editText.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_text_bottom_border));
        editText.setPadding(16,12,16,12);

        return editText;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImageURI = data.getData();

            Picasso.with(this).load(selectedImageURI).noPlaceholder().centerCrop().fit()
                    .into((ImageView) findViewById(R.id.coverRecipe));
        }

    }

}
