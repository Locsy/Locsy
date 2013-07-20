package com.lovbomobile.locsy.entities;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class Location{


	
	private double latitude;
	
	private double longitude;
	
	private long clientTime = -1;
	
	private long serverTime = -1;

    private float accuracy = 0;
	
	public Location() {};
	
	public Location(double latitude, double longitude) {
	
		this.latitude = latitude;
		this.longitude = longitude;
		
	}


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getClientTime() {
        return clientTime;
    }

    public void setClientTime(long clientTime) {
        this.clientTime = clientTime;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
}
