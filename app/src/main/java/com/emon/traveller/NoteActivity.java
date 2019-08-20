package com.emon.traveller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emon.traveller.Adapter.NoteAdapter;
import com.emon.traveller.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NoteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton addNote;
    private NoteAdapter nAdapter;
    List<Note> notes = new ArrayList<Note>();
    Note note = new Note();
    DBHandeler db;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String dialogTitle, dialogMessage, dialogText, dialogText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        addNote = findViewById(R.id.notefloting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbg));

        sharedPreferences = getApplicationContext().getSharedPreferences("language", MODE_PRIVATE);
        db = new DBHandeler(this);
        data();

        //  checkLan();
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteActivity.this, AddNoteActivity.class));
            }
        });
    }


    @Override
    protected void onRestart() {
        data();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        data();
        super.onResume();
    }

    @Override
    protected void onStart() {
        data();
        super.onStart();
    }

    void data() {

        if (!db.getNoteData().isEmpty()) {
            int position = 0;
            notes = db.getNoteData();
            recyclerView = findViewById(R.id.noterecycler_view);
            // recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            NoteAdapter myAdapter = new NoteAdapter(NoteActivity.this, notes);
            recyclerView.setAdapter(myAdapter);
            myAdapter.notifyItemInserted(position);
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dbdeletmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:

                deleteDialog(NoteActivity.this).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private void deleteAllDB() {
        db = new DBHandeler(getApplicationContext());
        db.allNoteDatadelete();
        finish();
    }

    public AlertDialog.Builder deleteDialog(Context c) {

        dialogTitle = "Delete All";
        dialogMessage = "Do you want to delete all notes ?";
        dialogText = "Yes";
        dialogText2 = "No";

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(dialogText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteAllDB();

            }
        });

        builder.setNegativeButton(dialogText2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    //    public void checkLan() {
//        String lan = sharedPreferences.getString("language", "Bangla");
//
//        if (lan.equalsIgnoreCase("Bangla")) {
//
//            dialogTitle="সব ডিলিট করুন";
//            dialogMessage="আপনি সব ডিলিট করতে চান ?";
//            dialogText="হ্যাঁ";
//            dialogText2="না";
//
//        } else {
//
//            dialogTitle="Delete All";
//            dialogMessage="Do you want to delete all notes ?";
//            dialogText="Yes";
//            dialogText2="No";
//        }
//    }


}
