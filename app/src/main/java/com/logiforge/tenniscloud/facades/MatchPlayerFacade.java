package com.logiforge.tenniscloud.facades;

import com.logiforge.tenniscloud.db.LeagueRegistrationTbl;
import com.logiforge.tenniscloud.model.Facility;
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
        boolean resolveLeagueRegistrationFlag = false;

        public Builder(MatchPlayer player) {
            this.player = player;
        }

        public Builder resolveLeagueRegistration() {
            resolveLeagueRegistrationFlag = true;
            return this;
        }

        public MatchPlayer build() {
            if (player != null) {
                if(resolveLeagueRegistrationFlag &&
                        player.getLeagueRegistrationId() != null &&
                        player.getLeagueRegistration() == null) {
                    LeagueRegistrationTbl registrationTbl = new LeagueRegistrationTbl();
                    player.setLeagueRegistration(
                            (LeagueRegistration)registrationTbl.find(player.getLeagueRegistrationId()));
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
