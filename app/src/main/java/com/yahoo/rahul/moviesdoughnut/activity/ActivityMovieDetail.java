package com.yahoo.rahul.moviesdoughnut.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.transition.AutoTransition;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yahoo.rahul.moviesdoughnut.R;
import com.yahoo.rahul.moviesdoughnut.api.TmdbService;
import com.yahoo.rahul.moviesdoughnut.api.TmdbUtils;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieCreditsResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieExternalIdsResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieImagesResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.MovieResponse;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Backdrop;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Cast;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Item;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Movie;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Poster;
import com.yahoo.rahul.moviesdoughnut.customClass.GlideApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

public class ActivityMovieDetail extends AppCompatActivity {

    public static final String MOVIE = "movie";
    @BindView(R.id.tbPrimary)
    Toolbar tbPrimary;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rvGenreContainer)
    RecyclerView rvGenreContainer;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvPhoto)
    TextView tvPhoto;
    @BindView(R.id.tvMovieName)
    TextView tvMovieName;
    @BindView(R.id.tvDuration)
    TextView tvDuration;
    @BindView(R.id.tvReleaseDate)
    TextView tvReleaseDate;
    @BindView(R.id.tvPhotoCount)
    TextView tvPhotoCount;
    @BindView(R.id.rvCastContainer)
    RecyclerView rvCastContainer;
    @BindView(R.id.ivGallery1)
    ImageView ivGallery1;
    @BindView(R.id.ivGallery2)
    ImageView ivGallery2;
    @BindView(R.id.ivGallery3)
    ImageView ivGallery3;
    @BindView(R.id.ivGallery4)
    ImageView ivGallery4;
    @BindView(R.id.ivGallery5)
    ImageView ivGallery5;
    @BindView(R.id.ivGallery6)
    ImageView ivGallery6;
    @BindView(R.id.ivGallery7)
    ImageView ivGallery7;
    @BindView(R.id.cvFloating)
    CardView cvFloating;
    @BindView(R.id.ivPoster)
    ImageView ivPoster;
    @BindView(R.id.abPrimary)
    AppBarLayout abPrimary;
    @BindView(R.id.nsvContainer)
    NestedScrollView nsvContainer;
    @BindView(R.id.llWebsite)
    LinearLayout llWebsite;
    @BindView(R.id.llImdb)
    LinearLayout llImdb;
    @BindView(R.id.llFacebook)
    LinearLayout llFacebook;
    @BindView(R.id.llTwitter)
    LinearLayout llTwitter;
    @BindView(R.id.llInstagram)
    LinearLayout llInstagram;
    @BindView(R.id.tvWebsite)
    TextView tvWebsite;
    @BindView(R.id.tvImdb)
    TextView tvImdb;
    @BindView(R.id.tvFacebook)
    TextView tvFacebook;
    @BindView(R.id.tvTwitter)
    TextView tvTwitter;
    @BindView(R.id.tvInstagram)
    TextView tvInstagram;
    private Movie movie;
    private ChipTextViewAdapter chipTextViewAdapter;
    private CastImageAdapter castImageAdapter;
    private TmdbService tmdbService;
    private Toast toast;
    @BindString(R.string.api_key)
    private String API;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        setSupportActionBar(tbPrimary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");

        tmdbService = new TmdbService(API);

        movie = Objects.requireNonNull(getIntent().getExtras()).getParcelable(MOVIE);
        String backDrop = isLandscape() ?
                TmdbUtils.generateImageUrl(movie.getPoster_path(), TmdbUtils.POSTER_W_300) :
                movie.getBackdrop_path() != null ?
                        TmdbUtils.generateImageUrl(movie.getBackdrop_path(), TmdbUtils.BACKDROP_W_780) :
                        TmdbUtils.generateImageUrl(movie.getPoster_path(), TmdbUtils.POSTER_W_300);

        int placeholder = isLandscape() ? R.drawable.placeholder_banner : R.drawable.placeholder_poster;

        tvTitle.setText(movie.getTitle());
        tvMovieName.setText(movie.getTitle());
        tvDescription.setText(movie.getOverview());
        GlideApp.with(getApplicationContext())
                .asBitmap()
                .fitCenter()
                .placeholder(placeholder)
                .load(backDrop)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }
                })
                .into(ivPoster);

        this.initExtras();
        this.initCast();
        this.initGenre();
        this.populateData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityMovieDetail.this.populateImages();
                ActivityMovieDetail.this.populateCast();
                ActivityMovieDetail.this.populateExternalLinks();
            }
        }, 600);

    }

    private void populateData() {


        if (!isLandscape())
            abPrimary.getLayoutParams().height = getActionBarSize() + Math.round(getSupportActionBar().getHeight() + getResources().getDisplayMetrics().widthPixels * 9 / 16);

        tmdbService.discover.getMovieById(movie.getId()).subscribe(new MaybeObserver<MovieResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(final MovieResponse movieResponse) {
                tvDuration.setText(String.format("%dmin", movieResponse.getRuntime()));
                chipTextViewAdapter.addItems(movieResponse.getGenres());
                tvReleaseDate.setText(String.format("%s%s", movieResponse.getStatus(), movieResponse.getRelease_date()));


                if (movieResponse.getHomepage() != null) {
                    llWebsite.setVisibility(View.VISIBLE);
                    llWebsite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieResponse.getHomepage())));
                        }
                    });
                }


            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void populateImages() {
        tmdbService.discover.getMovieImages(movie.getId()).subscribe(new MaybeObserver<MovieImagesResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(MovieImagesResponse movieImagesResponse) {
                android.support.transition.TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView(), new AutoTransition());
                int pCount = 0;
                Backdrop[] backdrops = movieImagesResponse.getBackdrops();
                Poster[] posters = movieImagesResponse.getPosters();

                if (backdrops.length > 1) {
                    Backdrop temp = backdrops[0];
                    backdrops[0] = backdrops[backdrops.length - 1];
                    backdrops[backdrops.length - 1] = temp;
                }

                ImageView[] imageViews = {
                        ivGallery1,
                        ivGallery2,
                        ivGallery3,
                        ivGallery4,
                        ivGallery5,
                        ivGallery6,
                        ivGallery7};

                for (int i = 0; i < imageViews.length; i++) {
                    String imagePath = "";
                    if (backdrops.length > i)
                        imagePath = TmdbUtils.generateImageUrl(backdrops[i].getFile_path(), TmdbUtils.BACKDROP_W_300);
                    else if (posters.length + backdrops.length > i) {
                        imagePath = TmdbUtils.generateImageUrl(posters[pCount].getFile_path(), TmdbUtils.BACKDROP_W_300);
                        pCount++;
                    } else {
                        imagePath = null;
                    }
                    GlideApp.with(getApplicationContext())
                            .asBitmap()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder_square)
                            .load(imagePath)
                            .into(imageViews[i]);
                }

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void populateCast() {
        tmdbService.discover.getMovieCredits(movie.getId()).subscribe(new MaybeObserver<MovieCreditsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(MovieCreditsResponse movieCreditsResponse) {
                castImageAdapter.addItems(movieCreditsResponse.getCast());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initCast() {
        castImageAdapter = new CastImageAdapter(new ArrayList<Cast>());
        rvCastContainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCastContainer.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int spacing = 42;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position

                if (position == 0)
                    outRect.left = 0;
                else
                    outRect.left = spacing;
            }
        });
        rvCastContainer.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.layout_animation_zoom_in));
        rvCastContainer.setAdapter(castImageAdapter);
    }

    private void initGenre() {
        chipTextViewAdapter = new ChipTextViewAdapter(new ArrayList<Item>());
        rvGenreContainer.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvGenreContainer.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int spacing = 20;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view); // item position

                if (position == 0)
                    outRect.left = 0;
                else
                    outRect.left = spacing;
            }
        });
        rvGenreContainer.setAdapter(chipTextViewAdapter);
    }

    private void populateExternalLinks() {
        tmdbService.discover.getMovieExternalIds(movie.getId()).subscribe(new MaybeObserver<MovieExternalIdsResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(final MovieExternalIdsResponse movieExternalIdsResponse) {

                if (movieExternalIdsResponse.getImdb_id() != null) {
                    llImdb.setVisibility(View.VISIBLE);
                    tvImdb.setText(movieExternalIdsResponse.getImdb_id());
                    llImdb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://www.imdb.com/title/%s/", movieExternalIdsResponse.getImdb_id()))));
                        }
                    });
                }
                if (movieExternalIdsResponse.getFacebook_id() != null) {
                    llFacebook.setVisibility(View.VISIBLE);
                    tvFacebook.setText(movieExternalIdsResponse.getFacebook_id());
                    llFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://www.facebook.com/%s", movieExternalIdsResponse.getFacebook_id()))));
                        }
                    });
                }
                if (movieExternalIdsResponse.getTwitter_id() != null) {
                    llTwitter.setVisibility(View.VISIBLE);
                    tvTwitter.setText(movieExternalIdsResponse.getTwitter_id());
                    llTwitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://twitter.com/%s", movieExternalIdsResponse.getTwitter_id()))));
                        }
                    });
                }
                if (movieExternalIdsResponse.getInstagram_id() != null) {
                    llInstagram.setVisibility(View.VISIBLE);
                    tvInstagram.setText(movieExternalIdsResponse.getInstagram_id());
                    llInstagram.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("https://www.instagram.com/%s/", movieExternalIdsResponse.getInstagram_id()))));
                        }
                    });
                }
            }

            @Override
            public void onError(Throwable e) {

                Log.e("populateExternalLinks", e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private int getActionBarSize() {
        TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    private void initExtras() {

        abPrimary.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int b = appBarLayout.getTotalScrollRange();
                int c = Math.abs(verticalOffset);
                int a = b - c;
                if ((float) a / (float) b * 100f <= 55 || isLandscape())
                    setLightStatusBar();

                else
                    clearLightStatusBar();
            }
        });

        ivGallery7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastMessage("TODO | To be implemented in phase 2");
            }
        });
    }

    private void toastMessage(String msg) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void setLightStatusBar() {
        tbPrimary.setNavigationIcon(R.drawable.ic_back);
    }

    private void clearLightStatusBar() {
        tbPrimary.setNavigationIcon(R.drawable.ic_back_dark);
    }

    private boolean isLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    class ChipTextViewAdapter extends RecyclerView.Adapter<ChipTextViewAdapter.ViewHolder> {

        private ArrayList<Item> items;

        ChipTextViewAdapter(ArrayList<Item> movies) {
            this.items = movies;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_text_chip, parent, false);
            return new ChipTextViewAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(items.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(Item item) {
            items.add(item);
            notifyItemInserted(items.size());
        }

        public void addItems(Item[] items) {

            this.items.addAll(Arrays.asList(items));
            notifyItemRangeInserted(this.items.size() - items.length, this.items.size());
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvChipTextView)
            TextView textView;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    class CastImageAdapter extends RecyclerView.Adapter<CastImageAdapter.ViewHolder> {

        private ArrayList<Cast> items;

        CastImageAdapter(ArrayList<Cast> casts) {
            this.items = casts;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cast, parent, false);
            return new CastImageAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            GlideApp.with(holder.ivCast)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_square)
                    .load(TmdbUtils.generateImageUrl(items.get(position).getProfile_path(), TmdbUtils.PROFILE_W_45))
                    .into(holder.ivCast);
            holder.ivCast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toastMessage("TODO | To be implemented in phase 2");
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void addItem(Cast cast) {
            items.add(cast);
            notifyItemInserted(items.size());
        }

        public void addItems(Cast[] casts) {

            this.items.addAll(Arrays.asList(casts));
            notifyItemRangeInserted(this.items.size() - casts.length, this.items.size());
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ivCast)
            ImageView ivCast;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
