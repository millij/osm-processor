package com.github.osm.domain;

import com.github.osm.domain.OsmEntity.Type;


public final class Member {

    private final Type type;
    private final long id;
    private final String role;


    // Constructors
    // ------------------------------------------------------------------------

    Member(Type type, long id, String role) {
        super();

        this.type = type;
        this.id = id;
        this.role = role;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public Type getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Member [type=" + type + ", Id=" + id + ", role=" + role + "]";
    }

}
