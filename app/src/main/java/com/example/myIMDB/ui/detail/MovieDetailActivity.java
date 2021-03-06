package com.example.myIMDB.ui.detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myIMDB.model.Movie;
import com.example.myIMDB.model.Reviews;
import com.example.myIMDB.model.Trailers;
import com.example.myIMDB.ui.detail.adapters.IShareTrailerHandler;
import com.example.myIMDB.ui.detail.adapters.IWatchTrailerClickHandler;
import com.example.myIMDB.ui.detail.adapters.ReviewsAdapter;
import com.example.myIMDB.ui.detail.adapters.TrailersAdapter;
import com.example.myIMDB.ui.detail.async.MovieDetailsAsyncTask;
import com.example.myIMDB.ui.detail.async.MovieDetailsCallBack;
import com.example.myIMDB.ui.detail.viewmodel.DetailViewModel;
import com.example.myIMDB.ui.detail.viewmodel.DetailViewModelFactory;
import com.example.myIMDB.util.InjectorUtils;
import com.example.myIMDB.util.imovieBackdropImageView;
import com.example.myIMDB.util.imoviePosterImageView;
import com.example.myIMDB.util.threads.AppExecutors;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.myIMDB.R;

import static com.example.myIMDB.util.Constants.BACKDROP_BASE_URL;
import static com.example.myIMDB.util.Constants.KEY_MOVIE_ID;
import static com.example.myIMDB.util.Constants.KEY_MOVIE_IS_FAVOURITE;
import static com.example.myIMDB.util.Constants.KEY_MOVIE_POSTER;
import static com.example.myIMDB.util.Constants.POSTER_BASE_URL;
import static com.example.myIMDB.util.PaletteExtractorUtil.getBitmapFromUrl;
import static com.example.myIMDB.util.PaletteExtractorUtil.getDarkVibrantColor;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailsCallBack, SharedPreferences.OnSharedPreferenceChangeListener {
    private final String KEY_MOVIE_PERSISTENCE = "movie";
    @BindView(R.id.toolbar_details)
    Toolbar mToolbar;
    @BindView(R.id.iv_poster_image_details)
    imoviePosterImageView vimoviePosterImageView;
    @BindView(R.id.backdrop_image_view)
    imovieBackdropImageView mimovieBackdropImageView;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout vCollapsingToolbarLayout;
    @BindView(R.id.text_view_movie_title)
    TextView tvMovieTitle;
    @BindView(R.id.rating_bar_movie_avg)
    RatingBar rbMovieRating;
    @BindView(R.id.text_view_release_date)
    TextView tvReleaseDate;
    @BindView(R.id.text_view_overview)
    TextView tvOverview;
    @BindView(R.id.recycler_view_reviews)
    RecyclerView rvReviews;
    @BindView(R.id.recycler_view_trailers)
    RecyclerView rvTrailers;
    @BindView(R.id.tv_no_review)
    TextView tvNoReview;
    @BindView(R.id.tv_no_trailer)
    TextView tvNoTrailer;
    @BindView(R.id.image_view_favourites)
    ImageView ivFavourites;

    private int mMovieId;
    private String mMoviePoster;
    private Movie mMovie;
    private DetailViewModel detailViewModel;
    private boolean mIsFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        supportPostponeEnterTransition();
        setupToolBar();
        vimoviePosterImageView.setTransitionName("poster");
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            mMovieId = intent.getExtras().getInt(KEY_MOVIE_ID, -1);
            mMoviePoster = intent.getStringExtra(KEY_MOVIE_POSTER);
            mIsFavourite = intent.getBooleanExtra(KEY_MOVIE_IS_FAVOURITE, false);
        }
        posterImageTransition();
        DetailViewModelFactory vDetailViewModelFactory = InjectorUtils.provideDetailViewModelFactory(this, this);
        detailViewModel = ViewModelProviders.of(this, vDetailViewModelFactory).get(DetailViewModel.class);
        checkIfFavourite(mMovieId);
        if (savedInstanceState != null) {
            mMovie = Parcels.unwrap(savedInstanceState.getParcelable(KEY_MOVIE_PERSISTENCE));
            complete(mMovie);
        } else {
            if (!mIsFavourite) {
                new MovieDetailsAsyncTask(detailViewModel, this).execute(mMovieId);
            } else {
                loadMovieDetailsFromCache();
            }
        }
    }

    private void loadMovieDetailsFromCache() {
        detailViewModel.getFav(mMovieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    mMovie = movie;
                    complete(mMovie);
                }
            }
        });
    }

    private void setupToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void checkIfFavourite(int movieId) {
        detailViewModel.getFav(movieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    ivFavourites.setImageDrawable(getDrawable(R.drawable.ic_favorite_true));
                } else {
                    ivFavourites.setImageDrawable(getDrawable(R.drawable.ic_favorite_false));
                }
            }
        });
    }

    private void posterImageTransition() {
        Picasso.with(this)
                .load(POSTER_BASE_URL + mMoviePoster)
                .into(vimoviePosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null)
            outState.putParcelable(KEY_MOVIE_PERSISTENCE, Parcels.wrap(mMovie));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupReviews(List<Reviews> reviews) {
        if (reviews != null && !reviews.isEmpty()) {
            ReviewsAdapter vReviewsAdapter = new ReviewsAdapter(reviews);
            rvReviews.setHasFixedSize(true);
            rvReviews.setLayoutManager(new LinearLayoutManager(this));
            rvReviews.setAdapter(vReviewsAdapter);
        } else {
            rvReviews.setVisibility(View.GONE);
            tvNoReview.setVisibility(View.VISIBLE);
        }
    }

    private void setupTrailers(List<Trailers> trailers) {
        if (trailers != null && !trailers.isEmpty()) {
            TrailersAdapter vTrailersAdapter = new TrailersAdapter(trailers, new IWatchTrailerClickHandler() {
                @Override
                public void onClick(Trailers trailers) {
                    TrailerIntentHandler.viewTrailer(trailers, MovieDetailActivity.this);
                }
            }, new IShareTrailerHandler() {
                @Override
                public void onClick(Trailers trailers) {
                    TrailerIntentHandler.shareTrailerLink(trailers, MovieDetailActivity.this);
                }
            });
            rvTrailers.setHasFixedSize(true);
            rvTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rvTrailers.setAdapter(vTrailersAdapter);
        } else {
            rvTrailers.setVisibility(View.GONE);
            tvNoTrailer.setVisibility(View.VISIBLE);
        }
    }

    public void setStatusBarColorFromBackdrop(final String url) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap sBitmap = getBitmapFromUrl(url);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int backGroundColor = ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark);
                        if (getDarkVibrantColor(sBitmap) != null) {
                            backGroundColor = getDarkVibrantColor(sBitmap).getRgb();
                        }
                        getWindow().setStatusBarColor(backGroundColor);
                        vCollapsingToolbarLayout.setContentScrimColor(backGroundColor);
                    }
                });
            }
        });
    }

    @Override
    public void complete(final Movie movie) {
        setStatusBarColorFromBackdrop(BACKDROP_BASE_URL + movie.getBackdrop());
        Picasso.with(MovieDetailActivity.this)
                .load(BACKDROP_BASE_URL + movie.getBackdrop())
                .into(mimovieBackdropImageView);
        vCollapsingToolbarLayout.setTitleEnabled(false);
        tvMovieTitle.setText(movie.getMovieTitle());
        rbMovieRating.setRating(movie.getVoterAverage() / 2);
        tvReleaseDate.setText(movie.getMovieReleaseDate());
        tvOverview.setText(movie.getMovieOverview());
        setupTrailers(movie.getTrailers());
        setupReviews(movie.getReviews());
        mMovie = movie;
        ivFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailViewModel.favouriteMovie(movie);
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
