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

package com.lovbomobile.locsy.pageController;

import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.entities.User;

import java.util.ArrayList;
import java.util.List;

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
