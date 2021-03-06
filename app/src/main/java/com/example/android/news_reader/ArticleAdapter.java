/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.news_reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An {@link ArticleAdapter} knows how to create a list item layout for each article
 * in the data source (a list of {@link Article} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    // Use this as a separator so that we may later break the date from the response into two parts
    // and return only the date to our TextView.
    private static final String LOCATION_SEPARATOR = "T";

    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context  of the app
     * @param articles is the list of articles, which is the data source of the adapter
     */
    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }


    // Returns a list item view that displays information about the article at the given position
    // in the list of articles.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;

        // Find the article at the given position in the list of articles
        Article currentArticle = getItem(position);
        ArticleViewHolder vh;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item, parent, false);
            // Use the viewHolder design to reduce the number of findViewById calls.
            vh = new ArticleViewHolder(listItemView);
            vh.title.setText(currentArticle.getmTitle());
            vh.section.setText(currentArticle.getmSection());
            vh.contributor.setText(currentArticle.getmContributor());
            // Formatting to split the date and time into different parts and return only the date.
            String rawDate = currentArticle.getmPubDate();
            String dateParts[] = rawDate.split(LOCATION_SEPARATOR);
            vh.date.setText(dateParts[0]);

            listItemView.setTag(vh);
        } else {
            vh = (ArticleViewHolder) listItemView.getTag();
        }

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    // View holder class definition and constructor
    class ArticleViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.section)
        TextView section;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.contributor)
        TextView contributor;

        ArticleViewHolder(View listItemView) {
            ButterKnife.bind(this, listItemView);
        }
    }
}