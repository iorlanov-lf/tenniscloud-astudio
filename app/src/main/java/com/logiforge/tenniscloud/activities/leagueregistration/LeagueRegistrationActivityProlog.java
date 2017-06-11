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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.facility.FacilityActivity;
import com.logiforge.tenniscloud.activities.facility.UserFacilityDlg;
import com.logiforge.tenniscloud.activities.util.ItemPickerDialogFragment;
import com.logiforge.tenniscloud.db.LeagueMetroAreaTbl;
import com.logiforge.tenniscloud.db.LeagueProfileTbl;
import com.logiforge.tenniscloud.db.LeagueProviderTbl;
import com.logiforge.tenniscloud.db.LeagueTbl;
import com.logiforge.tenniscloud.db.PlayingLevelTbl;
import com.logiforge.tenniscloud.facades.FacilityFacade;
import com.logiforge.tenniscloud.facades.LeagueRegistrationFacade;
import com.logiforge.tenniscloud.facades.TCUserFacade;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueMetroArea;
import com.logiforge.tenniscloud.model.LeagueProfile;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.Partner;
import com.logiforge.tenniscloud.model.PlayingLevel;
import com.logiforge.tenniscloud.model.TCUser;


public class LeagueRegistrationActivityProlog extends AppCompatActivity
        implements ItemPickerDialogFragment.OnItemSelectedListener,
        UserFacilityDlg.OnItemSelectedListener,
        View.OnClickListener {
    private static final String TAG = LeagueRegistrationActivityProlog.class.getSimpleName();

    private static final int REQUEST_SELECT_FACILITY = 20;
    private static final int REQUEST_REGISTRATION_EPILOG = 30;

    public static LeagueProvider provider;
    public static LeagueMetroArea metroArea;
    public static League league;
    public static PlayingLevel level;
    public static Facility facility;
    public static LeagueProfile profile;
    public static LeagueRegistration registration;

    TextView providerTextView;
    TextView metroAreaTextView;
    TextView leagueTextView;
    TextView levelTextView;
    TextView facilityTextView;

    public static void initState() {
        provider = null;
        metroArea = null;
        league = null;
        level = null;
        facility = null;
        profile = null;
        registration = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leaguereg_prolog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        providerTextView = (TextView)findViewById(R.id.txt_provider);
        metroAreaTextView = (TextView) findViewById(R.id.txt_metroArea);
        leagueTextView = (TextView) findViewById(R.id.txt_league);
        levelTextView = (TextView) findViewById(R.id.txt_level);
        facilityTextView = (TextView) findViewById(R.id.txt_facility);

        if(provider != null) {
            providerTextView.setText(provider.getProviderName());
        }

        if(metroArea != null) {
            metroAreaTextView.setText(metroArea.getMetroAreaName());
        } else if(provider != null) {
            metroAreaTextView.setText("Select Metro Area");
        }

        if(league != null) {
            leagueTextView.setText(league.getLeagueName());
        } else if(metroArea != null) {
            metroAreaTextView.setText("Select League");
        }

        if(level != null) {
            leagueTextView.setText(level.getDescription());
        } else if(league != null) {
            metroAreaTextView.setText("Select League");
        }

        if(facility != null) {
            facilityTextView.setText(facility.getName());
        }

        setNextButtonText();
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
        if(provider == null) {
            Toast.makeText(this, "Select League Provider", Toast.LENGTH_SHORT).show();
            return;
        }

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
        if(provider == null) {
            Toast.makeText(this, "Select League Provider", Toast.LENGTH_SHORT).show();
            return;
        }

        if(metroArea == null) {
            Toast.makeText(this, "Select Metro Area", Toast.LENGTH_SHORT).show();
            return;
        }

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
        if(provider == null) {
            Toast.makeText(this, "Select League Provider", Toast.LENGTH_SHORT).show();
            return;
        }

        if(metroArea == null) {
            Toast.makeText(this, "Select Metro Area", Toast.LENGTH_SHORT).show();
            return;
        }

        if(league == null) {
            Toast.makeText(this, "Select League", Toast.LENGTH_SHORT).show();
            return;
        }

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

    public void onNext(final View view) {
        boolean isFormValid = true;
        if(provider == null) {
            providerTextView.setError("Required!");
            isFormValid = false;
        } else if(metroArea == null) {
            metroAreaTextView.setError("Required!");
            metroAreaTextView.requestFocus();
            isFormValid = false;
        } else if(league == null) {
            leagueTextView.setError("Required!");
            leagueTextView.requestFocus();
            isFormValid = false;
        } else if(level == null) {
            levelTextView.setError("Required!");
            levelTextView.requestFocus();
            isFormValid = false;
        } else if(facility == null) {
            facilityTextView.setError("Required!");
            facilityTextView.requestFocus();
            isFormValid = false;
        }

        if(isFormValid) {
            if(profile != null && league.getTeamType() == League.TEAM_TYPE_SINGLES) {
                LeagueRegistrationFacade regFacade = new LeagueRegistrationFacade();
                registration = regFacade.createLeagueRegistration(provider, metroArea, league, level, facility, profile, null);

                if (registration != null) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Unable to create a new league registration", Toast.LENGTH_SHORT).show();
                }
            } else {
                Intent intent = new Intent(this, LeagueRegistrationActivityEpilog.class);
                startActivityForResult(intent, REQUEST_REGISTRATION_EPILOG);
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

            providerTextView.setText(item.getTitle());
            providerTextView.setError(null);
            LeagueProviderTbl providerTbl = new LeagueProviderTbl();
            provider = (LeagueProvider)providerTbl.find(item.getStringValue());

            metroAreaTextView.setText("Select Metro Area");
            metroAreaTextView.setError(null);
            metroArea = null;

            leagueTextView.setText(null);
            leagueTextView.setError(null);
            league = null;

            levelTextView.setText(null);
            levelTextView.setError(null);
            level = null;

            profile = null;

            setNextButtonText();
        } else if(fragment.getTag().equals("MetroAreaPicker")){
            if(metroArea != null && metroArea.id.equals(item.getStringValue())) {
                return;
            }

            metroAreaTextView.setText(item.getTitle());
            metroAreaTextView.setError(null);
            LeagueMetroAreaTbl areaTbl = new LeagueMetroAreaTbl();
            metroArea = (LeagueMetroArea)areaTbl.find(item.getStringValue());

            leagueTextView.setText("Select League");
            leagueTextView.setError(null);
            league = null;

            levelTextView.setText(null);
            levelTextView.setError(null);
            level = null;

            TCUserFacade userFacade = new TCUserFacade();
            TCUser self = userFacade.getSelf();
            LeagueProfileTbl profileTbl = new LeagueProfileTbl();
            profile = profileTbl.findByUserIdAndAreaId(self.id, metroArea.id);

            setNextButtonText();
        } else if(fragment.getTag().equals("LeaguePicker")){
            if(league != null && league.id.equals(item.getStringValue())) {
                return;
            }

            leagueTextView.setText(item.getTitle());
            leagueTextView.setError(null);
            LeagueTbl leagueTbl = new LeagueTbl();
            league = (League)leagueTbl.find(item.getStringValue());

            levelTextView.setText("Select Level");
            levelTextView.setError(null);
            level = null;

            setNextButtonText();
        } else if(fragment.getTag().equals("LevelPicker")){
            if(level != null && level.id.equals(item.getStringValue())) {
                   return;
            }

            String selectedTitle = item.getTitle();
            TextView levelTextView = (TextView)findViewById(R.id.txt_level);
            levelTextView.setText(selectedTitle);
            levelTextView.setError(null);

            PlayingLevelTbl levelTbl = new PlayingLevelTbl();
            level = (PlayingLevel)levelTbl.find(item.getStringValue());
        }
    }

    private void setNextButtonText() {
        if(profile != null && league != null && league.getTeamType() == League.TEAM_TYPE_SINGLES) {
            Button nextBtn = (Button)findViewById(R.id.btn_next);
            nextBtn.setText("Save");
        } else {
            Button nextBtn = (Button)findViewById(R.id.btn_next);
            nextBtn.setText("Next");
        }
    }

    @Override
    public void onItemSelected(Facility facility) {
        LeagueRegistrationActivityProlog.facility = facility;
        facilityTextView.setText(facility.getName());
        facilityTextView.setError(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_SELECT_FACILITY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                facility = FacilityActivity.facility;
                facilityTextView.setText(facility.getName());
                facilityTextView.setError(null);
            }

            dismissUserFacilityDlg();
        } else if (requestCode == REQUEST_REGISTRATION_EPILOG) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
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
