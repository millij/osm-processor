package com.github.osm.domain;

import java.util.List;
import java.util.Map;

import com.github.osm.domain.OsmEntity.Type;


/**
 * Factory for <a href="http://www.openstreetmap.org/">OSM</a> entities. <br>
 * Read <a href="http://wiki.openstreetmap.org/">Wiki</a> for more details.
 */
public class OSM {

    /**
     * factory.
     */
    private OSM() {}


    // Formats
    // ------------------------------------------------------------------------

    public static final String DATE_FORMAT_STR = "yyyy-MM-dd'T'HH:mm:ssz";


    // Labels
    // ------------------------------------------------------------------------

    // Elements

    public static final String ELEMENT_NODE = "node";
    public static final String ELEMENT_WAY = "way";
    public static final String ELEMENT_RELATION = "relation";


    // Attributes


    // Tags



    // Factory methods for OSM domain Objects
    // ------------------------------------------------------------------------

    public static MetaInfo metaInfo(int version, int changesetId, String timestamp, String userName, int userId) {
        return new MetaInfo(version, changesetId, timestamp, userName, userId);
    }

    public static Bound bound(double right, double left, double top, double bottom, String origin) {
        return new Bound(right, left, top, bottom, origin);
    }

    public static Node node(long osmId, MetaInfo metaInfo, Map<String, String> tags, double latitude, double longitude) {
        return new Node(osmId, metaInfo, tags, latitude, longitude);
    }

    public static Way way(long osmId, MetaInfo metaInfo, Map<String, String> tags, List<Long> nodeIds) {
        return new Way(osmId, metaInfo, tags, nodeIds);
    }

    public static Relation relation(long osmId, MetaInfo metaInfo, Map<String, String> tags, List<Member> members) {
        return new Relation(osmId, metaInfo, tags, members);
    }

    public static Member member(Type type, long refId, String role) {
        return new Member(type, refId, role);
    }


}
