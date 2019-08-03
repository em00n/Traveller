package com.emon.traveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.emon.traveller.Adapter.BlogAdapter;
import com.emon.traveller.Adapter.BlogRecyclerViewHolder;
import com.emon.traveller.model.Blog;
import com.emon.traveller.model.Post;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogActivity extends AppCompatActivity {

    Button addBTN, updateBTN;
    RecyclerView recyclerView;

    //firebase
    int position = 0;
    ArrayList<Blog> blogs;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
//    FirebaseRecyclerOptions<Post> option;
//    FirebaseRecyclerAdapter<Post, BlogRecyclerViewHolder> adapter;


    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;
    private FirebaseAuth mAuth;

    String muid;
    String preUid;

    Post selectedpost;
    String selectedkey;

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbg));

        mAuth = FirebaseAuth.getInstance();
        //recyclerview
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        muid = mAuth.getCurrentUser().getUid();
        //firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Traveller").child("Blog");
        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                blogs = new ArrayList<Blog>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Blog blog = ds.getValue(Blog.class);
                    blog.setSelectedkey(ds.getKey());
                    blogs.add(position, blog);
                    BlogAdapter adapter = new BlogAdapter(BlogActivity.this, blogs);
                    // recyclerview.setLayoutManager(new Line);
                    recyclerView.setAdapter(adapter);
                    recyclerView.smoothScrollToPosition(position);
                    adapter.notifyItemInserted(position);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onStart() {
        // showData();
        super.onStart();
    }

    public void addbutton(View view) {
        Intent intent = new Intent(BlogActivity.this, AddBlogdataActivity.class);
        startActivity(intent);
    }


}
