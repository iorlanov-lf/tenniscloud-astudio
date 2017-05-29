package com.logiforge.tenniscloud.activities.editleaguematch;

import android.app.Activity;
import android.support.design.widget.TabLayout;
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
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.facades.TCUserFacade;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.PlayingLevel;

import java.util.ArrayList;
import java.util.List;

public class EditLeagueMatchActivity extends AppCompatActivity {
    protected static Match match;
    public static void initStaticData(Match match) {
        LeagueMatchFacade.Builder matchBuilder = new LeagueMatchFacade.Builder(match);
        EditLeagueMatchActivity.match =
                matchBuilder.resolvePlayers().resolveLeagueData().build();
    }

    private TextView providerTextView;
    private TextView leagueTextView;
    private TabLayout tabLayout;
    private MatchFragment matchFragment;
    private PlayersFragment playersFragment;
    private ScheduleFragment scheduleFragment;

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
    }

    public void onCancel(final View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onSave(final View view) {
        // match attributes
        if(matchFragment != null) {
            if(matchFragment.validate()) {
                match.setLeagueWeek(matchFragment.getMatchWeek());
                match.setDeadlineDt(matchFragment.getDeadlineDt());
                match.setScheduledDt(matchFragment.getScheduledDt());
                match.setScheduledTm(matchFragment.getScheduledTm());
                match.setOutcome(matchFragment.getOutcome());
                Integer[] points = matchFragment.getPoints();
                match.setPoints(points);
            } else {
                return;
            }
        }

        // players
        List<MatchPlayer> playersToUpdate = new ArrayList<MatchPlayer>();
        if(matchFragment != null) {
            TCUserFacade userFacade = new TCUserFacade();
            MatchPlayer me = match.findPlayerByUserId(userFacade.getSelf().id);
            Match.HomeAway homeAway = matchFragment.getHomeAway();
            if ((me.getHomeTeam() && homeAway == Match.HomeAway.AwayMatch) ||
                    (!me.getHomeTeam() && homeAway == Match.HomeAway.HomeMatch)) {
                List<MatchPlayer> players = match.getPlayers();
                if (players != null) {
                    for (MatchPlayer player : players) {
                        player.setHomeTeam(!player.getHomeTeam());
                        playersToUpdate.add(player);
                    }
                }
            }
        }

        // scores
        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        if(matchFacade.updateLeagueMatch(this, match, playersToUpdate)) {
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Unable to update match", Toast.LENGTH_SHORT);
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
                matchFragment = new MatchFragment();
                return matchFragment;
            } else if(position == 1) {
                playersFragment = new PlayersFragment();
                return playersFragment;
            } else  {
                scheduleFragment = new ScheduleFragment();
                return scheduleFragment;
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
