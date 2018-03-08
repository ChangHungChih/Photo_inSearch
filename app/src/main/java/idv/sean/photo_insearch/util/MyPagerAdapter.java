package idv.sean.photo_insearch.util;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import idv.sean.photo_insearch.activity.MainActivity;
import idv.sean.photo_insearch.fragment.AboutUsFragment;
import idv.sean.photo_insearch.fragment.CaseFragment;
import idv.sean.photo_insearch.fragment.MemberDetailFragment;
import idv.sean.photo_insearch.fragment.MyCaseTypeFragment;
import idv.sean.photo_insearch.fragment.NewsFragment;
import idv.sean.photo_insearch.fragment.ProductOrderHistoryFragment;
import idv.sean.photo_insearch.fragment.QAFragment;
import idv.sean.photo_insearch.fragment.ReportFragment;
import idv.sean.photo_insearch.fragment.UserChatFragment;
import idv.sean.photo_insearch.fragment.PhotoFragment;
import idv.sean.photo_insearch.fragment.ProductFragment;
import idv.sean.photo_insearch.model.Page;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    List<Page> pageList;

    public MyPagerAdapter(FragmentManager fm, int pagerCode) {
        super(fm);
        pageList = new ArrayList<>();
        switch (pagerCode) {
            case MainActivity.PAGER_HOME:
                pageList.add(new Page(new PhotoFragment(), "Photos"));
                pageList.add(new Page(new CaseFragment(), "Cases"));
                pageList.add(new Page(new ProductFragment(), "Products"));
                break;

            case MainActivity.PAGER_MEM:
                pageList.add(new Page(new MemberDetailFragment(), "Detail"));
                pageList.add(new Page(new UserChatFragment(), "Chat"));
                pageList.add(new Page(new MyCaseTypeFragment(), "MyCases"));
                break;

            case MainActivity.PAGER_ORDER:
                pageList.add(new Page(new ProductOrderHistoryFragment(), "Product"));
                break;

            case MainActivity.PAGER_NEWS:
                pageList.add(new Page(new NewsFragment(), "News"));
                break;

            case MainActivity.PAGER_QA:
                pageList.add(new Page(new QAFragment(), "Q & A"));
                break;

            case MainActivity.PAGER_ABOUT_US:
                pageList.add(new Page(new AboutUsFragment(), "AboutUs"));
                break;

            case MainActivity.PAGER_REPORT:
                pageList.add(new Page(new ReportFragment(), "Report"));
                break;
            default:
                break;
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

    public void clearAll() {
        pageList.clear();
    }
}


