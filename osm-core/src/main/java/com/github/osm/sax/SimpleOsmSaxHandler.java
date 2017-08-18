package com.github.osm.sax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.domain.Bound;
import com.github.osm.domain.Node;
import com.github.osm.domain.OsmEntity;
import com.github.osm.domain.Relation;
import com.github.osm.domain.Way;


/**
 * An In-memory SAX parser event handler for OSM XML. Once the parsing is completed, the Object
 * equivalents of the elements can be accessed through the respective HashMaps.
 */
public final class SimpleOsmSaxHandler extends OsmSaxHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleOsmSaxHandler.class);

    // Element Stores
    private final Bound bound;
    private final List<Node> nodes;
    private final List<Way> ways;
    private final List<Relation> relations;


    // Constructors
    // ------------------------------------------------------------------------

    public SimpleOsmSaxHandler() {
        super();

        // initialize variables
        this.bound = null;

        this.nodes = new ArrayList<>();
        this.ways = new ArrayList<>();
        this.relations = new ArrayList<>();

        LOGGER.info("Initialized new InMemoryOsmHandler");
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


    // OsmEntityHandler methods
    // ------------------------------------------------------------------------

    @Override
    public void documentCompleted() {

        // OUTPUT
        LOGGER.info("No of Nodes Read: {}", nodes.size());
        LOGGER.info("No of Ways Read: {}", ways.size());
        LOGGER.info("No of Relations Read: {}", relations.size());
    }

    @Override
    public void elementCompleted(String elemName, OsmEntity element) {
        switch (elemName) {
            case NODE_ELEMENT:
                this.nodes.add((Node) element);
                break;

            case WAY_ELEMENT:
                this.ways.add((Way) element);
                break;

            case RELATION_ELEMENT:
                this.relations.add((Relation) element);
                break;

            default:
                break;
        }
    }


    // Counts

    @Override
    public long getNodesCount() {
        return this.nodes.size();
    }

    @Override
    public long getWaysCount() {
        return this.ways.size();
    }

    @Override
    public long getRelationsCount() {
        return this.ways.size();
    }


}
