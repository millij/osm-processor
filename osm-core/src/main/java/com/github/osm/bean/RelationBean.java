package com.github.osm.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.osm.domain.Relation;
import com.github.osm.domain.OsmEntity.Type;


public class RelationBean extends OsmBean {

    private static final long serialVersionUID = 1L;

    private final long osmId;
    private final Map<String, String> tags;

    private final List<MemberBean> members;


    // Constructor

    public RelationBean(Relation relation, List<MemberBean> memberBeans) {
        super(relation);

        // init
        this.osmId = relation.getOsmId();
        this.tags = new HashMap<>(relation.getTags());

        // Members
        this.members = memberBeans == null ? new ArrayList<>() : new ArrayList<>(memberBeans);
    }


    // OSM Bean

    @Override
    public Type getType() {
        return Type.relation;
    }


    // Getters and Setters

    public long getOsmId() {
        return osmId;
    }

    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    public List<MemberBean> getMembers() {
        return Collections.unmodifiableList(members);
    }



}
