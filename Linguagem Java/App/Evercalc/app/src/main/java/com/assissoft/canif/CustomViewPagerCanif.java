package com.assissoft.canif;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Marcos on 22/02/2017.
 *
 */
public class CustomViewPagerCanif extends ViewPager {

    private boolean isPagingEnabled = true;

    public CustomViewPagerCanif(Context context) {
        super(context);
    }

    public CustomViewPagerCanif(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }


}
