package com.lovbomobile.locsy.services;

import com.lovbomobile.locsy.entities.Location;

import java.util.Map;


public interface LocationInfoService {
	
	Map<String, Location> getLocationsForUser(String userID);
}
