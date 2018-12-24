package com.nicknathanjustin.streamercontracts.users;

import com.nicknathanjustin.streamercontracts.twitch.TwitchService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String TWITCH_USER_NAME = "twitchUserName";
    private static final int TOTAL_LOGINS = 1;
    private static final Timestamp CREATED_AT = new Timestamp(System.currentTimeMillis());
    private static final Timestamp LAST_LOGIN = new Timestamp(System.currentTimeMillis());
    private UserModel userModel;

    @Mock private TwitchService mockTwitchService;
    @Mock private UserModelRepository mockUserModelRepository;
    @InjectMocks private UserServiceImpl userService;

    @Before
    public void setup() {
        userModel = UserModel.builder()
                .twitchUsername(TWITCH_USER_NAME)
                .totalLogins(TOTAL_LOGINS)
                .createdAt(CREATED_AT)
                .lastLogin(LAST_LOGIN)
                .build();
    }


    @Test(expected = NullPointerException.class)
    public void createUser_nullInput_throwsException() {
        userService.createUser(null);
    }

    @Test(expected = NullPointerException.class)
    public void getUser_nullInput_throwsException() {
        userService.getUser(null);
    }

    @Test(expected = NullPointerException.class)
    public void login_nullInput_throwsException() {
        userService.login(null);
    }

    @Test
    public void createUser_validInput_createsUser() {
        final ArgumentCaptor<UserModel> userModelArgumentCaptor = ArgumentCaptor.forClass(UserModel.class);

        userService.createUser(TWITCH_USER_NAME);

        verify(mockUserModelRepository).save(userModelArgumentCaptor.capture());
        final UserModel userModel = userModelArgumentCaptor.getValue();
        Assert.assertEquals(TWITCH_USER_NAME, userModel.getTwitchUsername());
        Assert.assertEquals(1, userModel.getTotalLogins());
        //CreatedAt should equal LastLogin for newly created users
        Assert.assertEquals(userModel.getCreatedAt(), userModel.getLastLogin());
    }

    @Test
    public void getUser_validInput_returnsUser() {
        when(mockUserModelRepository.findByTwitchUsername(TWITCH_USER_NAME)).thenReturn(userModel);

        final Optional<UserModel> optionalUserModel = userService.getUser(TWITCH_USER_NAME);
        userModel = optionalUserModel.orElse(null);

        Assert.assertNotNull(userModel);
        Assert.assertEquals(userModel.getTwitchUsername(), TWITCH_USER_NAME);
        Assert.assertEquals(userModel.getTotalLogins(), TOTAL_LOGINS);
        Assert.assertEquals(userModel.getCreatedAt(), CREATED_AT);
        Assert.assertEquals(userModel.getLastLogin(), LAST_LOGIN);
    }

    @Test
    public void getUser_noUserForTwitchName_returnsNull() {
        when(mockUserModelRepository.findByTwitchUsername(TWITCH_USER_NAME)).thenReturn(null);

        final Optional<UserModel> optionalUserModel = userService.getUser(TWITCH_USER_NAME);

        Assert.assertTrue(!optionalUserModel.isPresent());
    }

    @Test
    public void login_validInput_updatesLoginCountAndLastLoginTime() {
        userService.login(userModel);

        Assert.assertEquals(TOTAL_LOGINS + 1, userModel.getTotalLogins());
        //LastLogin should greater after logging in
        Assert.assertTrue(LAST_LOGIN.before(userModel.getLastLogin()));
    }
}
