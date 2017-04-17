package com.logiforge.tenniscloud.activities.dashboard;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.logiforge.tenniscloud.R;

/**
 * Created by iorlanov on 1/28/2017.
 */
public class DashboardPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String TAB_TITLES[] = new String[] { "Matches", "Leagues" };
    private int IMAGE_RES_ID[] = new int[] {R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery};
    private Context context;

    public DashboardPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MatchListFragment.newInstance(null, null);

            case 1:
                return LeagueListFragment.newInstance(null, null);

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        /*
        Drawable image = context.getResources().getDrawable(IMAGE_RES_ID[position]);
        int w = image.getIntrinsicWidth();
        int h = image.getIntrinsicHeight();
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("   " + TAB_TITLES[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
        */
        return TAB_TITLES[position];
    }
}
