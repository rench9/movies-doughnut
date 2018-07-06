package com.yahoo.rahul.moviesdoughnut.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yahoo.rahul.moviesdoughnut.R;
import com.yahoo.rahul.moviesdoughnut.activity.ActivityMovieDetail;
import com.yahoo.rahul.moviesdoughnut.activity.ActivityMoviesGallery;
import com.yahoo.rahul.moviesdoughnut.api.TmdbService;
import com.yahoo.rahul.moviesdoughnut.api.TmdbUtils;
import com.yahoo.rahul.moviesdoughnut.api.response.NowPlayingMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.PopularMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.TopRatedMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.UpcomingMovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Movie;
import com.yahoo.rahul.moviesdoughnut.customClass.GlideApp;
import com.yahoo.rahul.moviesdoughnut.customClass.RecyclerViewPaginationOnScroll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

public class FragmentFilteredMovies extends Fragment {

    public static final String POPULARITY_ASC = "popularity.asc";
    public static final String POPULARITY_DESC = "popularity.desc";
    public static final String RELEASE_DATE_ASC = "release_date.asc";
    public static final String RELEASE_DATE_DESC = "release_date.desc";
    public static final String REVENUE_ASC = "revenue.asc";
    public static final String REVENUE_DESC = "revenue.desc";
    public static final String PRIMARY_RELEASE_DATE_ASC = "primary_release_date.asc";
    public static final String PRIMARY_RELEASE_DATE_DESC = "primary_release_date.desc";
    public static final String ORIGINAL_TITLE_ASC = "original_title.asc";
    public static final String ORIGINAL_TITLE_DESC = "original_title.desc";
    public static final String VOTE_AVERAGE_ASC = "vote_average.asc";
    public static final String VOTE_AVERAGE_DESC = "vote_average.desc";
    public static final String VOTE_COUNT_ASC = "vote_count.asc";
    public static final String VOTE_COUNT_DESC = "vote_count.desc";

    public static final int TOPRATED = 1001;
    public static final int NOWPLAYING = 1002;
    public static final int UPCOMING = 1003;
    public static final int POPULAR = 1004;
    @BindView(R.id.rvContainer_filtered_movie)
    RecyclerView rvContainer_filtered_movie;
    MovieCardAdapter movieCardAdapter;
    private TmdbService tmdbService;
    private int filter;

    private boolean loading = false;
    private boolean last = false;
    private int count = 0;

