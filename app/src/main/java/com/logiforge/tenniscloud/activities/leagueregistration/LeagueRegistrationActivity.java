package com.logiforge.tenniscloud.activities.leagueregistration;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbTransaction;
import com.logiforge.lavolta.android.db.InstallationTable;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.lavolta.android.model.Installation;
import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.facility.FacilityActivity;
import com.logiforge.tenniscloud.activities.facility.UserFacilityDlg;
import com.logiforge.tenniscloud.activities.util.ItemPickerDialogFragment;
import com.logiforge.tenniscloud.db.FacilityTbl;
import com.logiforge.tenniscloud.db.LeagueFlightTbl;
import com.logiforge.tenniscloud.db.LeagueMetroAreaTbl;
import com.logiforge.tenniscloud.db.LeagueProfileTbl;
import com.logiforge.tenniscloud.db.LeagueProviderTbl;
import com.logiforge.tenniscloud.db.LeagueRegistrationTbl;
import com.logiforge.tenniscloud.db.LeagueTbl;
import com.logiforge.tenniscloud.db.PlayingLevelTbl;
import com.logiforge.tenniscloud.db.TCLavoltaDb;
import com.logiforge.tenniscloud.db.TCUserFacilityTbl;
import com.logiforge.tenniscloud.db.TCUserTbl;
import com.logiforge.tenniscloud.facades.FacilityFacade;
import com.logiforge.tenniscloud.facades.LeagueRegistrationFacade;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueFlight;
import com.logiforge.tenniscloud.model.LeagueMetroArea;
import com.logiforge.tenniscloud.model.LeagueProfile;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.PlayingLevel;
import com.logiforge.tenniscloud.model.TCUser;


