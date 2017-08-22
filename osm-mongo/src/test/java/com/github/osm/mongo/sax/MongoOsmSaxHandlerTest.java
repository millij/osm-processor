package com.github.osm.mongo.sax;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.domain.Node;
import com.github.osm.mongo.OsmMongoStore;
import com.github.osm.mongo.helper.MongoConfig;
import com.github.osm.reader.OsmXmlReader;
import com.github.osm.sax.OsmSaxHandler;


public class MongoOsmSaxHandlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoOsmSaxHandlerTest.class);

    private OsmMongoStore _mongoStore;
    private OsmSaxHandler _osmSaxHandler;

    private OsmXmlReader _osmXmlReader;

    private String _osmXml_sample1;
    private String _osmXml_sample2;


    // Setup
    // ------------------------------------------------------------------------

    @Before
    public void setup() throws ParseException {
        // init
        final MongoConfig config = MongoConfig.with("localhost", 27017, "mongo_java_test");
        _mongoStore = OsmMongoStore.withConfig(config);

        _osmXmlReader = new OsmXmlReader();
        _osmSaxHandler = new MongoOsmSaxHandler(_mongoStore);


        // files
        _osmXml_sample1 = "src/test/resources/samples/osm_node.xml";
        _osmXml_sample2 = "src/test/resources/samples/osm_bbox.xml";
    }

    @After
    public void teardown() {
        // nothing to do
    }


    // Tests
    // ------------------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void test_readOsmXml_null_filepath() {
        _osmXmlReader.read(_osmSaxHandler, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_readOsmXml_empty_filepath() {
        _osmXmlReader.read(null, null);
    }

    @Test
    public void test_readOsmXml_valid_file1() throws FileNotFoundException {
        // Stream
        final InputStream is = new FileInputStream(_osmXml_sample1);
        Map<String, Object> result = _osmXmlReader.read(_osmSaxHandler, is);
        LOGGER.info("OsmXMLread Result for - {} is : {}", _osmXml_sample1, result);

        Assert.assertTrue(result != null && result.size() > 0);
        Assert.assertEquals(1, _osmSaxHandler.getNodesCount());

        Node node = _mongoStore.findOne(Node.class, 3233393892L);
        Assert.assertEquals(3233393892L, node.getOsmId());
    }

    @Test
    public void test_readOsmXml_valid_file2() throws FileNotFoundException {
        // Stream
        final InputStream is = new FileInputStream(_osmXml_sample2);
        Map<String, Object> result = _osmXmlReader.read(_osmSaxHandler, is);
        LOGGER.info("OsmXMLread Result for - {} is : {}", _osmXml_sample2, result);

        Assert.assertTrue(result != null && result.size() > 0);
    }

}