    public FragmentFilteredMovies() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filtered_movies, container, false);
        ButterKnife.bind(this, v);
        filter = POPULAR;
        tmdbService = ((ActivityMoviesGallery) Objects.requireNonNull(getActivity())).getTmdbService();
        movieCardAdapter = new MovieCardAdapter(new ArrayList<Movie>());
        rvContainer_filtered_movie.setLayoutManager(new GridLayoutManager(v.getContext(), isLandscape() ? 6 : 3, GridLayoutManager.VERTICAL, false));
        rvContainer_filtered_movie.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int spanCount = isLandscape() ? 6 : 3;
            private int spacing = 28;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int column = position % spanCount;
                outRect.left = spacing - column * spacing / spanCount;

                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing * 2;
            }
        });
        rvContainer_filtered_movie.setAdapter(movieCardAdapter);

        rvContainer_filtered_movie.addOnScrollListener(new RecyclerViewPaginationOnScroll() {


            @Override
            protected void loadMoreItems() {
                loadMovies(filter, count);
            }

            @Override
            public int getTotalItemCount() {
                return 0;
            }

            @Override
            public boolean isLastItem() {
                return last;
            }

            @Override
            public boolean isLoading() {
                return loading;
            }
        });
        loadMovies(POPULAR);
        return v;
    }

    public void loadMovies(int filter) {
        if (this.filter != filter) {
            count = 0;
            loading = false;
            last = false;
        }
        this.filter = filter;
        this.loadMovies(filter, 1);
    }

    private void loadMovies(final int filter, int page) {
        movieCardAdapter.removeAllItems();
        switch (filter) {
            case TOPRATED:
                tmdbService.discover.getTopRatedMovies(1).subscribe(new MaybeObserver<TopRatedMovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading = true;
                    }

                    @Override
                    public void onSuccess(TopRatedMovieResponse topRatedMovieResponse) {
                        movieCardAdapter.addAllItems(topRatedMovieResponse.getResults());
                        loading = false;
                        count = topRatedMovieResponse.getPage() + 1;
                        if (count > topRatedMovieResponse.getTotal_pages())
                            last = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading = false;
                    }

                    @Override
                    public void onComplete() {
                        loading = false;
                    }
                });
                break;
            case NOWPLAYING:
                tmdbService.discover.getNowPlayingMovies(1).subscribe(new MaybeObserver<NowPlayingMovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading = true;
                    }

                    @Override
                    public void onSuccess(NowPlayingMovieResponse nowPlayingMovieResponse) {
                        movieCardAdapter.addAllItems(nowPlayingMovieResponse.getResults());
                        count = nowPlayingMovieResponse.getPage() + 1;
                        if (count > nowPlayingMovieResponse.getTotal_pages())
                            last = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading = false;
                    }

                    @Override
                    public void onComplete() {
                        loading = false;
                    }
                });
                break;
            case UPCOMING:
                tmdbService.discover.getUpcomingMovies(1).subscribe(new MaybeObserver<UpcomingMovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading = true;
                    }

                    @Override
                    public void onSuccess(UpcomingMovieResponse upcomingMovieResponse) {
                        movieCardAdapter.addAllItems(upcomingMovieResponse.getResults());
                        count = upcomingMovieResponse.getPage() + 1;
                        if (count > upcomingMovieResponse.getTotal_pages())
                            last = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading = false;
                    }

                    @Override
                    public void onComplete() {
                        loading = false;
                    }
                });
                break;
            default:
                tmdbService.discover.getPopularMovies(1).subscribe(new MaybeObserver<PopularMovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading = true;
                    }

                    @Override
                    public void onSuccess(PopularMovieResponse popularMovieResponse) {
                        movieCardAdapter.addAllItems(popularMovieResponse.getResults());
                        count = popularMovieResponse.getPage() + 1;
                        if (count > popularMovieResponse.getTotal_pages())
                            last = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading = false;
                    }

                    @Override
                    public void onComplete() {
                        loading = false;
                    }
                });
                break;
        }


    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    class MovieCardAdapter extends RecyclerView.Adapter<MovieCardAdapter.ViewHolder> {

        private ArrayList<Movie> items;

        public MovieCardAdapter(ArrayList<Movie> movies) {
            this.items = movies;
        }

        @NonNull
        @Override
        public MovieCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movie_card_grid, parent, false);
            return new MovieCardAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final MovieCardAdapter.ViewHolder holder, final int position) {
            GlideApp.with(holder.ivPoster)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_poster)
                    .load(TmdbUtils.generateImageUrl(items.get(position).getPoster_path(), TmdbUtils.POSTER_W_300))
                    .into(holder.ivPoster);
            holder.tvMovieName.setText(items.get(position).getTitle());
            holder.tvDuration.setText(items.get(position).getRelease_date());
            holder.tvRating.setText(String.valueOf(items.get(position).getVote_average()));

            if (items.get(position).getVote_average() > 8.4)
                holder.tvRating.setBackgroundTintList(ContextCompat.getColorStateList(holder.tvRating.getContext(), R.color.rating_high));
            else if (items.get(position).getVote_average() < 5.5)
                holder.tvRating.setBackgroundTintList(ContextCompat.getColorStateList(holder.tvRating.getContext(), R.color.rating_low));
            else
                holder.tvRating.setBackgroundTintList(ContextCompat.getColorStateList(holder.tvRating.getContext(), R.color.rating_normal));

            holder.ivPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(holder.ivPoster.getContext(), ActivityMovieDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ActivityMovieDetail.MOVIE, items.get(position));
                    intent.putExtras(bundle);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(),
                                    holder.ivPoster,
                                    getResources().getString(R.string.cv_poster_transition));
                    startActivity(intent, options.toBundle());
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(Movie movie) {
            items.add(movie);
            notifyItemInserted(items.size());
        }

        public void addAllItems(ArrayList<Movie> movies) {
            items.addAll(movies);
            notifyItemRangeInserted(items.size(), items.size() + movies.size());
        }

        public void addAllItems(Movie[] movies) {
            items.addAll(Arrays.asList(movies));
            notifyItemRangeInserted(items.size(), items.size() + movies.length);
        }

        public void removeAllItems() {
            items.clear();
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ivPoster)
            ImageView ivPoster;
            @BindView(R.id.tvRating)
            TextView tvRating;
            @BindView(R.id.tvMovieName)
            TextView tvMovieName;
            @BindView(R.id.tvDuration)
            TextView tvDuration;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
