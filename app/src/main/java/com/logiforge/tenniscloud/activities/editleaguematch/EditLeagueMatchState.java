package com.logiforge.tenniscloud.activities.editleaguematch;

import com.logiforge.tenniscloud.facades.LeagueMatchFacade;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.util.EditableEntityList;

import java.util.List;
import java.util.Map;
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

        LeagueMatchFacade matchFacade = new LeagueMatchFacade();
        instance.playerBreakdown =
                matchFacade.getPlayerBreakdown(instance.updatedMatch);

        instance.editableAvailabilityLists =
                matchFacade.getEditableAvailabilityLists(instance.updatedMatch);
        instance.editableAvailabilityPlayerId = instance.playerBreakdown.self.id;


        return instance;
    }

    public static EditLeagueMatchState instance() {
        return instance;
    }

    private Match match;
    private Match updatedMatch;
    private boolean matchChanged;
    private LeagueMatchFacade.PlayerBreakdown playerBreakdown;
    private Map<String, EditableEntityList<MatchAvailability>> editableAvailabilityLists;
    private String editableAvailabilityPlayerId;

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

    public EditableEntityList<MatchAvailability> getAvailabilityList(String playerId) {
        return editableAvailabilityLists.get(playerId);
    }

    public String getEditableAvailabilityPlayerId() {
        return editableAvailabilityPlayerId;
    }

    public void setEditableAvailabilityPlayerId(String editableAvailabilityPlayerId) {
        this.editableAvailabilityPlayerId = editableAvailabilityPlayerId;
    }
}
