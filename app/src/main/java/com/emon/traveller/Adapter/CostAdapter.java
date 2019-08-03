package com.emon.traveller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emon.traveller.CostUpDeActivity;
import com.emon.traveller.R;
import com.emon.traveller.model.Model;

import java.util.List;

public class CostAdapter extends RecyclerView.Adapter<CostAdapter.CostRecyclerViewHolder> {
    private Context context;
    private List<Model> models;
    View.OnLongClickListener onLongClickListener;

    public CostAdapter(Context context, List<Model> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public CostRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cost_itemview, viewGroup, false);
        return new CostRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CostRecyclerViewHolder holder, int i) {

        holder.amount.setText(models.get(i).getAmount());
        holder.reason.setText(models.get(i).getReason());
        holder.date.setText(models.get(i).getDate() + " " + models.get(i).getTime());

        final int id = models.get(i).getId();
        final String amount = models.get(i).getAmount();
        final String reason = models.get(i).getReason();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CostUpDeActivity.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("amount", amount);
                intent.putExtra("reason", reason);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public static class CostRecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView amount, reason, date;

        public CostRecyclerViewHolder(View itemView) {

            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.amount);
            reason = (TextView) itemView.findViewById(R.id.reason);
            date = (TextView) itemView.findViewById(R.id.dateTv);

        }
    }
}

