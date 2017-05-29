package com.logiforge.tenniscloud.activities.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.viewleaguematch.ViewLeagueMatchActivity;
import com.logiforge.tenniscloud.db.MatchTbl;
import com.logiforge.tenniscloud.model.Match;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchListFragment extends Fragment {
    private List<String> listDataHeader; // header titles
    private HashMap<String, List<Match>> listDataChild;
    MatchListAdapter listAdapter;

    public MatchListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_dashboard_frag_match_list, container, false);

        ExpandableListView expListView = (ExpandableListView) rootView.findViewById(R.id.match_list);

        // preparing list data
        prepareListData();

        listAdapter = new MatchListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Match match = (Match)listAdapter.getChild(groupPosition, childPosition);
                ViewLeagueMatchActivity.initStaticData(match);
                Activity activity = MatchListFragment.this.getActivity();
                Intent intent = new Intent(activity, ViewLeagueMatchActivity.class);
                activity.startActivityForResult(intent, DashboardActivity.REQUEST_VIEW_LEAGUE_MATCH);
                return true;
            }
        });
        for(int i=0; i < listAdapter.getGroupCount(); i++)
            expListView.expandGroup(i);

        return rootView;
    }

    public void refresh() {
        prepareListData();
        listAdapter.notifyDataSetChanged();
    }

    protected void prepareListData() {
        if(listDataHeader == null) {
            listDataHeader = new ArrayList<String>();
        } else {
            listDataHeader.clear();
        }

        if(listDataChild == null) {
            listDataChild = new HashMap<String, List<Match>>();
        } else {
            listDataChild.clear();
        }

        MatchTbl matchTbl = new MatchTbl();
        List<Match> matches = matchTbl.getAll();
        LocalDate today = LocalDate.now();
        LocalDate todayPlusEight = LocalDate.now().plusDays(8);
        for(Match match : matches) {
            if(match.getScheduledDt() == null) {
                List<Match> unscheduledList = listDataChild.get("Unscheduled");
                if(unscheduledList == null) {
                    unscheduledList = new ArrayList<Match>();
                    listDataChild.put("Unscheduled", unscheduledList);
                }
                unscheduledList.add(match);
            } else if(match.getScheduledDt().equals(today)) {
                List<Match> todayList = listDataChild.get("Scheduled Today");
                if(todayList == null) {
                    todayList = new ArrayList<Match>();
                    listDataChild.put("Scheduled Today", todayList);
                }
                todayList.add(match);
            } else if(match.getScheduledDt().isAfter(today) && match.getScheduledDt().isBefore(todayPlusEight) ) {
                List<Match> nextSevenDaysList = listDataChild.get("Scheduled Next 7 Days");
                if(nextSevenDaysList == null) {
                    nextSevenDaysList = new ArrayList<Match>();
                    listDataChild.put("Scheduled Next 7 Days", nextSevenDaysList);
                }
                nextSevenDaysList.add(match);
            }
        }

        if(listDataChild.containsKey("Unscheduled")) {
            listDataHeader.add("Unscheduled");
        }

        if(listDataChild.containsKey("Scheduled Today")) {
            listDataHeader.add("Scheduled Today");
        }

        if(listDataChild.containsKey("Scheduled Next 7 Days")) {
            listDataHeader.add("Scheduled Next 7 Days");
        }
    }
}
