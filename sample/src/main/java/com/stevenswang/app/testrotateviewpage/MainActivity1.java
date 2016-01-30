package com.stevenswang.app.testrotateviewpage;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jason.component.rotateviewpage.ZoomOutPageTransformer;

import java.util.ArrayList;

public class MainActivity1 extends AppCompatActivity {
    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private MyAdViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        mPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<ImageView> imageViews= new ArrayList();
        for(int i = 0;i<6;i++){
            ImageView imageView = new ImageView(MainActivity1.this);
            imageView.setImageResource(R.mipmap.topspeedbanner);
            imageViews.add(imageView);
        }



        mPager.setBackgroundColor(Color.WHITE);
        mPagerAdapter = new MyAdViewPagerAdapter(MainActivity1.this,imageViews);
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageMargin(-200);
        mPager.setPageTransformer(true,new ZoomOutPageTransformer(mPager));
    }



    public class MyAdViewPagerAdapter extends PagerAdapter {

        private ArrayList<ImageView> pagerList;
        private Context ctx;

        public MyAdViewPagerAdapter(Context ctx, ArrayList<ImageView> pagerList) {
            this.ctx = ctx;
            this.pagerList = pagerList;
        }

        @Override
        public int getCount() {
            return pagerList.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(pagerList.get(position));
            return pagerList.get(position);
        }

        @Override
        public void destroyItem(View container, int position, Object view) {
            ((ViewPager) container).removeView(pagerList.get(position));
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }


}
