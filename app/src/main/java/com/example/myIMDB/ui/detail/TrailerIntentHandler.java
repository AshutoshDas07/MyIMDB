package com.example.myIMDB.ui.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import com.example.myIMDB.model.Trailers;

import com.example.myIMDB.R;

import static com.example.myIMDB.util.Constants.YOUTUBE_TRAILER_BASE_URL;

public final class TrailerIntentHandler {

    public static void viewTrailer(Trailers trailers, Context context) {
        String trailerLink = String.format(YOUTUBE_TRAILER_BASE_URL, trailers.getKey());
        Intent videoPlayerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerLink));
        if (videoPlayerIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(videoPlayerIntent);
        }
    }

    public static void shareTrailerLink(Trailers trailers, Activity activity) {
        String trailerLink = String.format(YOUTUBE_TRAILER_BASE_URL, trailers.getKey());
        String mimeType = "text/plain";
        String title = activity.getString(R.string.label_share);
        ShareCompat
                .IntentBuilder
                .from(activity)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText("Check Out This Trailer \n" + trailerLink + " \n For More Reviews and Trailers,check out imovie at {imovie Google Play Link Here} ")
                .startChooser();
    }
}
