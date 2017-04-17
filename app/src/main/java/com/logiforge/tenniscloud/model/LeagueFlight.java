package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 3/2/2017.
 */
public class LeagueFlight extends DynamicEntity {
    String leagueId;
    League league;
    String playingLevelId;
    PlayingLevel playingLevel;

    @Override
    public String getParentId() {
        return leagueId;
    }

    public LeagueFlight() {
        super();
    }

    public LeagueFlight(String id, Long version, Integer syncState,
                  String leagueId, String playingLevelId) {
        super(id, version, syncState);

        this.leagueId = leagueId;
        this.playingLevelId = playingLevelId;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public String getPlayingLevelId() {
        return playingLevelId;
    }

    public void setPlayingLevelId(String playingLevelId) {
        this.playingLevelId = playingLevelId;
    }

    public PlayingLevel getPlayingLevel() {
        return playingLevel;
    }

    public void setPlayingLevel(PlayingLevel playingLevel) {
        this.playingLevel = playingLevel;
    }
}
