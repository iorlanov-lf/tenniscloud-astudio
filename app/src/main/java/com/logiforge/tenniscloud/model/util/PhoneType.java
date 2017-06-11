package com.logiforge.tenniscloud.model.util;

/**
 * Created by iorlanov on 5/29/17.
 */

public enum PhoneType {
    HOME(0, "Home", "H"),
    CELL(10, "Cell", "C"),
    OFFICE(20, "Office", "O");

    public static PhoneType getById(int id) {
        for(PhoneType type : values()) {
            if(type.getId() == id) {
                return type;
            }
        }

        return null;
    }

    private final int id;
    private final String name;
    private final String shortName;

    PhoneType(int id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName =shortName;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }
}
