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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivity;
import com.logiforge.tenniscloud.activities.leagueregistration.UserLeagueRegistrationDlg;
import com.logiforge.tenniscloud.activities.util.ItemPickerDialogFragment;
import com.logiforge.tenniscloud.facades.LeagueRegistrationFacade;
import com.logiforge.tenniscloud.model.LeagueRegistration;

import java.util.ArrayList;
import java.util.List;

public class EditLeagueMatchActivity extends AppCompatActivity
        implements View.OnClickListener,
        UserLeagueRegistrationDlg.OnItemSelectedListener,
        ItemPickerDialogFragment.OnItemSelectedListener {

    private static final int REQUEST_NEW_REGISTRATION = 20;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static LeagueRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_editleaguematch);

        LinearLayout layout = (LinearLayout)findViewById(R.id.l_form_header_middle);
        layout.setOnClickListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_editleaguematch_menu, menu);
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

    public void onCancel(final View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.l_form_header_middle) {
            LeagueRegistrationFacade regFacade = new LeagueRegistrationFacade();
            if(regFacade.hasRegistration()) {
                FragmentManager fm = getSupportFragmentManager();
                UserLeagueRegistrationDlg userRegistrationDlg = UserLeagueRegistrationDlg.newInstance();
                userRegistrationDlg.show(fm, UserLeagueRegistrationDlg.DLG_TAG);
            } else {
                Intent intent = new Intent(this, LeagueRegistrationActivity.class);
                LeagueRegistrationActivity.initState();
                startActivityForResult(intent, REQUEST_NEW_REGISTRATION);
            }
        } else if(v.getId() == R.id.new_registration) {
            dismissUserFacilityDlg();

            Intent intent = new Intent(this, LeagueRegistrationActivity.class);
            LeagueRegistrationActivity.initState();
            startActivityForResult(intent, REQUEST_NEW_REGISTRATION);
        } else {
            Toast.makeText(this, "onClick of activity", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(LeagueRegistration registration) {
        EditLeagueMatchActivity.registration = registration;
        TextView leagueTextView = (TextView)findViewById(R.id.league_description);
        leagueTextView.setText(registration.getProfile().getLeagueMetroArea().getProvider().getProviderName() + " - " + registration.getLeagueFlight().getLeague().getLeagueName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_NEW_REGISTRATION) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                registration = LeagueRegistrationActivity.registration;
                TextView leagueTextView = (TextView)findViewById(R.id.league_description);
                leagueTextView.setText(registration.getProfile().getLeagueMetroArea().getProvider().getProviderName() + " - " + registration.getLeagueFlight().getLeague().getLeagueName());
            }

            dismissUserFacilityDlg();
        }
    }

    private void dismissUserFacilityDlg() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(UserLeagueRegistrationDlg.DLG_TAG);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    public void onSelectHomeAway(final View view) {

        ArrayList<ItemPickerDialogFragment.Item> pickerItems = new ArrayList<>();
        pickerItems.add(new ItemPickerDialogFragment.Item("Home Match", "0"));
        pickerItems.add(new ItemPickerDialogFragment.Item("Away Match", "1"));

        ItemPickerDialogFragment dialog = ItemPickerDialogFragment.newInstance(
                "Home/Away",
                pickerItems,
                -1
        );
        dialog.show(getFragmentManager(), "HomeAwayPicker");
    }

    @Override
    public void onItemSelected(ItemPickerDialogFragment fragment, ItemPickerDialogFragment.Item item, int index) {
        TextView homeAwayTextView = (TextView)findViewById(R.id.txt_homeAway);
        homeAwayTextView.setText(item.getTitle());
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        final int PAGE_COUNT = 3;
        private String TAB_TITLES[] = new String[] { "Match", "Players", "Schedule" };

        public SectionsPagerAdapter(FragmentManager fm) {
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
            // Show 3 total pages.
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }
    }
}
