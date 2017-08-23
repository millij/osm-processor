package com.github.osm.sax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.github.osm.domain.MetaInfo;
import com.github.osm.domain.Node;
import com.github.osm.domain.OSM;
import com.github.osm.domain.OsmEntity.Type;
import com.github.osm.domain.Relation;
import com.github.osm.domain.Member;
import com.github.osm.domain.Way;

/**
 * An abstract event handler for OSM XML parsing events.
 */
public abstract class OsmSaxHandler extends DefaultHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsmSaxHandler.class);

    public static final String ENTITY_ATTR_ID = "id";

    public static final String ENTITY_ATTR_VERSION = "version";
    public static final String ENTITY_ATTR_CHANGESET = "changeset";
    public static final String ENTITY_ATTR_TIMESTAMP = "timestamp";
    public static final String ENTITY_ATTR_USER = "user";
    public static final String ENTITY_ATTR_USERID = "uid";

    public static final String META_ATTR_VISIBLE = "visible";

    public static final String TAG_ELEMENT = "tag";
    public static final String TAG_ATTR_KEY = "k";
    public static final String TAG_ATTR_VALUE = "v";

    public static final String NODE_ELEMENT = "node";
    public static final String NODE_ATTR_LAT = "lat";
    public static final String NODE_ATTR_LONG = "lon";

    public static final String WAY_ELEMENT = "way";

    public static final String WAY_ND_ELEMENT = "nd";
    public static final String WAY_ND_ATTR_REF = "ref";

    public static final String RELATION_ELEMENT = "relation";

    public static final String MEMBER_ELEMENT = "member";
    public static final String MEMBER_ATTR_TYPE = "type";
    public static final String MEMBER_ATTR_REF = "ref";
    public static final String MEMBER_ATTR_ROLE = "role";


    // Entity temp variables
    private long osmId;
    private MetaInfo metaInfo;
    private Map<String, String> tagsMap = new HashMap<>();

    private double latitude;
    private double longitude;

    private List<Long> nodeIds = new ArrayList<>();

    private List<Member> members = new ArrayList<>();


    // Constructors
    // ------------------------------------------------------------------------

    public OsmSaxHandler() {
        super();
    }


    // SAX DefaultHandler methods
    // ------------------------------------------------------------------------

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        LOGGER.info("Document parsing started ...");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        LOGGER.info("Document parsing completed ...");

        // Trigger callback
        this.documentCompleted();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        super.startElement(uri, localName, qName, attrs);

        // read metaInfo
        if (attrs != null && attrs.getIndex(ENTITY_ATTR_ID) >= 0) {

            this.osmId = Long.valueOf(attrs.getValue(ENTITY_ATTR_ID));

            String inVersion = attrs.getValue(ENTITY_ATTR_VERSION);
            final int version = Integer.valueOf(inVersion == null ? "0" : inVersion);

            String inChangesetId = attrs.getValue(ENTITY_ATTR_CHANGESET);
            final int changesetId = Integer.valueOf(inChangesetId == null ? "0" : inChangesetId);

            final String timestamp = attrs.getValue(ENTITY_ATTR_TIMESTAMP);
            final String userName = attrs.getValue(ENTITY_ATTR_USER);

            String inUserId = attrs.getValue(ENTITY_ATTR_USERID);
            final int userId = Integer.valueOf(inUserId == null ? "0" : inUserId);

            this.metaInfo = OSM.metaInfo(version, changesetId, timestamp, userName, userId);
        }

        switch (qName) {
            case NODE_ELEMENT:
                this.latitude = Double.valueOf(attrs.getValue(NODE_ATTR_LAT));
                this.longitude = Double.valueOf(attrs.getValue(NODE_ATTR_LONG));
                break;

            case WAY_ELEMENT:
            case RELATION_ELEMENT:
                // Nothing to do here
                break;

            case TAG_ELEMENT:
                final String key = attrs.getValue(TAG_ATTR_KEY);
                if (!key.equalsIgnoreCase("created_by"))
                    this.tagsMap.put(key, attrs.getValue(TAG_ATTR_VALUE));
                break;

            case WAY_ND_ELEMENT:
                final long refNodeId = Long.valueOf(attrs.getValue(WAY_ND_ATTR_REF));
                this.nodeIds.add(refNodeId);
                break;

            case MEMBER_ELEMENT:
                final long refId = Long.valueOf(attrs.getValue(MEMBER_ATTR_REF));
                final Type type = Type.valueOf(attrs.getValue(MEMBER_ATTR_TYPE).toLowerCase());
                final String role = attrs.getValue(MEMBER_ATTR_ROLE);

                this.members.add(OSM.member(type, refId, role));
                break;

            default:
                break;
        }


        LOGGER.debug("Element parsing started - qname: {}, meta: {}", qName, "");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        LOGGER.debug("Element parsing completed - qname: {}, triggering callback", qName);

        switch (qName) {
            case NODE_ELEMENT:
                Node node = OSM.node(osmId, metaInfo, tagsMap, latitude, longitude);
                this.handleNode(node);

                resetTempVariables(); // Sanity work
                break;

            case WAY_ELEMENT:
                Way way = OSM.way(osmId, metaInfo, tagsMap, nodeIds);
                this.handleWay(way);

                resetTempVariables(); // Sanity work
                break;

            case RELATION_ELEMENT:
                Relation relation = OSM.relation(osmId, metaInfo, tagsMap, members);
                this.handleRelation(relation);

                resetTempVariables(); // Sanity work
                break;

            case TAG_ELEMENT:
            case WAY_ND_ELEMENT:
            case MEMBER_ELEMENT:
                // No need to handle
                break;

            default:
                LOGGER.info("Unhandled element : {}", qName);
                break;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);

        // OSM data is always in attributes. Cool isn't it ?
    }


    // Private Methods
    // ------------------------------------------------------------------------

    private void resetTempVariables() {
        this.osmId = 0;
        this.metaInfo = null;
        this.latitude = 0;
        this.longitude = 0;

        this.tagsMap = new HashMap<>();
        this.nodeIds = new ArrayList<>();
        this.members = new ArrayList<>();
    }


    // Abstract callbacks
    // ------------------------------------------------------------------------

    /**
     * Callback for handling nodes.
     * 
     * @param node object
     */
    public abstract void handleNode(Node node);

    /**
     * Callback for handling ways.
     * 
     * @param way object
     */
    public abstract void handleWay(Way way);

    /**
     * Callback for handling relations.
     * 
     * @param relation object
     */
    public abstract void handleRelation(Relation relation);

    /**
     * This callback is called once after the whole document is read.
     */
    public abstract void documentCompleted();


    // Counts

    /**
     * Get the total Count of the nodes that were read.
     * 
     * @return nodes count
     */
    public abstract long getNodesCount();


    /**
     * Get the total Count of the ways that were read.
     * 
     * @return ways count
     */
    public abstract long getWaysCount();

    /**
     * Get the total Count of the relations that were read.
     * 
     * @return relations count
     */
    public abstract long getRelationsCount();

}
