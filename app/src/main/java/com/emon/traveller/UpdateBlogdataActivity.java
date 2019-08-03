package com.emon.traveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emon.traveller.model.Post;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateBlogdataActivity extends AppCompatActivity {

    EditText discripET, titleET;
    TextView nameTv, dateTv;
    Button updateBtn, deletBtn;
    ImageView imageView;
    CircleImageView circleImageView;
    ProgressDialog pd;

    private static final int PICK_IMAGE_REQUEST = 2;
    Uri filepath;

    FirebaseStorage storage;
    StorageReference storageReference;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    String muid;
    String preUid;
    String selectedkey, discrip, title, image, name, propic, date;
    String crdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_blogdata);
        mAuth = FirebaseAuth.getInstance();

        discripET = findViewById(R.id.discripET);
        titleET = findViewById(R.id.titleET);
        imageView = findViewById(R.id.picupde);
        nameTv = findViewById(R.id.updepronameTV);
        dateTv = findViewById(R.id.updedateTV);
        circleImageView = findViewById(R.id.updepropicCIv);
        updateBtn = findViewById(R.id.updateBTN);
        deletBtn = findViewById(R.id.deletBTN);
        selectedkey = getIntent().getStringExtra("selectedkey");
        discrip = getIntent().getStringExtra("disc");
        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        propic = getIntent().getStringExtra("propicurl");
        preUid = getIntent().getStringExtra("uid");
        date = getIntent().getStringExtra("date");


        discripET.setText(discrip);
        titleET.setText(title);
        nameTv.setText(name);
        dateTv.setText(date);
        Picasso.get().load(image).into(imageView);
        Picasso.get().load(propic).into(circleImageView);

        //date
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final String currentTime = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        final String currentDate = sdf1.format(new Date());
        crdate = currentDate + " at " + currentTime;

        muid = mAuth.getCurrentUser().getUid();
        //firebase
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Traveller").child("Blog");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (mAuth.getCurrentUser().getUid().equals(preUid)) {

            updateBtn.setVisibility(View.VISIBLE);
            deletBtn.setVisibility(View.VISIBLE);
            imageView.setClickable(true);
        } else {
            updateBtn.setVisibility(View.INVISIBLE);
            deletBtn.setVisibility(View.INVISIBLE);
            imageView.setClickable(false);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().start(UpdateBlogdataActivity.this);

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filepath != null) {
                    pd = new ProgressDialog(UpdateBlogdataActivity.this);
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


                            databaseReference
                                    .child(selectedkey)
                                    .setValue(new Post(titleET.getText().toString(), discripET.getText().toString(), muid, sdownload_url, propic, name, crdate))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            clear();
                                            finish();
                                            pd.dismiss();
                                            Toast.makeText(UpdateBlogdataActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateBlogdataActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateBlogdataActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    pd = new ProgressDialog(UpdateBlogdataActivity.this);
                    pd.setTitle("Please wait");
                    pd.setMessage("Loading....");
                    pd.show();

                    databaseReference
                            .child(selectedkey)
                            .setValue(new Post(titleET.getText().toString(), discripET.getText().toString(), muid, image, propic, name, crdate))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    clear();

                                    finish();
                                    pd.dismiss();
                                    Toast.makeText(UpdateBlogdataActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateBlogdataActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

        deletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference
                        .child(selectedkey)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                clear();
                                Intent intent = new Intent(UpdateBlogdataActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(UpdateBlogdataActivity.this, "delete", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateBlogdataActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    public void clear() {
        discripET.setText("");
        titleET.setText("");
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
