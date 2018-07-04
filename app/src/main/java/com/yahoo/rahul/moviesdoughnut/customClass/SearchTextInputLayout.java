package com.yahoo.rahul.moviesdoughnut.customClass;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

public class SearchTextInputLayout extends TextInputLayout {
    private boolean hidden_f = false;

    public SearchTextInputLayout(Context context) {
        super(context);
    }

    public SearchTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isForceHidden() {
        return hidden_f;
    }

    public void forceHide() {
        hidden_f = true;
        this.setVisibility(GONE);
    }

    public void forceVisible() {
        hidden_f = false;
        this.setVisibility(VISIBLE);
    }

}
