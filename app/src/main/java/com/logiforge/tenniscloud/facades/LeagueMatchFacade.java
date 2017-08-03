package com.logiforge.tenniscloud.facades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbTransaction;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.tenniscloud.activities.editleaguematch.EditLeagueMatchState;
import com.logiforge.tenniscloud.db.FacilityTbl;
import com.logiforge.tenniscloud.db.LeagueFlightTbl;
import com.logiforge.tenniscloud.db.LeagueMetroAreaTbl;
import com.logiforge.tenniscloud.db.LeagueProviderTbl;
import com.logiforge.tenniscloud.db.LeagueTbl;
import com.logiforge.tenniscloud.db.MatchAvailabilityTbl;
import com.logiforge.tenniscloud.db.MatchPlayerEmailTbl;
import com.logiforge.tenniscloud.db.MatchPlayerPhoneTbl;
import com.logiforge.tenniscloud.db.MatchPlayerTbl;
import com.logiforge.tenniscloud.db.MatchTbl;
import com.logiforge.tenniscloud.db.PlayingLevelTbl;
import com.logiforge.tenniscloud.db.TCLavoltaDb;
import com.logiforge.tenniscloud.model.Facility;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueFlight;
import com.logiforge.tenniscloud.model.LeagueMetroArea;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchAvailability;
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.MatchPlayerEmail;
import com.logiforge.tenniscloud.model.MatchPlayerPhone;
import com.logiforge.tenniscloud.model.Partner;
import com.logiforge.tenniscloud.model.PartnerEmail;
import com.logiforge.tenniscloud.model.PartnerPhone;
import com.logiforge.tenniscloud.model.PlayingLevel;
import com.logiforge.tenniscloud.model.util.EditableEntityList;
import com.logiforge.tenniscloud.model.util.ListDiff;

import com.logiforge.tenniscloud.facades.MatchAvailabilityFacade.GroupAvailability;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by iorlanov on 5/2/17.
 */

public class LeagueMatchFacade {
    private static final String TAG = LeagueMatchFacade.class.getSimpleName();

    public static class Builder {
        Match match = null;
        boolean resolvePlayers = false;
        boolean resolveLeagueData = false;
        boolean resolveFacility = false;

        public Builder(Match match) {
            this.match = match;
        }

        public Builder resolvePlayers() {
            resolvePlayers = true;
            return this;
        }

        public Builder resolveLeagueData() {
            resolveLeagueData = true;
            return this;
        }

        public Builder resolveFacility() {
            resolveFacility = true;
            return this;
        }

        public Match build() {
            if(match != null) {
                if (resolvePlayers) {
                    MatchPlayerTbl playerTbl = new MatchPlayerTbl();
                    match.setPlayers(playerTbl.findPlayersByMatchId(match.id));

                    for(MatchPlayer player : match.getPlayers()) {
                        MatchPlayerFacade.Builder playerBuilder = new MatchPlayerFacade.Builder(player);
                        playerBuilder.resolveAvailability().resolveContactInfo().build();
                    }
                }

                if(resolveLeagueData) {
                    LeagueFlightTbl flightTbl = new LeagueFlightTbl();
                    LeagueFlight flight = (LeagueFlight)flightTbl.find(match.getLeagueFlightId());
                    PlayingLevelTbl levelTbl = new PlayingLevelTbl();
                    PlayingLevel level = (PlayingLevel)levelTbl.find(flight.getPlayingLevelId());
                    LeagueTbl leagueTbl = new LeagueTbl();
                    League league = (League)leagueTbl.find(flight.getLeagueId());
                    LeagueMetroAreaTbl areaTbl = new LeagueMetroAreaTbl();
                    LeagueMetroArea area = (LeagueMetroArea)areaTbl.find(league.getLeagueMetroAreaId());
                    LeagueProviderTbl providerTbl = new LeagueProviderTbl();
                    LeagueProvider provider = (LeagueProvider)providerTbl.find(area.getProviderId());

                    match.setLeagueFlight(flight);
                    flight.setPlayingLevel(level);
                    flight.setLeague(league);
                    league.setLeagueMetroArea(area);
                    area.setProvider(provider);
                }

                if(resolveFacility) {
                    if(match.getFacility() == null && match.getFacilityId() != null) {
                        FacilityTbl facilityTbl = new FacilityTbl();
                        match.setFacility((Facility)facilityTbl.find(match.getFacilityId()));
                    }
                }
            }

            return match;
        }
    }

