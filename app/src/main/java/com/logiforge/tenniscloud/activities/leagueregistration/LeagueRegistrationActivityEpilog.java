package com.logiforge.tenniscloud.activities.leagueregistration;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.util.EmailListView;
import com.logiforge.tenniscloud.facades.LeagueRegistrationFacade;
import com.logiforge.tenniscloud.facades.TCUserFacade;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueProfile;
import com.logiforge.tenniscloud.model.Partner;
import com.logiforge.tenniscloud.model.TCUser;
import com.logiforge.tenniscloud.model.TCUserEmail;

import java.util.ArrayList;
import java.util.List;

import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.provider;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.metroArea;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.league;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.level;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.facility;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.profile;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.partner;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.registration;

public class LeagueRegistrationActivityEpilog extends AppCompatActivity {
    private static final String TAG = LeagueRegistrationActivityEpilog.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leaguereg_epilog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(profile == null) {
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
        } else if(profile.id == null) {
            LinearLayout profileLayout = (LinearLayout)findViewById(R.id.layout_profile);
            profileLayout.setVisibility(View.VISIBLE);
        }

        if(league.getTeamType() == League.TEAM_TYPE_DOUBLES) {
            LinearLayout partnerLayout = (LinearLayout)findViewById(R.id.layout_partner);
            partnerLayout.setVisibility(View.VISIBLE);

            partner = new Partner();
        }
    }

    /*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        LinearLayout profileLayout = (LinearLayout)findViewById(R.id.layout_profile);
        if(profileLayout.getVisibility() == View.VISIBLE) {
            EditText displayNameEditText = (EditText) findViewById(R.id.edit_displayName);
            EditText phoneEditText = (EditText) findViewById(R.id.edit_phone);

            if(profile == null) {
                profile = new LeagueProfile();
            }

            profile.setDisplayName(displayNameEditText.getText().toString());
            profile.setPhoneNumber(phoneEditText.getText().toString());
        }
    }*/

    public void onSave(final View view) {
        boolean isFormValid = true;

        if(profile.id == null) {
            EmailListView emailListView = (EmailListView) findViewById(R.id.view_emails);
            List<String> emails = emailListView.getEmails();
            if(emails.size() == 0) {
                emailListView.setError("Required!");
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
            } else {
                profile.setDisplayName(etDisplayName.getText().toString());
            }

            EditText etPhone = (EditText)findViewById(R.id.edit_phone);
            profile.setPhoneNumber(etPhone.getText().toString());
            if(etPhone.getText().length() == 0) {
                etPhone.setError("Required!");
                etPhone.requestFocus();
                isFormValid = false;
            } else {
                profile.setPhoneNumber(etPhone.getText().toString());
            }
        }

        if(partner != null) {
            EmailListView emailListView = (EmailListView) findViewById(R.id.view_partner_emails);
            List<String> emails = emailListView.getEmails();
            if(emails.size() == 0) {
                emailListView.setError("Required!");
                isFormValid = false;
            } else {
                partner.setEmails2(emails);
            }

            EditText etDisplayName = (EditText)findViewById(R.id.edit_displayName);
            profile.setDisplayName(etDisplayName.getText().toString());
            if(etDisplayName.getText().length() == 0) {
                etDisplayName.setError("Required!");
                etDisplayName.requestFocus();
                isFormValid = false;
            } else {
                profile.setDisplayName(etDisplayName.getText().toString());
            }

            EditText etPhone = (EditText)findViewById(R.id.edit_phone);
            profile.setPhoneNumber(etPhone.getText().toString());
            if(etPhone.getText().length() == 0) {
                etPhone.setError("Required!");
                etPhone.requestFocus();
                isFormValid = false;
            } else {
                profile.setPhoneNumber(etPhone.getText().toString());
            }
        }

        if(isFormValid) {
            LeagueRegistrationFacade regFacade = new LeagueRegistrationFacade();
            registration = regFacade.createLeagueRegistration(provider, metroArea, league, level, facility, profile, partner);

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
}
