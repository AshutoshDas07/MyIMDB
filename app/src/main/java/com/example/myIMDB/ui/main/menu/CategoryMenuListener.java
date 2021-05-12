package com.example.myIMDB.ui.main.menu;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.example.myIMDB.model.Category;
import com.example.myIMDB.ui.main.async.MovieListAsync;
import com.example.myIMDB.ui.main.callbacks.FavouriteMoviesCallback;
import com.example.myIMDB.ui.main.callbacks.MovieListCallBack;
import com.example.myIMDB.ui.main.viewmodel.MainViewModel;

import com.example.myIMDB.R;

public class CategoryMenuListener implements PopupMenu.OnMenuItemClickListener {

    private MovieListCallBack mMovieListCallBackContext;
    private FavouriteMoviesCallback mFavouriteMoviesCallbackContext;
    private MainViewModel mMainViewModel;

    public CategoryMenuListener(MainViewModel mainViewModel, MovieListCallBack movieListCallBackContext, FavouriteMoviesCallback favouriteMoviesCallbackContext) {
        this.mMovieListCallBackContext = movieListCallBackContext;
        this.mMainViewModel = mainViewModel;
        this.mFavouriteMoviesCallbackContext = favouriteMoviesCallbackContext;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_top_rated:
                new MovieListAsync(mMainViewModel, Category.TOP_RATED, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_upcoming:
                new MovieListAsync(mMainViewModel, Category.UPCOMING, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_most_popular:
                new MovieListAsync(mMainViewModel, Category.POPULAR, mMovieListCallBackContext).execute();
                return true;
            case R.id.action_sort_favourites:
                mFavouriteMoviesCallbackContext.loadFavouriteMovies();
                return true;
        }
        return false;
    }
}
