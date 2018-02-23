package idv.sean.photo_insearch.util;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import idv.sean.photo_insearch.fragment.CaseFragment;
import idv.sean.photo_insearch.fragment.MemberDetailFragment;
import idv.sean.photo_insearch.fragment.UserChatFragment;
import idv.sean.photo_insearch.fragment.PhotoFragment;
import idv.sean.photo_insearch.fragment.ProductFragment;
import idv.sean.photo_insearch.model.Page;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
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
            pageList.add(new Page(new UserChatFragment(),"Chat"));
            pageList.add(new Page(new CaseFragment(), "MyCases"));
            pageList.add(new Page(new MemberDetailFragment(), "Detail"));

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


