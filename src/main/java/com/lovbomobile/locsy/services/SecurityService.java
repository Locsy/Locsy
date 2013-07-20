package com.lovbomobile.locsy.services;

public interface SecurityService {

	boolean isAccessAllowed(String userID, String timestamp, String hash);

	
}
