package com.github.osm.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.osm.domain.Way;
import com.github.osm.domain.OsmEntity.Type;


public class WayBean extends OsmBean {

    private static final long serialVersionUID = 1L;

    private final long osmId;
    private final Map<String, String> tags;

    private final List<NodeBean> nodes;


    // Constructor

    public WayBean(Way way, List<NodeBean> nodeBeans) {
        super(way);

        // init
        this.osmId = way.getOsmId();
        this.tags = new HashMap<>(way.getTags());

        // Nodes
        this.nodes = nodeBeans == null ? new ArrayList<>() : new ArrayList<>(nodeBeans);
    }

    // OSM Bean

    @Override
    public Type getType() {
        return Type.way;
    }


    // Getters and Setters

    public long getOsmId() {
        return osmId;
    }

    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public List<NodeBean> getNodes() {
        return Collections.unmodifiableList(nodes);
    }


}
