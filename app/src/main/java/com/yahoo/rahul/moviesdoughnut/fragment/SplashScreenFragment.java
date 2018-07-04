package com.yahoo.rahul.moviesdoughnut.fragment;

import android.arch.lifecycle.Lifecycle;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yahoo.rahul.moviesdoughnut.R;
import com.yahoo.rahul.moviesdoughnut.customClass.GlideApp;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenFragment extends DialogFragment {
    @BindView(R.id.ivLoading)
    ImageView ivLoading;
    private boolean isDismissed = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Light_TransparentStatusBar);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splashscreen, container, false);
        ButterKnife.bind(this, v);
        GlideApp.with(v)
                .asGif()
                .centerCrop()
                .load(R.drawable.video_camera_anim)
                .into(ivLoading);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getDialog().getWindow()).getAttributes().windowAnimations = R.style.AppTheme_Dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isDismissed)
            this.dismiss();
    }

    @Override
    public void dismiss() {
        isDismissed = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Objects.requireNonNull(getActivity()).getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED) && isDismissed)
                    SplashScreenFragment.super.dismiss();
            }
        }, 200);
    }
}