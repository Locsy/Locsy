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
        return  mongoOperations.findOne(query,User.class);
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
        return  mongoOperations.find(query,User.class);
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
