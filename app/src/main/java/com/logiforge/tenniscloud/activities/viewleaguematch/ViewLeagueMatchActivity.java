package com.logiforge.tenniscloud.activities.viewleaguematch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.editleaguematch.EditLeagueMatchActivity;
import com.logiforge.tenniscloud.activities.editleaguematch.EditLeagueMatchState;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.PlayingLevel;

public class ViewLeagueMatchActivity extends AppCompatActivity {
    private static final int REQUEST_EDIT = 10;

    static final String MATCH_FRAGMENT_TAG_KEY = "matchFragmentTag";
    static final String PLAYERS_FRAGMENT_TAG_KEY = "playersFragmentTag";
    static final String SCHEDULE_FRAGMENT_TAG_KEY = "scheduleFragmentTag";

    public static void initStaticData(Match match) {
        EditLeagueMatchState.initInstance(match);
    }

    private TextView providerTextView;
    private TextView leagueTextView;
    private TabLayout tabLayout;

    public String matchFragmentTag;
    public String playersFragmentTag;
    public String scheduleFragmentTag;

    private ViewLeagueMatchPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_viewleaguematch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        providerTextView = (TextView) findViewById(R.id.txt_provider);
        leagueTextView = (TextView) findViewById(R.id.txt_league);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        Match match = EditLeagueMatchState.instance().getMatch();

        PlayingLevel level = match.getLeagueFlight().getPlayingLevel();
        League league = match.getLeagueFlight().getLeague();
        LeagueProvider provider = league.getLeagueMetroArea().getProvider();
        providerTextView.setText(provider.getProviderName());
        leagueTextView.setText(league.getLeagueName() + " (" + level.getDescription() + ")");

        // Fragments
        pagerAdapter = new ViewLeagueMatchPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(ViewLeagueMatchPagerAdapter.PAGE_COUNT);
        tabLayout.setupWithViewPager(viewPager);

        if(savedInstanceState != null) {
            matchFragmentTag = savedInstanceState.getString(MATCH_FRAGMENT_TAG_KEY);
            playersFragmentTag = savedInstanceState.getString(PLAYERS_FRAGMENT_TAG_KEY);
            scheduleFragmentTag = savedInstanceState.getString(SCHEDULE_FRAGMENT_TAG_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(MATCH_FRAGMENT_TAG_KEY, matchFragmentTag);
        savedInstanceState.putString(PLAYERS_FRAGMENT_TAG_KEY, playersFragmentTag);
        savedInstanceState.putString(SCHEDULE_FRAGMENT_TAG_KEY, scheduleFragmentTag);
    }

    public void onDone(final View view) {
        if(EditLeagueMatchState.instance().isMatchChanged()) {
            setResult(Activity.RESULT_OK);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }
        finish();
    }

    public void onEdit(final View view) {
        Intent intent = new Intent(this, EditLeagueMatchActivity.class);
        startActivityForResult(intent, REQUEST_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT) {
            if(resultCode == RESULT_OK) {
                populateControls();
            }
        }
    }

    private void populateControls() {
        MatchFragment matchFragment =
                (MatchFragment)getSupportFragmentManager().findFragmentByTag(matchFragmentTag);
        matchFragment.populateControls();
        PlayersFragment playersFragment =
                (PlayersFragment)getSupportFragmentManager().findFragmentByTag(playersFragmentTag);
        playersFragment.populateControls();
        ScheduleFragment scheduleFragment =
                (ScheduleFragment)getSupportFragmentManager().findFragmentByTag(scheduleFragmentTag);
        scheduleFragment.populateControls();
    }

    public class ViewLeagueMatchPagerAdapter extends FragmentPagerAdapter {
        static final int PAGE_COUNT = 3;
        final private String TAB_TITLES[] = new String[] { "Match", "Players", "Schedule" };

        public ViewLeagueMatchPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return new MatchFragment();
            } else if(position == 1) {
                return new PlayersFragment();
            } else  {
                return new ScheduleFragment();
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
