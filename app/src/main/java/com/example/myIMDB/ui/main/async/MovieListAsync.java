package com.example.myIMDB.ui.main.async;

import android.os.AsyncTask;

import com.example.myIMDB.model.Category;
import com.example.myIMDB.model.MovieNetworkLite;
import com.example.myIMDB.ui.main.callbacks.MovieListCallBack;
import com.example.myIMDB.ui.main.viewmodel.MainViewModel;

import java.util.List;

public final class MovieListAsync extends AsyncTask<Void, Void, List<MovieNetworkLite>> {
    private MainViewModel mMainViewModel;
    private MovieListCallBack mMovieListCallBack;
    private Category mCategory;

    public MovieListAsync(MainViewModel mainViewModel, Category category, MovieListCallBack movieListCallBack) {
        mMainViewModel = mainViewModel;
        mMovieListCallBack = movieListCallBack;
        mCategory = category;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMovieListCallBack.inProgress();
    }

    @Override
    protected List<MovieNetworkLite> doInBackground(Void... voids) {
//                TODO Load Language From Preferences
//                TODO Add Pagination on Scrolling page to page
//                TODO Sort By Category
        return mMainViewModel.getMovies(mCategory, 1);
    }

    @Override
    protected void onPostExecute(List<MovieNetworkLite> movies) {
        super.onPostExecute(movies);
        mMovieListCallBack.onFinished(movies, mCategory);
    }
}
