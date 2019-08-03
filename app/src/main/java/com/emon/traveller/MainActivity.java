package com.emon.traveller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.emon.traveller.Adapter.BlogAdapter;
import com.emon.traveller.Adapter.FlipperAdapter;
import com.emon.traveller.model.Blog;
import com.emon.traveller.model.Post;
import com.emon.traveller.model.SlideModel;
import com.emon.traveller.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.network.connection.HttpURLConnectionFactoryImpl;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    CardView costCv, blogCv, weatherCv, noteCv;
    AdapterViewFlipper aviewFlipper;
    String name, prourl, uid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<SlideModel> urls;
    int position = 0;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        costCv = findViewById(R.id.costCvBtn);
        blogCv = findViewById(R.id.blogCvBtn);
        weatherCv = findViewById(R.id.weatherCvBtn);
        noteCv = findViewById(R.id.noteCvBtn);
        aviewFlipper = findViewById(R.id.avF);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Traveller").child("Profile");


        name = getIntent().getStringExtra("name");
        prourl = getIntent().getStringExtra("profilepic");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(uid)) {
            addData();
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Traveller").child("Blog");
        final Query data = dr.orderByChild("url");
        dr.keepSynced(true);

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                urls = new ArrayList<SlideModel>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Post post = ds.getValue(Post.class);
                    String img = post.getUrl();
                    urls.add(new SlideModel(img));

                }
                //   flipperImage(posts);
                // Toast.makeText(MainActivity.this, String.valueOf(posts.size()), Toast.LENGTH_SHORT).show();
//                    for (String image:posts) {
//
//                            //  Picasso.get().load(uri).into(viewFlipper);
//                        }

                //creating adapter object
                FlipperAdapter adapter = new FlipperAdapter(getApplicationContext(), (ArrayList<SlideModel>) urls);

                //adding it to adapterview flipper
                aviewFlipper.setAdapter(adapter);
                aviewFlipper.setFlipInterval(4000);
                aviewFlipper.startFlipping();
                adapter.notifyDataSetInvalidated();

                //      aviewFlipper.setInAnimation(getApplicationContext(),android.R.anim.slide_in_left);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sharedPreferences = getApplicationContext().getSharedPreferences("db", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        costCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = sharedPreferences.getInt("check", 0);
                if (check > 0) {
                    startActivity(new Intent(MainActivity.this, CostActivity.class));
                } else {
                    Intent intent = new Intent(MainActivity.this, BudjetaddActivity.class);
                    intent.putExtra("cb", "add");
                    startActivity(intent);
                }
            }
        });

        blogCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BlogActivity.class));
            }
        });

        weatherCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));
            }
        });

        noteCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });
    }


    private void addData() {
        User user = new User(name, prourl, uid);
        databaseReference.push()
                .setValue(user);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this, MapsActivity.class));

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
