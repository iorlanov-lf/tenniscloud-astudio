package com.logiforge.tenniscloud.activities.editleaguematch;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.facades.TCUserFacade;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.TCUser;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by iorlanov on 4/16/2017.
 */

public class MatchFragment extends Fragment {
    private Match match;
    private Match.HomeAway homeAway;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_editleaguematch_frag_match, container, false);

        match = EditLeagueMatchActivity.match;

        Spinner homeAwayDropdown = (Spinner)rootView.findViewById(R.id.spnr_homeAway);
        ArrayAdapter<Match.HomeAway> homeAwayAdapter =
                new ArrayAdapter<Match.HomeAway>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Match.HomeAway.values());
        homeAwayDropdown.setAdapter(homeAwayAdapter);
        if(homeAway != null) {
            homeAwayDropdown.setSelection(homeAway.ordinal());
        } else {
            TCUserFacade userFacade = new TCUserFacade();
            MatchPlayer me = match.findPlayer(userFacade.getSelf().id);
            if(me.getHomeTeam()) {
                homeAway = Match.HomeAway.HomeMatch;
            } else {
                homeAway = Match.HomeAway.AwayMatch;
            }
            homeAwayDropdown.setSelection(homeAway.ordinal());
        }

        EditText deadlineEditText = (EditText)rootView.findViewById(R.id.edit_deadline);
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

        EditText schDtEditText = (EditText)rootView.findViewById(R.id.edit_schDate);
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

        EditText schTimeEditText = (EditText)rootView.findViewById(R.id.edit_schTime);
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

        Spinner outcomeDropdown = (Spinner)rootView.findViewById(R.id.spnr_outcome);
        String[] outcomeItems = new String[]{
                "Not Yet Played", "Completed", "Incomplete", "Home Team Retired", "Home Team Forfeited", "Visiting Team Retired", "Visiting Team Forfeited" };
        ArrayAdapter<String> outcomeAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, outcomeItems);
        outcomeDropdown.setAdapter(outcomeAdapter);

        return rootView;
    }

    class OnDeadlineDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            LocalDate deadline = new LocalDate(year, month+1, dayOfMonth);
            EditText deadlineEditText = (EditText)getView().findViewById(R.id.edit_deadline);
            deadlineEditText.setText(deadline.toString());
        }
    }

    class OnSchDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            LocalDate schDt = new LocalDate(year, month+1, dayOfMonth);
            EditText schDtEditText = (EditText)getView().findViewById(R.id.edit_schDate);
            schDtEditText.setText(schDt.toString());
        }
    }

    class OnSchTimeSetListener implements TimePickerDialog.OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker view, int hours, int minutes) {
            LocalTime schTime = new LocalTime(hours, minutes);
            EditText schTmEditText = (EditText)getView().findViewById(R.id.edit_schTime);
            schTmEditText.setText(schTime.toString());
        }
    }
}
