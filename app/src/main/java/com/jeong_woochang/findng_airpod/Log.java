package com.jeong_woochang.findng_airpod;

/**
 * Created by jeong-woochang on 2018. 8. 27..
 */

public class Log {
    String name;
    String state;
    String lat;
    String lng;

    public Log(String name, String state, String lat, String lng){
        this.name=name;
        this.state=state;
        this.lat=lat;
        this.lng=lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
