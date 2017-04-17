package com.logiforge.tenniscloud.activities.entrypoint;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.facebook.stetho.Stetho;
import com.logiforge.lavolta.android.app.DefaultAuthPlugin;
import com.logiforge.lavolta.android.app.Lavolta;
import com.logiforge.lavolta.android.db.DbDynamicTable;
import com.logiforge.lavolta.android.db.InstallationTable;
import com.logiforge.tenniscloud.R;
import com.logiforge.tenniscloud.TCLavolta;
import com.logiforge.tenniscloud.activities.dashboard.DashboardActivity;
import com.logiforge.tenniscloud.db.LeagueMetroAreaTbl;
import com.logiforge.tenniscloud.db.LeagueProviderTbl;
import com.logiforge.tenniscloud.db.LeagueTbl;
import com.logiforge.tenniscloud.db.MatchTbl;
import com.logiforge.tenniscloud.db.PlayingLevelTbl;
import com.logiforge.tenniscloud.db.TCUserTbl;
import com.logiforge.tenniscloud.model.League;
import com.logiforge.tenniscloud.model.LeagueMetroArea;
import com.logiforge.tenniscloud.model.LeagueProvider;
import com.logiforge.tenniscloud.model.Match;
import com.logiforge.tenniscloud.model.PlayingLevel;
import com.logiforge.tenniscloud.model.TCUser;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.UUID;

