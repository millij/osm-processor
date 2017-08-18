package com.github.osm.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.github.osm.sax.OsmResult;
import com.github.osm.sax.OsmSaxHandler;



public class OsmXmlReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsmXmlReader.class);

    private final SAXParserFactory saxFactory;
    private final SAXParser saxParser;


    // Constructor
    // ------------------------------------------------------------------------

    public OsmXmlReader() {
        super();

        // init
        try {
            this.saxFactory = SAXParserFactory.newInstance();
            this.saxParser = saxFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            LOGGER.error("Error creating the SAXParser", e);
            throw new RuntimeException("Unable to create SAX Parser", e);
        }
    }


    // OSM XML Methods
    // ------------------------------------------------------------------------

    /**
     * Read the OSM XML using SAX parser. When true is passed for persist flag, the read OsmElements
     * gets persisted to DB for future processing.
     * 
     * @param is XML file as {@link InputStream}
     * 
     * @return {@link OsmResult} object for this operation
     */
    public Map<String, Object> read(final OsmSaxHandler handler, final InputStream is) {
        // Sanity checks
        if (handler == null) {
            throw new IllegalArgumentException("read :: SaxHandler should not be null");
        }

        if (is == null) {
            throw new IllegalArgumentException("read :: Data InputStream should not be null");
        }

        try {
            this.saxParser.parse(is, handler);
        } catch (SAXException | IOException ex) {
            LOGGER.error("Error parsing the XML from stream", ex);
        }

        // Results
        Map<String, Object> detailsMap = new HashMap<>();
        detailsMap.put("nodesCount", handler.getNodesCount());
        detailsMap.put("waysCount", handler.getWaysCount());
        detailsMap.put("relationsCount", handler.getRelationsCount());

        return detailsMap;
    }

}
