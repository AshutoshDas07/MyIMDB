package com.example.myIMDB.ui.detail.async;

import android.os.AsyncTask;

import com.example.myIMDB.model.Movie;
import com.example.myIMDB.model.Reviews;
import com.example.myIMDB.model.Trailers;
import com.example.myIMDB.ui.detail.viewmodel.DetailViewModel;

import java.util.List;


public final class MovieDetailsAsyncTask extends AsyncTask<Integer, Void, Movie> {

    private DetailViewModel mDetailViewModel;
    private MovieDetailsCallBack mMovieDetailsCallBack;
    private List<Trailers> trailers;
    private List<Reviews> reviews;

    public MovieDetailsAsyncTask(DetailViewModel detailViewModel, MovieDetailsCallBack movieDetailsCallBack) {
        mDetailViewModel = detailViewModel;
        mMovieDetailsCallBack = movieDetailsCallBack;
        trailers = null;
        reviews = null;
    }

    @Override
    protected Movie doInBackground(Integer... integers) {
        Movie vMovie = mDetailViewModel.getMovieDetails(integers[0]);
        reviews = mDetailViewModel.getMovieReviewsResponse(integers[0]).getReviews();
        trailers = mDetailViewModel.getMovieTrailersResponse(integers[0]).getTrailers();
        vMovie.setReviews(reviews);
        vMovie.setTrailers(trailers);
        return vMovie;
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        mMovieDetailsCallBack.complete(movie);
    }
}
