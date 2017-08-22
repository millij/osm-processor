package com.github.osm.mongo;

import java.text.ParseException;
import java.util.List;

import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.mongo.helper.MongoConfig;


public class MongoStoreTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoStoreTest.class);

    private MongoStore _mongoStore;


    // Setup
    // ------------------------------------------------------------------------

    @Before
    public void setup() throws ParseException {
        // init
        final MongoConfig config = MongoConfig.with("localhost", 27017, "mongo_java_test");
        _mongoStore = new MongoStore(config);
    }

    @After
    public void teardown() {
        // nothing to do
    }


    // Tests
    // ------------------------------------------------------------------------

    @Test
    public void test_emptyCollections() {
        final String collName = "test";
        final Document document = new Document("name", "unknown");
        _mongoStore.insert(collName, document);

        // Empty collections
        _mongoStore.emptyCollections();

        // find
        List<Document> documents = _mongoStore.findAll(collName, new Document("name", "unknown"));
        Assert.assertNotNull(documents);
        Assert.assertEquals(0, documents.size());
    }


    // Insert

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_null_collection() {
        _mongoStore.insert(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_insert_empty_collection() {
        _mongoStore.insert(" ", null);
    }

    @Test
    public void test_insert() {
        final String collName = "test";
        final Document document = new Document("name", "unknown");
        _mongoStore.insert(collName, document);

        // find
        Iterable<Document> documents = _mongoStore.find(collName, new Document("name", "unknown"));
        Assert.assertNotNull(documents);
        Assert.assertTrue(documents.iterator().hasNext());
        Assert.assertNotNull(documents.iterator().next());

        for (Document doc : documents) {
            LOGGER.info("Documents read : {}", doc);
        }
    }


}
