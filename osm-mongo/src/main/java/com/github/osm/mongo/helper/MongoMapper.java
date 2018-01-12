package com.github.osm.mongo.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.osm.bean.OsmBean;
import com.github.osm.domain.Member;
import com.github.osm.domain.MetaInfo;
import com.github.osm.domain.Node;
import com.github.osm.domain.OSM;
import com.github.osm.domain.OsmEntity.Type;
import com.github.osm.domain.Relation;
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

    public static Node node(Document nodeDoc) {
        long osmId = (long) nodeDoc.get("osmId");

        MetaInfo metaInfo = metaInfo((Map<String, Object>) nodeDoc.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) nodeDoc.get("tags");

        double latitude = (double) nodeDoc.get("latitude");
        double longitude = (double) nodeDoc.get("longitude");

        return OSM.node(osmId, metaInfo, tags, latitude, longitude);
    }


    public static Way way(Document wayDoc) {
        long osmId = (long) wayDoc.get("osmId");

        MetaInfo metaInfo = metaInfo((Map<String, Object>) wayDoc.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) wayDoc.get("tags");

        List<Long> nodeIds = (List<Long>) wayDoc.get("nodeIds");

        return OSM.way(osmId, metaInfo, tags, nodeIds);
    }


    public static Relation relation(Document relationDoc) {
        long osmId = (long) relationDoc.get("osmId");

        MetaInfo metaInfo = metaInfo((Map<String, Object>) relationDoc.get("metaInfo"));
        Map<String, String> tags = (Map<String, String>) relationDoc.get("tags");

        List<Map<String, Object>> membersList = (List<Map<String, Object>>) relationDoc.get("members");

        List<Member> members = new ArrayList<>();
        if (membersList != null && membersList.size() > 0) {
            members = membersList.stream().map(memberMap -> {
                return member(memberMap);
            }).collect(Collectors.toList());
        }

        return OSM.relation(osmId, metaInfo, tags, members);
    }

    public static Member member(Map<String, Object> memberMap) {
        if (memberMap == null) {
            return null;
        }

        Type type = Type.valueOf(memberMap.get("type").toString());
        long id = (long) memberMap.get("id");
        String role = (String) memberMap.get("role");

        return OSM.member(type, id, role);
    }

    public static MetaInfo metaInfo(Map<String, Object> metaInfo) {
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


    // To JSON methods
    // ------------------------------------------------------------------------

    public static String toJson(OsmBean osmBean) throws JsonProcessingException {
        MAPPER.setSerializationInclusion(Include.NON_EMPTY);
        return MAPPER.writeValueAsString(osmBean);
    }

}
