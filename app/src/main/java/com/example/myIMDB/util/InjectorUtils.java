package com.example.myIMDB.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.myIMDB.MoviesRepository;
import com.example.myIMDB.db.MovieDatabase;
import com.example.myIMDB.network.MovieApiServices;
import com.example.myIMDB.network.NetworkAdapter;
import com.example.myIMDB.ui.detail.viewmodel.DetailViewModelFactory;
import com.example.myIMDB.ui.main.viewmodel.MainViewModelFactory;
import com.example.myIMDB.util.threads.AppExecutors;

import com.example.myIMDB.R;

public final class InjectorUtils {

    private static MoviesRepository provideRepository(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MovieDatabase database = MovieDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        MovieApiServices vApiServices = NetworkAdapter
                .getRetrofitInstance()
                .create(MovieApiServices.class);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String language = sp.getString(context.getString(R.string.pref_language_key), "");
        sp.registerOnSharedPreferenceChangeListener(changeListener);
        return MoviesRepository.getInstance(database.movieDAO(), vApiServices, executors, language);
    }

    public static MainViewModelFactory provideMainViewModelFactory(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MoviesRepository repository = provideRepository(context.getApplicationContext(), changeListener);
        return new MainViewModelFactory(repository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context, SharedPreferences.OnSharedPreferenceChangeListener changeListener) {
        MoviesRepository repository = provideRepository(context.getApplicationContext(), changeListener);
        return new DetailViewModelFactory(repository);
    }
}
