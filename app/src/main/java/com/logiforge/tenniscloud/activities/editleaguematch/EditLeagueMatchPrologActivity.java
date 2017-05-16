package com.logiforge.tenniscloud.activities.editleaguematch;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private static final int REQUEST_NEXT_STEP = 10;
    private static final int REQUEST_NEW_REGISTRATION = 20;

    private static LeagueRegistration registration;
    private static Match.MatchWeek matchWeek;
    private static Match.HomeAway homeAway;

    public static void initStaticData() {
        registration = null;
        matchWeek = null;

        EditLeagueMatchActivity.initStaticData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_leaguematch_prolog);

        if(registration != null) {
            TextView leagueTextView = (TextView) findViewById(R.id.txt_league);
            leagueTextView.setText(
                    registration.getProfile().getLeagueMetroArea().getProvider().getProviderName()
                            + " - " + registration.getLeagueFlight().getLeague().getLeagueName());

            if(registration.getLeagueFlight().getLeague().getTeamType() == League.TEAM_TYPE_SINGLES) {
                hideOpponent2Email();
            }
        } else {
            hideOpponent2Email();
        }

        Spinner matchWeekDropdown = (Spinner)findViewById(R.id.spnr_matchWeek);
        ArrayAdapter<Match.MatchWeek> matchWeekAdapter = new ArrayAdapter<Match.MatchWeek>(this, android.R.layout.simple_spinner_dropdown_item, Match.MatchWeek.values());
        matchWeekDropdown.setAdapter(matchWeekAdapter);
        if(matchWeek != null) {
            matchWeekDropdown.setSelection(matchWeek.ordinal());
        } else {
            matchWeek = Match.MatchWeek.WEEK01;
        }

        Spinner homeAwayDropdown = (Spinner)findViewById(R.id.spnr_homeAway);
        ArrayAdapter<Match.HomeAway> homeAwayAdapter = new ArrayAdapter<Match.HomeAway>(this, android.R.layout.simple_spinner_dropdown_item, Match.HomeAway.values());
        homeAwayDropdown.setAdapter(homeAwayAdapter);
        if(homeAway != null) {
            homeAwayDropdown.setSelection(homeAway.ordinal());
        } else {
            homeAway = Match.HomeAway.HomeMatch;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Spinner matchWeekDropdown = (Spinner)findViewById(R.id.spnr_matchWeek);
        matchWeek = (Match.MatchWeek)matchWeekDropdown.getSelectedItem();

        Spinner homeAwayDropdown = (Spinner)findViewById(R.id.spnr_homeAway);
        homeAway = (Match.HomeAway)homeAwayDropdown.getSelectedItem();
    }

    public void onNext(final View view) {
        boolean isFormValid = true;
        if(registration == null) {
            TextView leagueTextView = (TextView)findViewById(R.id.txt_league);
            leagueTextView.setError("Required!");
            isFormValid = false;
        }

        Spinner matchWeekDropdown = (Spinner)findViewById(R.id.spnr_matchWeek);
        matchWeek = (Match.MatchWeek)matchWeekDropdown.getSelectedItem();

        Spinner homeAwayDropdown = (Spinner)findViewById(R.id.spnr_homeAway);
        homeAway = (Match.HomeAway)homeAwayDropdown.getSelectedItem();

        EditText opponent1EmailEditText = (EditText)findViewById(R.id.edit_opponent1Email);
        String email1 = opponent1EmailEditText.getText().toString();
        if(email1.length() == 0) {
            opponent1EmailEditText.setError("Required!");
            opponent1EmailEditText.requestFocus();
            isFormValid = false;
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            opponent1EmailEditText.setError("Invalid!");
            opponent1EmailEditText.requestFocus();
            isFormValid = false;
        }

        String email2 = null;
        View opponent2EmailLayout = findViewById(R.id.layout_opponent2Email);
        if(opponent2EmailLayout.getVisibility() == View.VISIBLE) {
            EditText opponent2EmailEditText = (EditText)findViewById(R.id.edit_opponent2Email);
            email2 = opponent2EmailEditText.getText().toString();
            if (email2.length() == 0) {
                opponent2EmailEditText.setError("Required!");
                opponent2EmailEditText.requestFocus();
                isFormValid = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email2).matches()) {
                opponent2EmailEditText.setError("Invalid!");
                opponent2EmailEditText.requestFocus();
                isFormValid = false;
            }
        }

        if(isFormValid) {
            EditLeagueMatchActivity.registration = registration;
            Match match = new Match();
            match.setLeagueWeek(matchWeek.getId());
            match.setLeagueFlight(registration.getLeagueFlight());
            EditLeagueMatchActivity.match = match;

            LeagueMatchFacade leagueMatchFacade = new LeagueMatchFacade();

            if(registration.getLeagueFlight().getLeague().getTeamType() == League.TEAM_TYPE_SINGLES) {
                leagueMatchFacade.createSinglesLeagueMatch(this, match, registration, homeAway, email1);
            } else {
                leagueMatchFacade.createDoublesLeagueMatch(this, match, registration, homeAway, email1, email2);
            }

            EditLeagueMatchActivity.match = match;
            EditLeagueMatchActivity.registration = registration;
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
        TextView leagueTextView = (TextView)findViewById(R.id.txt_league);
        leagueTextView.setError(null);
        leagueTextView.setText(
                registration.getProfile().getLeagueMetroArea().getProvider().getProviderName()
                        + " - " + registration.getLeagueFlight().getLeague().getLeagueName());
    }

    private void hideOpponent2Email() {
        View opponent2EmailLayout = findViewById(R.id.layout_opponent2Email);
        EditText email2EditText = (EditText) findViewById(R.id.edit_opponent2Email);
        email2EditText.setText(null);
        email2EditText.setError(null);
        opponent2EmailLayout.setVisibility(View.GONE);
    }

    private void showOpponent2Email() {
        View opponent2EmailLayout = findViewById(R.id.layout_opponent2Email);
        opponent2EmailLayout.setVisibility(View.VISIBLE);
    }
}
