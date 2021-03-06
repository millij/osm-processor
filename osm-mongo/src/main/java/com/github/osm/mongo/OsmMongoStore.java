package com.github.osm.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.bean.MemberBean;
import com.github.osm.bean.NodeBean;
import com.github.osm.bean.RelationBean;
import com.github.osm.bean.WayBean;
import com.github.osm.domain.Member;
import com.github.osm.domain.Node;
import com.github.osm.domain.OsmEntity;
import com.github.osm.domain.OsmEntity.Type;
import com.github.osm.domain.Relation;
import com.github.osm.domain.Way;
import com.github.osm.mongo.helper.MongoConfig;
import com.github.osm.mongo.helper.MongoMapper;
import com.mongodb.client.FindIterable;


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
    // ------------------------------------------------------------------------

    private OsmMongoStore(MongoConfig config) {
        super(config);
    }

    // Factory

    public static OsmMongoStore withConfig(MongoConfig config) {
        return new OsmMongoStore(config);
    }


    // DB Methods
    // ------------------------------------------------------------------------

    public void ensureIndexes() {
        // index on id field
        this.indexCollection(COLLECTION_NODE, "osmId");
        this.indexCollection(COLLECTION_WAY, "osmId");
        this.indexCollection(COLLECTION_RELATION, "osmId");
    }


    // Insert Methods
    // ------------------------------------------------------------------------

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


    // Count Method
    // ------------------------------------------------------------------------

    public <T extends OsmEntity> long count(final Class<T> osmEntityClz) {
        final String collectionName = this.getCollectionName(osmEntityClz);
        return this.count(collectionName);
    }


    // Find Methods
    // ------------------------------------------------------------------------


    // Node

    public Node node(final long osmId) {
        // Result
        Document result = this.findOne(COLLECTION_NODE, new Document("osmId", osmId));
        LOGGER.debug("Fetched Node with OSM id : {} - {}", osmId, result);

        return result == null ? null : MongoMapper.node(result);
    }

    public NodeBean nodeBean(final long osmId) {
        // Node
        Node node = this.node(osmId);
        if (node == null) {
            LOGGER.error("No Node object found with the passed id : {}", osmId);
            return null;
        }

        return new NodeBean(node);
    }


    // Way

    public Way way(final long osmId) {
        // Result
        Document result = this.findOne(COLLECTION_WAY, new Document("osmId", osmId));
        LOGGER.debug("Fetched Way with OSM id : {} - {}", osmId, result);

        return result == null ? null : MongoMapper.way(result);
    }

    public WayBean wayBean(final long osmId) {
        // Way
        Way way = this.way(osmId);
        if (way == null) {
            LOGGER.error("No Way object found with the passed id : {}", osmId);
            return null;
        }

        // NodesMap
        final List<NodeBean> nodeBeans = new ArrayList<>();
        for (long nodeId : way.getNodeIds()) {
            Node node = this.node(nodeId);
            nodeBeans.add(new NodeBean(node));
        }

        return new WayBean(way, nodeBeans);
    }



    // Relation

    public Relation relation(final long osmId) {
        // Result
        Document result = this.findOne(COLLECTION_RELATION, new Document("osmId", osmId));
        LOGGER.debug("Fetched Relation with OSM id : {} - {}", osmId, result);

        return result == null ? null : MongoMapper.relation(result);
    }

    public RelationBean relationBean(final long osmId) {
        // Way
        Relation relation = this.relation(osmId);
        if (relation == null) {
            LOGGER.error("No Relation object found with the passed id : {}", osmId);
            return null;
        }

        // NodesMap
        final List<MemberBean> memberBeans = new ArrayList<>();
        for (Member member : relation.getMembers()) {
            final Type type = member.getType();
            final long id = member.getId();
            final String role = member.getRole();

            // prepare bean
            switch (type) {
                case node:
                    NodeBean nBean = this.nodeBean(id);
                    memberBeans.add(MemberBean.from(id, role, nBean));
                    break;
                case way:
                    WayBean wBean = this.wayBean(id);
                    memberBeans.add(MemberBean.from(id, role, wBean));
                    break;
                case relation:
                    RelationBean relBean = this.relationBean(id);
                    memberBeans.add(MemberBean.from(id, role, relBean));
                    break;
                default:
                    throw new RuntimeException("Unhandled member type  found : " + type);
            }
        }

        return new RelationBean(relation, memberBeans);
    }



    // FindIterable Methods
    // ------------------------------------------------------------------------

    public FindIterable<Document> nodes(final Document filter) {
        return this.find(COLLECTION_NODE, filter);
    }

    public FindIterable<Document> ways(final Document filter) {
        return this.find(COLLECTION_WAY, filter);
    }

    public FindIterable<Document> relations(final Document filter) {
        return this.find(COLLECTION_RELATION, filter);
    }



    // Private Methods
    // ------------------------------------------------------------------------

    private <T extends OsmEntity> String getCollectionName(final Class<T> osmEntityClz) {
        // Sanity checks
        if (osmEntityClz == null) {
            throw new IllegalArgumentException("collectionName :: OSM Entity class should not be null");
        }

        return osmEntityClz.getSimpleName().toLowerCase();
    }


}
