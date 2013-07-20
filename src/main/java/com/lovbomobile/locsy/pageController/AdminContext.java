package com.lovbomobile.locsy.pageController;

import com.lovbomobile.locsy.entities.User;

/**
 * Created with IntelliJ IDEA.
 * User: bongo
 * Date: 5/11/13
 * Time: 12:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminContext {

    User selectedUser;

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }


}
