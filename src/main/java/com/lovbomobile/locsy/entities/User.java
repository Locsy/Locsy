package com.lovbomobile.locsy.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bongo
 * Date: 5/5/13
 * Time: 7:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Document(collection = "users")
public class User {

    private String userID;

    private String password;

    private Location location;

    private List<String> friendsWhoCanSeeLocation;

    @Id
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getFriendsWhoCanSeeLocation() {
        return friendsWhoCanSeeLocation;
    }

    public void setFriendsWhoCanSeeLocation(List<String> friendsWhoCanSeeMyLocation) {
        this.friendsWhoCanSeeLocation = friendsWhoCanSeeMyLocation;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof User) {
             return userID.equals(((User)obj).getUserID());
        }

        return false;
    }
}
