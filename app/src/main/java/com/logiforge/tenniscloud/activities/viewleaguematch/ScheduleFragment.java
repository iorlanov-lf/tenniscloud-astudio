package com.logiforge.tenniscloud.activities.viewleaguematch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.editleaguematch.EditLeagueMatchState;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade.PlayerBreakdown;
import com.logiforge.tenniscloud.facades.MatchAvailabilityFacade.GroupAvailability;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.util.LocalTimeRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/16/2017.
 */

public class ScheduleFragment extends Fragment {
    GroupAvailabilityListAdapter groupAvailabilityListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_viewleaguematch_frag_schedule, container, false);

        ListView groupAvailabilityListView = (ListView) rootView.findViewById(R.id.list_group_availability);
        groupAvailabilityListAdapter = new GroupAvailabilityListAdapter(getActivity(), getGroupAvailabilityList());
        groupAvailabilityListView.setAdapter(groupAvailabilityListAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ViewLeagueMatchActivity activity = (ViewLeagueMatchActivity)context;
        activity.scheduleFragmentTag = this.getTag();
    }

    public void populateControls() {
        groupAvailabilityListAdapter.replaceAvailablityList(getGroupAvailabilityList());
    }

    private List<GroupAvailability> getGroupAvailabilityList() {
        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        List<GroupAvailability> groupAvailabilityList =
                new ArrayList<>(matchFacade.getGroupAvailabilityList(EditLeagueMatchState.instance().getUpdatedMatch()));
        return groupAvailabilityList;
    }

    public static class GroupAvailabilityListAdapter extends ArrayAdapter<GroupAvailability> {

        public GroupAvailabilityListAdapter(Context context, List<GroupAvailability> groupAvailabilityList) {
            super(context, R.layout.act_viewleaguematch_frag_schedule_item, groupAvailabilityList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            PlayerBreakdown players = EditLeagueMatchState.instance().getPlayerBreakdown();
            GroupAvailability groupAvailability = getItem(position); //groupAvailabilityArray[position];
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.act_viewleaguematch_frag_schedule_item, null);
            }

            TextView dateRangeTextView = (TextView)convertView.findViewById(R.id.txt_group_availability_dtRange);
            dateRangeTextView.setText(groupAvailability.dateRange.toString());

            StringBuilder playerSb = new StringBuilder();
            StringBuilder tmRangesSb = new StringBuilder();
            if(groupAvailability.selfTimeRanges != null && groupAvailability.selfTimeRanges.size() > 0) {
                for(int i=0; i<groupAvailability.selfTimeRanges.size(); i++) {
                    LocalTimeRange tmRange = groupAvailability.selfTimeRanges.get(i);
                    if(i == 0) {
                        playerSb.append(players.self.getKnownDisplayName(Match.PLAYER_ROLE_SELF));
                        tmRangesSb.append(tmRange.toString());
                    } else {
                        playerSb.append("\n");
                        tmRangesSb.append("\n" + tmRange.toString());
                    }
                }
            }
            if(groupAvailability.partnerTimeRanges != null && groupAvailability.partnerTimeRanges.size() > 0) {
                for(int i=0; i<groupAvailability.partnerTimeRanges.size(); i++) {
                    LocalTimeRange tmRange = groupAvailability.partnerTimeRanges.get(i);
                    if(i == 0) {
                        if(playerSb.length() > 0) {
                            playerSb.append("\n");
                            tmRangesSb.append("\n");
                        }
                        playerSb.append(players.partner.getKnownDisplayName(Match.PLAYER_ROLE_PARTNER));
                        tmRangesSb.append(tmRange.toString());
                    } else {
                        playerSb.append("\n");
                        tmRangesSb.append("\n" + tmRange.toString());
                    }
                }
            }
            if(groupAvailability.opponent1TimeRanges != null && groupAvailability.opponent1TimeRanges.size() > 0) {
                for(int i=0; i<groupAvailability.opponent1TimeRanges.size(); i++) {
                    LocalTimeRange tmRange = groupAvailability.opponent1TimeRanges.get(i);
                    if(i == 0) {
                        if(playerSb.length() > 0) {
                            playerSb.append("\n");
                            tmRangesSb.append("\n");
                        }
                        playerSb.append(players.opponent1.getKnownDisplayName(Match.PLAYER_ROLE_OPPONENT1));
                        tmRangesSb.append(tmRange.toString());
                    } else {
                        playerSb.append("\n");
                        tmRangesSb.append("\n" + tmRange.toString());
                    }
                }
            }
            if(groupAvailability.opponent2TimeRanges != null && groupAvailability.opponent2TimeRanges.size() > 0) {
                for(int i=0; i<groupAvailability.opponent2TimeRanges.size(); i++) {
                    LocalTimeRange tmRange = groupAvailability.opponent2TimeRanges.get(i);
                    if(i == 0) {
                        if(playerSb.length() > 0) {
                            playerSb.append("\n");
                            tmRangesSb.append("\n");
                        }
                        playerSb.append(players.opponent2.getKnownDisplayName(Match.PLAYER_ROLE_OPPONENT2));
                        tmRangesSb.append(tmRange.toString());
                    } else {
                        playerSb.append("\n");
                        tmRangesSb.append("\n" + tmRange.toString());
                    }
                }
            }

            TextView playerTextView = (TextView)convertView.findViewById(R.id.txt_group_availability_player);
            playerTextView.setText(playerSb.toString());
            TextView tmRangeTextView = (TextView)convertView.findViewById(R.id.txt_group_availability_tmRange);
            tmRangeTextView.setText(tmRangesSb.toString());

            return convertView;
        }

        public void replaceAvailablityList(List<GroupAvailability> groupAvailabilityList) {
            clear();
            addAll(groupAvailabilityList);
            notifyDataSetChanged();
        }
    }
}
