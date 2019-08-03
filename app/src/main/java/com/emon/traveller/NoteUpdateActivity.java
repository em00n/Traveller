package com.emon.traveller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteUpdateActivity extends AppCompatActivity {

    EditText subjectet, detailset;
    Button updateBtn, deletBtn;
    String id, subject, details;
    DBHandeler db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);
        subjectet = findViewById(R.id.unoteSubEt);
        detailset = findViewById(R.id.unoteDetailEt);
        updateBtn = findViewById(R.id.unteupdateBTN);
        deletBtn = findViewById(R.id.unoteDeletBTN);
        db = new DBHandeler(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getApplicationContext().getSharedPreferences("language", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        id = getIntent().getStringExtra("id");
        subject = getIntent().getStringExtra("sub");
        details = getIntent().getStringExtra("det");
        subjectet.setText(subject);
        detailset.setText(details);
        //  checkLan();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                final String currentTime = sdf.format(new Date());
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
                final String currentDate = sdf1.format(new Date());

                String sub = subjectet.getText().toString();
                String det = detailset.getText().toString();
                db.updateNoteData(id, sub, det, currentDate, currentTime);
                finish();
            }
        });
        deletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteNote(Integer.valueOf(id));
                finish();
            }
        });
    }

//    public void checkLan() {
//        String lan = sharedPreferences.getString("language", "Bangla");
//
//        if (lan.equalsIgnoreCase("Bangla")) {
//
//            updateBtn.setText("আপডেট করুন");
//            deletBtn.setText("ডিলিট");
//        }
//    }


}
