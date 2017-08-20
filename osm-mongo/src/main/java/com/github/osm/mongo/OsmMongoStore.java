package com.github.osm.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.domain.Node;
import com.github.osm.domain.OsmEntity;
import com.github.osm.domain.Relation;
import com.github.osm.domain.Way;
import com.github.osm.mongo.helper.MongoConfig;
import com.github.osm.mongo.helper.MongoMapper;


/**
 * Mongo Store for OSM Entities
 */
public class OsmMongoStore extends MongoStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsmMongoStore.class);

    // Collections
    public static final String COLLECTION_BBOX = "bbox";
    public static final String COLLECTION_NODE = Node.class.getSimpleName().toLowerCase();
    public static final String COLLECTION_WAY = Way.class.getSimpleName().toLowerCase();
    public static final String COLLECTION_RELATION = Relation.class.getSimpleName().toLowerCase();


    // Constructor

    private OsmMongoStore(MongoConfig config) {
        super(config);

    }

    // Factory

    public static OsmMongoStore withConfig(MongoConfig config) {
        return new OsmMongoStore(config);
    }


    // MongoStore Methods
    // ------------------------------------------------------------------------

    // Collections

    public static <T extends OsmEntity> String getCollectionName(final Class<T> osmEntityClz) {
        // Sanity checks
        if (osmEntityClz == null) {
            throw new IllegalArgumentException("collectionName :: OSM Entity class should not be null");
        }

        return osmEntityClz.getSimpleName().toLowerCase();
    }


    // Insert Methods

    public void insert(final Node node) {
        // Sanity checks
        if (node == null) {
            throw new IllegalArgumentException("insert :: Node object should not be null");
        }

        final Document nodeDoc = MongoMapper.document(node);
        this.insert(COLLECTION_NODE, nodeDoc);
    }

    public void insert(final Way way) {
        // Sanity checks
        if (way == null) {
            throw new IllegalArgumentException("insert :: Way object should not be null");
        }

        final Document wayDoc = MongoMapper.document(way);
        this.insert(COLLECTION_WAY, wayDoc);
    }

    public void insert(final Relation relation) {
        // Sanity checks
        if (relation == null) {
            throw new IllegalArgumentException("insert :: Relation object should not be null");
        }

        final Document relationDoc = MongoMapper.document(relation);
        this.insert(COLLECTION_RELATION, relationDoc);
    }


    // Find Methods

    @SuppressWarnings("unchecked")
    public <T extends OsmEntity> T findOne(final Class<T> osmEntityClz, final long osmId) {
        // Sanity checks
        if (osmEntityClz == null) {
            throw new IllegalArgumentException("findOne :: OSM Entity class should not be null");
        }

        // filter
        final Document filter = new Document("osmId", osmId);
        LOGGER.debug("Fetching {} document with OSM id : {}", osmEntityClz, osmId);

        // Result
        final String collectionName = getCollectionName(osmEntityClz);
        Document result = this.findOne(collectionName, filter);

        if (osmEntityClz.equals(Node.class)) {
            return (T) MongoMapper.asNode(result);
        } else if (osmEntityClz.equals(Way.class)) {
            return (T) MongoMapper.asWay(result);
        } else if (osmEntityClz.equals(Relation.class)) {
            return (T) MongoMapper.asRelation(result);
        }

        throw new RuntimeException("Unknown OSM entity class passed");
    }


    public <T extends OsmEntity> List<T> find(final Class<T> osmEntity, final Document filter) {
        // Sanity checks

        return new ArrayList<T>();
    }



}
