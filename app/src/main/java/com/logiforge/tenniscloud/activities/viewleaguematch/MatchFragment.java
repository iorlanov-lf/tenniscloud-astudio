package com.logiforge.tenniscloud.activities.viewleaguematch;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.editleaguematch.EditLeagueMatchState;
import com.logiforge.tenniscloud.db.util.DbUtil;
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
    private static final String KEY_OUTCOME_SPINNER = "KEY_OUTCOME_SPINNER";
    private static final String KEY_SCORE_SPINNER = "KEY_OUTCOME_SPINNER_";
    public static final String TIME_FORMAT = "kk:mm a";
    public static final String DATE_FORMAT = "MM/dd/yyyy";

    TextView matchWeekText = null;
    TextView homeAwayText = null;
    TextView outcomeText = null;
    TextView deadlineText = null;
    TextView schDtText = null;
    TextView schTimeText = null;
    TextView facilityText = null;
    List<TextView> scoreTexts = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_viewleaguematch_frag_match, container, false);

        matchWeekText = (TextView) rootView.findViewById(R.id.txt_matchWeek);
        homeAwayText = (TextView)rootView.findViewById(R.id.txt_homeAway);
        outcomeText = (TextView)rootView.findViewById(R.id.txt_outcome);
        deadlineText = (TextView)rootView.findViewById(R.id.txt_deadline);
        schDtText = (TextView)rootView.findViewById(R.id.txt_schDate);
        schTimeText = (TextView)rootView.findViewById(R.id.txt_schTime);
        facilityText = (TextView)rootView.findViewById(R.id.txt_facility);
        scoreTexts = new ArrayList<TextView>();
        scoreTexts.add((TextView)rootView.findViewById(R.id.txt_scoreHome1));
        scoreTexts.add((TextView)rootView.findViewById(R.id.txt_scoreVisitor1));
        scoreTexts.add((TextView)rootView.findViewById(R.id.txt_scoreHome2));
        scoreTexts.add((TextView)rootView.findViewById(R.id.txt_scoreVisitor2));
        scoreTexts.add((TextView)rootView.findViewById(R.id.txt_scoreHome3));
        scoreTexts.add((TextView)rootView.findViewById(R.id.txt_scoreVisitor3));

        populateControls();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ViewLeagueMatchActivity activity = (ViewLeagueMatchActivity)context;
        activity.matchFragmentTag = this.getTag();
    }

    public void populateControls() {
        Match match = EditLeagueMatchState.instance().getMatch();

        Match.MatchWeek matchWeek = Match.MatchWeek.getById(match.getLeagueWeek());
        matchWeekText.setText(matchWeek.toString());

        TCUserFacade userFacade = new TCUserFacade();
        MatchPlayer me = match.findPlayerByUserId(userFacade.getSelf().id);
        if(me.getHomeTeam()) {
            homeAwayText.setText(Match.HomeAway.HomeMatch.toString());
        } else {
            homeAwayText.setText(Match.HomeAway.AwayMatch.toString());
        }

        LocalDate deadline = match.getDeadlineDt();
        if(deadline != null) {
            deadlineText.setText(deadline.toString(DATE_FORMAT));
        } else {
            deadlineText.setText(null);
        }

        LocalDate schDt = match.getScheduledDt();
        if(schDt != null) {
            schDtText.setText(schDt.toString(DATE_FORMAT));
        } else {
            schDtText.setText(null);
        }

        LocalTime schTm = match.getScheduledTm();
        if(schTm != null) {
            schTimeText.setText(schTm.toString(TIME_FORMAT));
        } else {
            schTimeText.setText(null);
        }

        Facility facility = match.getFacility();
        if(facility != null) {
            facilityText.setText(facility.getName());
        } else {
            facilityText.setText(null);
        }

        if(match.getOutcome() != null) {
            Match.Outcome outcome = Match.Outcome.getById(match.getOutcome());
            outcomeText.setText(outcome.toString());
        } else {
            outcomeText.setText(null);
        }

        Integer[] points = match.getPoints();
        for(int i=0; i<6; i++) {
            scoreTexts.get(i).setText(points[i].toString());
        }
    }
}
