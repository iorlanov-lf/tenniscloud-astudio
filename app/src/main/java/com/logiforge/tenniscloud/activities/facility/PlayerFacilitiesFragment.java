package com.logiforge.tenniscloud.activities.facility;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.facades.MatchPlayerFacade;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iorlanov on 6/13/17.
 */

public class PlayerFacilitiesFragment extends Fragment {
    public interface OnPlayerFacilitySelectedListener {
        void onPlayerFacilitySelected(Facility facility);
    }

    static Match match;
    static public void initStaticData(Match match) {
        PlayerFacilitiesFragment.match = match;
    }

    List<String> headers;
    Map<String, List<Facility>> facilities;
    FacilityListAdapter listAdapter;

    public PlayerFacilitiesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.dlg_matchfacility_frag_playerfacilities, container, false);

        populateList();
        listAdapter = new FacilityListAdapter(getActivity(), headers, facilities);
        ExpandableListView expListView = (ExpandableListView) rootView.findViewById(R.id.player_facility_list);
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Facility facility = (Facility) listAdapter.getChild(groupPosition, childPosition);
                OnPlayerFacilitySelectedListener listener =
                        (OnPlayerFacilitySelectedListener)PlayerFacilitiesFragment.this.getParentFragment();
                listener.onPlayerFacilitySelected(facility);
                return true;
            }
        });

        return rootView;
    }

    private void populateList() {
        if(headers == null) {
            headers = new ArrayList<String>();
        } else {
            headers.clear();
        }
        if(facilities == null) {
            facilities = new HashMap<String, List<Facility>>();
        } else {
            facilities.clear();
        }

        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        MatchPlayerFacade playerFacade = new MatchPlayerFacade();
        LeagueMatchFacade.PlayerBreakdown playerBreakdown = matchFacade.getPlayerBreakdown(match);
        List<Facility> playerFacilities = playerFacade.getPlayerFacilities(playerBreakdown.self);
        if(playerFacilities.size() > 0) {
            headers.add("You");
            facilities.put("You", playerFacilities);
        }
    }

    public static class FacilityListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> listDataHeader;
        private Map<String, List<Facility>> listDataChild;

        public FacilityListAdapter(Context context, List<String> listDataHeader,
                                   Map<String, List<Facility>> listChildData) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final Facility facility = (Facility) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dlg_matchfacility_frag_playerfacilities_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(facility.getName());

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this.listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.dlg_matchfacility_frag_playerfacilities_group, null);
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }
    }
}
