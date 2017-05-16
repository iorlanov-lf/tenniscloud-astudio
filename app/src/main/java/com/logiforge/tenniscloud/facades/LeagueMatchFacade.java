package com.logiforge.tenniscloud.facades;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbTransaction;
import com.logiforge.lavolta.android.db.LavoltaDb;
import com.logiforge.tenniscloud.db.MatchPlayerEmailTbl;
import com.logiforge.tenniscloud.db.MatchPlayerTbl;
import com.logiforge.tenniscloud.db.MatchTbl;
import com.logiforge.tenniscloud.db.TCLavoltaDb;
import com.logiforge.tenniscloud.model.LeagueRegistration;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.MatchPlayer;
import com.logiforge.tenniscloud.model.MatchPlayerEmail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iorlanov on 5/2/17.
 */

public class LeagueMatchFacade {
    private static final String TAG = LeagueMatchFacade.class.getSimpleName();

    public void createSinglesLeagueMatch(
            final Context context, final Match match, final LeagueRegistration registration,
            final Match.HomeAway homeAway, final String opponentEmail) {
        DummyTask task = new DummyTask(context, "Creating singles match ...");
        task.execute();

        // populate known/default match attributes
        match.setMatchType(Match.MATCH_TYPE_LEAGUE_MATCH);
        match.setMatchFormat(Match.MATCH_FORMAT_REGULAR_BESTOF3);
        match.setLeagueFlightId(registration.getLeagueFlightId());
        match.setLeagueFlight(registration.getLeagueFlight());
        if(homeAway == Match.HomeAway.HomeMatch) {
            match.setFacilityId(registration.getFacilityId());
            match.setFacility(registration.getFacility());
        }

        // add players
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

            db.commitTxn(txn);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
        } finally {
            db.endTxn(txn, false);
        }
    }

    public void createDoublesLeagueMatch(
            final Context context, final Match match, final LeagueRegistration registration,
            final Match.HomeAway homeAway, final String opponent1Email, String opponent2Email) {
        DummyTask task = new DummyTask(context, "Creating doubles match ...");
        task.execute();
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
