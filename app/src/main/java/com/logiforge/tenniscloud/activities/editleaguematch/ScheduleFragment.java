package com.logiforge.tenniscloud.activities.editleaguematch;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by iorlanov on 4/16/2017.
 */

public class ScheduleFragment extends Fragment implements View.OnClickListener {

    public interface ScheduleFragmentContext {
        void setScheduleFragmentTag(String tag);
    }

    Button addAvailabilityButton;
    ExpandableListView availabilityListView;
    AvailabilityListAdapter availabilityListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_editleaguematch_frag_schedule, container, false);
        addAvailabilityButton = (Button) rootView.findViewById(R.id.btn_add_availability);
        availabilityListView = (ExpandableListView) rootView.findViewById(R.id.availability_list);

        Match updatedMatch = EditLeagueMatchState.instance().getUpdatedMatch();


        availabilityListAdapter = new AvailabilityListAdapter(getActivity(), EditLeagueMatchState.instance());
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
        EditLeagueMatchState editState;


        public AvailabilityListAdapter(Context context, EditLeagueMatchState editState) {
            this.context = context;
            this.editState = editState;
        }

        @Override
        public int getGroupCount() {
            return editState.getAvailabilityList().size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return editState.getAvailabilityList().get(groupPosition).getTimeRanges().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return editState.getAvailabilityList().get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return editState.getAvailabilityList().get(groupPosition).getTimeRanges().get(childPosition);
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
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.act_editleaguematch_frag_schedule_child_item, null);
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
            return editState.getAvailabilityList().get(idx);
        }

        public void addAvailability(MatchAvailability availability) {
            LeagueMatchFacade.PlayerBreakdown playerBreakdown = editState.getPlayerBreakdown();
            availability.setMatchPlayerId(playerBreakdown.self.id);

            editState.getAddedAvailability().add(availability.id);
            editState.getAvailabilityList().add(availability);
            this.notifyDataSetChanged();
        }

        public void deleteAvailability(String availabilityId) {
            for(int i=0; i<editState.getAvailabilityList().size(); i++) {
                if(editState.getAvailabilityList().get(i).id.equals(availabilityId)) {
                    editState.getAvailabilityList().remove(i);
                    if(editState.getAddedAvailability().contains(availabilityId)) {
                        editState.getAddedAvailability().remove(availabilityId);
                    } else {
                        editState.getDeletedAvailability().add(availabilityId);
                        editState.getUpdatedAvailability().remove(availabilityId);
                    }
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }

        public void updateAvailability(MatchAvailability availability) {
            for(int i=0; i<editState.getAvailabilityList().size(); i++) {
                MatchAvailability availabilityToUpdate = editState.getAvailabilityList().get(i);
                if(availabilityToUpdate.id.equals(availability.id)) {
                    availabilityToUpdate.setDateRange(availability.getDateRange());
                    availabilityToUpdate.setTimeRanges(availability.getTimeRanges());
                    if(!editState.getAddedAvailability().contains(availability.id)) {
                        editState.getUpdatedAvailability().add(availability.id);
                    }
                    this.notifyDataSetChanged();
                    break;
                }
            }
        }

        private class OnEditClickListener implements View.OnClickListener {
            String availabilityId;

            public OnEditClickListener(String availabilityId) {
                this.availabilityId = availabilityId;
            }

            @Override
            public void onClick(View v) {
                for(MatchAvailability availability : editState.getAvailabilityList()) {
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
