package com.github.osm.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * OSM entity Meta information.
 */
public final class MetaInfo {

    private final int version;
    private final int changesetId;
    private final String timestamp;

    private final String userName;
    private final int userId;


    // Constructors
    // ------------------------------------------------------------------------

    MetaInfo(int version, int changesetId, String timestamp, String userName, int userId) {
        super();
        this.version = version;
        this.changesetId = changesetId;
        this.timestamp = timestamp;
        this.userName = userName;
        this.userId = userId;
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public int getVersion() {
        return version;
    }

    public int getChangesetId() {
        return changesetId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId() {
        return userId;
    }


    // Custom Methods
    // ------------------------------------------------------------------------

    public Map<String, String> asMap() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("osm_version", String.valueOf(version));
        infoMap.put("osm_changesetId", String.valueOf(changesetId));
        infoMap.put("osm_timestamp", timestamp);
        infoMap.put("osm_userName", userName);
        infoMap.put("osm_userId", String.valueOf(userId));

        return Collections.unmodifiableMap(infoMap);
    }

}
