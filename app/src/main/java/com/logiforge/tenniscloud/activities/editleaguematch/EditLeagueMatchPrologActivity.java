package com.logiforge.tenniscloud.activities.editleaguematch;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.leagueregistration.LeagueRegistrationActivityProlog;
import com.logiforge.tenniscloud.activities.leagueregistration.UserLeagueRegistrationDlg;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.facades.LeagueRegistrationFacade;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.Match;

public class EditLeagueMatchPrologActivity extends AppCompatActivity
        implements View.OnClickListener,
        UserLeagueRegistrationDlg.OnItemSelectedListener {
    private static final String KEY_MATCHWEEK_SPINNER = "KEY_MATCHWEEK_SPINNER";
    private static final String KEY_HOMEAWAY_SPINNER = "KEY_HOMEAWAY_SPINNER";

    private static final int REQUEST_NEXT_STEP = 10;
    private static final int REQUEST_NEW_REGISTRATION = 20;

    private static LeagueRegistration registration;
    private static Match.MatchWeek matchWeek;
    private static Match.HomeAway homeAway;

    TextView leagueTextView = null;
    Spinner matchWeekSpinner = null;
    Spinner homeAwaySpinner = null;
    View opponent1EmailLayout = null;
    EditText email1EditText = null;
    View opponent2EmailLayout = null;
    EditText email2EditText = null;

    public static void initStaticData() {
        registration = null;
        matchWeek = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leaguematch_prolog);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        leagueTextView = (TextView) findViewById(R.id.txt_league);
        matchWeekSpinner = (Spinner)findViewById(R.id.spnr_matchWeek);
        homeAwaySpinner = (Spinner)findViewById(R.id.spnr_homeAway);
        opponent1EmailLayout = findViewById(R.id.layout_opponent1Email);
        email1EditText = (EditText) findViewById(R.id.edit_opponent1Email);
        opponent2EmailLayout = findViewById(R.id.layout_opponent2Email);
        email2EditText = (EditText) findViewById(R.id.edit_opponent2Email);

        if(registration != null) {
            leagueTextView.setText(
                    registration.getProfile().getLeagueMetroArea().getProvider().getProviderName()
                            + " - " + registration.getLeagueFlight().getLeague().getLeagueName());

            if(registration.getLeagueFlight().getLeague().getTeamType() == League.TEAM_TYPE_SINGLES) {
                hideOpponent2Email();
            }
        } else {
            hideOpponent2Email();
        }

        ArrayAdapter<Match.MatchWeek> matchWeekAdapter = new ArrayAdapter<Match.MatchWeek>(this, android.R.layout.simple_spinner_dropdown_item, Match.MatchWeek.values());
        matchWeekSpinner.setAdapter(matchWeekAdapter);
        if(savedInstanceState != null) {
            matchWeekSpinner.setSelection(savedInstanceState.getInt(KEY_MATCHWEEK_SPINNER));
        }

        ArrayAdapter<Match.HomeAway> homeAwayAdapter = new ArrayAdapter<Match.HomeAway>(this, android.R.layout.simple_spinner_dropdown_item, Match.HomeAway.values());
        homeAwaySpinner.setAdapter(homeAwayAdapter);
        if(savedInstanceState != null) {
            homeAwaySpinner.setSelection(savedInstanceState.getInt(KEY_HOMEAWAY_SPINNER));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_MATCHWEEK_SPINNER, matchWeekSpinner.getSelectedItemPosition());
        outState.putInt(KEY_HOMEAWAY_SPINNER, homeAwaySpinner.getSelectedItemPosition());
    }

    public void onNext(final View view) {
        boolean isFormValid = true;
        if(registration == null) {
            leagueTextView.setError("Required!");
            isFormValid = false;
        }

        matchWeek = (Match.MatchWeek)matchWeekSpinner.getSelectedItem();
        homeAway = (Match.HomeAway)homeAwaySpinner.getSelectedItem();

        String email1 = email1EditText.getText().toString();
        if(email1.length() == 0) {
            email1EditText.setError("Required!");
            email1EditText.requestFocus();
            isFormValid = false;
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            email1EditText.setError("Invalid!");
            email1EditText.requestFocus();
            isFormValid = false;
        }

        String email2 = null;
        if(opponent2EmailLayout.getVisibility() == View.VISIBLE) {
            email2 = email2EditText.getText().toString();
            if (email2.length() == 0) {
                email2EditText.setError("Required!");
                email2EditText.requestFocus();
                isFormValid = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email2).matches()) {
                email2EditText.setError("Invalid!");
                email2EditText.requestFocus();
                isFormValid = false;
            }
        }

        if(isFormValid) {
            Match match = new Match();
            match.setLeagueWeek(matchWeek.getId());
            match.setLeagueFlight(registration.getLeagueFlight());

            LeagueMatchFacade leagueMatchFacade = new LeagueMatchFacade();

            if(registration.getLeagueFlight().getLeague().getTeamType() == League.TEAM_TYPE_SINGLES) {
                leagueMatchFacade.createLeagueMatch(this, match, registration, homeAway, email1, null);
            } else {
                leagueMatchFacade.createLeagueMatch(this, match, registration, homeAway, email1, email2);
            }

            EditLeagueMatchActivity.initStaticData(match);
            Intent intent = new Intent(this, EditLeagueMatchActivity.class);
            startActivityForResult(intent, REQUEST_NEXT_STEP);
        }
    }

    public void onCancel(final View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    public void onSelectLeague(final View view) {
        LeagueRegistrationFacade regFacade = new LeagueRegistrationFacade();
        if(regFacade.hasRegistration()) {
            FragmentManager fm = getSupportFragmentManager();
            UserLeagueRegistrationDlg userRegistrationDlg = UserLeagueRegistrationDlg.newInstance();
            userRegistrationDlg.show(fm, UserLeagueRegistrationDlg.DLG_TAG);
        } else {
            Intent intent = new Intent(this, LeagueRegistrationActivityProlog.class);
            LeagueRegistrationActivityProlog.initState();
            startActivityForResult(intent, REQUEST_NEW_REGISTRATION);
        }
    }

    @Override
    public void onClick(View v) {
        dismissUserLeagueRegistrationDlg();

        Intent intent = new Intent(this, LeagueRegistrationActivityProlog.class);
        LeagueRegistrationActivityProlog.initState();
        startActivityForResult(intent, REQUEST_NEW_REGISTRATION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEXT_STEP) {
            setResult(resultCode);
            finish();
        } else if (requestCode == REQUEST_NEW_REGISTRATION) {
            if (resultCode == RESULT_OK) {
                setLeagueTextView(LeagueRegistrationActivityProlog.registration);

                if(registration.getLeagueFlight().getLeague().getTeamType() == League.TEAM_TYPE_SINGLES) {
                    hideOpponent2Email();
                } else {
                    showOpponent2Email();
                }
            }
        }
    }

    @Override
    public void onItemSelected(LeagueRegistration registration) {
        setLeagueTextView(registration);

        if(registration.getLeagueFlight().getLeague().getTeamType() == League.TEAM_TYPE_SINGLES) {
            hideOpponent2Email();
        } else {
            showOpponent2Email();
        }
    }

    private void dismissUserLeagueRegistrationDlg() {
        Fragment prev = getSupportFragmentManager().findFragmentByTag(UserLeagueRegistrationDlg.DLG_TAG);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    private void setLeagueTextView(LeagueRegistration registration) {
        EditLeagueMatchPrologActivity.registration = registration;
        leagueTextView.setError(null);
        leagueTextView.setText(
                registration.getProfile().getLeagueMetroArea().getProvider().getProviderName()
                        + " - " + registration.getLeagueFlight().getLeague().getLeagueName());
    }

    private void hideOpponent2Email() {
        email2EditText.setText(null);
        email2EditText.setError(null);
        opponent2EmailLayout.setVisibility(View.GONE);
    }

    private void showOpponent2Email() {
        opponent2EmailLayout.setVisibility(View.VISIBLE);
    }
}
