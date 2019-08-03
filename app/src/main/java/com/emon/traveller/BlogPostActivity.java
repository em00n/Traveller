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

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogPostActivity extends AppCompatActivity {

    TextView discripTv, titleTv, pronameTv, dateTv;
    Button updatepostBtn, deletpostBtn;
    ImageView imageView;
    CircleImageView circleImageView;
    ProgressDialog pd;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    String muid;
    String preUid;
    String selectedkey, discrip, title, image, name, propic, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbg));
        mAuth = FirebaseAuth.getInstance();

        discripTv = findViewById(R.id.postdiscripTv);
        titleTv = findViewById(R.id.posttitleTv);
        dateTv = findViewById(R.id.postdateTV);
        imageView = findViewById(R.id.postpic);
        circleImageView = findViewById(R.id.postpropicCIv);
        pronameTv = findViewById(R.id.postpronameTV);
        updatepostBtn = findViewById(R.id.postupdateBTN);
        deletpostBtn = findViewById(R.id.postdeletBTN);
        selectedkey = getIntent().getStringExtra("selectedkey");
        discrip = getIntent().getStringExtra("disc");
        title = getIntent().getStringExtra("title");
        image = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        propic = getIntent().getStringExtra("propicurl");
        preUid = getIntent().getStringExtra("uid");
        date = getIntent().getStringExtra("date");


        pronameTv.setText(name);
        titleTv.setText(title);
        discripTv.setText(discrip);
        dateTv.setText(date);
        Picasso.get().load(image).into(imageView);
        Picasso.get().load(propic).into(circleImageView);

        muid = mAuth.getCurrentUser().getUid();

        if (mAuth.getCurrentUser().getUid().equals(preUid)) {

            updatepostBtn.setVisibility(View.VISIBLE);
            deletpostBtn.setVisibility(View.VISIBLE);
        } else {
            updatepostBtn.setVisibility(View.INVISIBLE);
            deletpostBtn.setVisibility(View.INVISIBLE);
        }

        updatepostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlogPostActivity.this, UpdateBlogdataActivity.class);
                intent.putExtra("selectedkey", selectedkey);
                intent.putExtra("disc", discrip);
                intent.putExtra("title", title);
                intent.putExtra("uid", preUid);
                intent.putExtra("url", image);
                intent.putExtra("propicurl", propic);
                intent.putExtra("name", name);
                intent.putExtra("date", date);
                startActivity(intent);
                finish();
            }
        });

        deletpostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference
                        .child(selectedkey)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                clear();
                                Intent intent = new Intent(BlogPostActivity.this, BlogActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(BlogPostActivity.this, "delete", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BlogPostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }

    public void clear() {

    }

}
