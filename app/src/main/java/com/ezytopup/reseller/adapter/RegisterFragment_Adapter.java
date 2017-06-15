package com.ezytopup.reseller.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;

import com.ezytopup.reseller.fragment.FavoriteFragment;
import com.ezytopup.reseller.fragment.HistoryFragment;
import com.ezytopup.reseller.fragment.HomeFragment;
import com.ezytopup.reseller.fragment.SearchFragment;


/**
 * Created by indraaguslesmana on 3/9/17.
 */

public class RegisterFragment_Adapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Home", "Popular", "History", "Search"}; //TODO temporary hardcode
    private Context mContext;

    private static final int HOME = 0;
    private static final int POPULAR = 1;
    private static final int HISTORY = 2;
    private static final int SEARCH = 3;

    public RegisterFragment_Adapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HOME:
                return new HomeFragment();
            case POPULAR:
                return new FavoriteFragment();
            case HISTORY:
                return new HistoryFragment();
            case SEARCH:
                return new SearchFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SpannableString sb = new SpannableString(tabTitles[position]);
        sb.setSpan(null, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
