package com.emon.traveller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.emon.traveller.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    EditText subET, detailET;
    Button button;
    DBHandeler db;
    List<Note> notes;
    Note note = new Note();
    String sub, detail;
    Context context = this;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbg));
        subET = findViewById(R.id.noteSubET);
        detailET = findViewById(R.id.noteDetailET);
        button = findViewById(R.id.addNoteBTN);
        db = new DBHandeler(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getApplicationContext().getSharedPreferences("language", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //  checkLan();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                final String currentTime = sdf.format(new Date());
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
                final String currentDate = sdf1.format(new Date());
                sub = subET.getText().toString();
                detail = detailET.getText().toString();
                if (!sub.isEmpty() && !detail.isEmpty()) {
                    db.addNoteData(context, sub, detail, currentDate, currentTime);
                    // notes=db.getNoteData();

                    finish();
                }
            }
        });
    }

//    public void checkLan() {
//        String lan = sharedPreferences.getString("language", "Bangla");
//
//        if (lan.equalsIgnoreCase("Bangla")) {
//            subET.setHint("বিষয় বস্তু");
//            detailET.setHint("বিস্তারিত");
//            button.setText("নোট করুন");
//        }
//    }

}
