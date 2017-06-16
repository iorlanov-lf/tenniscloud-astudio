package com.logiforge.tenniscloud.activities.leagueregistration;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.util.ContactInfoView;
import com.logiforge.tenniscloud.activities.util.EmailListView;
import com.logiforge.tenniscloud.activities.util.PhoneListView;
import com.logiforge.tenniscloud.facades.LeagueRegistrationFacade;
import com.logiforge.tenniscloud.facades.TCUserFacade;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueProfile;
import com.logiforge.tenniscloud.model.Partner;
import com.logiforge.tenniscloud.model.TCUser;
import com.logiforge.tenniscloud.model.TCUserEmail;
import com.logiforge.tenniscloud.model.TCUserPhone;
import com.logiforge.tenniscloud.model.util.Phone;

import java.util.ArrayList;
import java.util.List;

import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.provider;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.metroArea;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.league;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.level;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.facility;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.profile;
import static com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog.registration;

public class LeagueRegistrationActivityEpilog extends AppCompatActivity {
    private static final String TAG = LeagueRegistrationActivityEpilog.class.getSimpleName();

    ContactInfoView profileView;
    ContactInfoView partnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leaguereg_epilog);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        profileView = (ContactInfoView)findViewById(R.id.view_self);
        partnerView = (ContactInfoView)findViewById(R.id.view_partner);

        if(savedInstanceState == null) {
            if (profile == null) {

                TCUser user = TCUserFacade.builder().resolveEmails().resolvePhones().build();

                EditText etDisplayName = (EditText) findViewById(R.id.edit_displayName);
                etDisplayName.setText(user.getDisplayName());
                etDisplayName.setError(null);

                List<String> userEmails = new ArrayList<String>();
                for (TCUserEmail userEmail : user.getEmails()) {
                    userEmails.add(userEmail.getEmail());
                }
                EmailListView emailListView = (EmailListView) findViewById(R.id.view_emails);
                emailListView.initItems(userEmails);

                List<Phone> userPhones = new ArrayList<Phone>();
                for (TCUserPhone userPhone : user.getPhones()) {
                    userPhones.add(new Phone(userPhone.getPhone(), userPhone.getPhoneType()));
                }
                PhoneListView phoneListView = (PhoneListView) findViewById(R.id.view_phones);
                phoneListView.initItems(userPhones);
            } else {
                profileView.setVisibility(View.GONE);
            }
        }

        if (league.getTeamType() == League.TEAM_TYPE_SINGLES) {
            partnerView.setVisibility(View.GONE);
        }
    }

    public void onSave(final View view) {
        boolean isFormValid = true;
        LeagueProfile profileToSubmit = profile;
        Partner partnerToSubmit = null;

        if(profile == null) {
            profileToSubmit = new LeagueProfile();

            if(profileView.getName().length() == 0) {
                profileView.setNameError("Required!");
                isFormValid = false;
            } else {
                profileToSubmit.setDisplayName(profileView.getName());
            }

            List<String> emails = profileView.getEmails();
            if(emails.size() == 0) {
                profileView.setEmailError("Required!");
                isFormValid = false;
            } else {
                profileToSubmit.setEmailsFromStrings(emails);
            }

            List<Phone> phones = profileView.getPhones();
            if(phones.size() > 0) {
                profileToSubmit.setPhonesFromUtilPhones(phones);
            }
        }

        if(league.getTeamType() == League.TEAM_TYPE_DOUBLES) {
            partnerToSubmit = new Partner();

            if(partnerView.getName().length() == 0) {
                partnerView.setNameError("Required!");
                isFormValid = false;
            } else {
                partnerToSubmit.setDisplayName(partnerView.getName());
            }

            List<String> emails = partnerView.getEmails();
            if(emails.size() == 0) {
                partnerView.setEmailError("Required!");
                isFormValid = false;
            } else {
                partnerToSubmit.setEmailsFromStrings(emails);
            }

            List<Phone> phones = partnerView.getPhones();
            partnerToSubmit.setPhonesFromUtilPhones(phones);
        }

        if(isFormValid) {
            LeagueRegistrationFacade regFacade = new LeagueRegistrationFacade();
            registration = regFacade.createLeagueRegistration(provider, metroArea, league, level, facility, profileToSubmit, partnerToSubmit);

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
