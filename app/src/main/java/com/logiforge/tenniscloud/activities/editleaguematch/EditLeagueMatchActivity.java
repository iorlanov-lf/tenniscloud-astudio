package com.logiforge.tenniscloud.activities.editleaguematch;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.facility.FacilityActivity;
import com.logiforge.tenniscloud.activities.facility.MatchFacilityDlg;
import com.logiforge.tenniscloud.activities.facility.PlayerFacilitiesFragment;
import com.logiforge.tenniscloud.activities.facility.UserFacilityDlg;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.facades.TCUserFacade;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.PlayingLevel;

import java.util.ArrayList;
import java.util.List;

public class EditLeagueMatchActivity extends AppCompatActivity
        implements MatchFacilityDlg.OnFacilitySelectedListener,
        View.OnClickListener,
        MatchFragment.MatchFragmentContext,
        PlayersFragment.PlayersFragmentContext,
        ScheduleFragment.ScheduleFragmentContext,
        AvailabilityDlg.OnAvailabilityActionListener {

    static final String MATCH_FRAGMENT_TAG_KEY = "matchFragmentTag";
    static final String PLAYERS_FRAGMENT_TAG_KEY = "playersFragmentTag";
    static final String SCHEDULE_FRAGMENT_TAG_KEY = "scheduleFragmentTag";

    static final int REQUEST_SELECT_FACILITY = 20;

    public static void initStaticData(Match match) {

        EditLeagueMatchState.initInstance(match);
    }

    TextView providerTextView;
    TextView leagueTextView;
    TabLayout tabLayout;
    ViewPager viewPager;
    EditLeagueMatchPagerAdapter pagerAdapter;

    String matchFragmentTag;
    String playersFragmentTag;
    String scheduleFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_editleaguematch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        providerTextView = (TextView) findViewById(R.id.txt_provider);
        leagueTextView = (TextView) findViewById(R.id.txt_league);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.container);

        pagerAdapter = new EditLeagueMatchPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(EditLeagueMatchPagerAdapter.PAGE_COUNT);
        tabLayout.setupWithViewPager(viewPager);

        if(savedInstanceState != null) {
            matchFragmentTag = savedInstanceState.getString(MATCH_FRAGMENT_TAG_KEY);
            playersFragmentTag = savedInstanceState.getString(PLAYERS_FRAGMENT_TAG_KEY);
            scheduleFragmentTag = savedInstanceState.getString(SCHEDULE_FRAGMENT_TAG_KEY);
        }

        formatHeader();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(MATCH_FRAGMENT_TAG_KEY, matchFragmentTag);
        savedInstanceState.putString(PLAYERS_FRAGMENT_TAG_KEY, playersFragmentTag);
        savedInstanceState.putString(SCHEDULE_FRAGMENT_TAG_KEY, scheduleFragmentTag);
    }

    public void onCancel(final View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onSave(final View view) {
        MatchFragment matchFragment =
                (MatchFragment)getSupportFragmentManager().findFragmentByTag(matchFragmentTag);
        PlayersFragment playersFragment =
                (PlayersFragment)getSupportFragmentManager().findFragmentByTag(playersFragmentTag);

        if(!matchFragment.validate()) {
            Toast.makeText(this, "There are errors on Match page", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!playersFragment.validate()) {
            Toast.makeText(this, "There are errors on Players page", Toast.LENGTH_SHORT).show();
            return;
        }

        Match match = EditLeagueMatchState.instance().getMatch();
        Match updatedMatch = EditLeagueMatchState.instance().getUpdatedMatch();

        matchFragment.populateMatch(updatedMatch);
        playersFragment.populateMatch(updatedMatch);

        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        if(matchFacade.updateLeagueMatch(this, EditLeagueMatchState.instance())) {
            EditLeagueMatchState editState = EditLeagueMatchState.instance();
            editState = EditLeagueMatchState.initInstance(editState.getUpdatedMatch());
            editState.setMatchChanged(true);

            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Unable to update match", Toast.LENGTH_SHORT);
        }
    }

    public void onDeleteMatch(final View view) {
        Match match = EditLeagueMatchState.instance().getMatch();

        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        if(matchFacade.unsubscribeFromLeagueMatch(this, match)) {
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Unable to delete match", Toast.LENGTH_SHORT);
        }
    }

    public void onSelectFacility(View v) {
        Match match = EditLeagueMatchState.instance().getMatch();

        FragmentManager fm = getSupportFragmentManager();
        MatchFacilityDlg matchFacilityDlg = MatchFacilityDlg.newInstance();
        PlayerFacilitiesFragment.initStaticData(match);
        matchFacilityDlg.show(fm, MatchFacilityDlg.DLG_TAG);
    }

    @Override
    public void onFacilitySelected(Facility facility) {
        MatchFragment matchFragment =
                (MatchFragment)getSupportFragmentManager().findFragmentByTag(matchFragmentTag);
        matchFragment.setFacility(facility);
        dismissMatchFacilityDlg();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.new_facility) {
            dismissMatchFacilityDlg();
            Intent intent = new Intent(this, FacilityActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_FACILITY);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_SELECT_FACILITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Facility facility = FacilityActivity.facility;
                MatchFragment matchFragment =
                        (MatchFragment)getSupportFragmentManager().findFragmentByTag(matchFragmentTag);
                matchFragment.setFacility(facility);
            }

            dismissMatchFacilityDlg();
        }
    }

    @Override
    public void setMatchFragmentTag(String tag) {
        matchFragmentTag = tag;
    }

    @Override
    public void setPlayersFragmentTag(String tag) {
        playersFragmentTag = tag;
    }

    @Override
    public void setScheduleFragmentTag(String tag) {
        scheduleFragmentTag = tag;
    }

    @Override
    public void onDeleteAvailability(String availabilityId) {
        ScheduleFragment scheduleFragment =
                (ScheduleFragment)getSupportFragmentManager().findFragmentByTag(scheduleFragmentTag);
        scheduleFragment.deleteAvailability(availabilityId);
    }

    @Override
    public void onAddAvailability(MatchAvailability availability) {
        ScheduleFragment scheduleFragment =
                (ScheduleFragment)getSupportFragmentManager().findFragmentByTag(scheduleFragmentTag);
        scheduleFragment.addAvailability(availability);
    }

    @Override
    public void onUpdateAvailability(MatchAvailability availability) {
        ScheduleFragment scheduleFragment =
                (ScheduleFragment)getSupportFragmentManager().findFragmentByTag(scheduleFragmentTag);
        scheduleFragment.updateAvailability(availability);
    }

    private void formatHeader() {
        Match match = EditLeagueMatchState.instance().getMatch();

        PlayingLevel level = match.getLeagueFlight().getPlayingLevel();
        League league = match.getLeagueFlight().getLeague();
        LeagueProvider provider = league.getLeagueMetroArea().getProvider();
        providerTextView.setText(provider.getProviderName());
        leagueTextView.setText(league.getLeagueName() + " (" + level.getDescription() + ")");
    }

    private void dismissMatchFacilityDlg() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(MatchFacilityDlg.DLG_TAG);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    static public class EditLeagueMatchPagerAdapter extends FragmentPagerAdapter {
        static public final int PAGE_COUNT = 3;
        static private String TAB_TITLES[] = new String[] { "Match", "Players", "Schedule" };

        public EditLeagueMatchPagerAdapter(FragmentManager fm) {
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
