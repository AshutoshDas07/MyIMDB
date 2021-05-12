package com.example.myIMDB.model.responses;

import com.example.myIMDB.model.Reviews;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewResponse {

    @SerializedName("page")
    @Expose
    private int page;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("results")
    @Expose
    private List<Reviews> mReviews;


    public int getPage() {
        return page;
    }

    public List<Reviews> getReviews() {
        return mReviews;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
