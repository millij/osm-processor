package com.github.osm.sax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.osm.domain.Bound;
import com.github.osm.domain.Node;
import com.github.osm.domain.Relation;
import com.github.osm.domain.Way;


public class OsmResult {

    private final Bound bound;

    private final List<Node> nodes;
    private final List<Way> ways;
    private final List<Relation> relations;


    // Constructors
    // ------------------------------------------------------------------------

    public OsmResult(Bound bound, List<Node> nodes, List<Way> ways, List<Relation> relations) {
        super();

        this.bound = bound;
        this.nodes = (nodes == null) ? new ArrayList<>() : new ArrayList<>(nodes);
        this.ways = (ways == null) ? new ArrayList<>() : new ArrayList<>(ways);
        this.relations = (relations == null) ? new ArrayList<>() : new ArrayList<>(relations);
    }

    public OsmResult(List<Node> nodes, List<Way> ways, List<Relation> relations) {
        this(null, nodes, ways, relations);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public Bound getBound() {
        return bound;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public List<Way> getWays() {
        return Collections.unmodifiableList(ways);
    }

    public List<Relation> getRelations() {
        return Collections.unmodifiableList(relations);
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "OsmXmlResult [bound=" + bound + ", nodes=" + nodes.size() + ", ways=" + ways.size() + ", relations="
                + relations.size() + "]";
    }


    // Factory
    // ------------------------------------------------------------------------

    public static OsmResult empty() {
        return new OsmResult(null, null, null);
    }
    
}
