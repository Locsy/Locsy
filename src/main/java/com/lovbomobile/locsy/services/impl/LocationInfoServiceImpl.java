package com.lovbomobile.locsy.services.impl;

import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.entities.Location;
import com.lovbomobile.locsy.entities.User;
import com.lovbomobile.locsy.services.LocationInfoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LocationInfoServiceImpl implements LocationInfoService {


    UserDao userDao;

    public Map<String, Location> getLocationsForUser(String userID) {
        Map<String, Location> locations = new HashMap<String, Location>();
        List<User> friends = userDao.getFriendsWhoCanBeSeenBy(userID);

        for (User friend : friends) {
            if (friend.getLocation() != null) {
                locations.put(friend.getUserID(), friend.getLocation());
            }
        }


        return locations;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
