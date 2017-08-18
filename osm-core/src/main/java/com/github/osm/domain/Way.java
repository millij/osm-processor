package com.github.osm.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Way extends OsmEntity {

    private final List<Long> nodeIds;


    // Constructors
    // ------------------------------------------------------------------------

    Way(long osmId, MetaInfo metaInfo, Map<String, String> tags, List<Long> nodeIds) {
        super(osmId, metaInfo, tags);
        this.nodeIds = nodeIds;
    }

    Way(long osmId, MetaInfo metaInfo, List<Long> nodeIds) {
        this(osmId, metaInfo, new HashMap<>(), nodeIds);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public List<Long> getNodeIds() {
        return nodeIds;
    }

    @Override
    public Type getType() {
        return Type.way;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Way [nodeIds=" + nodeIds + "]";
    }


}
