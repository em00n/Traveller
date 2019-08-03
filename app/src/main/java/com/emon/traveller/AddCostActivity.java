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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddCostActivity extends AppCompatActivity {
    EditText reasonET, amountET;
    Button button;
    DBHandeler db;
    List<Model> list;
    Model model = new Model();
    String reason, amount;
    Context context = this;
    String id, budget, expense;
    int totalcost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbg));

        reasonET = findViewById(R.id.reasonET);
        amountET = findViewById(R.id.amountET);
        button = findViewById(R.id.addcostBTN);
        db = new DBHandeler(this);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final String currentTime = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
        final String currentDate = sdf1.format(new Date());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reason = reasonET.getText().toString();
                amount = amountET.getText().toString();
                if (!reason.isEmpty() && !amount.isEmpty()) {
                    db.adddata(context, reason, amount, currentDate, currentTime);
                    list = db.getData();

                    Cursor res = db.getShowData();
                    while (res.moveToNext()) {
                        budget = res.getString(1);
                        expense = res.getString(2);

                        id = res.getString(0);


                    }
                    totalcost = Integer.valueOf(expense) + Integer.valueOf(amount);
                    db.updateShowDate(id, budget, String.valueOf(totalcost));

                    //   startActivity(new Intent(AddCostActivity.this,CostActivity.class));
                    finish();
                }
            }
        });
    }
}
