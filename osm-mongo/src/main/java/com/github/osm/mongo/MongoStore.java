package com.github.osm.mongo;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.mongo.helper.MongoConfig;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;



public class MongoStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoStore.class);

    protected final MongoClient client;
    protected final MongoDatabase database;


    // Constructor

    public MongoStore(MongoConfig config) {
        super();

        // Sanity checks
        if (config == null) {
            throw new IllegalArgumentException("MongoStore :: config, should not be blank");
        }

        // client
        this.client = new MongoClient(config.getHost(), config.getPort());
        this.database = client.getDatabase(config.getDatabase());
        LOGGER.info("Successfully initiated the Mongo Connection with config : {}", config);
    }


    // DB Methods
    // ------------------------------------------------------------------------



    // Collection Methods
    // ------------------------------------------------------------------------

    public void insert(final String collectionName, final Document dbObject) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("insert :: Collection name should not be blank");
        }

        // Collection
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        collection.insertOne(dbObject);
    }

    public Document findOne(final String collectionName, final Document filter) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("findOne :: Collection name should not be blank");
        }

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        Document result = collection.find(filter).first();

        return result;
    }

    public FindIterable<Document> find(final String collectionName, final Document filter) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("find :: Collection name should not be blank");
        }

        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        FindIterable<Document> result = collection.find(filter);

        return result;
    }


}
