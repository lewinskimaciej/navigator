package com.atc.navigator.models;

import android.location.Location;

import org.joda.time.DateTime;

public class PoiModel {
    private String poiName;
    private Location poiCoordinates;
    private DateTime poiDate;

    public String getPoiName() {
        return poiName;
    }

    public PoiModel setPoiName(String poiName) {
        this.poiName = poiName;
        return this;
    }

    public Location getPoiCoordinates() {
        return poiCoordinates;
    }

    public PoiModel setPoiCoordinates(Location poiCoordinates) {
        this.poiCoordinates = poiCoordinates;
        return this;
    }

    public DateTime getPoiDate() {
        return poiDate;
    }

    public PoiModel setPoiDate(DateTime poiDate) {
        this.poiDate = poiDate;
        return this;
    }
}
