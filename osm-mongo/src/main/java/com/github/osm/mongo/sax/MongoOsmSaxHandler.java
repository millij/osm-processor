package com.github.osm.mongo.sax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.github.osm.domain.Node;
import com.github.osm.domain.Relation;
import com.github.osm.domain.Way;
import com.github.osm.mongo.OsmMongoStore;
import com.github.osm.sax.OsmSaxHandler;


/**
 * A SAX parser event handler for OSM XML primarily intended for large XML files (typically of sizes
 * more the 0.5GB). Instead of keeping the parsed element objects in memory, this uses a docstore to
 * store them.
 * 
 * @see InMemoryOsmHandler
 */
public class MongoOsmSaxHandler extends OsmSaxHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoOsmSaxHandler.class);

    private final OsmMongoStore mongoStore;

    private long nodesCount = 0;
    private long waysCount = 0;
    private long relationsCount = 0;


    // Constructors
    // ------------------------------------------------------------------------

    public MongoOsmSaxHandler(OsmMongoStore mongoStore) {
        super();

        // Sanity checks
        if (mongoStore == null) {
            throw new IllegalArgumentException("MongoOsmSaxHandler :: mongo store is null");
        }

        // init
        this.mongoStore = mongoStore;

        LOGGER.info("Successfully Initialized new OsmHandler with MongoStore : {}", mongoStore);
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    @Override
    public long getNodesCount() {
        return nodesCount;
    }

    @Override
    public long getWaysCount() {
        return waysCount;
    }

    @Override
    public long getRelationsCount() {
        return relationsCount;
    }


    // SAX Handler methods
    // ------------------------------------------------------------------------

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        // Reset
        this.nodesCount = 0;
        this.waysCount = 0;
        this.relationsCount = 0;
    }

    @Override
    public void documentCompleted() {
        // OUTPUT
        LOGGER.info("No of Nodes Read: {}", this.nodesCount);
        LOGGER.info("No of Ways Read: {}", this.waysCount);
        LOGGER.info("No of Relations Read: {}", this.relationsCount);
    }


    // Handle OSM Elements
    // ------------------------------------------------------------------------

    @Override
    public void handleNode(Node node) {
        if (node == null) {
            return;
        }

        // Save to DB
        try {
            this.mongoStore.insert(node);
        } catch (Exception ex) {
            String errMsg = String.format("Failed to insert Node in to DB : %s - ", node, node.getTags());
            LOGGER.error(errMsg, ex);
        }

        this.nodesCount++;
        LOGGER.debug("Created new Node in the DB : {}", node);
    }

    @Override
    public void handleWay(Way way) {
        if (way == null) {
            return;
        }

        // Save to DB
        try {
            this.mongoStore.insert(way);
        } catch (Exception ex) {
            String errMsg = String.format("Failed to insert Way in to DB : %s - ", way, way.getTags());
            LOGGER.error(errMsg, ex);
        }

        this.waysCount++;
        LOGGER.debug("Created new Way in the DB : {}", way);
    }

    @Override
    public void handleRelation(Relation relation) {
        if (relation == null) {
            return;
        }

        // Save to DB
        try {
            this.mongoStore.insert(relation);
        } catch (Exception ex) {
            String errMsg = String.format("Failed to insert Relation in to DB : %s - ", relation, relation.getTags());
            LOGGER.error(errMsg, ex);
        }

        this.relationsCount++;
        LOGGER.debug("Created new Relation in the DB : {}", relation);
    }


}
