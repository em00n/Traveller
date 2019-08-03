package com.emon.traveller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emon.traveller.NoteUpdateActivity;
import com.emon.traveller.R;
import com.emon.traveller.model.Note;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteRecyclerViewHolder> {
    private Context context;
    private List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view, parent, false);
        return new NoteRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteRecyclerViewHolder holder, final int position) {

        holder.subTv.setText(notes.get(position).getSubject());
        if (notes.get(position).getDetails().length() > 60) {
            String detail = notes.get(position).getDetails();
            String subdet = detail.substring(0, 50);
            holder.detailTv.setText(subdet + "...");
        } else
            holder.detailTv.setText(notes.get(position).getDetails());

        holder.timeTv.setText(notes.get(position).getDate() + " at " + notes.get(position).getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NoteUpdateActivity.class);
                int id = notes.get(position).getNoteid();
                String sub = notes.get(position).getSubject();
                String det = notes.get(position).getDetails();
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("sub", sub);
                intent.putExtra("det", det);
                context.startActivity(intent);
                // ((Activity) context).finish();

            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView subTv, detailTv, timeTv;

        public NoteRecyclerViewHolder(View itemView) {
            super(itemView);
            subTv = itemView.findViewById(R.id.noteSubTv);
            detailTv = itemView.findViewById(R.id.noteDetailTv);
            timeTv = itemView.findViewById(R.id.noteTimeTv);
        }
    }
}
