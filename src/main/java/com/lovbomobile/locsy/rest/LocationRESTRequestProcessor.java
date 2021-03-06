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

package com.lovbomobile.locsy.rest;


import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.entities.Location;
import com.lovbomobile.locsy.entities.User;
import com.lovbomobile.locsy.services.LocationInfoService;
import com.lovbomobile.locsy.services.SecurityService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Scope("singleton")
@Path("/location")
public class LocationRESTRequestProcessor {

	 Logger logger = Logger.getLogger(this.getClass());
	
	 LocationInfoService locationInfoService;

     SecurityService securityService;

    UserDao userDao;

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("user/{user}/{timestamp}/{hash}")
	public Location setLocation(@PathParam("user") String userID,Location location,
			@PathParam("timestamp") String timestamp,@PathParam("hash") String hash) {
		if (logger.isDebugEnabled()) {
			logger.debug("new location update received from "+userID);
		}

		if (location != null && securityService.isAccessAllowed(userID, timestamp, hash)) {
			location.setServerTime(System.currentTimeMillis());
			User user = userDao.getUser(userID);
            user.setLocation(location);
            userDao.addOrUpdateUser(user);
			return location;
		} else {
			return new Location();
		}
		
		
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user/{user}/friends/{timestamp}/{hash}")
	public Map<String, Location> getLocationOfFriends(@PathParam("user") String userID, 
			@PathParam("timestamp") String timestamp, @PathParam("hash") String hash) {
		
		if (logger.isDebugEnabled()) {
			logger.debug("location info requested by "+ userID);
		}

		if (securityService.isAccessAllowed(userID, timestamp, hash)) {
			return locationInfoService.getLocationsForUser(userID);
		} else {
			return new HashMap<String, Location>();
		}
	}


    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void setLocationInfoService(LocationInfoService locationInfoService) {
        this.locationInfoService = locationInfoService;

    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }



}
