package com.logiforge.tenniscloud.activities.facility;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.facades.FacilityFacade;
import com.logiforge.tenniscloud.model.Facility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/12/2017.
 */
public class MatchFacilityDlg extends DialogFragment
        implements PlayerFacilitiesFragment.OnPlayerFacilitySelectedListener,
        FacilityLookupFragment.OnLookupFacilitySelectedListener {

    public interface OnFacilitySelectedListener {
        void onFacilitySelected(Facility facility);
    }

    public static final String DLG_TAG = "dlg_matchfacility";

    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    public MatchFacilityDlg() {

    }

    public static MatchFacilityDlg newInstance() {
        MatchFacilityDlg frag = new MatchFacilityDlg();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_matchfacility, container);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager = (ViewPager) view.findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


        Button newFacilityBtn = (Button)view.findViewById(R.id.new_facility);
        newFacilityBtn.setOnClickListener((View.OnClickListener)getActivity());

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onPlayerFacilitySelected(Facility facility) {
        OnFacilitySelectedListener listener = (OnFacilitySelectedListener)getActivity();
        listener.onFacilitySelected(facility);
    }

    @Override
    public void onLookupFacilitySelected(Facility facility) {
        OnFacilitySelectedListener listener = (OnFacilitySelectedListener)getActivity();
        listener.onFacilitySelected(facility);
    }

    public static class PagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String TAB_TITLES[] = new String[] { "Player Facilities", "Lookup" };

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new PlayerFacilitiesFragment();
            } else {
                return new FacilityLookupFragment();
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }
}
