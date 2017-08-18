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

    /**
     * Returns the specific data type represented by this entity.
     * 
     * @return The entity type enum value.
     */
    public abstract Type getType();



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
