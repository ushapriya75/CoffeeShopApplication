package com.org.coffeeshop;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.coffeeshop.modal.nearbyResturants.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {

    private Context mContext;

    private List<Restaurant> restaurantList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mName, mRating, mVote, mRatingStatus;
        ImageView mShopImage;

        public MyViewHolder(View view) {
            super(view);
            mShopImage = view.findViewById(R.id.shop_list_image);
            mName = view.findViewById(R.id.shop_list_name);
            mRating = view.findViewById(R.id.shop_list_rating);
            mRatingStatus = view.findViewById(R.id.shop_list_rating_status);
            mVote = view.findViewById(R.id.shop_list_votes);
        }
    }


    public ShopAdapter(Context context, List<Restaurant> restaurants) {
        mContext = context;
        this.restaurantList = restaurants;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (restaurantList.get(position).getThumb() != null && !restaurantList.get(position).getThumb().equals("")) {
            Picasso.with(mContext).load(restaurantList.get(position).getThumb()).
                    fit()
                    .into(holder.mShopImage);
        }

        holder.mName.setText(restaurantList.get(position).getName());
        holder.mRating.setText(mContext.getString(R.string.rating_text, restaurantList.get(position).getUserRating().getAggregateRating()));
        holder.mRatingStatus.setText(restaurantList.get(position).getUserRating().getRatingText());
        holder.mRatingStatus.setBackgroundColor(Color.parseColor("#"+restaurantList.get(position).getUserRating().getRatingColor()));
        holder.mVote.setText(mContext.getString(R.string.votes_text,restaurantList.get(position).getUserRating().getVotes()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra("name", restaurantList.get(position).getName());
                detailIntent.putExtra("address", restaurantList.get(position).getLocation().getAddress());
                detailIntent.putExtra("rating", restaurantList.get(position).getUserRating().toString());
                detailIntent.putExtra("link", restaurantList.get(position).getUrl());
                detailIntent.putExtra("featuredImage", restaurantList.get(position).getFeaturedImage());
                detailIntent.putExtra("aggregateRating", restaurantList.get(position).getUserRating().getAggregateRating());
                mContext.startActivity(detailIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}