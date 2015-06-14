package com.quinny898.library.persistentsearch;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

public class SearchResult {

    public String title;
    public Drawable icon;
    public int drawableResId;

    /**
     * Create a search result with text and an icon
     * @param title
     * @param icon
     */
    public SearchResult(String title, Drawable icon) {
       this.title = title;
       this.icon = icon;
    }

    public SearchResult(String title, @DrawableRes int drawableResId) {
        this.title = title;
        this.drawableResId = drawableResId;
    }
    
    /**
     * Return the title of the result
     */
    @Override
    public String toString() {
        return title;
    }
    
}