package com.emon.traveller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BudjetaddActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    DBHandeler db;
    String idshow, budjet, expence = "0";
    int checkdb;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budjetadd);
        editText = findViewById(R.id.budjetEt);
        button = findViewById(R.id.addBudjet);
        sharedPreferences = getApplicationContext().getSharedPreferences("db", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        String cb = getIntent().getStringExtra("cb");
        final String ub = getIntent().getStringExtra("ub");
        if (cb.equals("add")) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    budjet = editText.getText().toString().trim();
                    if (!budjet.isEmpty()) {
                        db = new DBHandeler(getApplicationContext());
                        db.getReadableDatabase();
                        db.getWritableDatabase();

                        db.addShowdata(getApplicationContext(), budjet, expence);
                        checkdb = 1;
                        editor.putInt("check", checkdb);
                        editor.commit();
                        startActivity(new Intent(BudjetaddActivity.this, CostActivity.class));
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "please set budjet", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (cb.equals("update")) {
            editText.setText(ub);
            button.setText("Update");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!editText.getText().toString().isEmpty()) {
                        db = new DBHandeler(getApplicationContext());
                        db.getReadableDatabase();
                        db.getWritableDatabase();
                        Cursor res = db.getShowData();
                        while (res.moveToNext()) {
                            budjet = res.getString(1);
                            expence = res.getString(2);

                            idshow = res.getString(0);

                        }
                        if (Integer.valueOf(ub) > Integer.valueOf(editText.getText().toString())) {
                            int totalBudjet = Integer.valueOf(ub) - Integer.valueOf(editText.getText().toString());
                            int budj = Integer.valueOf(budjet) - Integer.valueOf(totalBudjet);
                            db.updateShowDate(idshow, String.valueOf(budj), expence);

                            finish();
                        } else if (Integer.valueOf(ub) < Integer.valueOf(editText.getText().toString())) {
                            int totalbudjet = Integer.valueOf(editText.getText().toString()) - Integer.valueOf(ub);
                            int budg = Integer.valueOf(budjet) + Integer.valueOf(totalbudjet);
                            db.updateShowDate(idshow, String.valueOf(budg), expence);
                            finish();
                        } else {

                            db.updateShowDate(idshow, ub, expence);
                            finish();
                        }


                    }
                }
            });
        }
    }
}
