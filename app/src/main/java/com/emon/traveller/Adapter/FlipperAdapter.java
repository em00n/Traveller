package com.emon.traveller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emon.traveller.R;
import com.emon.traveller.model.SlideModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FlipperAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SlideModel> list;

    public FlipperAdapter(Context context, ArrayList<SlideModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SlideModel model = list.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.slide_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //  Picasso.with(mCtx).load(hero.getUrl()).into(imageView);
        Picasso.get().load(model.getUrl()).into(imageView);
        return view;
    }
}

