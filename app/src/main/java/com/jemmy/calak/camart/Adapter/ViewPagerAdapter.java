package com.example.jemmycalak.thisismymarket.Adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.jemmycalak.thisismymarket.MainActivity;
import com.example.jemmycalak.thisismymarket.fragment.FragmentCategory;
import com.example.jemmycalak.thisismymarket.fragment.FragmentProduct;

import java.lang.ref.WeakReference;

/**
 * Created by Jemmy Calak on 5/25/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Activity activity;
    Fragment fragment =null;

    //registration fragment
    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public ViewPagerAdapter(Activity mainActivity, FragmentManager fm) {
        super(fm);
        this.activity =mainActivity;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                fragment = new FragmentCategory();
                break;
            case 1:
                fragment = new FragmentProduct();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Category";
        }
        else if (position == 1)
        {
            title = "Product";
        }
        return title;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //registration fragment
        Fragment fragment = (Fragment)super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //registration fragment
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegistrasionFragment(int position) {
        //registration fragment
        return registeredFragments.get(position);
    }
}
