package com.github.nikhilbhutani.popularmovies1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.nikhilbhutani.popularmovies1.R;
import com.github.nikhilbhutani.popularmovies1.model.Movie;

import java.util.List;

/**
 * Created by Nikhil Bhutani on 8/3/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


     List<Movie> mList;
     Context mcontext;

    public RecyclerViewAdapter(Context context, List<Movie> movieList){
        this.mcontext = context;
        this.mList = movieList;
       System.out.println(movieList);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

     Glide.with(mcontext).load(mList.get(position).getPosterPath()).crossFade().placeholder(R.drawable.place_holder).into(holder.imageView);
        holder.movieTitle.setText(mList.get(position).getTitle());
        holder.releaseDate.setText(mList.get(position).getReleaseDate());

        System.out.println(mList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView  movieTitle;
        TextView releaseDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster_image);
            movieTitle = (TextView) itemView.findViewById(R.id.movie_name);
            releaseDate = (TextView) itemView.findViewById(R.id.releaseDateMainActivit);
        }
    }

}
