package com.yahoo.rahul.moviesdoughnut.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yahoo.rahul.moviesdoughnut.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentAlert extends DialogFragment {

    public final static String TITLE = "title";
    public final static String MESSAGE = "msg";
    public final static String ICON = "icon";
    @BindView(R.id.btnExit)
    Button btnExit;
    @BindView(R.id.secMsg)
    TextView secMsg;

    public static FragmentAlert newInstance(String title, String message, int drawable) {
        FragmentAlert f = new FragmentAlert();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        args.putInt(ICON, drawable);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = inflater.inflate(R.layout.dialog_alert, container, false);
        ButterKnife.bind(this, v);

        secMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rench9/movies-doughnut")));
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        return v;
    }
}
