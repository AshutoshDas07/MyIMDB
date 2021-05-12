package com.example.myIMDB.model.responses;

import com.example.myIMDB.model.Trailers;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerResponse {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("results")
    @Expose
    private List<Trailers> moviesResult;

    public int getId() {
        return id;
    }

    public List<Trailers> getTrailers() {
        return moviesResult;
    }
}
