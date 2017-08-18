package com.github.osm.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.osm.domain.MetaInfo;
import com.github.osm.domain.Node;
import com.github.osm.domain.OSM;
import com.github.osm.domain.Relation;
import com.github.osm.domain.RelationMember;
import com.github.osm.domain.Way;
import com.github.osm.domain.OsmEntity.Type;


@SuppressWarnings("unchecked")
public class OsmMapper {

    private OsmMapper() {
        // Utility Class
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    
    

    // ObjectMapper methods
    // ------------------------------------------------------------------------

    public static Map<String, Object> asMap(Node node) {
        if (node == null) {
            return new HashMap<>();
        }

        return MAPPER.convertValue(node, Map.class);
    }

    public static Node asNode(Map<String, Object> nodeMap) {
        long osmId = (long) nodeMap.get("osmId");

        MetaInfo metaInfo = asMetaInfo((Map<String, Object>) nodeMap.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) nodeMap.get("tags");

        double latitude = (double) nodeMap.get("latitude");
        double longitude = (double) nodeMap.get("longitude");

        return OSM.node(osmId, metaInfo, tags, latitude, longitude);
    }

    public static Map<String, Object> asMap(Way way) {
        if (way == null) {
            return new HashMap<>();
        }

        return MAPPER.convertValue(way, Map.class);
    }

    public static Way asWay(Map<String, Object> wayMap) {
        long osmId = (long) wayMap.get("osmId");

        MetaInfo metaInfo = asMetaInfo((Map<String, Object>) wayMap.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) wayMap.get("tags");

        List<Long> nodeIds = (List<Long>) wayMap.get("nodeIds");

        return OSM.way(osmId, metaInfo, tags, nodeIds);
    }

    public static Map<String, Object> asMap(Relation relation) {
        if (relation == null) {
            return new HashMap<>();
        }

        return MAPPER.convertValue(relation, Map.class);
    }

    public static Relation asRelation(Map<String, Object> relationMap) {
        long osmId = (long) relationMap.get("osmId");

        MetaInfo metaInfo = asMetaInfo((Map<String, Object>) relationMap.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) relationMap.get("tags");

        List<Map<String, Object>> membersList = (List<Map<String, Object>>) relationMap.get("members");

        List<RelationMember> members = new ArrayList<>();
        if (membersList != null && membersList.size() > 0) {
            members = membersList.stream().map(memberMap -> {
                return asRelationMember(memberMap);
            }).collect(Collectors.toList());
        }

        return OSM.relation(osmId, metaInfo, tags, members);
    }



    // Util Methods
    // ------------------------------------------------------------------------

    public static boolean isPointOfInterest(Map<String, String> tags) {
        if (tags.containsKey("amenity") && tags.containsKey("name")) {
            return true;
        }

        return false;
    }

    public static boolean isGeoRegion(Map<String, String> tags) {
        // ignore maritime
        String mt = tags.get("maritime");
        if (mt != null && mt.equalsIgnoreCase("yes")) {
            return false;
        }

        if (!tags.containsKey("name")) {
            return false;
        }

        if (tags.containsKey("place") || tags.containsKey("boundary")) {
            return true;
        }

        return false;
    }

    public static boolean isRoute(Map<String, String> tags) {

        // highway
        if (tags.containsKey("highway") && tags.containsKey("name")) {
            return true;
        }

        // String hw = tags.get("highway");
        // if (hw != null) {
        // return true;
        // }

        // TODO Complete
        return false;
    }

    public static boolean isOuterWay(RelationMember m) {
        String type = m.getMemberType().toString();
        String role = m.getMemberRole();

        if (!type.equals("way")) {
            return false;
        }

        if (role == null || role.trim().length() == 0 || role.equals("outer")) {
            return true;
        }

        return false;
    }



    // Private methods
    // ------------------------------------------------------------------------

    private static RelationMember asRelationMember(Map<String, Object> memberMap) {
        if (memberMap == null) {
            return null;
        }

        long memberId = (long) memberMap.get("memberId");
        Type memberType = Type.valueOf(memberMap.get("memberType").toString());
        String memberRole = (String) memberMap.get("memberRole");

        return OSM.relationMember(memberId, memberType, memberRole);
    }


    private static MetaInfo asMetaInfo(Map<String, Object> metaInfo) {
        if (metaInfo == null) {
            return null;
        }

        int version = (int) metaInfo.get("version");
        int changesetId = (int) metaInfo.get("version");
        String timestamp = (String) metaInfo.get("timestamp");
        String userName = (String) metaInfo.get("userName");
        int userId = (int) metaInfo.get("userId");

        return OSM.metaInfo(version, changesetId, timestamp, userName, userId);
    }

}
