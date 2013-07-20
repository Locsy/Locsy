package com.lovbomobile.locsy.services.impl;

import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.services.SecurityService;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleSecurityService implements SecurityService{

	UserDao userDao;
	
	Map<String,Long> timestamps;

    public SimpleSecurityService() {
		timestamps = new ConcurrentHashMap<String, Long>();
	}
	
	@Override
	public boolean isAccessAllowed(String userID, String timestamp, String hash) {
		
		if (!isTimestampValid(userID, timestamp) || userDao.getUser(userID)==null) {
			return false;
		}
		
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");
			String computedHash = ((new HexBinaryAdapter()).marshal((digest.digest((userID+userDao.getUser(userID).getPassword()+timestamp).getBytes()))));
			if (computedHash.equalsIgnoreCase(hash)) {
				return true;
			}
		} catch (NoSuchAlgorithmException e) {
			return false;
		}
		return false;
	}
	
	private boolean isTimestampValid(String userID, String timestamp) {
		
		Long timestampLong;
	
		try {
			timestampLong = Long.valueOf(timestamp);
		}catch (NumberFormatException exception) {
			System.out.println("Invalid timestamp received!");
			return false;
		}
		
		Long storedTimestamp = timestamps.get(userID);
		if (storedTimestamp == null || storedTimestamp < timestampLong) {
			timestamps.put(userID, timestampLong);
			return true;
		} else { 
			return false;
		}
	}


    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }



}
