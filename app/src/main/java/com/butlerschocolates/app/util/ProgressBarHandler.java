package com.butlerschocolates.app.util;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;

import com.butlerschocolates.app.R;

/**
 * Created by Ravinder on 09-08-2020.
 */

public class ProgressBarHandler {
    private ProgressBar mProgressBar;
     FragmentActivity mContext;
    RelativeLayout rl;

    public ProgressBarHandler(FragmentActivity context) {
        mContext = context;
        ViewGroup layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyle);
        mProgressBar.setIndeterminate(true);

        try {
            mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
//            mProgressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.White), android.graphics.PorterDuff.Mode.SRC_IN);
        } catch (Exception e) {

        }
        RelativeLayout.LayoutParams params = new
                RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        rl = new RelativeLayout(context);
        rl.setGravity(Gravity.CENTER);
        rl.addView(mProgressBar);

        layout.addView(rl, params);

        hide();
    }
    public void setGravityCenter() {
        rl.setGravity(Gravity.CENTER);
    }

    public void show() {
        mContext.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mProgressBar.setVisibility(View.VISIBLE);
    }
    public void hide() {
        mContext.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        mProgressBar.setVisibility(View.GONE);
    }
}