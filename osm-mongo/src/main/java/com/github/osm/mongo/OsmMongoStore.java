package com.github.osm.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public static final String COLLECTION_BBOX = "osm_bbox";
    public static final String COLLECTION_NODE = "osm_nodes";
    public static final String COLLECTION_WAY = "osm_ways";
    public static final String COLLECTION_RELATION = "osm_relations";


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

    @Override
    public void emptyCollections() {
        // drop only relevant collections
        this.database.getCollection(COLLECTION_NODE).drop();
        this.database.getCollection(COLLECTION_WAY).drop();
        this.database.getCollection(COLLECTION_RELATION).drop();
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
        final Document result = this.findOne(COLLECTION_NODE, new Document("osmId", osmId));
        LOGGER.debug("Fetched Node with OSM id : {} - {}", osmId, result);

        return Objects.isNull(result) ? null : MongoMapper.node(result);
    }

    public NodeBean nodeBean(final long osmId) {
        // Node
        final Node node = this.node(osmId);
        if (Objects.isNull(node)) {
            LOGGER.error("No Node object found with the passed id : {}", osmId);
            return null;
        }

        return new NodeBean(node);
    }

    public Map<Long, NodeBean> nodeBeans(final List<Long> osmIds) {
        // Sanity checks
        if (Objects.isNull(osmIds)) {
            return new HashMap<>();
        }

        // Filter
        final Document filter = new Document("osmId", new Document("$in", osmIds));

        // Result
        final List<Document> nodes = this.find(COLLECTION_NODE, filter, 1, osmIds.size());
        final Map<Long, NodeBean> nodeBeanMap = //
                nodes.stream().map(MongoMapper::node).collect(Collectors.toMap(n -> n.getOsmId(), NodeBean::new));

        return nodeBeanMap;
    }


    // Way

    public Way way(final long osmId) {
        // Result
        final Document result = this.findOne(COLLECTION_WAY, new Document("osmId", osmId));
        LOGGER.debug("Fetched Way with OSM id : {} - {}", osmId, result);

        return Objects.isNull(result) ? null : MongoMapper.way(result);
    }

    public WayBean wayBean(final long osmId) {
        // Way
        final Way way = this.way(osmId);
        if (Objects.isNull(way)) {
            LOGGER.error("No Way object found with the passed id : {}", osmId);
            return null;
        }

        // NodeIds
        final List<Long> nodeIds = way.getNodeIds();

        // NodesList
        final Map<Long, NodeBean> nodeBeansMap = this.nodeBeans(nodeIds);
        final List<NodeBean> nodeBeans =
                nodeIds.stream().map(nodeId -> nodeBeansMap.get(nodeId)).collect(Collectors.toList());

        return new WayBean(way, nodeBeans);
    }



    // Relation

    public Relation relation(final long osmId) {
        // Result
        final Document result = this.findOne(COLLECTION_RELATION, new Document("osmId", osmId));
        LOGGER.debug("Fetched Relation with OSM id : {} - {}", osmId, result);

        return Objects.isNull(result) ? null : MongoMapper.relation(result);
    }

    public RelationBean relationBean(final long osmId) {
        // Way
        final Relation relation = this.relation(osmId);
        if (Objects.isNull(relation)) {
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
