package com.logiforge.tenniscloud.activities.editleaguematch;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade.MatchPlayerWithRole;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.util.EditableEntityList;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by iorlanov on 4/16/2017.
 */

public class ScheduleFragment extends Fragment
        implements View.OnClickListener, Spinner.OnItemSelectedListener {

    public interface ScheduleFragmentContext {
        void setScheduleFragmentTag(String tag);
    }

    Spinner playerSpinner;
    Button addAvailabilityButton;
    ExpandableListView availabilityListView;

    ArrayAdapter<MatchPlayerWithRole> playerSpinnerAdapter;
    AvailabilityListAdapter availabilityListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_editleaguematch_frag_schedule, container, false);
        playerSpinner = (Spinner) rootView.findViewById(R.id.spnr_player);
        addAvailabilityButton = (Button) rootView.findViewById(R.id.btn_add_availability);
        availabilityListView = (ExpandableListView) rootView.findViewById(R.id.availability_list);

        EditLeagueMatchState editState = EditLeagueMatchState.instance();
        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        List<MatchPlayerWithRole> playersWithRoles = matchFacade.getPlayersWithRoles(editState.getUpdatedMatch());
        playerSpinnerAdapter =
                new ArrayAdapter<MatchPlayerWithRole>(
                        this.getActivity(), android.R.layout.simple_spinner_dropdown_item, playersWithRoles);
        playerSpinner.setAdapter(playerSpinnerAdapter);
        playerSpinner.setOnItemSelectedListener(this);

        availabilityListAdapter =
                new AvailabilityListAdapter(getActivity(),
                        editState.getAvailabilityList(editState.getEditableAvailabilityPlayerId()));
        availabilityListView.setAdapter(availabilityListAdapter);

        addAvailabilityButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ScheduleFragmentContext) {
            ((ScheduleFragmentContext) context).setScheduleFragmentTag(getTag());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == addAvailabilityButton.getId()) {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            AvailabilityDlg availabilityDlg = AvailabilityDlg.newInstance(null);
            availabilityDlg.show(fm, AvailabilityDlg.DLG_TAG);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MatchPlayerWithRole playerWithRole = playerSpinnerAdapter.getItem(position);
        EditLeagueMatchState editState = EditLeagueMatchState.instance();
        availabilityListAdapter.replaceAvailabilityList(
                editState.getAvailabilityList(playerWithRole.player.id));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void deleteAvailability(String availabilityId) {
        availabilityListAdapter.deleteAvailability(availabilityId);
    }

    public void addAvailability(MatchAvailability availability) {
        availabilityListAdapter.addAvailability(availability);
    }

    public void updateAvailability(MatchAvailability availability) {
        availabilityListAdapter.updateAvailability(availability);
    }

    public class AvailabilityListAdapter extends BaseExpandableListAdapter {

        private Context context;
        EditableEntityList<MatchAvailability> availabilities;


        public AvailabilityListAdapter(Context context, EditableEntityList<MatchAvailability> availabilities) {
            this.context = context;
            this.availabilities = availabilities;
        }

        @Override
        public int getGroupCount() {
            return availabilities.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return availabilities.get(groupPosition).getTimeRanges().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return availabilities.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return availabilities.get(groupPosition).getTimeRanges().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            MatchAvailability availability = (MatchAvailability) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.act_editleaguematch_frag_schedule_group_item, null);
            }

            TextView headerView = (TextView) convertView.findViewById(R.id.lbl_group);
            headerView.setText(availability.getDateRange().toString());

            TextView editView = (TextView) convertView.findViewById(R.id.lbl_edit);
            OnEditClickListener listener = new OnEditClickListener(availability.id);
            editView.setOnClickListener(listener);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            MatchAvailability availability = (MatchAvailability) getGroup(groupPosition);
            LocalTimeRange tmRange = availability.getTimeRanges().get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.act_editleaguematch_frag_schedule_child_item, null);
            }

            TextView childDescriptionView = (TextView) convertView.findViewById(R.id.lbl_child);
            childDescriptionView.setText(tmRange.toString());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        public MatchAvailability getAvailability(int idx) {
            return availabilities.get(idx);
        }

        public void addAvailability(MatchAvailability availability) {
            availabilities.addEntity(availability);
            this.notifyDataSetChanged();
        }

        public void deleteAvailability(String availabilityId) {
            availabilities.deleteEntity(availabilityId);
            this.notifyDataSetChanged();
        }

        public void updateAvailability(MatchAvailability availability) {
            availabilities.markAsUpdated(availability);
            this.notifyDataSetChanged();
        }

        public void replaceAvailabilityList(EditableEntityList<MatchAvailability> availabilities) {
            this.availabilities = availabilities;
            this.notifyDataSetChanged();
        }

        private class OnEditClickListener implements View.OnClickListener {
            String availabilityId;

            public OnEditClickListener(String availabilityId) {
                this.availabilityId = availabilityId;
            }

            @Override
            public void onClick(View v) {
                for(MatchAvailability availability : availabilities.getEntities()) {
                    if(availability.id.equals(availabilityId)) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        AvailabilityDlg availabilityDlg =
                                AvailabilityDlg.newInstance(availability);
                        availabilityDlg.show(fm, AvailabilityDlg.DLG_TAG);
                        break;
                    }
                }
            }
        }
    }
}
