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
