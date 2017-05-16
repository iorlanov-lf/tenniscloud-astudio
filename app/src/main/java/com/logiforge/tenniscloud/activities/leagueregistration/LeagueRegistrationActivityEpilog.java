package com.logiforge.tenniscloud.activities.leagueregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.facility.FacilityActivity;
import com.logiforge.tenniscloud.activities.facility.UserFacilityDlg;
import com.logiforge.tenniscloud.activities.util.EmailListView;
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
import com.logiforge.tenniscloud.model.PlayingLevel;
import com.logiforge.tenniscloud.model.TCUser;
import com.logiforge.tenniscloud.model.TCUserEmail;

import java.util.ArrayList;
import java.util.List;

import static com.logiforge.tenniscloud.R.id.edit_email;


public class LeagueRegistrationActivityEpilog extends AppCompatActivity
        implements ItemPickerDialogFragment.OnItemSelectedListener,
        UserFacilityDlg.OnItemSelectedListener,
        View.OnClickListener {
    private static final String TAG = LeagueRegistrationActivityEpilog.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leaguereg_prolog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LeagueRegistrationActivityProlog.profile;

        // profile
        if(profile != null && profile.id == null) {
            LinearLayout profileLayout = (LinearLayout)findViewById(R.id.layout_profile);
            profileLayout.setVisibility(View.VISIBLE);

            //TODO: EditText emailEditText = (EditText) findViewById(R.id.edit_email);
            //emailEditText.setText(profile.getEmail());
            //emailEditText.setError(null);
            EditText displayNameEditText = (EditText) findViewById(R.id.edit_displayName);
            displayNameEditText.setText(profile.getDisplayName());
            displayNameEditText.setError(null);
            EditText phoneEditText = (EditText) findViewById(R.id.edit_phone);
            phoneEditText.setText(profile.getPhoneNumber());
            phoneEditText.setError(null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        LinearLayout profileLayout = (LinearLayout)findViewById(R.id.layout_profile);
        if(profileLayout.getVisibility() == View.VISIBLE) {
            EditText emailEditText = (EditText) findViewById(edit_email);
            EditText displayNameEditText = (EditText) findViewById(R.id.edit_displayName);
            EditText phoneEditText = (EditText) findViewById(R.id.edit_phone);

            if(profile == null) {
                profile = new LeagueProfile();
            }

            //TODOD: profile.setEmail(emailEditText.getText().toString());
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
            /*TODO: EditText etEmail = (EditText)findViewById(R.id.edit_email);
            profile.setEmail(etEmail.getText().toString());
            if(etEmail.getText().length() == 0) {
                etEmail.setError("Required!");
                etEmail.requestFocus();
                isFormValid = false;
            } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                etEmail.setError("Invalid!");
                etEmail.requestFocus();
                isFormValid = false;
            }*/

            EmailListView emailListView = (EmailListView) findViewById(R.id.view_emails);
            List<String> emails = emailListView.getEmails();
            if(emails.size() == 0) {
                TextView emailLbl = (TextView)findViewById(R.id.txt_email);
                emailLbl.setError("Required!");
                isFormValid = false;
            } else {
                profile.setEmails2(emails);
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

            LeagueProviderTbl providerTbl = new LeagueProviderTbl();
            provider = (LeagueProvider)providerTbl.find(item.getStringValue());

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

            LeagueMetroAreaTbl areaTbl = new LeagueMetroAreaTbl();
            metroArea = (LeagueMetroArea)areaTbl.find(item.getStringValue());

            TCUserFacade userFacade = new TCUserFacade();
            TCUser self = userFacade.getSelf();
            LeagueProfileTbl profileTbl = new LeagueProfileTbl();
            LeagueProfile p = profileTbl.findByUserIdAndAreaId(self.id, metroArea.id);

            if(p == null) {
                profile = new LeagueProfile();

                LinearLayout profileLayout = (LinearLayout) findViewById(R.id.layout_profile);

                TCUser user = TCUserFacade.builder().resolveEmails().build();

                List<String> userEmails = new ArrayList<String>();
                for(TCUserEmail userEmail : user.getEmails()) {
                    userEmails.add(userEmail.getEmail());
                }
                EmailListView emailListView = (EmailListView) findViewById(R.id.view_emails);
                emailListView.initItems(userEmails);

                EditText etDisplayName = (EditText)findViewById(R.id.edit_displayName);
                etDisplayName.setText(user.getDisplayName());
                etDisplayName.setError(null);
                profile.setDisplayName(user.getDisplayName());
                EditText etPhone = (EditText)findViewById(R.id.edit_phone);
                etPhone.setText(user.getPhoneNbr());
                etPhone.setError(null);
                profile.setPhoneNumber(user.getPhoneNbr());

                profileLayout.setVisibility(View.VISIBLE);
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

            LeagueTbl leagueTbl = new LeagueTbl();
            league = (League)leagueTbl.find(item.getStringValue());

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

            PlayingLevelTbl levelTbl = new PlayingLevelTbl();
            level = (PlayingLevel)levelTbl.find(item.getStringValue());
        }
    }

    @Override
    public void onItemSelected(Facility facility) {
        LeagueRegistrationActivityEpilog.facility = facility;
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
