package com.emon.traveller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.emon.traveller.model.Post;
import com.emon.traveller.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AddBlogdataActivity extends AppCompatActivity {

    EditText titleET, discripET;
    Button addBTN;
    ProgressDialog pd;
    String useruid;
    String title;
    String discrip;
    String date;

    private static final int PICK_IMAGE_REQUEST = 21;
    ImageView imageView;
    Uri filepath;

    FirebaseStorage storage;
    StorageReference storageReference;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blogdata);

        titleET = findViewById(R.id.titleEDT);
        discripET = findViewById(R.id.discripEDT);
        addBTN = findViewById(R.id.addpostBTN);

        imageView = findViewById(R.id.addpostIv);
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Traveller").child("Blog");

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        //mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        useruid = mAuth.getCurrentUser().getUid();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(AddBlogdataActivity.this);
            }
        });

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();


            }
        });


        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final String currentTime = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        final String currentDate = sdf1.format(new Date());
        date = currentDate + " at " + currentTime;
    }


    //add data to database
    private void addData() {

        title = titleET.getText().toString();
        discrip = discripET.getText().toString();
        if (filepath != null) {
            pd = new ProgressDialog(AddBlogdataActivity.this);
            pd.setTitle("Please wait");
            pd.setMessage("Loading....");
            pd.show();

            StorageReference reference = storageReference.child("images/" + UUID.randomUUID().toString());
            reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    final String sdownload_url = String.valueOf(downloadUrl);



                    if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(discrip)) {
                        //  Toast.makeText(AddBlogdataActivity.this, "add", Toast.LENGTH_SHORT).show();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Traveller").child("Profile");
                        final Query data = reference.orderByChild("uid").equalTo(useruid);
                        data.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //  Post post = new ArrayList<String>();

                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    User user = userSnapshot.getValue(User.class);

                                    Post post = new Post(title, discrip, useruid, sdownload_url, user.getProurl(), user.getName(), date);
                                    databaseReference.push()  //use this method to creat unik id
                                            .setValue(post);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        // startActivity(new Intent(AddData_Activity.this, MainActivity.class));
                        clear();
                        pd.dismiss();
                        finish();


                    } else {
                        Toast.makeText(AddBlogdataActivity.this, "enter values", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddBlogdataActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });


        } else
            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();


        // adapter.notifyDataSetChanged();
    }

    public void clear() {
        titleET.setText("");
        discripET.setText("");
        imageView.setImageURI(null);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                filepath = resultUri;
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
