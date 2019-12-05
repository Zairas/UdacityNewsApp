package com.example.myappnews;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myappnews.fragment.*;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends DrawerActivity {

    @BindView(R.id.materialViewPager)
    MaterialViewPager mViewPager;

    @BindView(R.id.header_logo)
    ImageView headerLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        ButterKnife.bind(this);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 5) {
                    case 0:
                        return NewsFragment.newInstance();
                    case 1:
                        return OpinionFragment.newInstance();
                    case 2:
                        return SportFragment.newInstance();
                    case 3:
                        return CultureFragment.newInstance();
                    case 4:
                        return LifestyleFragment.newInstance();
                    default:
                        return NewsFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 5) {
                    case 0:
                        return "News";
                    case 1:
                        return "Opinion";
                    case 2:
                        return "Sport";
                    case 3:
                        return "Culture";
                    case 4:
                        return "Lifestyle";
                }
                return "";
            }
        });


        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        headerLogo.setImageDrawable(getResources().getDrawable(R.drawable.news));
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "http://www.lag-laura.hr/wp-content/uploads/2019/01/sport.jpg");
                    case 1:
                        headerLogo.setImageDrawable(getResources().getDrawable(R.drawable.opinion));
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.magnitude4,
                                "http://www.lag-laura.hr/wp-content/uploads/2019/01/sport.jpg");
                    case 2:
                        headerLogo.setImageDrawable(getResources().getDrawable(R.drawable.sport));
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        headerLogo.setImageDrawable(getResources().getDrawable(R.drawable.culture));
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                    case 4:
                        headerLogo.setImageDrawable(getResources().getDrawable(R.drawable.lifestyle));
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.magnitude2,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }
}
