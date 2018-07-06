package com.yahoo.rahul.moviesdoughnut.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.yahoo.rahul.moviesdoughnut.R;
import com.yahoo.rahul.moviesdoughnut.api.TmdbService;
import com.yahoo.rahul.moviesdoughnut.customClass.SearchTextInputLayout;
import com.yahoo.rahul.moviesdoughnut.fragment.FragmentFilteredMovies;
import com.yahoo.rahul.moviesdoughnut.fragment.FragmentMoviesGallery;
import com.yahoo.rahul.moviesdoughnut.fragment.SplashScreenFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMoviesGallery extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.tbPrimary)
    Toolbar toolbar;
    @BindView(R.id.tilSearch)
    SearchTextInputLayout searchInputLayout;
    @BindView(R.id.hsvSortFilters)
    HorizontalScrollView hsvSortFilters;
    @BindView(R.id.rgSortFilter)
    RadioGroup rgSortFilter;
    @BindView(R.id.abPrimary)
    AppBarLayout abPrimary;
    @BindView(R.id.spnContainer)
    Spinner spnContainer;
    @BindString(R.string.api_key)
    public String API;

    private SplashScreenFragment splashScreenFragment;
    private TmdbService tmdbService;
    private FragmentFilteredMovies fragmentFilteredMovies;
    private FragmentMoviesGallery fragmentMoviesGallery;
    private boolean splashGone = false;
    private Menu settingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_gallery);
        ButterKnife.bind(this);
        showDialog();
        toolbar.inflateMenu(R.menu.search_toggle);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
//                searchBarToggle();
                switch (item.getItemId()) {
                    case R.id.action_filter:
                        showDiscoverMovie();
                        collapseSearchBar();
                        /*Menu icon*/
                        settingMenu.findItem(R.id.action_filter).setVisible(false);
                        settingMenu.findItem(R.id.action_search).setVisible(true);
                        break;
                    case R.id.action_search:
                        showMovieGallery();
                        expandSearchBar();
                        /*Menu icon*/
                        settingMenu.findItem(R.id.action_search).setVisible(false);
                        settingMenu.findItem(R.id.action_filter).setVisible(true);
                        break;
                }

                return false;
            }
        });
        settingMenu = toolbar.getMenu();
        tmdbService = new TmdbService(API);

        abPrimary.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int b = appBarLayout.getTotalScrollRange();
                int c = Math.abs(verticalOffset);
                int a = b - c;
                if ((float) a / (float) b * 100f <= 55)
                    setLightStatusBar();

                else
                    clearLightStatusBar();
            }
        });
        showMovieGallery();
        initFilterSpinner();
    }

    private void initFilterSpinner() {
        Filter[] filters = new Filter[]{
                new Filter(FragmentFilteredMovies.POPULAR, getResources().getString(R.string.popular_movies)),
                new Filter(FragmentFilteredMovies.TOPRATED, getResources().getString(R.string.top_rated_movies)),
                new Filter(FragmentFilteredMovies.NOWPLAYING, getResources().getString(R.string.now_playing_movies)),
                new Filter(FragmentFilteredMovies.UPCOMING, getResources().getString(R.string.upcoming_movies))
        };

        spnContainer.setAdapter(new FilterSpinnerAdapter(this, R.layout.adapter_filter, filters));
        spnContainer.setOnItemSelectedListener(this);

    }

    public void showDialog() {
        if (splashGone)
            return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("splash_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        splashScreenFragment = new SplashScreenFragment();
        splashScreenFragment.show(ft, "splash_dialog");
        splashScreenFragment.setCancelable(false);

    }

    public void hideDialog() {
        if (splashScreenFragment.isVisible()) {
            splashScreenFragment.dismiss();
            splashGone = true;
        }
    }

    public TmdbService getTmdbService() {
        return tmdbService;
    }

    private void showMovieGallery() {
        if (fragmentMoviesGallery == null)
            fragmentMoviesGallery = new FragmentMoviesGallery();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragmentMoviesGallery).commit();
    }

    private void showDiscoverMovie() {
        if (fragmentFilteredMovies == null)
            fragmentFilteredMovies = new FragmentFilteredMovies();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragmentFilteredMovies).commit();
    }

    private void setLightStatusBar() {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.grd_1_s));
    }

    private void clearLightStatusBar() {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getSelectedItem() instanceof Filter) {
            Filter f = (Filter) adapterView.getSelectedItem();
            if (fragmentFilteredMovies != null)
                fragmentFilteredMovies.loadMovies(f.getId());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    void collapseSearchBar() {
        ValueAnimator anim = ValueAnimator.ofInt(searchInputLayout.getMeasuredWidth(), getResources().getDimensionPixelSize(R.dimen.filter_w));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = searchInputLayout.getLayoutParams();
                layoutParams.width = val;
                searchInputLayout.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), searchInputLayout.getBackgroundTintList().getDefaultColor(), ContextCompat.getColor(getBaseContext(), R.color.shade0));
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                searchInputLayout.setBackgroundTintList(ColorStateList.valueOf(val));
            }
        });

        ValueAnimator alphaAnimation = ValueAnimator.ofFloat(searchInputLayout.getChildAt(0).getAlpha(), 0f);
        alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (float) valueAnimator.getAnimatedValue();
                searchInputLayout.getChildAt(0).setAlpha(val);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(anim, colorAnimation, alphaAnimation);
        animatorSet.setDuration(200);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                searchInputLayout.forceHide();
                spnContainer.setVisibility(View.VISIBLE);
            }
        });
        animatorSet.start();
    }

    void expandSearchBar() {
        ValueAnimator anim = ValueAnimator.ofInt(searchInputLayout.getMeasuredWidth(), getResources().getDimensionPixelSize(R.dimen.search_bar_w));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = searchInputLayout.getLayoutParams();
                layoutParams.width = val;
                searchInputLayout.setLayoutParams(layoutParams);
            }
        });

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), searchInputLayout.getBackgroundTintList().getDefaultColor(), ContextCompat.getColor(getBaseContext(), R.color.sky_blue_s2));
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                searchInputLayout.setBackgroundTintList(ColorStateList.valueOf(val));
            }
        });

        ValueAnimator alphaAnimation = ValueAnimator.ofFloat(searchInputLayout.getChildAt(0).getAlpha(), 1.0f);
        alphaAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (float) valueAnimator.getAnimatedValue();
                searchInputLayout.getChildAt(0).setAlpha(val);
            }
        });


        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(anim, colorAnimation, alphaAnimation);
        animatorSet.setDuration(200);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                searchInputLayout.setVisibility(View.GONE);
