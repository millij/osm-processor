package com.github.osm.domain;

import java.util.Date;

public final class Bound extends OsmEntity {

    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    private final double right;
    private final double left;
    private final double top;
    private final double bottom;
    private final String origin;


    // Constructors
    // ------------------------------------------------------------------------

    Bound(double right, double left, double top, double bottom, String origin) {
        super(0, new MetaInfo(0, 0, new Date().toString(), "", -1));

        // Check if any coordinates are out of bounds
        if (Double.compare(right, MAX_LONGITUDE + 1.0d) > 0 || Double.compare(right, MIN_LONGITUDE - 1.0d) < 0
                || Double.compare(left, MAX_LONGITUDE + 1.0d) > 0 || Double.compare(left, MIN_LONGITUDE - 1.0d) < 0
                || Double.compare(top, MAX_LATITUDE + 1.0d) > 0 || Double.compare(top, MIN_LATITUDE - 1.0d) < 0
                || Double.compare(bottom, MAX_LATITUDE + 1.0d) > 0 || Double.compare(bottom, MIN_LATITUDE - 1.0d) < 0) {

            throw new IllegalArgumentException("Bound coordinates outside of valid range");
        }

        if (Double.compare(top, bottom) < 0) {
            throw new IllegalArgumentException("Bound top < bottom");
        }

        this.right = right;
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.origin = origin;
    }

    Bound(String origin) {
        this(MAX_LONGITUDE, MIN_LONGITUDE, MAX_LATITUDE, MIN_LATITUDE, origin);
    }


    // Getters and Setters
    // ------------------------------------------------------------------------

    public double getRight() {
        return right;
    }

    public double getLeft() {
        return left;
    }

    public double getTop() {
        return top;
    }

    public double getBottom() {
        return bottom;
    }

    public String getOrigin() {
        return origin;
    }

    @Override
    public Type getType() {
        return Type.bound;
    }


    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "Bound [right=" + right + ", left=" + left + ", top=" + top + ", bottom=" + bottom + "]";
    }



}
