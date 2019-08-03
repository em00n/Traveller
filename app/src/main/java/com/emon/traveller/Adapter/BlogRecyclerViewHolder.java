package com.emon.traveller.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.emon.traveller.OnLongClickListener;
import com.emon.traveller.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView titleTV, discripTV, pronameTv, dateTv;
    public ImageView imageView;
    public CircleImageView propicCiv;
    // public   OnLongClickListener onLongClickListener;
    public View.OnClickListener onClickListener;


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public BlogRecyclerViewHolder(View itemView) {
        super(itemView);
        titleTV = (TextView) itemView.findViewById(R.id.titleTV);
        discripTV = (TextView) itemView.findViewById(R.id.discripTV);
        dateTv = (TextView) itemView.findViewById(R.id.pdateTV);
        imageView = itemView.findViewById(R.id.picsow);
        pronameTv = itemView.findViewById(R.id.pronameTV);
        propicCiv = itemView.findViewById(R.id.propicCIv);
        // itemView.setOnLongClickListener(this);
    }


    @Override
    public void onClick(View v) {
        onClickListener.onClick(v);
    }


}

