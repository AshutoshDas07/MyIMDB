package com.example.myIMDB.ui.main.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.myIMDB.MoviesRepository;
import com.example.myIMDB.model.Category;
import com.example.myIMDB.model.Movie;
import com.example.myIMDB.model.MovieNetworkLite;
import com.example.myIMDB.model.responses.MovieListResponse;

import java.util.List;

/**
 * Created By blackcoder
 * On 30/04/19
 **/
public final class MainViewModel extends ViewModel {
    private MoviesRepository mMoviesRepository;

    MainViewModel(MoviesRepository moviesRepository) {
        mMoviesRepository = moviesRepository;
    }

    public List<MovieNetworkLite> getMovies(Category category, int page) {
        MovieListResponse vMovieListResponse = mMoviesRepository
                .getMovies(category, page);
        return page <= vMovieListResponse.getTotalPages() ? vMovieListResponse.getMoviesResult() : null;
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return mMoviesRepository.getFavouriteMovies();
    }
}
