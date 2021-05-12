package com.example.myIMDB.model.responses;

import com.example.myIMDB.model.MovieNetworkLite;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MovieListResponse {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("results")
    @Expose
    private List<MovieNetworkLite> moviesResult;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<MovieNetworkLite> getMoviesResult() {
        return moviesResult;
    }


}
