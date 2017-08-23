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


    @Override
    public Type getType() {
        return Type.node;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Node other = (Node) o;
        if (Double.compare(latitude, other.getLatitude()) != 0) {
            return false;
        }

        if (Double.compare(longitude, other.getLongitude()) != 0) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Node [" + latitude + ", " + longitude + "]";
    }

}
