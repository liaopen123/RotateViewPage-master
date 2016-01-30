package com.jason.component.rotateviewpage;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by jason on 15/10/21.
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;
    private static final float MAX_DEGREE = 10;
    private ViewPager mViewPager;

    public ZoomOutPageTransformer(ViewPager host) {
        mViewPager = host;
    }

    public void transformPage(View view, float position) {
        position = currentCenter(view);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);
            view.setRotationY(0f);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float degree = MAX_DEGREE;
            if (position < 0) {
                degree = Math.min(degree, Math.abs(position) * MAX_DEGREE);
                view.setRotationY(degree);
            } else if (position > 0) {
                degree = Math.max(-degree, -position * MAX_DEGREE);
                view.setRotationY(degree);
            } else {
                view.setRotationY(0f);
            }
            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            //target = baseAlpha + detaAlpha
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
            view.setRotationY(0f);
        }
    }

    /**
     * 重新计算center view的position，把paddingLeft考虑进去
     * @param view
     * @return
     */
    protected float currentCenter(View view) {
        int paddingLeft = mViewPager.getPaddingLeft();
        int paddingRight = mViewPager.getPaddingRight();
        int pageWidth = mViewPager.getMeasuredWidth() - paddingLeft - paddingRight;
        int position = view.getLeft() - mViewPager.getScrollX() - paddingLeft;
        return (float) position / pageWidth;
    }
}