//                spnContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                searchInputLayout.forceVisible();
                spnContainer.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }

    public void revealView(View myView) {


        int cx = abPrimary.getRight();
        int cy = abPrimary.getTop();

        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        anim.setDuration(500);

//        myView.setVisibility(View.VISIBLE);

        anim.start();


    }

    public void hideView(final View myView) {


        int cx = (abPrimary.getLeft() + abPrimary.getRight()) / 2;
        int cy = (abPrimary.getTop() + abPrimary.getBottom()) / 2;

        int initialRadius = myView.getWidth();

        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
        anim.setDuration(300);
        anim.setInterpolator(new DecelerateInterpolator());

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

//                myView.setVisibility(View.INVISIBLE);

            }
        });


        anim.start();

    }

    class FilterSpinnerAdapter extends ArrayAdapter<Filter> {
        private Filter[] objects;

        FilterSpinnerAdapter(Context context, int textViewResourceId, Filter[] objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {

            ViewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                rowview = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filter, parent, false);

                holder = new ViewHolder(rowview);
                rowview.setTag(holder);
            } else {
                holder = (ViewHolder) rowview.getTag();
            }
            holder.tvItem.setText(objects[position].getName());
            holder.tvItem.setTextColor(ContextCompat.getColor(getContext(), R.color.grd_1_s));
            holder.cvContainer.setCardElevation(0f);

            Animation hyperspaceJump = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_top);
            hyperspaceJump.setInterpolator(new DecelerateInterpolator());
            hyperspaceJump.setStartOffset(150);
            holder.tvItem.setAnimation(hyperspaceJump);
            return rowview;
        }

        private View getCustomView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View rowview = convertView;
            if (rowview == null) {

                rowview = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filter, parent, false);

                holder = new ViewHolder(rowview);
                rowview.setTag(holder);
            } else {
                holder = (ViewHolder) rowview.getTag();
            }
            holder.tvItem.setText(objects[position].getName());

            Animation hyperspaceJump = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
            hyperspaceJump.setInterpolator(new DecelerateInterpolator());
            hyperspaceJump.setStartOffset(100 * position);
            rowview.setAnimation(hyperspaceJump);

            return rowview;
        }

        class ViewHolder {
            @BindView(R.id.tvItem)
            TextView tvItem;
            @BindView(R.id.cvContainer)
            CardView cvContainer;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }

    public class Filter {
        private int id;
        private String name;

        public Filter(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }


}
