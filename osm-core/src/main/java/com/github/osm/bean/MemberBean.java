package com.github.osm.bean;

import com.github.osm.domain.OsmEntity.Type;


public class MemberBean {

    private final Type type;
    private final long id;
    private final String role;

    private final OsmBean bean;


    // Constructor

    private MemberBean(Type type, long id, String role, OsmBean bean) {
        super();

        this.type = type;
        this.id = id;
        this.role = role;
        this.bean = bean;
    }


    // Getters and Setters

    public Type getType() {
        return type;
    }
    
    public long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public OsmBean getBean() {
        return bean;
    }


    // Factory Methods
    // ------------------------------------------------------------------------

    public static MemberBean from(long id, String role, NodeBean node) {
        return new MemberBean(Type.node, id, role, node);
    }

    public static MemberBean from(long id, String role, WayBean way) {
        return new MemberBean(Type.way, id, role, way);
    }
    
    public static MemberBean from(long id, String role, RelationBean relation) {
        return new MemberBean(Type.relation, id, role, relation);
    }


}
