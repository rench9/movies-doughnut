package com.yahoo.rahul.moviesdoughnut.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yahoo.rahul.moviesdoughnut.R;
import com.yahoo.rahul.moviesdoughnut.api.TmdbService;
import com.yahoo.rahul.moviesdoughnut.api.response.model.Movie;
import com.yahoo.rahul.moviesdoughnut.customClass.SearchTextInputLayout;
import com.yahoo.rahul.moviesdoughnut.fragment.FragmentFilteredMovies;
import com.yahoo.rahul.moviesdoughnut.fragment.FragmentMoviesGallery;
import com.yahoo.rahul.moviesdoughnut.fragment.SplashScreenFragment;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMoviesGallery extends AppCompatActivity {
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
    @BindString(R.string.api_key)
    private String API;

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
                searchBarToggle();
                return false;
            }
        });
        settingMenu = toolbar.getMenu();
        tmdbService = new TmdbService(API);
        rgSortFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                filterMovies(radioGroup.getCheckedRadioButtonId());
            }
        });
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

    }

    private void searchBarToggle() {
        if (searchInputLayout.getVisibility() == View.VISIBLE) {
            /*Search Bar*/
            Animation searchbarOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.searchbar_out);
            searchbarOutAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            searchInputLayout.setAnimation(searchbarOutAnimation);
            searchInputLayout.forceHide();
            /*Radio button group*/
            hsvSortFilters.setVisibility(View.VISIBLE);
            for (int i = 0; i < rgSortFilter.getChildCount(); i++) {
                RadioButton radioButton = (RadioButton) rgSortFilter.getChildAt(i);
                Animation slideInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
                slideInAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
                slideInAnimation.setStartOffset(75 * i);
                radioButton.setAnimation(slideInAnimation);
                radioButton.setVisibility(View.VISIBLE);
            }
            /*Fragment*/
            showDiscoverMovie();
            /*Menu icon*/
            settingMenu.findItem(R.id.action_filter).setIcon(R.drawable.ic_search);

        } else {
            /*Radio button group*/
            Animation slideOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_bottom);
            slideOutAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            hsvSortFilters.setAnimation(slideOutAnimation);
            hsvSortFilters.setLayoutAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    for (int i = 0; i < rgSortFilter.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) rgSortFilter.getChildAt(i);
                        radioButton.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            hsvSortFilters.setVisibility(View.GONE);
            /*Search Bar*/
            Animation searchbarInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.searchbar_in);
            searchbarInAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            searchInputLayout.setAnimation(searchbarInAnimation);
            searchInputLayout.forceVisible();
            /*Fragment*/
            showMovieGallery();
            /*Menu icon*/
            settingMenu.findItem(R.id.action_filter).setIcon(R.drawable.ic_option);
        }

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

    private void filterMovies(int radioButtonId) {
        switch (radioButtonId) {
            case R.id.rbSortAll:
                fragmentFilteredMovies.loadMovies(FragmentFilteredMovies.POPULARITY_DESC);
                break;
            case R.id.rbSortPopularity:
                fragmentFilteredMovies.loadMovies(FragmentFilteredMovies.POPULARITY_DESC);
                break;
            case R.id.rbSortRating:
                fragmentFilteredMovies.loadMovies(FragmentFilteredMovies.VOTE_AVERAGE_DESC);
                break;
            case R.id.rbSortDate:
                fragmentFilteredMovies.loadMovies(FragmentFilteredMovies.RELEASE_DATE_DESC);
                break;
            case R.id.rbSortTitle:
                fragmentFilteredMovies.loadMovies(FragmentFilteredMovies.ORIGINAL_TITLE_ASC);
                break;
            default:
                break;
        }
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

    private void showDetails(Movie movie) {

    }

}
