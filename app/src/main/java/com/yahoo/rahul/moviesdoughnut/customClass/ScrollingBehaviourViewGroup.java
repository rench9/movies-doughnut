package com.yahoo.rahul.moviesdoughnut.customClass;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ScrollingBehaviourViewGroup extends CoordinatorLayout.Behavior<ViewGroup> {
    public ScrollingBehaviourViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency) {
        if (child instanceof SearchTextInputLayout) {
            SearchTextInputLayout searchTextInputLayout = (SearchTextInputLayout) child;
            if (searchTextInputLayout.isForceHidden())
                return super.onDependentViewChanged(parent, child, dependency);
        }

        float ratio = (((float) dependency.getBottom() / dependency.getHeight()) * 100) - 50;
        child.setAlpha(ratio / 40);

        if (ratio / 50 < 0)
            if (child.getVisibility() == View.VISIBLE)
                child.setVisibility(View.GONE);
            else if (child.getVisibility() == View.GONE)
                child.setVisibility(View.VISIBLE);
        child.setTranslationY(-(dependency.getHeight() - dependency.getBottom()));
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewGroup child, View dependency) {
        return dependency instanceof AppBarLayout;
    }
}
