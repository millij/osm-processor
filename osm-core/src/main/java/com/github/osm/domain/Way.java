package com.github.osm.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Way extends OsmEntity {

    private final List<Long> nodeIds;


    // Constructors
    // ------------------------------------------------------------------------

    Way(long osmId, MetaInfo metaInfo, Map<String, String> tags, List<Long> nodeIds) {
        super(osmId, metaInfo, tags);

        // init
        this.nodeIds = nodeIds == null ? new ArrayList<>() : new ArrayList<>(nodeIds);
    }

    Way(long osmId, MetaInfo metaInfo, List<Long> nodeIds) {
        this(osmId, metaInfo, new HashMap<>(), nodeIds);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public List<Long> getNodeIds() {
        return Collections.unmodifiableList(nodeIds);
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Way [osmId=" + getOsmId() + ", nodeIds=" + nodeIds + "]";
    }


}
