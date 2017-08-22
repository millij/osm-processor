package com.github.osm.mongo;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Drops all documents of all the collections of the current database.
     */
    public void emptyCollections() {
        // Iterate over collection names
        for (String collectionName : this.database.listCollectionNames()) {
            if (collectionName.startsWith("system")) {
                continue;
            }

            this.database.getCollection(collectionName).drop();
            LOGGER.debug("Successfully dropped the database collection : {}", collectionName);
        }
    }

    /**
     * Count the documents of a collection.
     * 
     * @param collectionName name of the collection to insert the document in to.
     * @return total no. of documents of a collection.
     */
    public long count(final String collectionName) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("count :: Collection name should not be blank");
        }

        // Collection
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        return collection.count();
    }


    // Collection CRUD Methods
    // ------------------------------------------------------------------------


    // Insert

    /**
     * Insert a new document in to a collection.
     * 
     * @param collectionName name of the collection to insert the document in to.
     * @param dbObject the {@link Document} object to be inserted.
     */
    public void insert(final String collectionName, final Document dbObject) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("insert :: Collection name should not be blank");
        }

        // Collection
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        collection.insertOne(dbObject);
    }


    // Find

    /**
     * Find single/one document of the collection, matching the filter criteria.
     * 
     * @param collectionName name of the collection
     * @param filter filtering criteria
     * 
     * @return the first {@link Document} matching the passed criteria.
     */
    public Document findOne(final String collectionName, final Document filter) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("findOne :: Collection name should not be blank");
        }

        // Collection
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        Document result = collection.find(filter).first();

        return result;
    }

    /**
     * Find all the documents of the collection, matching the filter criteria.
     * 
     * @param collectionName name of the collection
     * @param filter filtering criteria
     * 
     * @return {@link List} of {@link Document}s.
     * 
     * @see MongoStore#find(String, Document)
     */
    public List<Document> findAll(final String collectionName, final Document filter) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("find :: Collection name should not be blank");
        }

        // Collection
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        List<Document> result = collection.find(filter).into(new ArrayList<>());

        return result;
    }


    /**
     * Find Documents of a collection matching the passed filter, paginated.
     */
    public List<Document> find(final String collectionName, final Document filter, int pageNo, int pageSize) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("find :: Collection name should not be blank");
        }

        final int skipCount = (pageNo - 1) * pageSize;

        // Collection
        MongoCollection<Document> collection = this.database.getCollection(collectionName);
        List<Document> result = collection.find(filter).skip(skipCount).limit(pageSize).into(new ArrayList<>());

        return result;
    }

    /**
     * Find all documents of a collection, matching the passed filter
     * 
     * @param collectionName name of the collection
     * @param filter filtering criteria
     * 
     * @return {@link FindIterable} of documents.
     */
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