    public static class PlayerBreakdown {
        public PlayerBreakdown(Match match) {
            TCUserFacade userFacade = new TCUserFacade();
            self = match.findPlayerByUserId(userFacade.getSelf().id);

            for(MatchPlayer player : match.getPlayers()) {
                if(!player.id.equals(self.id)) {
                    if(player.getHomeTeam() == self.getHomeTeam()) {
                        partner = player;
                    } else {
                        if(opponent1 == null) {
                            opponent1 = player;
                        } else {
                            opponent2 = player;
                        }
                    }
                }
            }
        }

        public MatchPlayer self;
        public MatchPlayer partner;
        public MatchPlayer opponent1;
        public MatchPlayer opponent2;
    }



    public static class MatchPlayerWithRole {
        public String role;
        public MatchPlayer player;

        public MatchPlayerWithRole(String role, MatchPlayer player) {
            this.role = role;
            this.player = player;
        }

        @Override
        public String toString() {
            return player.getKnownDisplayName(role);
        }
    }

    private static class MatchBreakdownBuilder {
        private static final String SCHEDULE_GROUP_UNSCHEDULED = "Unscheduled";
        private static final String SCHEDULE_GROUP_TODAY = "Scheduled Today";
        private static final String SCHEDULE_GROUP_NEXT7 = "Scheduled Next 7 Days";

        public void buildTimeBreakdown(List<String> headers, Map<String, List<Match>> matches) {
            MatchTbl matchTbl = new MatchTbl();
            List<Match> allMatches = matchTbl.getAll();
            LocalDate today = LocalDate.now();
            LocalDate todayPlusEight = LocalDate.now().plusDays(8);
            for(Match match : allMatches) {
                Builder builder = new Builder(match);
                builder.resolveLeagueData().resolvePlayers().resolveFacility().build();
                PlayerBreakdown pb = new PlayerBreakdown(match);
                if(pb.self.getSubscribed()) {

                    if (match.getScheduledDt() == null) {
                        addMatch(match, matches, SCHEDULE_GROUP_UNSCHEDULED);
                    } else if (match.getScheduledDt().equals(today)) {
                        addMatch(match, matches, SCHEDULE_GROUP_TODAY);
                    } else if (match.getScheduledDt().isAfter(today) && match.getScheduledDt().isBefore(todayPlusEight)) {
                        addMatch(match, matches, SCHEDULE_GROUP_NEXT7);
                    }
                }
            }

            if(matches.containsKey(SCHEDULE_GROUP_UNSCHEDULED)) {
                headers.add(SCHEDULE_GROUP_UNSCHEDULED);
            }

            if(matches.containsKey(SCHEDULE_GROUP_TODAY)) {
                headers.add(SCHEDULE_GROUP_TODAY);
            }

            if(matches.containsKey(SCHEDULE_GROUP_NEXT7)) {
                headers.add(SCHEDULE_GROUP_NEXT7);
            }
        }

        private void addMatch(Match match, Map<String, List<Match>> matches, String header) {
            List<Match> matchList = matches.get(header);
            if (matchList == null) {
                matchList = new ArrayList<Match>();
                matches.put(header, matchList);
            }
            matchList.add(match);
        }
    }

    public PlayerBreakdown getPlayerBreakdown(Match match) {
        PlayerBreakdown playerBreakdown = new PlayerBreakdown(match);
        return playerBreakdown;
    }

    public List<MatchPlayerWithRole> getPlayersWithRoles(Match match) {
        List<MatchPlayerWithRole> playersWithRoles = new ArrayList<>();

        PlayerBreakdown players = new PlayerBreakdown(match);
        playersWithRoles.add(new MatchPlayerWithRole(Match.PLAYER_ROLE_SELF, players.self));

        if(players.partner != null) {
            playersWithRoles.add(new MatchPlayerWithRole(Match.PLAYER_ROLE_PARTNER, players.partner));
        }

        if(players.opponent1 != null) {
            playersWithRoles.add(new MatchPlayerWithRole(Match.PLAYER_ROLE_OPPONENT1, players.opponent1));
        }

        if(players.opponent2 != null) {
            playersWithRoles.add(new MatchPlayerWithRole(Match.PLAYER_ROLE_OPPONENT2, players.opponent2));
        }

        return playersWithRoles;
    }

