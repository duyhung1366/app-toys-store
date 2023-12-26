package com.example.apptoyselling.ui.user.fragment.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.apptoyselling.R;
import com.example.apptoyselling.model.Banner;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<Banner> bannerArrayList;

    public ViewPagerAdapter(ArrayList<Banner> bannerArrayList) {
        this.bannerArrayList = bannerArrayList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.view_pager,container,false);
        ImageView imgPager = view.findViewById(R.id.imgPager);
        Banner pager = bannerArrayList.get(position);
        imgPager.setImageResource(pager.getImgBanner());
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (bannerArrayList != null){
            return bannerArrayList.size();
        }
        return  0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
