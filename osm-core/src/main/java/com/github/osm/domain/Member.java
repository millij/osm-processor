package com.github.osm.domain;

import com.github.osm.domain.OsmEntity.Type;


public final class Member {

    private final Type type;
    private final long Id;
    private final String role;


    // Constructors
    // ------------------------------------------------------------------------

    Member(Type type, long id, String role) {
        super();

        this.type = type;
        this.Id = id;
        this.role = role;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public Type getMemberType() {
        return type;
    }

    public long getMemberId() {
        return Id;
    }

    public String getMemberRole() {
        return role;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Member [type=" + type + ", Id=" + Id + ", role=" + role + "]";
    }

}
