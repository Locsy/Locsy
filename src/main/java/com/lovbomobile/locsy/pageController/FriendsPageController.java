package com.lovbomobile.locsy.pageController;

import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bongo
 * Date: 5/11/13
 * Time: 12:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class FriendsPageController {

    AdminContext adminContext;

    UserDao userDao;



    public User getSelectedUser() {
        return adminContext.getSelectedUser();
    }



    public void setAdminContext(AdminContext adminContext) {
        this.adminContext = adminContext;
    }

    public List<User> getUsersWhoCanBeSeen() {
           String selectedUserID =   adminContext.getSelectedUser().getUserID();
           return userDao.getFriendsWhoCanBeSeenBy(selectedUserID);
    }

    public List<User> getUsersWhoCannotBeSeen() {
        String selectedUserID =   adminContext.getSelectedUser().getUserID();
        List<User> usersThatCanBeSeen = userDao.getFriendsWhoCanBeSeenBy(selectedUserID);
        List<User> allUsersButFriends = userDao.getAllUsers();
        allUsersButFriends.removeAll(usersThatCanBeSeen);
        allUsersButFriends.remove(adminContext.getSelectedUser());
        return allUsersButFriends;
    }

    public String addSelectedUserToFriendListOf(User user) {
        List<String> friends = user.getFriendsWhoCanSeeLocation();
        if (friends == null) {
            friends = new ArrayList<String>();
            user.setFriendsWhoCanSeeLocation(friends);
        }
        friends.add(adminContext.getSelectedUser().getUserID());
        userDao.addOrUpdateUser(user);
        return Pages.FRIENDS_PAGE;
    }


    public String removeSelectedUserFromFriendListOf(User user) {
        user.getFriendsWhoCanSeeLocation().remove(adminContext.getSelectedUser().getUserID());
        userDao.addOrUpdateUser(user);
        return Pages.FRIENDS_PAGE;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }




}
