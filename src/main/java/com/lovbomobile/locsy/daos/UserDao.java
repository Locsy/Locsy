package com.lovbomobile.locsy.daos;

import com.lovbomobile.locsy.entities.User;

import java.util.List;

public interface UserDao {

	User getUser(String userID);

    void addOrUpdateUser(User user);

    void deleteUser(String userID);

    List<User> getFriendsWhoCanBeSeenBy(String userID);

    List<User> getAllUsers();

}