    public Map<String, EditableEntityList<MatchAvailability>> getEditableAvailabilityLists(Match match) {
        Map<String, EditableEntityList<MatchAvailability>> lists = new TreeMap<>();

        PlayerBreakdown players = getPlayerBreakdown(match);

        lists.put(players.self.id, new EditableEntityList<>(players.self.getAvailabilityList()));

        if(players.partner != null && players.partner.hasEditableAvailability()) {
            lists.put(players.partner.id, new EditableEntityList<>(players.partner.getAvailabilityList()));
        }

        if(players.opponent1 != null && players.opponent1.hasEditableAvailability()) {
            lists.put(players.opponent1.id, new EditableEntityList<>(players.opponent1.getAvailabilityList()));
        }

        if(players.opponent2 != null && players.opponent2.hasEditableAvailability()) {
            lists.put(players.opponent2.id, new EditableEntityList<>(players.opponent2.getAvailabilityList()));
        }

        return lists;
    }

    public Collection<GroupAvailability> getGroupAvailabilityList(Match match) {
        PlayerBreakdown players = getPlayerBreakdown(match);

        List<MatchAvailability> selfAvailabilityList = null;
        List<MatchAvailability> partnerAvailabilityList = null;
        List<MatchAvailability> opponent1AvailabilityList = null;
        List<MatchAvailability> opponent2AvailabilityList = null;

        if(players.self.getAvailabilityList() != null) {
            selfAvailabilityList = players.self.getAvailabilityList();
        }

        if(players.partner != null && players.partner.getAvailabilityList() != null) {
            partnerAvailabilityList = players.partner.getAvailabilityList();
        }

        if(players.opponent1 != null && players.opponent1.getAvailabilityList() != null) {
            opponent1AvailabilityList = players.opponent1.getAvailabilityList();
        }

        if(players.opponent2 != null && players.opponent2.getAvailabilityList() != null) {
            opponent2AvailabilityList = players.opponent2.getAvailabilityList();
        }

        MatchAvailabilityFacade availabilityFacade = new MatchAvailabilityFacade();

        return availabilityFacade.getGroupAvailabilityList(
                selfAvailabilityList,
                partnerAvailabilityList,
                opponent1AvailabilityList,
                opponent2AvailabilityList);
    }

    public void getMatchBreakdownByTime(List<String> headers, Map<String, List<Match>> matches) {
        MatchBreakdownBuilder builder = new MatchBreakdownBuilder();
        builder.buildTimeBreakdown(headers, matches);
    }

