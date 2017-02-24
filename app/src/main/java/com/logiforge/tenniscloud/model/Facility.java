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

    @Override
    public String getParentId() {
        return null;
    }

    public Facility() {
        super();
    }

    public Facility(String id, Long version, Integer syncState,
        String name, String streetAddress, String city, String zip, String directions) {
        super(id, version, syncState);

        this.name = name;
        this.streetAddress = streetAddress;
        this.city = city;
        this.zip = zip;
        this.directions = directions;
    }
}
