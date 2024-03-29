package com.github.osm.mongo;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.mongo.helper.MongoConfig;
import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;


public class MongoStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoStore.class);

    public static final int DEFAULT_BATCH_SIZE = 100;

    protected final MongoClient client;
    protected final MongoDatabase database;


    // Constructor

    public MongoStore(final MongoConfig config) {
        super();

        // Sanity checks
        if (config == null) {
            throw new IllegalArgumentException("MongoStore :: config, should not be blank");
        }

        // Inputs
        final String host = config.getHost();
        final int port = config.getPort();
        final String dbName = config.getDatabase();

        // MongoDB URI
        final String mongoURI = String.format("mongodb://%s:%d", host, port);
        LOGGER.info("MongoDB URI : {}", mongoURI);

        // MongoDB Client Settings
        final Block<ConnectionPoolSettings.Builder> connPoolSettings = builder -> builder //
                .maxConnectionIdleTime(0, MILLISECONDS) //
                .minSize(16);

        final Block<SocketSettings.Builder> socketSettings = builder -> builder //
                .connectTimeout(0, MILLISECONDS) //
                .readTimeout(0, MILLISECONDS);

        final MongoClientSettings clientSettings = MongoClientSettings.builder() //
                .applyConnectionString(new ConnectionString(mongoURI)) //
                .applyToConnectionPoolSettings(connPoolSettings) //
                .applyToSocketSettings(socketSettings) //
                .build();

        // MongoDB Client
        this.client = MongoClients.create(clientSettings);
        LOGGER.info("MongoDataStore initiated with :: {}:{}", host, port);

        this.database = client.getDatabase(dbName);
        LOGGER.info("Successfully initiated the Mongo Connection with config : {}", config);
    }


    // DB Methods
    // ------------------------------------------------------------------------

    // Index

    public void indexCollection(final String collectionName, final String field) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("count :: Collection name should not be blank");
        }

        // Collection
        final MongoCollection<Document> collection = this.database.getCollection(collectionName);
        collection.createIndex(Indexes.descending(field));
    }


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
     * 
     * @return total no. of documents of a collection.
     */
    public long count(final String collectionName) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("count :: Collection name should not be blank");
        }

        // Collection
        final MongoCollection<Document> collection = this.database.getCollection(collectionName);
        return collection.countDocuments();
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
        final MongoCollection<Document> collection = this.database.getCollection(collectionName);
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
        final MongoCollection<Document> collection = this.database.getCollection(collectionName);
        final Document result = collection.find(filter).first();

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
        final MongoCollection<Document> collection = this.database.getCollection(collectionName);
        final List<Document> result = collection.find(filter).noCursorTimeout(true).into(new ArrayList<>());

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
        final MongoCollection<Document> collection = this.database.getCollection(collectionName);
        final List<Document> result =
                collection.find(filter).noCursorTimeout(true).skip(skipCount).limit(pageSize).into(new ArrayList<>());

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
        return this.find(collectionName, filter, DEFAULT_BATCH_SIZE);
    }


    /**
     * Find all documents of a collection, matching the passed filter
     * 
     * @param collectionName name of the collection
     * @param filter filtering criteria
     * @param batchSize the number of documents to return per batch.
     * 
     * @return {@link FindIterable} of documents.
     */
    public FindIterable<Document> find(final String collectionName, final Document filter, final int batchSize) {
        // Sanity checks
        if (StringUtils.isEmpty(collectionName)) {
            throw new IllegalArgumentException("find :: Collection name should not be blank");
        }

        final MongoCollection<Document> collection = this.database.getCollection(collectionName);
        final FindIterable<Document> result = collection.find(filter).noCursorTimeout(true).batchSize(batchSize);

        return result;
    }


}
