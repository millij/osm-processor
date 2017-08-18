package com.github.osm.domain;

import com.github.osm.domain.OsmEntity.Type;

public final class RelationMember {

    private final long memberId;
    private final Type memberType;
    private final String memberRole;


    // Constructors
    // ------------------------------------------------------------------------

    RelationMember(long memberId, Type memberType, String memberRole) {
        super();
        this.memberId = memberId;
        this.memberType = memberType;
        this.memberRole = memberRole;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public long getMemberId() {
        return memberId;
    }

    public Type getMemberType() {
        return memberType;
    }

    public String getMemberRole() {
        return memberRole;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "[memberType=" + memberType + ", memberRole=" + memberRole + "]";
    }

}
