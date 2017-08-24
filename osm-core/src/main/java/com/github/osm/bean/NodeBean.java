package com.github.osm.bean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.osm.domain.Node;
import com.github.osm.domain.OsmEntity.Type;


public class NodeBean extends OsmBean {

    private static final long serialVersionUID = 1L;

    private final long osmId;
    private final Map<String, String> tags;

    private final double latitude;
    private final double longitude;


    // Constructor

    public NodeBean(Node node) {
        super(node);

        // init
        this.osmId = node.getOsmId();
        this.tags = new HashMap<>(node.getTags());

        this.latitude = node.getLatitude();
        this.longitude = node.getLongitude();
    }

    
    // OSM Bean

    @Override
    public Type getType() {
        return Type.node;
    }


    // Getters and Setters

    public long getOsmId() {
        return osmId;
    }

    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}
