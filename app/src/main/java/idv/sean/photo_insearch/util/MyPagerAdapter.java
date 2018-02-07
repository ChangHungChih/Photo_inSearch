package idv.sean.photo_insearch.util;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import idv.sean.photo_insearch.fragment.CaseFragment;
import idv.sean.photo_insearch.fragment.PhotoFragment;
import idv.sean.photo_insearch.fragment.ProductFragment;
import idv.sean.photo_insearch.vo.Page;

public class MyPagerAdapter extends FragmentPagerAdapter {
    List<Page> pageList;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        pageList = new ArrayList<>();
        pageList.add(new Page(new PhotoFragment(),"Photos"));
        pageList.add(new Page(new ProductFragment(),"Products"));
        pageList.add(new Page(new CaseFragment(),"Cases"));

    }

    @Override
    public Fragment getItem(int position) {
        return pageList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageList.get(position).getTitle();
    }
}


