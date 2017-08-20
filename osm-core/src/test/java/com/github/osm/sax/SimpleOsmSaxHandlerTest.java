package com.github.osm.sax;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.osm.domain.Node;
import com.github.osm.reader.OsmXmlReader;
import com.github.osm.sax.OsmSaxHandler;
import com.github.osm.sax.SimpleOsmSaxHandler;


public class SimpleOsmSaxHandlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleOsmSaxHandlerTest.class);

    private OsmXmlReader _osmXmlReader;
    private OsmSaxHandler _osmSaxHandler;

    private String _osmXml_sample1;
    private String _osmXml_sample2;


    // Setup
    // ------------------------------------------------------------------------

    @Before
    public void setup() throws ParseException {
        // init
        _osmXmlReader = new OsmXmlReader();
        _osmSaxHandler = new SimpleOsmSaxHandler();

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
        
        Assert.assertTrue(result != null && result.size() >0);

        // Simple Handler
        final SimpleOsmSaxHandler simpleHandler = (SimpleOsmSaxHandler) _osmSaxHandler;
        List<Node> nodes = simpleHandler.getNodes();
        LOGGER.info("Nodes Read : {}", nodes);

        Assert.assertTrue(nodes != null && nodes.size() > 0);
    }

    @Test
    public void test_readOsmXml_valid_file2() throws FileNotFoundException {
     // Stream
        final InputStream is = new FileInputStream(_osmXml_sample2);
        Map<String, Object> result = _osmXmlReader.read(_osmSaxHandler, is);
        LOGGER.info("OsmXMLread Result for - {} is : {}", _osmXml_sample2, result);
        
        Assert.assertTrue(result != null && result.size() >0);
    }


}
