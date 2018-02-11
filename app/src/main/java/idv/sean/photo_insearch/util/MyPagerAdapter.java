package idv.sean.photo_insearch.util;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import idv.sean.photo_insearch.fragment.CaseFragment;
import idv.sean.photo_insearch.fragment.MemberDetailFragment;
import idv.sean.photo_insearch.fragment.PhotoFragment;
import idv.sean.photo_insearch.fragment.ProductFragment;
import idv.sean.photo_insearch.vo.Page;

public class MyPagerAdapter extends FragmentPagerAdapter {
    List<Page> pageList;

    public MyPagerAdapter(FragmentManager fm, int pagerCode) {
        super(fm);
        pageList = new ArrayList<>();

        if(pagerCode == 1) {
            pageList.add(new Page(new PhotoFragment(), "Photos"));
            pageList.add(new Page(new ProductFragment(), "Products"));
            pageList.add(new Page(new CaseFragment(), "Cases"));
        }

        if(pagerCode == 2){
            pageList.add(new Page(new MemberDetailFragment(), "Detail"));
            pageList.add(new Page(new PhotoFragment(), "Photos"));
        }
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

    public void clearAll(){
        pageList.clear();
    }
}


