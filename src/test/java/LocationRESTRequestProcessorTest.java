import com.lovbomobile.locsy.daos.UserDao;
import com.lovbomobile.locsy.entities.Location;
import com.lovbomobile.locsy.entities.User;
import com.lovbomobile.locsy.rest.LocationRESTRequestProcessor;
import com.lovbomobile.locsy.services.LocationInfoService;
import com.lovbomobile.locsy.services.SecurityService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created with IntelliJ IDEA.
 * User: bongo
 * Date: 5/8/13
 * Time: 11:43 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationRESTRequestProcessorTest extends TestCase {

    private static final String TEST_USER_ID = "testuser";

    private static final String TEST_USER_FRIEND_ID = "testuserfriend";

    @Mock
    UserDao userDao;
    @Mock
    LocationInfoService locationInfoService;
    @Mock
    SecurityService securityService;

    @Test
    public void testGetLocationOfFriends() {
        LocationRESTRequestProcessor locationRESTRequestProcessor = createLocationRESTRequestProcessor();
        when(securityService.isAccessAllowed(TEST_USER_ID, "timestamp", "hash")).thenReturn(true);
        when(locationInfoService.getLocationsForUser(TEST_USER_ID)).thenReturn(getTestLocations());
        Map<String,Location> locations = locationRESTRequestProcessor.getLocationOfFriends(TEST_USER_ID, "timestamp", "hash");
        assertThat(locations.size(),is(1));
        assertThat(locations.get(TEST_USER_FRIEND_ID),is(notNullValue()));

    }

    Map<String,Location> getTestLocations() {
        Map<String,Location> locations = new HashMap<String, Location>();
        locations.put(TEST_USER_FRIEND_ID, new Location());
        return locations;
    }

    @Test
    public void testSetLocation() {
        LocationRESTRequestProcessor locationRESTRequestProcessor = createLocationRESTRequestProcessor();
        when(securityService.isAccessAllowed(TEST_USER_ID, "timestamp", "hash")).thenReturn(true);
        when(userDao.getUser(TEST_USER_ID)).thenReturn(getTestUser());


        Location location = new Location();
        location.setLongitude(1.0);
        location.setLatitude(1.0);
        locationRESTRequestProcessor.setLocation(TEST_USER_ID, location, "timestamp", "hash");

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao).addOrUpdateUser(argumentCaptor.capture());

        Location updatedLocation = argumentCaptor.getValue().getLocation();
        assertThat(updatedLocation.getLatitude(), is(1.0));


    }

    private User getTestUser() {
        User user = new User();
        user.setUserID(TEST_USER_ID);
        Location location = new Location();
        location.setLongitude(0);
        location.setLatitude(0);
        return user;
    }

    private LocationRESTRequestProcessor createLocationRESTRequestProcessor() {
        LocationRESTRequestProcessor locationRESTRequestProcessor = new LocationRESTRequestProcessor();
        locationRESTRequestProcessor.setUserDao(userDao);
        locationRESTRequestProcessor.setSecurityService(securityService);
        locationRESTRequestProcessor.setLocationInfoService(locationInfoService);
        return locationRESTRequestProcessor;
    }


}
