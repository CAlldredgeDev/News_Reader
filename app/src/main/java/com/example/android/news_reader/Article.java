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

/**
 * An {@link Article} object contains information related to a single earthquake.
 */
public class Article {

// Required params

    private String mTitle;
    private String mSection;
    private String mPubDate;
    private String mWebUrl;
    private String mContributor;


    /**
     * Constructs a new {@link Article} object.
     *
     * @param title   is the human readable title of the article
     * @param section is the section that the article was published in
     * @param pubDate is the date that the article was published on.
     * @param webUrl  is the url that will launch a web intert and go to the article selected.
     */

    public Article(String title, String section, String pubDate, String contributor, String webUrl) {
        mTitle = title;
        mSection = section;
        mPubDate = pubDate;
        mContributor = contributor;
        mWebUrl = webUrl;

    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmPubDate() {
        return mPubDate;
    }

    public String getmContributor() {
        return mContributor;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }
}