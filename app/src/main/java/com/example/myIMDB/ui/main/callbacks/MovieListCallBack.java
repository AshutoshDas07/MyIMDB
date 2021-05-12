package com.example.myIMDB.ui.main.callbacks;

import com.example.myIMDB.model.Category;
import com.example.myIMDB.model.MovieNetworkLite;

import java.util.List;


public interface MovieListCallBack {

    void inProgress();

    void onFinished(List<MovieNetworkLite> movies, Category category);
}
