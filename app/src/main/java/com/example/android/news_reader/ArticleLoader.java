package com.example.android.news_reader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG, "Loader is starting to load, interface cleared");
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            Log.v(LOG_TAG, "Loader Loaded in Background is null");
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        Log.v(LOG_TAG, "Loader Loaded in Background is not null");
        return articles;
    }
}