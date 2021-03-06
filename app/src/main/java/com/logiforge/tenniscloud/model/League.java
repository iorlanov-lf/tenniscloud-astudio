package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class League extends DynamicEntity {
    public static final int SEASON_OTHER = 0;
    public static final int SEASON_WINTER = 1;
    public static final int SEASON_SPRING = 2;
    public static final int SEASON_SUMMER = 3;
    public static final int SEASON_FALL = 4;

    public static final int TEAM_TYPE_SINGLES = 1;
    public static final int TEAM_TYPE_DOUBLES = 2;

    public static final int AGE_FLIGHT_OPEN = 0;
    public static final int AGE_FLIGHT_JUNIOR = 1;
    public static final int AGE_FLIGHT_ADULT = 2;
    public static final int AGE_FLIGHT_SENIOR = 3;
    public static final int AGE_FLIGHT_SUPER_SENIOR = 4;

    public static final int GENDER_MEN = 0;
    public static final int GENDER_WOMEN = 1;
    public static final int GENDER_MIXED = 2;

    public static final String SCHEDULING_BUSINESS = "Business";
    public static final String SCHEDULING_WEEKDAY = "Weekday";

    String leagueMetroAreaId;
    LeagueMetroArea leagueMetroArea;
    Integer year;
    Integer season;
    String customSeasonName;
    Integer teamType;
    Integer ageFlight;
    Integer gender;
    String scheduling;
    String leagueName;
    LocalDate registrationEndDt;
    LocalDate playStartDt;
    LocalDate playEndDt;

    @Override
    public String getParentId() {
        return leagueMetroAreaId;
    }

    public League() {
        super();
    }

    public League(String id, Long version, Integer syncState,
            String leagueMetroAreaId, Integer year, Integer season, String customSeasonName,
            Integer teamType, Integer ageFlight, Integer gender, String scheduling,
            String leagueName,
            LocalDate registrationEndDt, LocalDate playStartDt, LocalDate playEndDt) {
        super(id, version, syncState);

        this.leagueMetroAreaId = leagueMetroAreaId;
        this.year = year;
        this.season = season;
        this.customSeasonName = customSeasonName;
        this.teamType = teamType;
        this.ageFlight = ageFlight;
        this.gender = gender;
        this.scheduling = scheduling;
        this.leagueName = leagueName;
        this.registrationEndDt = registrationEndDt;
        this.playStartDt = playStartDt;
        this.playEndDt = playEndDt;
    }

    public String getLeagueMetroAreaId() {
        return leagueMetroAreaId;
    }

    public void setLeagueMetroAreaId(String leagueMetroAreaId) {
        this.leagueMetroAreaId = leagueMetroAreaId;
    }

    public LeagueMetroArea getLeagueMetroArea() {
        return leagueMetroArea;
    }

    public void setLeagueMetroArea(LeagueMetroArea leagueMetroArea) {
        this.leagueMetroArea = leagueMetroArea;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getCustomSeasonName() {
        return customSeasonName;
    }

    public void setCustomSeasonName(String customSeasonName) {
        this.customSeasonName = customSeasonName;
    }

    public Integer getTeamType() {
        return teamType;
    }

    public void setTeamType(Integer teamType) {
        this.teamType = teamType;
    }

    public Integer getAgeFlight() {
        return ageFlight;
    }

    public void setAgeFlight(Integer ageFlight) {
        this.ageFlight = ageFlight;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getScheduling() {
        return scheduling;
    }

    public void setScheduling(String scheduling) {
        this.scheduling = scheduling;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public LocalDate getRegistrationEndDt() {
        return registrationEndDt;
    }

    public void setRegistrationEndDt(LocalDate registrationEndDt) {
        this.registrationEndDt = registrationEndDt;
    }

    public LocalDate getPlayStartDt() {
        return playStartDt;
    }

    public void setPlayStartDt(LocalDate playStartDt) {
        this.playStartDt = playStartDt;
    }

    public LocalDate getPlayEndDt() {
        return playEndDt;
    }

    public void setPlayEndDt(LocalDate playEndDt) {
        this.playEndDt = playEndDt;
    }
}
