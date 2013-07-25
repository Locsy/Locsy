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

import com.lovbomobile.locsy.daos.impl.MongoUserDao;
import com.lovbomobile.locsy.entities.Location;
import com.lovbomobile.locsy.entities.User;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.RuntimeConfig;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.extract.UserTempNaming;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class MongoUserDaoTest extends TestCase {

    private static final String LOCALHOST = "127.0.0.1";
    private static final int MONGO_TEST_PORT = 27028;
    private static final String DB_NAME = "waydb";

    private static final String TEST_USER_ID = "testuser";
    private static final String TEST_USER_PASSWORD = "testpassword";
    private static final double TEST_USER_LATITUDE = 3.0;
    private static final double TEST_USER_LONGITUDE = 5.0;
    private static final String TEST_USER_FRIEND_ID = "testuserfriend";
    MongoUserDao userDao;
    private MongodExecutable mongodExecutable;
    private MongoTemplate mongoTemplate;
    private MongodProcess mongodProcess;
    private MongoClient mongoClient;

    @Before
    public void setUp() throws Exception {
        initializeDb();
        userDao = new MongoUserDao();
        userDao.setMongoOperations(mongoTemplate);
    }

    private void initializeDb() throws IOException {

        RuntimeConfig config = new RuntimeConfig();
        config.setExecutableNaming(new UserTempNaming());
        MongodStarter mongodStarter = MongodStarter.getInstance(config);
        mongodExecutable = mongodStarter.prepare(new MongodConfig(Version.V2_2_0, MONGO_TEST_PORT, false));
        mongodProcess = mongodExecutable.start();
        mongoClient = new MongoClient(LOCALHOST, MONGO_TEST_PORT);
        mongoClient.getDB(DB_NAME);
        mongoTemplate = new MongoTemplate(mongoClient, DB_NAME);

    }

    @After
    public void tearDown() throws InterruptedException {
        shutdownDb();
    }

    private void shutdownDb() throws InterruptedException {

        mongoClient.close();
        mongodProcess.stop();
        mongodExecutable.stop();
    }


    @Test
    public void testSaveUser() {
        User user = userDao.getUser(TEST_USER_ID);
        assertThat(user, is(nullValue()));
        user = getTestUser();
        userDao.addOrUpdateUser(user);
        user = userDao.getUser(TEST_USER_ID);
        assertThat(user, is(notNullValue()));
        assertThat(user.getUserID(), is(TEST_USER_ID));
        assertThat(user.getPassword(), is(TEST_USER_PASSWORD));

        Location location = user.getLocation();
        assertThat(location.getLatitude(), is(TEST_USER_LATITUDE));
        assertThat(location.getLongitude(), is(TEST_USER_LONGITUDE));

        List<String> friends = user.getFriendsWhoCanSeeLocation();
        assertThat(friends.contains(TEST_USER_FRIEND_ID), is(true));
    }


    @Test
    public void testDeleteUser() {
        User user = getTestUser();
        userDao.addOrUpdateUser(user);
        user = userDao.getUser(TEST_USER_ID);
        assertThat(user, is(notNullValue()));
        userDao.deleteUser(TEST_USER_ID);
        user = userDao.getUser(TEST_USER_ID);
        assertThat(user, is(nullValue()));
    }

    @Test
    public void testUpdateUser() {
        User user = getTestUser();
        List<String> friends;

        userDao.addOrUpdateUser(user);
        user = userDao.getUser(TEST_USER_ID);
        friends = user.getFriendsWhoCanSeeLocation();
        assertThat(friends.contains(TEST_USER_FRIEND_ID), is(true));

        user.getFriendsWhoCanSeeLocation().remove(TEST_USER_FRIEND_ID);
        userDao.addOrUpdateUser(user);
        user = userDao.getUser(TEST_USER_ID);
        friends = user.getFriendsWhoCanSeeLocation();
        assertThat(friends.contains(TEST_USER_FRIEND_ID), is(false));
    }


    @Test
    public void testGetFriends() {
        User user = getTestUser();
        User friendOfUser = getFriendOfTestUser();
        userDao.addOrUpdateUser(user);
        userDao.addOrUpdateUser(friendOfUser);
        List<User> friends = userDao.getFriendsWhoCanBeSeenBy(TEST_USER_FRIEND_ID);
        assertThat(friends.get(0).getUserID(), is(TEST_USER_ID));
    }

    @Test
    public void testGetAllUsers() {
         List<User> users = userDao.getAllUsers();
        assertThat(users.size(),is(0));
        userDao.addOrUpdateUser(getTestUser());
        userDao.addOrUpdateUser(getFriendOfTestUser());
        users = userDao.getAllUsers();
        assertThat(users.size(),is(2));
        assertThat(users.indexOf(getTestUser())>-1,is(true));
        assertThat(users.indexOf(getFriendOfTestUser())>-1,is(true));
    }

    Location getTestLocation() {
        Location location = new Location();
        location.setLatitude(TEST_USER_LATITUDE);
        location.setLongitude(TEST_USER_LONGITUDE);
        return location;
    }



    User getTestUser() {
        User user = new User();
        user.setUserID(TEST_USER_ID);
        user.setPassword(TEST_USER_PASSWORD);
        user.setLocation(getTestLocation());
        user.setFriendsWhoCanSeeLocation(getTestFriends());
        return user;
    }

    List<String> getTestFriends() {
        List<String> friends = new ArrayList<String>();
        friends.add(TEST_USER_FRIEND_ID);
        return friends;
    }

    User getFriendOfTestUser() {
        User user = new User();
        user.setUserID(TEST_USER_FRIEND_ID);
        user.setPassword(TEST_USER_PASSWORD);
        return user;
    }



}