    public void createLeagueMatch(
            final Context context, final Match match, final LeagueRegistration registration,
            final Match.HomeAway homeAway, final String opponentEmail, final String opponent2Email) {
        //DummyTask task = new DummyTask(context, "Creating league match ...");
        //task.execute();

        League league = registration.getLeagueFlight().getLeague();
        Partner partner = registration.getPartner();

        // populate known/default match attributes
        match.setMatchType(Match.MATCH_TYPE_LEAGUE_MATCH);
        match.setMatchFormat(Match.MATCH_FORMAT_REGULAR_BESTOF3);

        // flight
        match.setLeagueFlight(registration.getLeagueFlight());

        // facility
        if(homeAway == Match.HomeAway.HomeMatch) {
            match.setFacility(registration.getFacility());
        }

        // PLAYERS
        List<MatchPlayer> players = new ArrayList<MatchPlayer>();
        // self
        MatchPlayer me = new MatchPlayer(null, true,
                homeAway == Match.HomeAway.HomeMatch,
                null,
                null, null, registration.getLeagueProfileId(), registration.id);
        players.add(me);
        // opponent
        MatchPlayer opponent = new MatchPlayer(null, false,
                !(homeAway == Match.HomeAway.HomeMatch),
                null, null, null, null, null);
        List<MatchPlayerEmail> opponentEmails = new ArrayList<MatchPlayerEmail>();
        MatchPlayerEmail opponentMatchPlayerEmail = new MatchPlayerEmail(null, opponentEmail);
        opponentEmails.add(opponentMatchPlayerEmail);
        opponent.setEmails(opponentEmails);
        players.add(opponent);

        // partner and opponent 2
        MatchPlayer partnerMatchPlayer = null;
        MatchPlayer opponent2 = null;
        MatchPlayerEmail opponent2MatchPlayerEmail = null;
        if(league.getTeamType() == League.TEAM_TYPE_DOUBLES) {
            // partner
            partnerMatchPlayer =
                    new MatchPlayer(null, false,
                            me.getHomeTeam(),
                            partner.getDisplayName(), null, null, null, null);
            List<MatchPlayerEmail> partnerEmails = new ArrayList<MatchPlayerEmail>();
            for(PartnerEmail pEmail : partner.getEmails()) {
                partnerEmails.add(new MatchPlayerEmail(null, pEmail.getEmail()));
            }
            partnerMatchPlayer.setEmails(partnerEmails);
            List<MatchPlayerPhone> partnerPhones = new ArrayList<MatchPlayerPhone>();
            for(PartnerPhone pPhone : partner.getPhones()) {
                partnerPhones.add(new MatchPlayerPhone(null, pPhone.getPhone(), pPhone.getPhoneType()));
            }
            partnerMatchPlayer.setPhones(partnerPhones);
            players.add(partnerMatchPlayer);

            // opponent 2
            opponent2 = new MatchPlayer(null, false,
                    opponent.getHomeTeam(),
                    null, null, null, null, null);
            List<MatchPlayerEmail> opponent2Emails = new ArrayList<MatchPlayerEmail>();
            opponent2MatchPlayerEmail = new MatchPlayerEmail(null, opponent2Email);
            opponent2Emails.add(opponent2MatchPlayerEmail);
            opponent2.setEmails(opponent2Emails);
            players.add(opponent2);
        }

        match.setPlayers(players);

        LavoltaDb db = Lavolta.db();
        DbTransaction txn = null;
        try {
            txn = db.beginUiTxn(TCLavoltaDb.ACTION_NEW_LEAGUE_MATCH);

            MatchTbl matchTbl = new MatchTbl();
            matchTbl.uiAdd(txn, match, null);

            MatchPlayerTbl matchPlayerTbl = new MatchPlayerTbl();
            me.setMatchId(match.id);
            matchPlayerTbl.uiAdd(txn, me, null);
            opponent.setMatchId(match.id);
            matchPlayerTbl.uiAdd(txn, opponent, null);

            MatchPlayerEmailTbl emailTbl = new MatchPlayerEmailTbl();
            opponentMatchPlayerEmail.setMatchPlayerId(opponent.id);
            emailTbl.uiAdd(txn, opponentMatchPlayerEmail, null);

            if(partnerMatchPlayer != null) {
                partnerMatchPlayer.setMatchId(match.id);
                matchPlayerTbl.uiAdd(txn, partnerMatchPlayer, null);

                List<MatchPlayerEmail> partnerEmails = partnerMatchPlayer.getEmails();
                for (MatchPlayerEmail email : partnerEmails) {
                    email.setMatchPlayerId(partnerMatchPlayer.id);
                    emailTbl.uiAdd(txn, email, null);
                }

                MatchPlayerPhoneTbl phoneTbl = new MatchPlayerPhoneTbl();
                List<MatchPlayerPhone> partnerPhones = partnerMatchPlayer.getPhones();
                for (MatchPlayerPhone phone : partnerPhones) {
                    phone.setMatchPlayertId(partnerMatchPlayer.id);
                    phoneTbl.uiAdd(txn, phone, null);
                }
            }

            if(opponent2 != null) {
                opponent2.setMatchId(match.id);
                matchPlayerTbl.uiAdd(txn, opponent2, null);

                opponent2MatchPlayerEmail.setMatchPlayerId(opponent2.id);
                emailTbl.uiAdd(txn, opponent2MatchPlayerEmail, null);
            }

            db.commitTxn(txn);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
        } finally {
            db.endTxn(txn, false);
        }
    }

