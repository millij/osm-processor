package com.github.osm.mongo.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.osm.domain.MetaInfo;
import com.github.osm.domain.Node;
import com.github.osm.domain.OSM;
import com.github.osm.domain.OsmEntity.Type;
import com.github.osm.domain.Relation;
import com.github.osm.domain.RelationMember;
import com.github.osm.domain.Way;


@SuppressWarnings("unchecked")
public class MongoMapper {
    
    private MongoMapper() {
        // Util Class
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();


    // To Document methods
    // ------------------------------------------------------------------------

    public static Document document(Node node) {
        if (node == null) {
            return new Document();
        }

        return new Document(MAPPER.convertValue(node, Map.class));
    }

    public static Document document(Way way) {
        if (way == null) {
            return new Document();
        }

        return new Document(MAPPER.convertValue(way, Map.class));
    }

    public static Document document(Relation relation) {
        if (relation == null) {
            return new Document();
        }

        return new Document(MAPPER.convertValue(relation, Map.class));
    }


    // From Document Methods
    // ------------------------------------------------------------------------

    public static Node asNode(Document nodeDoc) {
        long osmId = (long) nodeDoc.get("osmId");

        MetaInfo metaInfo = asMetaInfo((Map<String, Object>) nodeDoc.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) nodeDoc.get("tags");

        double latitude = (double) nodeDoc.get("latitude");
        double longitude = (double) nodeDoc.get("longitude");

        return OSM.node(osmId, metaInfo, tags, latitude, longitude);
    }


    public static Way asWay(Document wayDoc) {
        long osmId = (long) wayDoc.get("osmId");

        MetaInfo metaInfo = asMetaInfo((Map<String, Object>) wayDoc.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) wayDoc.get("tags");

        List<Long> nodeIds = (List<Long>) wayDoc.get("nodeIds");

        return OSM.way(osmId, metaInfo, tags, nodeIds);
    }


    public static Relation asRelation(Document relationDoc) {
        long osmId = (long) relationDoc.get("osmId");

        MetaInfo metaInfo = asMetaInfo((Map<String, Object>) relationDoc.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) relationDoc.get("tags");

        List<Map<String, Object>> membersList = (List<Map<String, Object>>) relationDoc.get("members");

        List<RelationMember> members = new ArrayList<>();
        if (membersList != null && membersList.size() > 0) {
            members = membersList.stream().map(memberMap -> {
                return asRelationMember(memberMap);
            }).collect(Collectors.toList());
        }

        return OSM.relation(osmId, metaInfo, tags, members);
    }

    public static RelationMember asRelationMember(Map<String, Object> memberMap) {
        if (memberMap == null) {
            return null;
        }

        long memberId = (long) memberMap.get("memberId");
        Type memberType = Type.valueOf(memberMap.get("memberType").toString());
        String memberRole = (String) memberMap.get("memberRole");

        return OSM.relationMember(memberId, memberType, memberRole);
    }


    public static MetaInfo asMetaInfo(Map<String, Object> metaInfo) {
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
