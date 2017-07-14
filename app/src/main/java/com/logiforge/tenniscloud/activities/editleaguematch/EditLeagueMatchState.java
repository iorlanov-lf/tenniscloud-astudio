package com.logiforge.tenniscloud.activities.editleaguematch;

import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchAvailability;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by iorlanov on 7/12/17.
 */

public class EditLeagueMatchState {
    private static EditLeagueMatchState instance;

    public static EditLeagueMatchState initInstance(Match match) {
        LeagueMatchFacade.Builder matchBuilder = new LeagueMatchFacade.Builder(match);
        matchBuilder.resolvePlayers().resolveLeagueData().resolveFacility().build();

        if(instance == null) {
            instance = new EditLeagueMatchState();
        }

        instance.match = match;
        instance.updatedMatch = new Match(match);
        instance.matchChanged = false;
        instance.addedAvailability = new TreeSet<String>();
        instance.updatedAvailability = new TreeSet<String>();
        instance.deletedAvailability = new TreeSet<String>();

        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        instance.playerBreakdown =
                matchFacade.getPlayerBreakdown(instance.updatedMatch);
        instance.availabilityList = instance.playerBreakdown.self.getAvailabilityList();

        return instance;
    }

    public static EditLeagueMatchState instance() {
        return instance;
    }

    Match match;
    Match updatedMatch;
    boolean matchChanged;
    LeagueMatchFacade.PlayerBreakdown playerBreakdown;
    Set<String> addedAvailability;
    Set<String> updatedAvailability;
    Set<String> deletedAvailability;
    List<MatchAvailability> availabilityList;

    public Match getMatch() {
        return match;
    }

    public Match getUpdatedMatch() {
        return updatedMatch;
    }

    public boolean isMatchChanged() {
        return matchChanged;
    }

    public LeagueMatchFacade.PlayerBreakdown getPlayerBreakdown() {
        return playerBreakdown;
    }

    public void setMatchChanged(boolean matchChanged) {
        this.matchChanged = matchChanged;
    }

    public Set<String> getAddedAvailability() {
        return addedAvailability;
    }

    public Set<String> getUpdatedAvailability() {
        return updatedAvailability;
    }

    public Set<String> getDeletedAvailability() {
        return deletedAvailability;
    }

    public List<MatchAvailability> getAvailabilityList() {
        return availabilityList;
    }

    public MatchAvailability getAvailability(String availabilityId) {
        for(MatchAvailability availability : availabilityList) {
            if(availability.id.equals(availabilityId)) {
                return availability;
            }
        }

        return null;
    }
}
