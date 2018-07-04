package com.yahoo.rahul.moviesdoughnut.customClass;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewPaginationOnScroll extends RecyclerView.OnScrollListener {


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int totalItemCount;
        int visibleItemCount;
        int lastVisibleItem;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            totalItemCount = layoutManager.getItemCount();
            visibleItemCount = layoutManager.getChildCount();
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        } else if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            totalItemCount = layoutManager.getItemCount();
            visibleItemCount = layoutManager.getChildCount();
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        } else
            return;

        if (totalItemCount <= visibleItemCount + lastVisibleItem && !isLoading() && !isLastItem()) {
            loadMoreItems();
        }

    }

    protected abstract void loadMoreItems();

    public abstract int getTotalItemCount();

    public abstract boolean isLastItem();

    public abstract boolean isLoading();
}
