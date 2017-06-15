package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ezytopup.reseller.utility.Constant;

/**
 * Created by indraaguslesmana on 5/16/17.
 */

public class CustomViewPager extends ViewPager {
    private boolean mIsEnabledSwipe = Constant.ENABLE_HOME_SWIPE;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEnabledSwipe) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!mIsEnabledSwipe) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setEnabledSwipe(boolean enabled) {
        mIsEnabledSwipe = enabled;
    }
}
