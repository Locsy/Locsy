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

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SettingsPageController {


    UserDao userDao;
    String enteredUserID = "";
    String enteredUserPassword = "";
    AdminContext adminContext;

    public void setAdminContext(AdminContext adminContext) {
        this.adminContext = adminContext;
    }

    public String getEnteredUserID() {
        return enteredUserID;
    }

    public void setEnteredUserID(String enteredUserID) {
        this.enteredUserID = enteredUserID;
    }

    public String getEnteredUserPassword() {
        return enteredUserPassword;
    }

    public void setEnteredUserPassword(String enteredUserPassword) {
        this.enteredUserPassword = enteredUserPassword;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public int getNumberOfUsers() {
        return userDao.getAllUsers().size();
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public String addNewUserButtonClicked() {
        addEnteredUserIfInputIsValid();
        clearEnteredUsernameAndPassword();
        return Pages.SETTINGS_PAGE;
    }

    public String deleteUserButtonClicked(User user) {
        userDao.deleteUser(user.getUserID());
        return Pages.SETTINGS_PAGE;
    }

    private void addEnteredUserIfInputIsValid() {
        if (isInputValid()) {
            addUser();
        }
    }

    public String editFriendsButtonClicked(User user) {
        adminContext.setSelectedUser(user);
        return Pages.FRIENDS_PAGE;
    }

    private void clearEnteredUsernameAndPassword() {
        enteredUserID = "";
        enteredUserPassword = "";
    }

    private void addUser() {
        User user = new User();
        user.setUserID(enteredUserID);
        user.setPassword(enteredUserPassword);
        userDao.addOrUpdateUser(user);
    }

    private boolean isInputValid() {
        return enteredUserID.length() > 0 && enteredUserPassword.length() > 0;
    }


    public String formatTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("dd.MM.yy HH:mm");
        return format.format(date);
    }

}
