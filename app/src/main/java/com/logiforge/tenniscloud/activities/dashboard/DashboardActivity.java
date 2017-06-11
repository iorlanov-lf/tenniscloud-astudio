package com.logiforge.tenniscloud.activities.dashboard;

import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.editleaguematch.EditLeagueMatchPrologActivity;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
            View.OnClickListener {

    public static final int REQUEST_NEW_LEAGUE_MATCH = 10;
    public static final int REQUEST_VIEW_LEAGUE_MATCH = 20;

    static final String MATCH_LIST_FRAGMENT_TAG_KEY = "matchListFragmentTag";
    static final String LEAGUE_LIST_FRAGMENT_TAG_KEY = "leagueListFragmentTag";

    public String matchListFragmentTag;
    public String leagueListFragmentTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // pager setup
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new DashboardPagerAdapter(getSupportFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        if(savedInstanceState != null) {
            matchListFragmentTag = savedInstanceState.getString(MATCH_LIST_FRAGMENT_TAG_KEY);
            leagueListFragmentTag = savedInstanceState.getString(LEAGUE_LIST_FRAGMENT_TAG_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(MATCH_LIST_FRAGMENT_TAG_KEY, matchListFragmentTag);
        savedInstanceState.putString(LEAGUE_LIST_FRAGMENT_TAG_KEY, leagueListFragmentTag);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab) {
            FragmentManager fm = getSupportFragmentManager();
            MatchTypeDlg matchTypeDlg = MatchTypeDlg.newInstance();
            matchTypeDlg.show(fm, "act_dashboard_dlg_add");
        } else if(view.getId() == R.id.new_league_match) {
            DialogFragment matchTypeDlg = (DialogFragment)getSupportFragmentManager().findFragmentByTag("act_dashboard_dlg_add");
            if(matchTypeDlg != null) {
                matchTypeDlg.dismiss();
            }
            EditLeagueMatchPrologActivity.initStaticData();
            Intent intent = new Intent(this, EditLeagueMatchPrologActivity.class);
            startActivityForResult(intent, REQUEST_NEW_LEAGUE_MATCH);
        } else if(view.getId() == R.id.new_friendly_match) {
            DialogFragment matchTypeDlg = (DialogFragment)getSupportFragmentManager().findFragmentByTag("act_dashboard_dlg_add");
            if(matchTypeDlg != null) {
                matchTypeDlg.dismiss();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_LEAGUE_MATCH) {
            if(resultCode == RESULT_OK) {
                MatchListFragment matchListFragment =
                        (MatchListFragment)getSupportFragmentManager().findFragmentByTag(matchListFragmentTag);
                matchListFragment.refresh();
            }
        } else if (requestCode == REQUEST_VIEW_LEAGUE_MATCH) {
            if(resultCode == RESULT_OK) {
                MatchListFragment matchListFragment =
                        (MatchListFragment)getSupportFragmentManager().findFragmentByTag(matchListFragmentTag);
                matchListFragment.refresh();
            }
        }
    }

    public class DashboardPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String TAB_TITLES[] = new String[] { "Matches", "Leagues" };

        public DashboardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MatchListFragment();

                case 1:
                    return new LeagueListFragment();

                default:
                    return null;
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
