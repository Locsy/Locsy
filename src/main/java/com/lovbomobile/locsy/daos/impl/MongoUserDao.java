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

package com.lovbomobile.locsy.daos.impl;

import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.entities.User;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MongoUserDao implements UserDao {

    MongoOperations mongoOperations;

    @Override
    public User getUser(String userID) {
        Query query = getUserIDQuery(userID);
        return mongoOperations.findOne(query, User.class);
    }

    @Override
    public void deleteUser(String userID) {
        Query query = getUserIDQuery(userID);
        mongoOperations.findAndRemove(query, User.class);
    }

    private Query getUserIDQuery(String userID) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userID").is(userID));
        return query;
    }

    @Override
    public List<User> getFriendsWhoCanBeSeenBy(String userID) {
        Query query = getFriendsOfUserQuery(userID);
        return mongoOperations.find(query, User.class);
    }

    @Override
    public List<User> getAllUsers() {
        return mongoOperations.findAll(User.class);
    }

    private Query getFriendsOfUserQuery(String userID) {
        Query query = new Query();
        query.addCriteria(Criteria.where("friendsWhoCanSeeLocation").is(userID));
        return query;
    }

    @Override
    public void addOrUpdateUser(User user) {
        mongoOperations.save(user);
    }

    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }


}
