package com.logiforge.tenniscloud.facades;

import com.logiforge.tenniscloud.db.LeagueProviderTbl;
import com.logiforge.tenniscloud.db.LeagueRegistrationTbl;
import com.logiforge.tenniscloud.db.MatchAvailabilityTbl;
import com.logiforge.tenniscloud.db.MatchPlayerEmailTbl;
import com.logiforge.tenniscloud.db.MatchPlayerPhoneTbl;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.LeagueProfile;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.MatchPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 6/13/17.
 */

public class MatchPlayerFacade {

    public static class Builder {
        MatchPlayer player = null;
        boolean resolveContactInfo = false;
        boolean resolveLeagueRegistration = false;
        boolean resolveAvailability = false;

        public Builder(MatchPlayer player) {
            this.player = player;
        }

        public Builder resolveContactInfo() {
            resolveContactInfo = true;
            return this;
        }

        public Builder resolveLeagueRegistration() {
            resolveLeagueRegistration = true;
            return this;
        }

        public Builder resolveAvailability() {
            resolveAvailability = true;
            return this;
        }

        public MatchPlayer build() {
            if (player != null) {
                if(resolveContactInfo) {
                    if(player.getLeagueProfileId() == null) {
                        MatchPlayerEmailTbl emailTbl = new MatchPlayerEmailTbl();
                        player.setEmails(emailTbl.findEmailsByPlayerId(player.id));

                        MatchPlayerPhoneTbl phoneTbl = new MatchPlayerPhoneTbl();
                        player.setPhones(phoneTbl.findPhonesByPlayerId(player.id));
                    } else if(player.getLeagueProfile() == null) {
                        LeagueProviderTbl profileTbl = new LeagueProviderTbl();
                        LeagueProfile profile = (LeagueProfile)profileTbl.find(player.getLeagueProfileId());
                        LeagueProfileFacade.Builder profileBuilder = new LeagueProfileFacade.Builder(profile);
                        profileBuilder.resolveEmailsAndPhones().build();
                        player.setLeagueProfile(profile);
                    }
                }

                if(resolveLeagueRegistration &&
                        player.getLeagueRegistrationId() != null &&
                        player.getLeagueRegistration() == null) {
                    LeagueRegistrationTbl registrationTbl = new LeagueRegistrationTbl();
                    player.setLeagueRegistration(
                            (LeagueRegistration)registrationTbl.find(player.getLeagueRegistrationId()));
                }

                if(resolveAvailability && player.getAvailabilityList() == null) {
                    MatchAvailabilityTbl availabilityTbl = new MatchAvailabilityTbl();
                    player.setAvailabilityList(availabilityTbl.getPlayerAvailability(player.id));
                }
            }

            return player;
        }
    }

    public List<Facility> getPlayerFacilities(MatchPlayer player) {
        List<Facility> facilities = new ArrayList<Facility>();
        Builder builder = new Builder(player);
        builder.resolveLeagueRegistration().build();
        LeagueRegistration registration = player.getLeagueRegistration();
        if(registration != null) {
            LeagueRegistrationFacade.Builder registrationBuilder =
                    new LeagueRegistrationFacade.Builder(registration);
            registrationBuilder.resolveFacility().build();
            if(registration.getFacility() != null) {
                facilities.add(registration.getFacility());
            }
        }

        return facilities;
    }
}
