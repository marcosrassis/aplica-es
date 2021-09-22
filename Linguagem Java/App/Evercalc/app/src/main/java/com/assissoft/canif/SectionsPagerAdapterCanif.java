package com.assissoft.canif;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Marcos on 12/09/2016.
 *
 */
class SectionsPagerAdapterCanif extends FragmentStatePagerAdapter {

    private static Fragment fragment = null;
    private final Context context;

    SectionsPagerAdapterCanif(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        //return mFragments.get(position);

        if (position==0) fragment = new SimcalcFragmentCanif();

        if (position==1) fragment = new ConversorFragmentCanif();

        return fragment;
    }

    @Override
    public int getCount() {
        //return mFragments.size();
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return mFragmentTitles.get(position);

        switch (position) {
            case 0:
                return context.getResources().getString(R.string.tab1_label);
            case 1:
                return context.getResources().getString(R.string.tab2_label);
            default:
                return context.getResources().getString(R.string.tab1_label);
        }

    }

}