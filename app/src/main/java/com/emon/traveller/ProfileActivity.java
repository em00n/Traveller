package com.emon.traveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.emon.traveller.Adapter.BlogAdapter;
import com.emon.traveller.model.Blog;
import com.emon.traveller.model.Post;
import com.emon.traveller.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView myRecyclerView;
    CircleImageView profilrPic;
    TextView profileName;
    DatabaseReference reference;
    String myuid;
    String proUrl, name;
    int position = 0;
    ArrayList<Blog> blogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        reference = FirebaseDatabase.getInstance().getReference("Traveller").child("Profile");
        myRecyclerView = findViewById(R.id.myPost_recyclerView);

        profilrPic = findViewById(R.id.profilePicCiv);
        profileName = findViewById(R.id.profileNameTV);
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        proInfo();
        myPost();

    }

    private void myPost() {
        reference = FirebaseDatabase.getInstance().getReference("Traveller").child("Blog");
        reference.keepSynced(true);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final Query data = reference.orderByChild("uid").equalTo(myuid);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blogs = new ArrayList<Blog>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Blog blog = ds.getValue(Blog.class);
                    blog.setSelectedkey(ds.getKey());
                    blogs.add(position, blog);
                    BlogAdapter adapter = new BlogAdapter(ProfileActivity.this, blogs);
                    // recyclerview.setLayoutManager(new Line);
                    myRecyclerView.setAdapter(adapter);
                    myRecyclerView.smoothScrollToPosition(position);
                    adapter.notifyItemInserted(position);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void proInfo() {

        final Query data = reference.orderByChild("uid").equalTo(myuid);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  Post post = new ArrayList<String>();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                    proUrl = user.getProurl();
                    name = user.getName();
                    Picasso.get().load(proUrl).into(profilrPic);
                    profileName.setText(name);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

