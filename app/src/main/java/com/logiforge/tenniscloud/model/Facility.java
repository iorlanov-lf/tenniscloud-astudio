package com.logiforge.tenniscloud.model;

import com.logiforge.lavolta.android.model.DynamicEntity;

/**
 * Created by iorlanov on 2/4/2017.
 */
public class Facility extends DynamicEntity {
    String name;
    String streetAddress;
    String city;
    String state;
    String zip;
    String directions;
    Boolean isReferenceEntity = false;

    @Override
    public String getParentId() {
        return null;
    }

    public Facility() {
        super();
    }

    public Facility(String id, Long version, Integer syncState,
        String name, String streetAddress, String city, String state, String zip, String directions, Boolean isReferenceEntity) {
        super(id, version, syncState);

        this.name = name;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.directions = directions;
        this.isReferenceEntity = isReferenceEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public Boolean getReferenceEntity() {
        return isReferenceEntity;
    }

    public void setReferenceEntity(Boolean referenceEntity) {
        isReferenceEntity = referenceEntity;
    }
}
