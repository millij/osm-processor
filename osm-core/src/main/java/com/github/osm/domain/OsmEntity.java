package com.github.osm.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a single OSM entity. All top level data types inherit from this class.
 */
public abstract class OsmEntity {

    private final long osmId;
    private final MetaInfo metaInfo;
    private final Map<String, String> tags;


    // Constructors
    // ------------------------------------------------------------------------

    protected OsmEntity(long osmId, MetaInfo metaInfo, Map<String, String> tags) {
        super();
        this.osmId = osmId;
        this.metaInfo = metaInfo;
        this.tags = (tags == null) ? Collections.emptyMap() : tags;
    }

    protected OsmEntity(long osmId, MetaInfo metaInfo) {
        this(osmId, metaInfo, new HashMap<>());
    }


    // abstract methods
    // ------------------------------------------------------------------------


    // Getters and Setters
    // ------------------------------------------------------------------------

    public long getOsmId() {
        return osmId;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public Map<String, String> getTags() {
        if (tags == null) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap(tags);
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

        OsmEntity other = (OsmEntity) o;
        return this.osmId == other.osmId;
    }

    
    
    // Private definitions
    // ------------------------------------------------------------------------

    public enum Type {
        /**
         * Representation of the latitude/longitude bounding box of the entity stream.
         */
        bound,

        /**
         * Represents a geographical point.
         */
        node,

        /**
         * Represents a set of segments forming a path.
         */
        way,

        /**
         * Represents a relationship between multiple entities.
         */
        relation
    }

}
