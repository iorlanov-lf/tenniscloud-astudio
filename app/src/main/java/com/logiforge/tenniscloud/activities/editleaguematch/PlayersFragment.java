package com.logiforge.tenniscloud.activities.editleaguematch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.activities.util.ContactInfoView;
import com.logiforge.tenniscloud.activities.util.EmailListView;
import com.logiforge.tenniscloud.activities.util.PhoneListView;
import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.MatchPlayerEmail;
import com.logiforge.tenniscloud.model.MatchPlayerPhone;
import com.logiforge.tenniscloud.model.PartnerEmail;
import com.logiforge.tenniscloud.model.util.Phone;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 4/16/2017.
 */

public class PlayersFragment extends Fragment {
    private Match match;

    ContactInfoView partnerView;
    ContactInfoView opponent1View;
    ContactInfoView opponent2View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.act_editleaguematch_frag_players, container, false);

        match = EditLeagueMatchActivity.match;

        partnerView = (ContactInfoView)rootView.findViewById(R.id.view_partner);
        opponent1View = (ContactInfoView)rootView.findViewById(R.id.view_opponent1);
        opponent2View = (ContactInfoView)rootView.findViewById(R.id.view_opponent2);

        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        LeagueMatchFacade.PlayerBreakdown playerBreakdown = matchFacade.getPlayerBreakdown(match);


        MatchPlayer partner = playerBreakdown.partner;
        if(partner != null) {
            if(savedInstanceState == null) {

                if(partner.getLeagueProfileId() == null) {
                    partnerView.initName(partner.getDisplayName());
                    partnerView.initEmails(partner.getEmailsAsStrings());
                    partnerView.initPhones(partner.getPhonesAsUtilPhones());
                } else {
                    //TODO: populate from the profile

                    partnerView.setEnabled(false);
                }

            }
        } else {
            partnerView.setVisibility(View.GONE);
        }

        MatchPlayer opponent1 = playerBreakdown.opponent1;
        if(opponent1 != null) {
            if(savedInstanceState == null) {

                if(opponent1.getLeagueProfileId() == null) {
                    opponent1View.initName(opponent1.getDisplayName());
                    opponent1View.initEmails(opponent1.getEmailsAsStrings());
                    opponent1View.initPhones(opponent1.getPhonesAsUtilPhones());

                } else {
                    //TODO: populate from the profile

                    opponent1View.setEnabled(false);
                }

            }
        } else {
            opponent1View.setVisibility(View.GONE);
        }

        MatchPlayer opponent2 = playerBreakdown.opponent2;
        if(opponent2 != null) {
            if(savedInstanceState == null) {

                if(opponent2.getLeagueProfileId() == null) {
                    opponent2View.initName(opponent2.getDisplayName());
                    opponent2View.initEmails(opponent2.getEmailsAsStrings());
                    opponent2View.initPhones(opponent2.getPhonesAsUtilPhones());

                } else {
                    //TODO: populate from the profile

                    opponent2View.setEnabled(false);
                }

            }
        } else {
            opponent2View.setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        EditLeagueMatchActivity activity = (EditLeagueMatchActivity)context;
        activity.playersFragmentTag = this.getTag();
    }

    public boolean validate() {
        return true;
    }
}
