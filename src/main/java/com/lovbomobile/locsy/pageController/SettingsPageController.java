package com.lovbomobile.locsy.pageController;

import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.entities.User;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: bongo
 * Date: 5/9/13
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
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
