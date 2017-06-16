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
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.PlayingLevel;

import java.util.ArrayList;
import java.util.List;

public class EditLeagueMatchActivity extends AppCompatActivity
        implements MatchFacilityDlg.OnFacilitySelectedListener,
        View.OnClickListener {

    static final String MATCH_FRAGMENT_TAG_KEY = "matchFragmentTag";
    static final String PLAYERS_FRAGMENT_TAG_KEY = "playersFragmentTag";
    static final String SCHEDULE_FRAGMENT_TAG_KEY = "scheduleFragmentTag";

    private static final int REQUEST_SELECT_FACILITY = 20;

    protected static Match match;
    public static void initStaticData(Match match) {
        LeagueMatchFacade.Builder matchBuilder = new LeagueMatchFacade.Builder(match);
        EditLeagueMatchActivity.match =
                matchBuilder.resolvePlayers().resolveLeagueData().resolveFacility().build();
    }
    public static Match getUpdatedMatch() {
        return match;
    }

    private TextView providerTextView;
    private TextView leagueTextView;
    private TabLayout tabLayout;

    public String matchFragmentTag;
    public String playersFragmentTag;
    public String scheduleFragmentTag;

    private EditLeagueMatchPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_editleaguematch);

        providerTextView = (TextView) findViewById(R.id.txt_provider);
        leagueTextView = (TextView) findViewById(R.id.txt_league);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        PlayingLevel level = match.getLeagueFlight().getPlayingLevel();
        League league = match.getLeagueFlight().getLeague();
        LeagueProvider provider = league.getLeagueMetroArea().getProvider();
        providerTextView.setText(provider.getProviderName());
        leagueTextView.setText(league.getLeagueName() + " (" + level.getDescription() + ")");

        // Fragments
        pagerAdapter = new EditLeagueMatchPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
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

        LeagueMatchFacade matchFacade = new LeagueMatchFacade();

        Match updatedMatch = new Match(match);
        matchFragment.populateMatch(updatedMatch);
        playersFragment.populateMatch(updatedMatch);

        if(matchFacade.updateLeagueMatch(this, match, updatedMatch)) {
            match = updatedMatch;
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Unable to update match", Toast.LENGTH_SHORT);
        }
    }

    public void onDelete(final View view) {
        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        if(matchFacade.unsubscribeFromLeagueMatch(this, match)) {
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Unable to delete match", Toast.LENGTH_SHORT);
        }
    }

    public void onSelectFacility(View v) {

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

    private void dismissMatchFacilityDlg() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(MatchFacilityDlg.DLG_TAG);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    public class EditLeagueMatchPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String TAB_TITLES[] = new String[] { "Match", "Players", "Schedule" };

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
