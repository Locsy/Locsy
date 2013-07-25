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

import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.entities.Location;
import com.lovbomobile.locsy.entities.User;
import com.lovbomobile.locsy.services.impl.LocationInfoServiceImpl;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationInfoServiceTest extends TestCase {

    private static final String TEST_FRIEND1_ID = "testfriend1";

    private static final String TEST_FRIEND2_ID = "testfriend2";

    @Mock
    UserDao userDao;

    @Test
    public void testGetLocationsForUser() {
        LocationInfoServiceImpl locationInfoService = new LocationInfoServiceImpl();
        locationInfoService.setUserDao(userDao);

        List<User> friends = getTestFriends();
        when(userDao.getFriendsWhoCanBeSeenBy("test")).thenReturn(friends);

        Map<String,Location> locations = locationInfoService.getLocationsForUser("test");

        assertThat(locations.keySet().size(),is(2));
        assertThat(locations.get(TEST_FRIEND1_ID),is(notNullValue()));
        assertThat(locations.get(TEST_FRIEND2_ID),is(notNullValue()));

    }

    private List<User> getTestFriends() {
        List<User> friends = new ArrayList<User>();

        User friend1 = new User();
        friend1.setUserID(TEST_FRIEND1_ID);
        friend1.setLocation(new Location());

        User friend2 = new User();
        friend2.setUserID(TEST_FRIEND2_ID);
        friend2.setLocation(new Location());

        friends.add(friend1);
        friends.add(friend2);

        return friends;
    }


}
