package com.yahoo.rahul.moviesdoughnut.fragment;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

public class FragmentMoviesGallery extends Fragment {
    @BindView(R.id.rvContainer_popular_movie)
    RecyclerView rvContainer_popular_movie;
    @BindView(R.id.rvContainer_top_movie)
    RecyclerView rvContainer_top_movie;
    @BindView(R.id.rvContainer_upcoming_movie)
    RecyclerView rvContainer_upcoming_movie;
    @BindView(R.id.rvContainer_running_movie)
    RecyclerView rvContainer_running_movie;
    private TmdbService tmdbService;
    private MovieCardAdapter adapter_popular_movies;
    private MovieCardAdapter adapter_top_movies;
    private MovieCardAdapter adapter_upcomming_movies;
    private MovieCardAdapter adapter_running_movies;

    public FragmentMoviesGallery() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_movies_gallery, container, false);
        ButterKnife.bind(this, v);
        tmdbService = ((ActivityMoviesGallery) getActivity()).getTmdbService();

        adapter_popular_movies = new MovieCardAdapter(new ArrayList<Movie>());
        adapter_top_movies = new MovieCardAdapter(new ArrayList<Movie>());
        adapter_upcomming_movies = new MovieCardAdapter(new ArrayList<Movie>());
        adapter_running_movies = new MovieCardAdapter(new ArrayList<Movie>());

        loadPosters();
        return v;
    }

    private void loadPosters() {


        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            private int spacing = 32;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position
                if (position == 0) {
                    outRect.left = spacing;
                }
                outRect.right = spacing;
            }
        };

        rvContainer_popular_movie.addItemDecoration(itemDecoration);
        rvContainer_popular_movie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvContainer_top_movie.addItemDecoration(itemDecoration);
        rvContainer_top_movie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvContainer_upcoming_movie.addItemDecoration(itemDecoration);
        rvContainer_upcoming_movie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvContainer_running_movie.addItemDecoration(itemDecoration);
        rvContainer_running_movie.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


        rvContainer_popular_movie.setAdapter(adapter_popular_movies);
        rvContainer_top_movie.setAdapter(adapter_top_movies);
        rvContainer_upcoming_movie.setAdapter(adapter_upcomming_movies);
        rvContainer_running_movie.setAdapter(adapter_running_movies);

        tmdbService.discover.getPopularMovies(1).subscribe(new MaybeObserver<PopularMovieResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(PopularMovieResponse popularMovieResponse) {
                for (Movie m : popularMovieResponse.getResults())
                    adapter_popular_movies.addItem(m);
                try {
                    ((ActivityMoviesGallery) getActivity()).hideDialog();
                } catch (Exception e) {

                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
        tmdbService.discover.getTopRatedMovies(1).subscribe(new MaybeObserver<TopRatedMovieResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(TopRatedMovieResponse topRatedMovieResponse) {
                for (Movie m : topRatedMovieResponse.getResults())
                    adapter_top_movies.addItem(m);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        tmdbService.discover.getUpcomingMovies(1).subscribe(new MaybeObserver<UpcomingMovieResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(UpcomingMovieResponse upcomingMovieResponse) {
                for (Movie m : upcomingMovieResponse.getResults())
                    adapter_upcomming_movies.addItem(m);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        tmdbService.discover.getNowPlayingMovies(1).subscribe(new MaybeObserver<NowPlayingMovieResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(NowPlayingMovieResponse nowPlayingMovieResponse) {
                for (Movie m : nowPlayingMovieResponse.getResults())
                    adapter_running_movies.addItem(m);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        rvContainer_popular_movie.addOnScrollListener(new RecyclerViewPaginationOnScroll() {
            private boolean loading = false;
            private boolean last = false;
            private int count = 2;

            @Override
            protected void loadMoreItems() {
                tmdbService.discover.getPopularMovies(count).subscribe(new MaybeObserver<PopularMovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading = true;
                    }

                    @Override
                    public void onSuccess(PopularMovieResponse popularMovieResponse) {
                        adapter_popular_movies.addAllItems(popularMovieResponse.getResults());
                        loading = false;
                        count++;
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
        rvContainer_top_movie.addOnScrollListener(new RecyclerViewPaginationOnScroll() {
            private boolean loading = false;
            private boolean last = false;
            private int count = 2;

            @Override
            protected void loadMoreItems() {
                tmdbService.discover.getTopRatedMovies(count).subscribe(new MaybeObserver<TopRatedMovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading = true;
                    }

                    @Override
                    public void onSuccess(TopRatedMovieResponse topRatedMovieResponse) {
                        adapter_top_movies.addAllItems(topRatedMovieResponse.getResults());
                        loading = false;
                        count++;
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
        rvContainer_upcoming_movie.addOnScrollListener(new RecyclerViewPaginationOnScroll() {
            private boolean loading = false;
            private boolean last = false;
            private int count = 2;

            @Override
            protected void loadMoreItems() {
                tmdbService.discover.getUpcomingMovies(count).subscribe(new MaybeObserver<UpcomingMovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading = true;
                    }

                    @Override
                    public void onSuccess(UpcomingMovieResponse upcomingMovieResponse) {
                        adapter_upcomming_movies.addAllItems(upcomingMovieResponse.getResults());
                        loading = false;
                        count++;
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
        rvContainer_running_movie.addOnScrollListener(new RecyclerViewPaginationOnScroll() {
            private boolean loading = false;
            private boolean last = false;
            private int count = 2;

            @Override
            protected void loadMoreItems() {
                tmdbService.discover.getNowPlayingMovies(count).subscribe(new MaybeObserver<NowPlayingMovieResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        loading = true;
                    }

                    @Override
                    public void onSuccess(NowPlayingMovieResponse nowPlayingMovieResponse) {
                        adapter_running_movies.addAllItems(nowPlayingMovieResponse.getResults());
                        loading = false;
                        count++;
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


    }

    class MovieCardAdapter extends RecyclerView.Adapter<MovieCardAdapter.ViewHolder> {

        private ArrayList<Movie> items;

        public MovieCardAdapter(ArrayList<Movie> movies) {
            this.items = movies;
        }

        @NonNull
        @Override
        public MovieCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movie_card, parent, false);
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
