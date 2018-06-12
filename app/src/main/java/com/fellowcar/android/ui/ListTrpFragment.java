package com.fellowcar.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fellowcar.android.R;

import java.util.ArrayList;
import java.util.List;

public class ListTrpFragment extends Fragment {
    private boolean emptyDataFlag = true;

    public ListTrpFragment() {
    }

    public static ListTrpFragment newInstance(boolean isEmptyData) {
        ListTrpFragment fragment = new ListTrpFragment();
        Bundle args = new Bundle();
        args.putBoolean("isEmptyData", isEmptyData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emptyDataFlag = getArguments().getBoolean("isEmptyData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_trp, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        if (emptyDataFlag) {
            adapter.addFragment(EmptyDataFragment.newInstance
                            (getString(R.string.title_empty_data_active_tab),
                                    getString(R.string.subtitle_empty_data)),
                    getString(R.string.title_tab_active));
            adapter.addFragment(EmptyDataFragment.newInstance
                            (getString(R.string.title_empty_data_expired_tab),
                                    getString(R.string.subtitle_empty_data)),
                    getString(R.string.title_tab_expired));
        } else {
            adapter.addFragment(ModelTripsFragment.newInstance(),
                    getResources().getString(R.string.title_tab_active));
            adapter.addFragment(ModelTripsFragment.newInstance(),
                    getResources().getString(R.string.title_tab_expired));
        }

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
