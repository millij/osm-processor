package com.github.osm.domain;

import java.util.HashMap;
import java.util.Map;


public final class Node extends OsmEntity {

    private final double latitude;
    private final double longitude;


    // Constructors
    // ------------------------------------------------------------------------

    Node(long osmId, MetaInfo metaInfo, Map<String, String> tags, double latitude, double longitude) {
        super(osmId, metaInfo, tags);

        // init
        this.latitude = latitude;
        this.longitude = longitude;
    }

    Node(long osmId, MetaInfo metaInfo, double latitude, double longitude) {
        this(osmId, metaInfo, new HashMap<>(), latitude, longitude);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Node [osmId=" + getOsmId() + ", latitude=" + latitude + ", longitude=" + longitude + "]";
    }

}