public class EntryPointActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.act_entrypoint);

        try {
            // initialize sync framework
            if (Lavolta.instance() == null) {
                //throw new Exception("testing snackbar");
                TCLavolta lavolta = new TCLavolta();
                lavolta.init(this);
            }
            if (DefaultAuthPlugin.needsInitialization()) {
                String appId = UUID.randomUUID().toString();
                String deviceId = UUID.randomUUID().toString();
                DefaultAuthPlugin.init(appId, deviceId);
            }

            // lavolta
            InstallationTable instTbl = new InstallationTable();
            instTbl.updateAuthorization("iorlanov", "iorlanov@comcast.net", "Igor Orlanov", null, null);

            // TCUser
            TCUserTbl userTbl = new TCUserTbl();
            if(userTbl.count() == 0) {
                userTbl.syncAdd(new TCUser(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        "iorlanov", "Igor Orlanov", "iorlanov@comcast.net", "770 633-0568", TCUser.GENDER_MALE));


                // PROVIDERS
                LeagueProviderTbl leagueProviderTbl = new LeagueProviderTbl();
                String ultimateUid = UUID.randomUUID().toString();
                leagueProviderTbl.syncAdd(
                        new LeagueProvider(ultimateUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                "UltimateTennis")
                );
                String t2Uid = UUID.randomUUID().toString();
                leagueProviderTbl.syncAdd(
                        new LeagueProvider(t2Uid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                "T2Tennis")
                );
                String leagueTennisUid = UUID.randomUUID().toString();
                leagueProviderTbl.syncAdd(
                        new LeagueProvider(leagueTennisUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                "LeagueTennis")
                );
                String flexTennisUid = UUID.randomUUID().toString();
                leagueProviderTbl.syncAdd(
                        new LeagueProvider(flexTennisUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                "FlexTennis")
                );

                // PLAYING LEVELS
                PlayingLevelTbl levelTbl = new PlayingLevelTbl();
                // ultimate
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 10, "2.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 20, "3.0-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 30, "3.0"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 40, "3.5-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 50, "3.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 60, "4.0-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 70, "4.0"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 80, "4.5-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 90, "4.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 100, "5.0-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 110, "5.0"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 120, "5.5-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 130, "5.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 140, "6.0-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        ultimateUid, 150, "6.0"));
                // t2
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 0, "2.5-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 10, "2.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 20, "3.0-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 30, "3.0"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 40, "3.5-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 50, "3.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 60, "4.0-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 70, "4.0"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 80, "4.5-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 90, "4.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 100, "5.0-"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        t2Uid, 110, "5.0"));
                // league tennis
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 10, "2.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 20, "2.75"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 30, "3.0"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 40, "3.25"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 50, "3.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 60, "3.75"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 70, "4.0"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 80, "4.25"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 90, "4.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 100, "4.75"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        leagueTennisUid, 110, "5.0"));
                // flex league
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        flexTennisUid, 10, "3 to 3.25"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        flexTennisUid, 20, "3.25 to 3.5"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        flexTennisUid, 30, "3.5 to 4"));
                levelTbl.syncAdd(new PlayingLevel(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                        flexTennisUid, 30, "4 to 4.5"));

                // METRO AREAS
                LeagueMetroAreaTbl areaTbl = new LeagueMetroAreaTbl();
                areaTbl.deleteAll();
                String ultimateLosAngelesUid = UUID.randomUUID().toString();
                areaTbl.syncAdd(
                        new LeagueMetroArea(ultimateLosAngelesUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateUid, "CA", "Los Angeles")
                );
                String ultimateAtlantaUid = UUID.randomUUID().toString();
                areaTbl.syncAdd(
                        new LeagueMetroArea(ultimateAtlantaUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateUid, "GA", "Atlanta")
                );

                String t2CharlotteUid = UUID.randomUUID().toString();
                areaTbl.syncAdd(
                        new LeagueMetroArea(t2CharlotteUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                t2Uid, "SC", "Charlotte")
                );
                String t2AtlantaUid = UUID.randomUUID().toString();
                areaTbl.syncAdd(
                        new LeagueMetroArea(t2AtlantaUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                t2Uid, "GA", "Atlanta")
                );

                String leagueTennisAustinUid = UUID.randomUUID().toString();
                areaTbl.syncAdd(
                        new LeagueMetroArea(leagueTennisAustinUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                leagueTennisUid, "TX", "Austin")
                );
                String leagueTennisAtlantaUid = UUID.randomUUID().toString();
                areaTbl.syncAdd(
                        new LeagueMetroArea(leagueTennisAtlantaUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                leagueTennisUid, "GA", "Atlanta")
                );

                String flexTennisAtlantaUid = UUID.randomUUID().toString();
                areaTbl.syncAdd(
                        new LeagueMetroArea(flexTennisAtlantaUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                flexTennisUid, "GA", "Atlanta")
                );

                // LEAGUES
                LeagueTbl leagueTbl = new LeagueTbl();
                leagueTbl.deleteAll();
                // ultimate/LosAngeles
                String ultimateLosAngelesSpring17WomenUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(ultimateLosAngelesSpring17WomenUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateLosAngelesUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_WOMEN, League.SCHEDULING_BUSINESS,
                                "Spring Women's Flex Singles",
                                new LocalDate(2017, 1, 17), new LocalDate(2017, 2, 11), new LocalDate(2017, 4, 9))
                );
                String ultimateLosAngelesSpring17MenUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(ultimateLosAngelesSpring17MenUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateLosAngelesUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "Spring Men's Flex Singles",
                                new LocalDate(2017, 1, 17), new LocalDate(2017, 2, 11), new LocalDate(2017, 4, 9))
                );
                String ultimateLosAngelesSummer17WomenUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(ultimateLosAngelesSummer17WomenUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateLosAngelesUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_WOMEN, League.SCHEDULING_BUSINESS,
                                "Summer Women's Flex Singles",
                                new LocalDate(2017, 5, 26), new LocalDate(2017, 6, 3), new LocalDate(2017, 7, 30))
                );
                String ultimateLosAngelesSummer17MenUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(ultimateLosAngelesSummer17MenUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateLosAngelesUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "Summer Men's Flex Singles",
                                new LocalDate(2017, 5, 26), new LocalDate(2017, 6, 3), new LocalDate(2017, 7, 30))
                );

                // ultimate/Atlanta
                String ultimateAtlantaSpring17WomenBusinessUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(ultimateAtlantaSpring17WomenBusinessUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateAtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_WOMEN, League.SCHEDULING_BUSINESS,
                                "Spring Women's Flex Singles - Business",
                                new LocalDate(2017, 1, 17), new LocalDate(2017, 2, 11), new LocalDate(2017, 4, 9))
                );
                String ultimateAtlantaSpring17WomenWeekdaysUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(ultimateAtlantaSpring17WomenWeekdaysUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateAtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_WOMEN, League.SCHEDULING_WEEKDAY,
                                "Spring Women's Flex Singles - Weekday",
                                new LocalDate(2017, 1, 17), new LocalDate(2017, 2, 11), new LocalDate(2017, 4, 9))
                );
                String ultimateAtlantaSpring17MenUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(ultimateAtlantaSpring17MenUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateAtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "Spring Men's Flex Singles",
                                new LocalDate(2017, 1, 17), new LocalDate(2017, 2, 11), new LocalDate(2017, 4, 9))
                );
                String ultimateAtlantaSpring17MixedUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(ultimateAtlantaSpring17MixedUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                ultimateAtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_DOUBLES, League.AGE_FLIGHT_ADULT, League.GENDER_MIXED, League.SCHEDULING_BUSINESS,
                                "Spring Flex Mixed",
                                new LocalDate(2017, 4, 6), new LocalDate(2017, 4, 16), new LocalDate(2017, 5, 28))
                );

                // t2/Atlanta
                String t2AtlantaSpring17MixedUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(t2AtlantaSpring17MixedUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                t2AtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_DOUBLES, League.AGE_FLIGHT_ADULT, League.GENDER_MIXED, League.SCHEDULING_BUSINESS,
                                "Mixed Doubles -Spring '17",
                                new LocalDate(2017, 2, 28), new LocalDate(2017, 3, 5), new LocalDate(2017, 3, 5).plusDays(5 * 7))
                );
                String t2AtlantaSpring17SeniorMenDoubleUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(t2AtlantaSpring17SeniorMenDoubleUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                t2AtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_DOUBLES, League.AGE_FLIGHT_SENIOR, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "SENIOR - Men's Doubles- Spring '17",
                                new LocalDate(2017, 2, 28), new LocalDate(2017, 3, 5), new LocalDate(2017, 3, 5).plusDays(5 * 7))
                );
                String t2AtlantaSpring17MenDoubleUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(t2AtlantaSpring17MenDoubleUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                t2AtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_DOUBLES, League.AGE_FLIGHT_ADULT, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "Men's Doubles-Spring '17",
                                new LocalDate(2017, 4, 6), new LocalDate(2017, 4, 16), new LocalDate(2017, 4, 16).plusDays(5 * 7))
                );
                String t2AtlantaSpring17MenUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(t2AtlantaSpring17MenUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                t2AtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "Men's Singles - Spring 2017",
                                new LocalDate(2017, 4, 6), new LocalDate(2017, 4, 16), new LocalDate(2017, 4, 16).plusDays(5 * 7))
                );

                // LeagueTennis/Atlanta
                String leagueTennisAtlantaSpring17MenUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(leagueTennisAtlantaSpring17MenUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                leagueTennisAtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "Men's Singles SPRING 2017 - ATL",
                                new LocalDate(2017, 4, 17), new LocalDate(2017, 4, 25), new LocalDate(2017, 6, 18))
                );
                String leagueTennisAtlantaSpring17MenDoublesUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(leagueTennisAtlantaSpring17MenDoublesUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                leagueTennisAtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_DOUBLES, League.AGE_FLIGHT_ADULT, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "Mens Doubles SPRING 2017 - ATL",
                                new LocalDate(2017, 5, 8), new LocalDate(2017, 5, 16), new LocalDate(2017, 7, 9))
                );
                String leagueTennisAtlantaSpring17MixedUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(leagueTennisAtlantaSpring17MixedUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                leagueTennisAtlantaUid, 2017, League.SEASON_SPRING, null,
                                League.TEAM_TYPE_DOUBLES, League.AGE_FLIGHT_ADULT, League.GENDER_MIXED, League.SCHEDULING_BUSINESS,
                                "Mixed Doubles SPRING 2017 - ATL",
                                new LocalDate(2017, 5, 8), new LocalDate(2017, 5, 16), new LocalDate(2017, 7, 9))
                );

                // FlexTennis/Atlanta
                String flexTennisAtlantaSummer17MenUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(flexTennisAtlantaSummer17MenUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                flexTennisAtlantaUid, 2017, League.SEASON_SUMMER, null,
                                League.TEAM_TYPE_SINGLES, League.AGE_FLIGHT_ADULT, League.GENDER_MEN, League.SCHEDULING_BUSINESS,
                                "Mens Evening Singles League",
                                new LocalDate(2017, 5, 14), new LocalDate(2017, 5, 22), new LocalDate(2017, 7, 9))
                );
                String flexTennisAtlantaSummer17MixedUid = UUID.randomUUID().toString();
                leagueTbl.syncAdd(
                        new League(flexTennisAtlantaSummer17MixedUid, 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                flexTennisAtlantaUid, 2017, League.SEASON_SUMMER, null,
                                League.TEAM_TYPE_DOUBLES, League.AGE_FLIGHT_ADULT, League.GENDER_MIXED, League.SCHEDULING_BUSINESS,
                                "Mixed Evening Doubles League",
                                new LocalDate(2017, 5, 14), new LocalDate(2017, 5, 22), new LocalDate(2017, 7, 9))
                );

                MatchTbl matchTbl = new MatchTbl();
                matchTbl.deleteAll();
                matchTbl.syncAdd(
                        new Match(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                Match.MATCH_TYPE_FREINDLY_MATCH, LocalDate.now(), new LocalTime(9, 00),
                                Match.MATCH_FORMAT_REGULAR_BESTOF3, Match.MATCH_OUTCOME_NOT_YET_PLAYED,
                                null, null, null, null));
                matchTbl.syncAdd(
                        new Match(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                Match.MATCH_TYPE_FREINDLY_MATCH, LocalDate.now(), new LocalTime(18, 00),
                                Match.MATCH_FORMAT_REGULAR_BESTOF3, Match.MATCH_OUTCOME_NOT_YET_PLAYED,
                                null, null, null, null));
                for (int i = 1; i < 10; i++) {
                    matchTbl.syncAdd(
                            new Match(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                    Match.MATCH_TYPE_FREINDLY_MATCH, LocalDate.now().plusDays(1), new LocalTime(9, 00),
                                    Match.MATCH_FORMAT_REGULAR_BESTOF3, Match.MATCH_OUTCOME_NOT_YET_PLAYED,
                                    null, null, null, null));
                    matchTbl.syncAdd(
                            new Match(UUID.randomUUID().toString(), 0L, DbDynamicTable.SYNC_STATE_ADDED,
                                    Match.MATCH_TYPE_FREINDLY_MATCH, LocalDate.now().plusDays(1), new LocalTime(18, 00),
                                    Match.MATCH_FORMAT_REGULAR_BESTOF3, Match.MATCH_OUTCOME_NOT_YET_PLAYED,
                                    null, null, null, null));
                }
            }

            Intent intent = new Intent(EntryPointActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } catch(Exception e) {
            Log.e(this.getClass().getName(), e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage());
            RelativeLayout pageLayout = (RelativeLayout) findViewById(R.id.pageLayout);
            Snackbar snackbar = Snackbar
                    .make(pageLayout, "Unable to init Sync framework", Snackbar.LENGTH_INDEFINITE);

            snackbar.show();
        }

    }
}
