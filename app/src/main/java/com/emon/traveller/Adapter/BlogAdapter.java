package com.emon.traveller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emon.traveller.BlogActivity;
import com.emon.traveller.BlogPostActivity;
import com.emon.traveller.R;
import com.emon.traveller.model.Blog;
import com.emon.traveller.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogRecyclerViewHolder> {
    Context context;
    List<Blog> blogs;

    public BlogAdapter(Context context, List<Blog> blogs) {
        this.context = context;
        this.blogs = blogs;
    }

    @NonNull
    @Override
    public BlogRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_itempost, parent, false);
        return new BlogRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogRecyclerViewHolder holder, final int position) {
        String bigdiscrip = blogs.get(position).getDiscrip();

        if (bigdiscrip.length() < 40) {
            holder.discripTV.setText(blogs.get(position).getDiscrip());
            holder.discripTV.setMaxLines(2);
        } else {
            String subdiscrip = bigdiscrip.substring(0, 40);
            holder.discripTV.setText(subdiscrip + "...");
        }

        holder.titleTV.setText(blogs.get(position).getTitle());
        holder.pronameTv.setText(blogs.get(position).getProname());
        holder.dateTv.setText(blogs.get(position).getDate());
        Picasso.get().load(blogs.get(position).getUrl()).into(holder.imageView);
        Picasso.get().load(blogs.get(position).getProurl()).into(holder.propicCiv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String preUid = blogs.get(position).getUid();
                String selectedkey = blogs.get(position).getSelectedkey();
                // updateDelet(position);
                String disc = blogs.get(position).getDiscrip();
                String title = blogs.get(position).getTitle();
                String url = blogs.get(position).getUrl();
                String name = blogs.get(position).getProname();
                String propicurl = blogs.get(position).getProurl();
                String date = blogs.get(position).getDate();

                Intent intent = new Intent(context, BlogPostActivity.class);
                intent.putExtra("selectedkey", selectedkey);
                intent.putExtra("disc", disc);
                intent.putExtra("title", title);
                intent.putExtra("uid", preUid);
                intent.putExtra("url", url);
                intent.putExtra("propicurl", propicurl);
                intent.putExtra("name", name);
                intent.putExtra("date", date);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public class BlogRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTV, discripTV, pronameTv, dateTv;
        public ImageView imageView;
        public CircleImageView propicCiv;
        // public   OnLongClickListener onLongClickListener;
        public View.OnClickListener onClickListener;

        //    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
//        this.onLongClickListener = onLongClickListener;
//    }
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
}
