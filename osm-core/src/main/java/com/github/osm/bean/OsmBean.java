package com.github.osm.bean;

import java.io.Serializable;

import com.github.osm.domain.OsmEntity;
import com.github.osm.domain.OsmEntity.Type;



public abstract class OsmBean implements Serializable {

    /**
     * Serializable
     */
    protected static final long serialVersionUID = 1L;


    // Constructors
    // ------------------------------------------------------------------------

    public <OE extends OsmEntity> OsmBean(OE oe, Object... objects) {
        super();

        // Sanity checks
        if (oe == null) {
            throw new IllegalArgumentException("OSM Domain type object should not be null");
        }
    }


    // Abstract Methods

    /**
     * Returns the OSM Entity type represented by this bean.
     * 
     * @return The entity type enum value.
     */
    abstract Type getType();


}
