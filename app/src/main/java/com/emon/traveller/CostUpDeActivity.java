package com.emon.traveller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.emon.traveller.model.Model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CostUpDeActivity extends AppCompatActivity {
    EditText resionEt, amountEt;
    Button update, delete;
    DBHandeler db;
    List<Model> list = new ArrayList<Model>();
    Model model = new Model();
    String res, amou;
    String id, amount, reason;
    Context context = this;
    String idshow, budget, expense;
    int cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_up_de);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbg));

        resionEt = findViewById(R.id.resonupET);
        amountEt = findViewById(R.id.amountupET);
        update = findViewById(R.id.updateCostBTN);
        delete = findViewById(R.id.deleteCostBTN);
        db = new DBHandeler(this);
        id = getIntent().getStringExtra("id");
        amount = getIntent().getStringExtra("amount");
        reason = getIntent().getStringExtra("reason");

        resionEt.setText(reason);
        amountEt.setText(amount);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final String currentTime = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        final String currentDate = sdf1.format(new Date());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res = resionEt.getText().toString();
                amou = amountEt.getText().toString();
                if (!res.isEmpty() && !amou.isEmpty()) {
                    db.updateData(id, res, amou, currentDate, currentTime);
                    list = db.getData();

                    Cursor res = db.getShowData();
                    while (res.moveToNext()) {
                        budget = res.getString(1);
                        expense = res.getString(2);

                        idshow = res.getString(0);

                    }
                    if (Integer.valueOf(amount) > Integer.valueOf(amou)) {
                        int totalCost = Integer.valueOf(amount) - Integer.valueOf(amou);
                        cost = Integer.valueOf(expense) - Integer.valueOf(totalCost);
                        db.updateShowDate(idshow, budget, String.valueOf(cost));

                        finish();
                    } else if (Integer.valueOf(amount) < Integer.valueOf(amou)) {
                        int totalCost = Integer.valueOf(amou) - Integer.valueOf(amount);
                        cost = Integer.valueOf(expense) + Integer.valueOf(totalCost);
                        db.updateShowDate(idshow, budget, String.valueOf(cost));
                        finish();
                    } else {

                        db.updateShowDate(idshow, budget, amount);
                        finish();
                    }

                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amou = amountEt.getText().toString().trim();
                Cursor res = db.getShowData();
                while (res.moveToNext()) {
                    budget = res.getString(1);
                    expense = res.getString(2);

                    idshow = res.getString(0);

                }

                // int totalCost=Integer.valueOf(amount)-Integer.valueOf(amou);
                cost = Integer.valueOf(expense) - Integer.valueOf(amount);
                db.updateShowDate(idshow, budget, String.valueOf(cost));
                db.delete(Integer.parseInt(id));
                list = db.getData();
                startActivity(new Intent(CostUpDeActivity.this, CostActivity.class));
                finish();
            }
        });
    }
}
