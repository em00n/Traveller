package com.emon.traveller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.emon.traveller.Adapter.CostAdapter;
import com.emon.traveller.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CostActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    TextView balanceTv, costTv, budjetTv;
    FloatingActionButton addcostFAButton;
    private RecyclerView recyclerView;
    private CostAdapter mAdapter;
    List<Model> list = new ArrayList<Model>();
    Model model = new Model();
    DBHandeler db;
    String budjet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbg));

        addcostFAButton = findViewById(R.id.costfloting);
        balanceTv = findViewById(R.id.balanceTV);
        costTv = findViewById(R.id.costTV);
        budjetTv = findViewById(R.id.budjetTV);
        sharedPreferences = getApplicationContext().getSharedPreferences("db", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        db = new DBHandeler(getApplicationContext());



        data();
        showData();
        addcostFAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CostActivity.this, AddCostActivity.class));
            }
        });
        budjetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                budjcetUpDialog(CostActivity.this).show();
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        data();
        showData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        data();
        showData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        data();
        showData();
    }

    void data() {
        int position = 0;
        if (!db.getData().isEmpty()) {
            list = db.getData();
            recyclerView = findViewById(R.id.costRecyclerView);
            // recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            CostAdapter costAdapter = new CostAdapter(CostActivity.this, list);
            recyclerView.setAdapter(costAdapter);
            costAdapter.notifyItemInserted(position);
            costAdapter.notifyDataSetChanged();
        }


    }

    void showData() {
        db = new DBHandeler(this);
        db.getReadableDatabase();
        //   db.addShowdata(this,in,ex);
        Cursor res = db.getShowData();

        while (res.moveToNext()) {
            budjet = res.getString(1);
            budjetTv.setText(budjet);
            String cost = res.getString(2);
            costTv.setText(cost);
            String id = res.getString(0);
            int balance = Integer.valueOf(budjet) - Integer.valueOf(cost);
            balanceTv.setText(String.valueOf(balance));
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

                deleteDialog(CostActivity.this).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void deleteAllDB() {
        db = new DBHandeler(getApplicationContext());
        db.showDatadelete();
        db.Alldelete();
        int check = 0;
        editor.putInt("check", check);
        editor.commit();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void budjetUpdate() {
        Intent intent = new Intent(getApplicationContext(), BudjetaddActivity.class);
        intent.putExtra("cb", "update");
        intent.putExtra("ub", budjet);
        startActivity(intent);
    }

    public AlertDialog.Builder deleteDialog(Context c) {

        String dialogTitle = "Delete All";
        String dialogMessage = "Do you want to delete all notes ?";
        String dialogText = "Yes";
        String dialogText2 = "No";

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

    public AlertDialog.Builder budjcetUpDialog(Context c) {

        String dialogTitle = "Update Budjet";
        String dialogMessage = "Do you want to update your budjet ?";
        String dialogText = "Yes";
        String dialogText2 = "No";

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(dialogTitle);
        builder.setMessage(dialogMessage);

        builder.setPositiveButton(dialogText, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                budjetUpdate();

            }
        });

        builder.setNegativeButton(dialogText2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder;
    }
}
