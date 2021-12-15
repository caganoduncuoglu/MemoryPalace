package com.atm.memoryPalace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<PlaceViewHolder> {

    private Context context;
    private int[] images;
    private String[] placeNames;


    MyAdapter(Context context, int[] images, String[] placeNames) {
        this.context = context;
        this.images = images;
        this.placeNames = placeNames;

    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_row, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlaceViewHolder holder, int position) {
        holder.placeName.setText(placeNames[position]);
        holder.place.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }
}

class PlaceViewHolder extends RecyclerView.ViewHolder {

    ImageView place;
    TextView placeName;
    Button share;
    Button visit;

    PlaceViewHolder(View itemView) {
        super(itemView);

        place = itemView.findViewById(R.id.ivPlace);
        placeName = itemView.findViewById(R.id.tvPlaceName);
        share = itemView.findViewById(R.id.btnShare);
        visit = itemView.findViewById(R.id.btnVisit);
    }
}