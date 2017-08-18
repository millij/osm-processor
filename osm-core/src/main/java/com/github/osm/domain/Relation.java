package com.github.osm.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Relation extends OsmEntity {

    private final List<RelationMember> members;


    // Constructors
    // ------------------------------------------------------------------------

    Relation(long osmId, MetaInfo metaInfo, Map<String, String> tags, List<RelationMember> members) {
        super(osmId, metaInfo, tags);
        this.members = members;
    }

    Relation(long osmId, MetaInfo metaInfo, List<RelationMember> members) {
        this(osmId, metaInfo, new HashMap<>(), members);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public List<RelationMember> getMembers() {
        return members;
    }

    @Override
    public Type getType() {
        return Type.relation;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Relation [members=" + members + "]";
    }


}