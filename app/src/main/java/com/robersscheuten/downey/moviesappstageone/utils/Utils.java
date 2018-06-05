package com.robersscheuten.downey.moviesappstageone.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.robersscheuten.downey.moviesappstageone.models.Movie;

/**
 * class which contains methods whom are reused throughout the project
 */
public class Utils {

    /**
     * Shows a dialog box to the user
     *
     * @param errorMessage the message to be shown
     * @param titleMessage the titleMessage of the dialogBox
     */
    public static void showDialogBox(String errorMessage, String titleMessage, Context context) {
        //get builder & set builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context)
                .setMessage(errorMessage)
                .setTitle(titleMessage);
        //create alert
        AlertDialog dialog = dialogBuilder.create();
        //show alert
        dialog.show();
    }

    /**
     * Does a check if the user has an active network & checks if the network has a connection.
     *
     * @return Boolean which represents the connection state.
     */
    public static Boolean checkNetworkAvailable(Context context) {
        //get ConnectivityManager
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //getActive networkInfo
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * Retrieves the status bar height to adjust transparency.
     *
     * @return the height in pixels
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * Method which constructs the full poster path in string format.
     *
     * @return the full posterpath as string.
     */
    public static String getFullPosterURL(String posterPath, String size) {
        String basePosterURL = "http://image.tmdb.org/t/p/" + size + "/";
         return basePosterURL + posterPath;
    }

    /**
     * Interface to handle the selection of movies via a callback for SoC.
     */
    public interface onMovieClickListener {
        void onClick(Movie movie);
    }

    public static String sizeTile = "w185";
    public static String sizeBackground = "w342";



   /* public static void makeBarTransparent(Context context, View v) {
        if (v != null) {
            int paddingTop = MyScreenUtils .getStatusBarHeight(this);
            TypedValue tv = new TypedValue();
            getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, tv, true);
            paddingTop += TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            v.setPadding(0, makeTranslucent ? paddingTop : 0, 0, 0);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }*/



}