    public boolean updateLeagueMatch(
            final Context context, EditLeagueMatchState editState) {
        LavoltaDb db = Lavolta.db();
        DbTransaction txn = null;
        try {
            txn = db.beginUiTxn(TCLavoltaDb.ACTION_UPDATE_LEAGUE_MATCH);

            if(editState.getUpdatedMatch().isDifferent(editState.getMatch())) {
                MatchTbl matchTbl = new MatchTbl();
                matchTbl.uiUpdate(txn, editState.getUpdatedMatch(), null, null);
            }

            MatchPlayerTbl playerTbl = new MatchPlayerTbl();
            MatchAvailabilityTbl availabilityTbl = new MatchAvailabilityTbl();
            MatchAvailabilityFacade availabilityFacade = new MatchAvailabilityFacade();

            for(MatchPlayer updatedPlayer : editState.getUpdatedMatch().getPlayers()) {
                MatchPlayer originalPlayer = editState.getMatch().findPlayerByPlayerId(updatedPlayer.id);

                if(updatedPlayer.isDifferent(originalPlayer)) {
                    playerTbl.uiUpdate(txn, updatedPlayer, null, null);
                }

                if(originalPlayer.hasEditableContctInfo()) {
                    ListDiff<MatchPlayerEmail> emailDiff =
                            originalPlayer.getEmailDiff(updatedPlayer.getEmails());
                    if (emailDiff.hasDifference()) {
                        MatchPlayerEmailTbl emailTbl = new MatchPlayerEmailTbl();
                        for (MatchPlayerEmail email : emailDiff.deleted) {
                            emailTbl.uiDelete(txn, email.id, null);
                        }
                        for (MatchPlayerEmail email : emailDiff.added) {
                            emailTbl.uiAdd(txn, email, null);
                        }
                    }

                    ListDiff<MatchPlayerPhone> phoneDiff =
                            originalPlayer.getPhoneDiff(updatedPlayer.getPhones());
                    if (phoneDiff.hasDifference()) {
                        MatchPlayerPhoneTbl phoneTbl = new MatchPlayerPhoneTbl();
                        for (MatchPlayerPhone phone : phoneDiff.deleted) {
                            phoneTbl.uiDelete(txn, phone.id, null);
                        }
                        for (MatchPlayerPhone phone : phoneDiff.added) {
                            phoneTbl.uiAdd(txn, phone, null);
                        }
                        for (ListDiff.UpdatedEntity<MatchPlayerPhone> ue : phoneDiff.updated) {
                            ue.updated.copyLavoltaAttributes(ue.original);
                            phoneTbl.uiUpdate(txn, ue.updated, null, null);
                        }
                    }
                }

                EditableEntityList<MatchAvailability> availabilityList = editState.getAvailabilityList(updatedPlayer.id);
                availabilityFacade.normalize(availabilityList);

                if(availabilityList != null) {
                    for (MatchAvailability availability : availabilityList.getAddedEntities()) {
                        availability.setMatchPlayerId(updatedPlayer.id);
                        availabilityTbl.uiAdd(txn, availability, null);
                    }

                    for (MatchAvailability availability : availabilityList.getUpdatedEntities()) {
                        availabilityTbl.uiUpdate(txn, availability, null, null);
                    }

                    for (String availabilityId : availabilityList.getDeletedEntityIds()) {
                        availabilityTbl.uiDelete(txn, availabilityId, null);
                    }
                }
            }

            db.commitTxn(txn);
            editState.setMatchChanged(true);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
            return false;
        } finally {
            db.endTxn(txn, false);
        }
    }

    public boolean unsubscribeFromLeagueMatch(
            final Context context, final Match match) {
        PlayerBreakdown playerBreakdown = getPlayerBreakdown(match);

        LavoltaDb db = Lavolta.db();
        DbTransaction txn = null;
        try {
            txn = db.beginUiTxn(TCLavoltaDb.ACTION_UNSUBSCRIBE_FROM_LEAGUE_MATCH);

            playerBreakdown.self.setSubscribed(false);
            MatchPlayerTbl matchPlayerTbl = new MatchPlayerTbl();
            matchPlayerTbl.uiUpdate(txn, playerBreakdown.self, null, null);

            db.commitTxn(txn);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
            playerBreakdown.self.setSubscribed(true);
            return false;
        } finally {
            db.endTxn(txn, false);
        }
    }

    public void setPlayerHomeTeam(Match match, Match.HomeAway homeAway) {
        PlayerBreakdown playerBreakdown = new PlayerBreakdown(match);

        playerBreakdown.self.setHomeTeam(homeAway == Match.HomeAway.HomeMatch);
        if(playerBreakdown.partner != null) {
            playerBreakdown.partner.setHomeTeam(homeAway == Match.HomeAway.HomeMatch);
        }
        if(playerBreakdown.opponent1 != null) {
            playerBreakdown.opponent1.setHomeTeam(homeAway == Match.HomeAway.AwayMatch);
        }
        if(playerBreakdown.opponent2 != null) {
            playerBreakdown.opponent2.setHomeTeam(homeAway == Match.HomeAway.AwayMatch);
        }
    }

    static private class DummyTask extends AsyncTask<Void, Void, Void> {
        private Context context;
        private String message;
        private ProgressDialog dlg;

        public DummyTask(Context context, String message) {
            this.context = context;
            this.message = message;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch(Exception e) {}

            return null;
        }

        @Override
        protected void onPreExecute() {
            dlg = new ProgressDialog(context);
            dlg.setMessage(message);
            dlg.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(dlg.isShowing()) {
                dlg.dismiss();
            }
        }
    }
}
