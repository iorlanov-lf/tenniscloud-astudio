package com.logiforge.tenniscloud.activities.editleaguematch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.db.util.DbUtil;
import com.logiforge.tenniscloud.facades.FacilityFacade;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.facades.TCUserFacade;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchPlayer;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/16/2017.
 */

public class MatchFragment extends Fragment {
    private static final String KEY_MATCHWEEK_SPINNER = "KEY_MATCHWEEK_SPINNER";
    private static final String KEY_HOMEAWAY_SPINNER = "KEY_HOMEAWAY_SPINNER";
    private static final String KEY_FACILITY_ID = "KEY_FACILITY_ID";
    private static final String KEY_OUTCOME_SPINNER = "KEY_OUTCOME_SPINNER";
    private static final String KEY_SCORE_SPINNER = "KEY_OUTCOME_SPINNER_";
    public static final String TIME_FORMAT = "kk:mm a";
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    private Match match;

    Spinner matchWeekSpinner = null;
    Spinner homeAwaySpinner = null;
    Spinner outcomeSpinner = null;
    EditText deadlineEditText = null;
    Button clearDeadlineBtn = null;
    EditText schDtEditText = null;
    EditText schTimeEditText = null;
    TextView facilityText = null;
    Button clearScheduledBtn = null;
    List<Spinner> scoreSpinners = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_editleaguematch_frag_match, container, false);

        match = EditLeagueMatchActivity.match;
        matchWeekSpinner = (Spinner)rootView.findViewById(R.id.spnr_matchWeek);
        homeAwaySpinner = (Spinner)rootView.findViewById(R.id.spnr_homeAway);
        outcomeSpinner = (Spinner)rootView.findViewById(R.id.spnr_outcome);
        deadlineEditText = (EditText)rootView.findViewById(R.id.edit_deadline);
        clearDeadlineBtn = (Button)rootView.findViewById(R.id.btn_clear_deadline);
        schDtEditText = (EditText)rootView.findViewById(R.id.edit_schDate);
        schTimeEditText = (EditText)rootView.findViewById(R.id.edit_schTime);
        clearScheduledBtn = (Button)rootView.findViewById(R.id.btn_clear_scheduled);
        facilityText = (TextView)rootView.findViewById(R.id.txt_facility);
        scoreSpinners = new ArrayList<Spinner>();
        scoreSpinners.add((Spinner)rootView.findViewById(R.id.spnr_scoreHome1));
        scoreSpinners.add((Spinner)rootView.findViewById(R.id.spnr_scoreVisitor1));
        scoreSpinners.add((Spinner)rootView.findViewById(R.id.spnr_scoreHome2));
        scoreSpinners.add((Spinner)rootView.findViewById(R.id.spnr_scoreVisitor2));
        scoreSpinners.add((Spinner)rootView.findViewById(R.id.spnr_scoreHome3));
        scoreSpinners.add((Spinner)rootView.findViewById(R.id.spnr_scoreVisitor3));

        ArrayAdapter<Match.MatchWeek> matchWeekAdapter =
                new ArrayAdapter<Match.MatchWeek>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Match.MatchWeek.values());
        matchWeekSpinner.setAdapter(matchWeekAdapter);
        if(savedInstanceState != null) {
            matchWeekSpinner.setSelection(savedInstanceState.getInt(KEY_MATCHWEEK_SPINNER));
        } else {
            Match.MatchWeek matchWeek = Match.MatchWeek.getById(match.getLeagueWeek());
            matchWeekSpinner.setSelection(matchWeek.ordinal());
        }

        ArrayAdapter<Match.HomeAway> homeAwayAdapter =
                new ArrayAdapter<Match.HomeAway>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Match.HomeAway.values());
        homeAwaySpinner.setAdapter(homeAwayAdapter);
        if(savedInstanceState != null) {
            homeAwaySpinner.setSelection(savedInstanceState.getInt(KEY_HOMEAWAY_SPINNER));
        } else {
            TCUserFacade userFacade = new TCUserFacade();
            MatchPlayer me = match.findPlayerByUserId(userFacade.getSelf().id);
            if(me.getHomeTeam()) {
                homeAwaySpinner.setSelection(Match.HomeAway.HomeMatch.ordinal());
            } else {
                homeAwaySpinner.setSelection(Match.HomeAway.AwayMatch.ordinal());
            }
        }

        if(savedInstanceState == null) {
            LocalDate deadline = match.getDeadlineDt();
            if (deadline != null) {
                deadlineEditText.setText(deadline.toString(DATE_FORMAT));
            }
        }
        deadlineEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LocalDate now = LocalDate.now();
                    DatePickerDialog datePickerDlg =
                            new DatePickerDialog(getActivity(), new OnDeadlineDateSetListener(),
                                    now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth());
                    datePickerDlg.show();
                }
                return false;
            }
        });
        clearDeadlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadlineEditText.setText(null);
            }
        });

        if(savedInstanceState == null) {
            LocalDate schDt = match.getScheduledDt();
            if (schDt != null) {
                schDtEditText.setText(schDt.toString(DATE_FORMAT));
            }
        }
        schDtEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LocalDate now = LocalDate.now();
                    DatePickerDialog datePickerDlg =
                            new DatePickerDialog(getActivity(), new OnSchDateSetListener(),
                                    now.getYear(), now.getMonthOfYear()-1, now.getDayOfMonth());
                    datePickerDlg.show();
                }
                return false;
            }
        });

        if(savedInstanceState == null) {
            LocalTime schTm = match.getScheduledTm();
            if (schTm != null) {
                schTimeEditText.setText(schTm.toString(TIME_FORMAT));
            }
        }
        schTimeEditText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LocalDate now = LocalDate.now();
                    TimePickerDialog datePickerDlg =
                            new TimePickerDialog(getActivity(), new OnSchTimeSetListener(),
                                    19, 0, false);
                    datePickerDlg.show();
                }
                return false;
            }
        });

        clearScheduledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schDtEditText.setText(null);
                schTimeEditText.setText(null);
                schTimeEditText.setError(null);
                schDtEditText.setError(null);
            }
        });

        if(savedInstanceState == null) {
            Facility facility = match.getFacility();
            if (facility != null) {
                facilityText.setText(facility.getName());
                facilityText.setTag(facility.id);
            }
        } else {
            String facilityId = savedInstanceState.getString(KEY_FACILITY_ID);
            if (facilityId != null) {
                facilityText.setTag(facilityId);
            }
        }

        ArrayAdapter<Match.Outcome> outcomeAdapter =
                new ArrayAdapter<Match.Outcome>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, Match.Outcome.values());
        outcomeSpinner.setAdapter(outcomeAdapter);
        if(savedInstanceState != null) {
            outcomeSpinner.setSelection(savedInstanceState.getInt(KEY_OUTCOME_SPINNER));
        } else if(match.getOutcome() != null){
            Match.Outcome outcome = Match.Outcome.getById(match.getOutcome());
            outcomeSpinner.setSelection(outcome.ordinal());
        }

        String[] scoreItems = new String[]{"0", "1", "2", "3", "4", "5", "6", "7" };
        for(int i=0; i<scoreSpinners.size(); i++) {
            Spinner spinner = scoreSpinners.get(i);
            spinner.setAdapter(
                    new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, scoreItems));
            if(savedInstanceState != null) {
                spinner.setSelection(savedInstanceState.getInt(KEY_SCORE_SPINNER+i));
            }
        }
        if(savedInstanceState == null) {
            Integer[] points = match.getPoints();
            for(int i=0; i<6; i++) {
                scoreSpinners.get(i).setSelection(points[i]);
            }
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EditLeagueMatchActivity activity = (EditLeagueMatchActivity)context;
        activity.matchFragmentTag = this.getTag();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_MATCHWEEK_SPINNER, matchWeekSpinner.getSelectedItemPosition());
        outState.putInt(KEY_HOMEAWAY_SPINNER, homeAwaySpinner.getSelectedItemPosition());
        if(facilityText.getTag() != null) {
            outState.putString(KEY_FACILITY_ID, (String) facilityText.getTag());
        }
        outState.putInt(KEY_OUTCOME_SPINNER, outcomeSpinner.getSelectedItemPosition());

        for(int i=0; i<scoreSpinners.size(); i++) {
            Spinner spinner = scoreSpinners.get(i);
            outState.putInt(KEY_SCORE_SPINNER + i, spinner.getSelectedItemPosition());
        }
    }

    public void setFacility(Facility facility) {
        facilityText.setText(facility.getName());
        facilityText.setTag(facility.id);
    }



    public void populateMatch(Match updatedMatch) {
        updatedMatch.setLeagueWeek(getMatchWeek());
        updatedMatch.setDeadlineDt(getDeadlineDt());
        updatedMatch.setScheduledDt(getScheduledDt());
        updatedMatch.setScheduledTm(getScheduledTm());
        updatedMatch.setOutcome(getOutcome());
        Integer[] points = getPoints();
        updatedMatch.setPoints(points);
        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        matchFacade.setPlayerHomeTeam(updatedMatch, getHomeAway());

        Facility facility = null;
        String facilityId = (String)facilityText.getTag();
        if(facilityId != null) {
            FacilityFacade facilityFacade = new FacilityFacade();
            facility = facilityFacade.findFacility(facilityId);
        }
        updatedMatch.setFacility(facility);
    }

    public boolean validate() {
        boolean isValid = true;
        LocalDate schDt = getScheduledDt();
        LocalTime schTm = getScheduledTm();

        if(schDt != null && schTm == null) {
            schTimeEditText.setError("Required if scheduled date is set!");
            isValid = false;
        }

        if(schDt == null && schTm != null) {
            schDtEditText.setError("Required if scheduled time is set!");
            isValid = false;
        }

        return isValid;
    }

    private int getMatchWeek() {
        Match.MatchWeek matchWeek = (Match.MatchWeek)matchWeekSpinner.getSelectedItem();
        return matchWeek.getId();
    }

    private Match.HomeAway getHomeAway() {
        return (Match.HomeAway)homeAwaySpinner.getSelectedItem();
    }

    private LocalDate getDeadlineDt() {
        String deadlineDtAsString = deadlineEditText.getText().toString();
        if(deadlineDtAsString.length() > 0) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
            return LocalDate.parse(deadlineDtAsString, formatter);
        } else {
            return null;
        }
    }

    private LocalDate getScheduledDt() {
        String schDtAsString = schDtEditText.getText().toString();
        if(schDtAsString.length() > 0) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
            return LocalDate.parse(schDtAsString, formatter);
        } else {
            return null;
        }
    }

    private LocalTime getScheduledTm() {
        String schTmAsString = schTimeEditText.getText().toString();
        if(schTmAsString.length() > 0) {
            DateTimeFormatter formatter = DateTimeFormat.forPattern(TIME_FORMAT);
            return LocalTime.parse(schTmAsString, formatter);
        } else {
            return null;
        }
    }

    private int getOutcome() {
        Match.Outcome outcome = (Match.Outcome)outcomeSpinner.getSelectedItem();
        return outcome.getId();
    }

    private Integer[] getPoints() {
        Integer[] points = new Integer[6];
        for(int i=0; i<scoreSpinners.size(); i++) {
            points[i] = scoreSpinners.get(i).getSelectedItemPosition();
        }

        return points;
    }

    class OnDeadlineDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            LocalDate deadline = new LocalDate(year, month+1, dayOfMonth);
            EditText deadlineEditText = (EditText)getView().findViewById(R.id.edit_deadline);
            deadlineEditText.setText(deadline.toString(DATE_FORMAT));
        }
    }

    class OnSchDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            LocalDate schDt = new LocalDate(year, month+1, dayOfMonth);
            schDtEditText.setText(schDt.toString(DATE_FORMAT));

            schTimeEditText.setError(null);
            schDtEditText.setError(null);
        }
    }

    class OnSchTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker view, int hours, int minutes) {
            LocalTime schTime = new LocalTime(hours, minutes);
            schTimeEditText.setText(schTime.toString(TIME_FORMAT));

            schTimeEditText.setError(null);
            schDtEditText.setError(null);
        }
    }
}