public class LeagueRegistrationActivity extends AppCompatActivity
        implements ItemPickerDialogFragment.OnItemSelectedListener,
        UserFacilityDlg.OnItemSelectedListener,
        View.OnClickListener {
    private static final String TAG = LeagueRegistrationActivity.class.getSimpleName();

    private static final int REQUEST_SELECT_FACILITY = 20;

    private static LeagueProvider provider;
    private static LeagueMetroArea metroArea;
    private static LeagueProfile profile;
    private static League league;
    private static PlayingLevel level;
    private static Facility facility;
    public static LeagueRegistration registration;

    public static void initState() {
        provider = null;
        metroArea = null;
        profile = null;
        league = null;
        level = null;
        facility = null;
        registration = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leaguereg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // provider name
        if(provider != null) {
            TextView providerTextView = (TextView) findViewById(R.id.txt_provider);
            providerTextView.setText(provider.getProviderName());

            LinearLayout metroAreaLayout = (LinearLayout)findViewById(R.id.layout_metroArea);
            metroAreaLayout.setVisibility(View.VISIBLE);
        }

        // metro area name
        if(metroArea != null) {
            TextView metroAreaTextView = (TextView) findViewById(R.id.txt_metroArea);
            metroAreaTextView.setText(metroArea.getMetroAreaName());

            LinearLayout leagueLayout = (LinearLayout)findViewById(R.id.layout_league);
            leagueLayout.setVisibility(View.VISIBLE);
        }

        // profile
        if(profile != null && profile.id == null) {
            LinearLayout profileLayout = (LinearLayout)findViewById(R.id.layout_profile);
            profileLayout.setVisibility(View.VISIBLE);

            EditText emailEditText = (EditText) findViewById(R.id.edit_email);
            emailEditText.setText(profile.getEmail());
            emailEditText.setError(null);
            EditText displayNameEditText = (EditText) findViewById(R.id.edit_displayName);
            displayNameEditText.setText(profile.getDisplayName());
            displayNameEditText.setError(null);
            EditText phoneEditText = (EditText) findViewById(R.id.edit_phone);
            phoneEditText.setText(profile.getPhoneNumber());
            phoneEditText.setError(null);
        }

        // league
        if(league != null) {
            TextView leagueTextView = (TextView) findViewById(R.id.txt_league);
            leagueTextView.setText(league.getLeagueName());

            LinearLayout levelLayout = (LinearLayout)findViewById(R.id.layout_level);
            levelLayout.setVisibility(View.VISIBLE);
        }

        // level
        if(level != null) {
            TextView leagueTextView = (TextView) findViewById(R.id.txt_level);
            leagueTextView.setText(level.getDescription());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        LinearLayout profileLayout = (LinearLayout)findViewById(R.id.layout_profile);
        if(profileLayout.getVisibility() == View.VISIBLE) {
            EditText emailEditText = (EditText) findViewById(R.id.edit_email);
            EditText displayNameEditText = (EditText) findViewById(R.id.edit_displayName);
            EditText phoneEditText = (EditText) findViewById(R.id.edit_phone);

            if(profile == null) {
                profile = new LeagueProfile();
            }

            profile.setEmail(emailEditText.getText().toString());
            profile.setDisplayName(displayNameEditText.getText().toString());
            profile.setPhoneNumber(phoneEditText.getText().toString());
        }
    }

    public void onSelectProvider(final View view) {
        LeagueProviderTbl lpTbl = new LeagueProviderTbl();
        List<LeagueProvider> providers = lpTbl.getAll();

        ArrayList<ItemPickerDialogFragment.Item> pickerItems = new ArrayList<>();
        for(LeagueProvider lp : providers) {
            pickerItems.add(new ItemPickerDialogFragment.Item(lp.getProviderName(), lp.id));
        }

        ItemPickerDialogFragment dialog = ItemPickerDialogFragment.newInstance(
                "League Providers",
                pickerItems,
                -1
        );
        dialog.show(getFragmentManager(), "ProviderPicker");
    }

    public void onSelectMetroArea(final View view) {
        LeagueMetroAreaTbl maTbl = new LeagueMetroAreaTbl();
        List<LeagueMetroArea> metroAreas = maTbl.getByProviderId(provider.id);

        ArrayList<ItemPickerDialogFragment.Item> pickerItems = new ArrayList<>();
        for(LeagueMetroArea ma : metroAreas) {
            pickerItems.add(new ItemPickerDialogFragment.Item(ma.getMetroAreaName(), ma.id));
        }

        ItemPickerDialogFragment dialog = ItemPickerDialogFragment.newInstance(
                "Metro Areas",
                pickerItems,
                -1
        );
        dialog.show(getFragmentManager(), "MetroAreaPicker");
    }

    public void onSelectLeague(final View view) {
        LeagueTbl leagueTbl = new LeagueTbl();
        List<League> leagues = leagueTbl.getActiveLeagues(metroArea.id);

        ArrayList<ItemPickerDialogFragment.Item> pickerItems = new ArrayList<>();
        for(League league : leagues) {
            pickerItems.add(new ItemPickerDialogFragment.Item(league.getLeagueName(), league.id));
        }

        ItemPickerDialogFragment dialog = ItemPickerDialogFragment.newInstance(
                "Leagues",
                pickerItems,
                -1
        );
        dialog.show(getFragmentManager(), "LeaguePicker");
    }

    public void onSelectLevel(final View view) {
        PlayingLevelTbl levelTbl = new PlayingLevelTbl();
        List<PlayingLevel> levels = levelTbl.getLevelsByProviderId(provider.id);

        ArrayList<ItemPickerDialogFragment.Item> pickerItems = new ArrayList<>();
        for(PlayingLevel level : levels) {
            pickerItems.add(new ItemPickerDialogFragment.Item(level.getDescription(), level.id));
        }

        ItemPickerDialogFragment dialog = ItemPickerDialogFragment.newInstance(
                "Levels",
                pickerItems,
                -1
        );
        dialog.show(getFragmentManager(), "LevelPicker");
    }

    public void onSelectFacility(final View view) {
        FacilityFacade facilityFacade = new FacilityFacade();
        if(facilityFacade.hasFacility()) {
            FragmentManager fm = getSupportFragmentManager();
            UserFacilityDlg userFacilityDlg = UserFacilityDlg.newInstance();
            userFacilityDlg.show(fm, UserFacilityDlg.DLG_TAG);
        } else {
            Intent intent = new Intent(this, FacilityActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_FACILITY);
        }
    }

    public void onSave(final View view) {
        //Toast.makeText(this, "onSave", Toast.LENGTH_LONG).show();
        boolean isFormValid = true;
        if(provider == null) {
            TextView providerTextView = (TextView)findViewById(R.id.txt_provider);
            providerTextView.setError("Required!");
            isFormValid = false;
        } else if(metroArea == null) {
            TextView metroAreaTextView = (TextView)findViewById(R.id.txt_metroArea);
            metroAreaTextView.setError("Required!");
            metroAreaTextView.requestFocus();
            isFormValid = false;
        } else if(league == null) {
            TextView leagueTextView = (TextView) findViewById(R.id.txt_league);
            leagueTextView.setError("Required!");
            leagueTextView.requestFocus();
            isFormValid = false;
        } else if(level == null) {
            TextView levelTextView = (TextView)findViewById(R.id.txt_level);
            levelTextView.setError("Required!");
            levelTextView.requestFocus();
            isFormValid = false;
        } else if(facility == null) {
            TextView facilityTextView = (TextView)findViewById(R.id.txt_facility);
            facilityTextView.setError("Required!");
            facilityTextView.requestFocus();
            isFormValid = false;
        }

        if(profile != null && profile.id == null) {
            EditText etEmail = (EditText)findViewById(R.id.edit_email);
            profile.setEmail(etEmail.getText().toString());
            if(etEmail.getText().length() == 0) {
                etEmail.setError("Required!");
                etEmail.requestFocus();
                isFormValid = false;
            }

            EditText etDisplayName = (EditText)findViewById(R.id.edit_displayName);
            profile.setDisplayName(etDisplayName.getText().toString());
            if(etDisplayName.getText().length() == 0) {
                etDisplayName.setError("Required!");
                etDisplayName.requestFocus();
                isFormValid = false;
            }

            EditText etPhone = (EditText)findViewById(R.id.edit_phone);
            profile.setPhoneNumber(etPhone.getText().toString());
            if(etPhone.getText().length() == 0) {
                etPhone.setError("Required!");
                etPhone.requestFocus();
                isFormValid = false;
            }
        }

        if(isFormValid) {
            LeagueRegistrationFacade regFacade = new LeagueRegistrationFacade();
            registration = regFacade.createLeagueRegistration(provider, metroArea, league, level, profile, facility);

            if(registration != null) {
                setResult(Activity.RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Unable to create a new league registration", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onCancel(final View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onItemSelected(ItemPickerDialogFragment fragment, ItemPickerDialogFragment.Item item, int index) {
        if(fragment.getTag().equals("ProviderPicker")) {
            if(provider != null && provider.id.equals(item.getStringValue())) {
                return;
            }

            String selectedTitle = item.getTitle();
            TextView providerTextView = (TextView)findViewById(R.id.txt_provider);
            providerTextView.setText(selectedTitle);
            providerTextView.setError(null);

            provider = new LeagueProvider();
            provider.id = item.getStringValue();
            provider.setProviderName(selectedTitle);

            LinearLayout metroAreaLayout = (LinearLayout)findViewById(R.id.layout_metroArea);
            metroAreaLayout.setVisibility(View.VISIBLE);
            TextView metroAreaTextView = (TextView)findViewById(R.id.txt_metroArea);
            metroAreaTextView.setText("Select Metro Area");
            metroAreaTextView.setError(null);
            metroArea = null;

            LinearLayout profileLayout = (LinearLayout) findViewById(R.id.layout_profile);
            profileLayout.setVisibility(View.GONE);
            profile = null;

            LinearLayout leagueLayout = (LinearLayout)findViewById(R.id.layout_league);
            leagueLayout.setVisibility(View.GONE);
            league = null;

            LinearLayout levelLayout = (LinearLayout)findViewById(R.id.layout_level);
            levelLayout.setVisibility(View.GONE);
            level = null;

        } else if(fragment.getTag().equals("MetroAreaPicker")){
            if(metroArea != null && metroArea.id.equals(item.getStringValue())) {
                return;
            }

            String selectedTitle = item.getTitle();
            TextView metroAreaTextView = (TextView)findViewById(R.id.txt_metroArea);
            metroAreaTextView.setText(selectedTitle);
            metroAreaTextView.setError(null);

            metroArea = new LeagueMetroArea();
            metroArea.id = item.getStringValue();
            metroArea.setMetroAreaName(selectedTitle);

            LeagueProfileTbl profileTbl = new LeagueProfileTbl();
            LeagueProfile p = profileTbl.findByAreaId(metroArea.id);

            if(p == null) {
                profile = new LeagueProfile();

                LinearLayout profileLayout = (LinearLayout) findViewById(R.id.layout_profile);
                profileLayout.setVisibility(View.VISIBLE);

                TCUserTbl userTbl = new TCUserTbl();
                TCUser user = userTbl.getSelf();
                EditText etEmail = (EditText)findViewById(R.id.edit_email);
                etEmail.setText(user.getEmail());
                etEmail.setError(null);
                profile.setEmail(user.getEmail());
                EditText etDisplayName = (EditText)findViewById(R.id.edit_displayName);
                etDisplayName.setText(user.getDisplayName());
                etDisplayName.setError(null);
                profile.setDisplayName(user.getDisplayName());
                EditText etPhone = (EditText)findViewById(R.id.edit_phone);
                etPhone.setText(user.getPhoneNbr());
                etPhone.setError(null);
                profile.setEmail(user.getPhoneNbr());
            } else {
                profile = p;
                LinearLayout profileLayout = (LinearLayout) findViewById(R.id.layout_profile);
                profileLayout.setVisibility(View.GONE);

            }

            LinearLayout leagueLayout = (LinearLayout) findViewById(R.id.layout_league);
            leagueLayout.setVisibility(View.VISIBLE);
            TextView leagueTextView = (TextView) findViewById(R.id.txt_league);
            leagueTextView.setText("Select League");
            leagueTextView.setError(null);
            league = null;

            LinearLayout levelLayout = (LinearLayout)findViewById(R.id.layout_level);
            levelLayout.setVisibility(View.GONE);
            level = null;

        } else if(fragment.getTag().equals("LeaguePicker")){
            if(league != null && league.id.equals(item.getStringValue())) {
                return;
            }

            String selectedTitle = item.getTitle();
            TextView leagueTextView = (TextView)findViewById(R.id.txt_league);
            leagueTextView.setText(selectedTitle);
            leagueTextView.setError(null);

            league = new League();
            league.id = item.getStringValue();
            league.setLeagueName(selectedTitle);

            LinearLayout levelLayout = (LinearLayout)findViewById(R.id.layout_level);
            levelLayout.setVisibility(View.VISIBLE);
            TextView levelTextView = (TextView) findViewById(R.id.txt_level);
            levelTextView.setText("Select Level");
            levelTextView.setError(null);
            level = null;
        } else if(fragment.getTag().equals("LevelPicker")){
            if(level != null && level.id.equals(item.getStringValue())) {
                   return;
            }

            String selectedTitle = item.getTitle();
            TextView levelTextView = (TextView)findViewById(R.id.txt_level);
            levelTextView.setText(selectedTitle);
            levelTextView.setError(null);

            level = new PlayingLevel();
            level.id = item.getStringValue();
            level.setDescription(selectedTitle);
        }
    }

    @Override
    public void onItemSelected(Facility facility) {
        LeagueRegistrationActivity.facility = facility;
        TextView facilityTextView = (TextView)findViewById(R.id.txt_facility);
        facilityTextView.setText(facility.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_SELECT_FACILITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                facility = FacilityActivity.facility;
                TextView facilityTextView = (TextView)findViewById(R.id.txt_facility);
                facilityTextView.setText(facility.getName());
            }

            dismissUserFacilityDlg();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.new_facility) {
            dismissUserFacilityDlg();

            Intent intent = new Intent(this, FacilityActivity.class);
            startActivityForResult(intent, REQUEST_SELECT_FACILITY);
        }
    }

    private void dismissUserFacilityDlg() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(UserFacilityDlg.DLG_TAG);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }
}
