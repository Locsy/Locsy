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

/**
 * Created with IntelliJ IDEA.
 * User: bongo
 * Date: 5/8/13
 * Time: 10:57 PM
 * To change this template use File | Settings | File Templates.
 */
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
