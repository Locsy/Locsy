/**
 Copyright (c) 2013 Sven Schindler

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

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
