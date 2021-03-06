package com.github.nikhilbhutani.popularmovies1.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.nikhilbhutani.popularmovies1.R;
import com.github.nikhilbhutani.popularmovies1.model.Movie;
import com.github.nikhilbhutani.popularmovies1.ui.activities.DetailActivity;
import com.github.nikhilbhutani.popularmovies1.ui.fragments.MainActivityFragment;
import com.github.nikhilbhutani.popularmovies1.utils.ItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Nikhil Bhutani on 8/3/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    List<Movie> mList;
    Context mcontext;
    View view;

    public RecyclerViewAdapter(Context context, List<Movie> movieList) {
        this.mcontext = context;
        this.mList = movieList;
        System.out.println(movieList);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Glide.with(mcontext).load(mList.get(position).getPosterPath()).crossFade().placeholder(R.drawable.place_holder).into(holder.imageView);
        holder.movieTitle.setText(mList.get(position).getTitle());
        holder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int Position) {
                Movie moviesDetais = mList.get(position);
                Intent intent = new Intent(mcontext, DetailActivity.class);
                intent.putExtra(MainActivityFragment.DETAILS, moviesDetais);
                mcontext.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.poster_image)
        ImageView imageView;

        @BindView(R.id.movie_name)
        TextView movieTitle;
        ItemClickListener itemClickListener;

        public MyViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition());
        }
    }

}
