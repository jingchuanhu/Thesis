package com.jch.scrolltest;

import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

/**
 * Created by ACER on 2014/12/8.
 */
public class FreshViewLayout extends LinearLayout {

    private ScrollerCompat mScroller = null;
    private View mHeadView = null;


    public FreshViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FreshViewLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

        mScroller = ScrollerCompat.create(context, new AccelerateDecelerateInterpolator());

        mHeadView = View.inflate(context, R.layout.headview, null);

    }

    @Override
    public void computeScroll() {
        super.computeScroll();


    }


}
