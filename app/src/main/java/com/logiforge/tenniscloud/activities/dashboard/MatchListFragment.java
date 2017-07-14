package com.logiforge.tenniscloud.activities.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.viewleaguematch.ViewLeagueMatchActivity;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.model.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchListFragment extends Fragment {
    private List<String> headers;
    private HashMap<String, List<Match>> matches;

    ExpandableListView matchListView;
    MatchListAdapter matchListAdapter;

    public MatchListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_dashboard_frag_match_list, container, false);

        matchListView = (ExpandableListView) rootView.findViewById(R.id.match_list);
        prepareMatchData();

        matchListAdapter = new MatchListAdapter(getActivity(), headers, matches);
        matchListView.setAdapter(matchListAdapter);
        matchListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Match match = (Match) matchListAdapter.getChild(groupPosition, childPosition);
                ViewLeagueMatchActivity.initStaticData(match);
                Activity activity = MatchListFragment.this.getActivity();
                Intent intent = new Intent(activity, ViewLeagueMatchActivity.class);
                activity.startActivityForResult(intent, DashboardActivity.REQUEST_VIEW_LEAGUE_MATCH);
                return true;
            }
        });
        for(int i = 0; i < matchListAdapter.getGroupCount(); i++) {
            matchListView.expandGroup(i);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        DashboardActivity dashboardActivity = (DashboardActivity)context;
        dashboardActivity.matchListFragmentTag = this.getTag();
    }

    public void refresh() {
        prepareMatchData();
        matchListAdapter.notifyDataSetChanged();
    }

    protected void prepareMatchData() {
        if(headers == null) {
            headers = new ArrayList<String>();
        } else {
            headers.clear();
        }

        if(matches == null) {
            matches = new HashMap<String, List<Match>>();
        } else {
            matches.clear();
        }

        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        matchFacade.getMatchBreakdownByTime(headers, matches);
    }
}
