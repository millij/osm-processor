package com.github.osm.mongo;

import java.text.ParseException;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.domain.MetaInfo;
import com.github.osm.domain.Node;
import com.github.osm.domain.OSM;
import com.github.osm.mongo.helper.MongoConfig;


public class OsmMongoStoreTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsmMongoStoreTest.class);

    private OsmMongoStore _mongoStore;


    // Setup
    // ------------------------------------------------------------------------

    @Before
    public void setup() throws ParseException {
        // init
        final MongoConfig config = MongoConfig.with("localhost", 27017, "mongo_java_test");
        _mongoStore = OsmMongoStore.withConfig(config);
    }

    @After
    public void teardown() {
        // nothing to do
    }


    // Tests
    // ------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_node_null() {
        Node node = null;
        _mongoStore.insert(node);
    }

    @Test
    public void test_insert_node() {
        Random rnd = new Random();
        final long osmId = 100000 + rnd.nextInt(900000);

        // Node
        MetaInfo mInfo = OSM.metaInfo(1, 123, "some_timestamp", "username", 1234321);
        Node inNode = OSM.node(osmId, mInfo, null, 12.12345, 77.12345);

        // insert
        _mongoStore.insert(inNode);

        // read back
        Node outNode = _mongoStore.findOne(Node.class, osmId);
        LOGGER.info("Node read from db : {}", outNode);

        Assert.assertNotNull(outNode);
        Assert.assertEquals(osmId, outNode.getOsmId());
    }


}
