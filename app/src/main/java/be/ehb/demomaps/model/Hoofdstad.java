package be.ehb.demomaps.model;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Hoofdstad implements Serializable {

    public enum Continent{EUROPA, USA, NOORD_AMERIKA, ZUID_AMERIKA, AFRIKA, AZIE, OCEANIE , ANTARTICA}

    transient private LatLng coordinate;
    private String cityName;
    private Continent continent;
    private int drawableId;

    public Hoofdstad(LatLng coordinate, String cityName, Continent continent, int drawabled) {
        this.coordinate = coordinate;
        this.cityName = cityName;
        this.continent = continent;
        this.drawableId = drawabled;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = this.drawableId;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setContinent(Continent continent) {
        this.continent = continent;
    }
}
