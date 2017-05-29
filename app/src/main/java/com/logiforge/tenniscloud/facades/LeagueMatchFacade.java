package com.logiforge.tenniscloud.facades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbTransaction;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.tenniscloud.db.LeagueFlightTbl;
import com.logiforge.tenniscloud.db.LeagueMetroAreaTbl;
import com.logiforge.tenniscloud.db.LeagueProviderTbl;
import com.logiforge.tenniscloud.db.LeagueTbl;
import com.logiforge.tenniscloud.db.MatchPlayerEmailTbl;
import com.logiforge.tenniscloud.db.MatchPlayerTbl;
import com.logiforge.tenniscloud.db.MatchTbl;
import com.logiforge.tenniscloud.db.PlayingLevelTbl;
import com.logiforge.tenniscloud.db.TCLavoltaDb;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueFlight;
import com.logiforge.tenniscloud.model.LeagueMetroArea;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.MatchPlayerEmail;
import com.logiforge.tenniscloud.model.Partner;
import com.logiforge.tenniscloud.model.PartnerEmail;
import com.logiforge.tenniscloud.model.PlayingLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 5/2/17.
 */

public class LeagueMatchFacade {
    private static final String TAG = LeagueMatchFacade.class.getSimpleName();

    public static class Builder {
        Match match = null;
        boolean resolvePlayersFlag = false;
        boolean resolveLeagueData = false;

        public Builder(Match match) {
            this.match = match;
        }

        public Builder resolvePlayers() {
            resolvePlayersFlag = true;
            return this;
        }

        public Builder resolveLeagueData() {
            resolveLeagueData = true;
            return this;
        }

        public Match build() {
            if(match != null) {
                if (resolvePlayersFlag) {
                    MatchPlayerTbl playerTbl = new MatchPlayerTbl();
                    match.setPlayers(playerTbl.findPlayersByMatchId(match.id));
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
            }

            return match;
        }
    }

    public void createLeagueMatch(
            final Context context, final Match match, final LeagueRegistration registration,
            final Match.HomeAway homeAway, final String opponentEmail, final String opponent2Email) {
        DummyTask task = new DummyTask(context, "Creating singles match ...");
        task.execute();

        League league = registration.getLeagueFlight().getLeague();
        Partner partner = registration.getPartner();

        // populate known/default match attributes
        match.setMatchType(Match.MATCH_TYPE_LEAGUE_MATCH);
        match.setMatchFormat(Match.MATCH_FORMAT_REGULAR_BESTOF3);
        match.setLeagueFlightId(registration.getLeagueFlightId());
        match.setLeagueFlight(registration.getLeagueFlight());
        if(homeAway == Match.HomeAway.HomeMatch) {
            match.setFacilityId(registration.getFacilityId());
            match.setFacility(registration.getFacility());
        }

        // PLAYERS
        List<MatchPlayer> players = new ArrayList<MatchPlayer>();
        // self
        MatchPlayer me = new MatchPlayer(null, homeAway == Match.HomeAway.HomeMatch,
                null, null,
                null, null, registration.getLeagueProfileId());
        players.add(me);
        // opponent
        MatchPlayer opponent = new MatchPlayer(null, !(homeAway == Match.HomeAway.HomeMatch),
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
                    new MatchPlayer(null, me.getHomeTeam(),
                            partner.getFirstLastName(), partner.getPhoneNbr(), null, null, null);
            List<MatchPlayerEmail> partnerEmails = new ArrayList<MatchPlayerEmail>();
            for(PartnerEmail pEmail : partner.getEmails()) {
                partnerEmails.add(new MatchPlayerEmail(null, pEmail.getEmail()));
            }
            partnerMatchPlayer.setEmails(partnerEmails);
            players.add(partnerMatchPlayer);

            // opponent 2
            opponent2 = new MatchPlayer(null, opponent.getHomeTeam(),
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
            final Context context, final Match match, final List<MatchPlayer> playersToUpdate) {
        LavoltaDb db = Lavolta.db();
        DbTransaction txn = null;
        try {
            txn = db.beginUiTxn(TCLavoltaDb.ACTION_UPDATE_LEAGUE_MATCH);

            MatchTbl matchTbl = new MatchTbl();
            matchTbl.uiUpdate(txn, match, null, null);

            MatchPlayerTbl playerTbl = new MatchPlayerTbl();
            for(MatchPlayer player : playersToUpdate) {
                playerTbl.uiUpdate(txn, player, null, null);
            }



            db.commitTxn(txn);
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
            return false;
        } finally {
            db.endTxn(txn, false);
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
