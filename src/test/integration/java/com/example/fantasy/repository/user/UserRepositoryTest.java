package com.example.fantasy.repository.user;

import com.example.fantasy.UserFixture;
import com.example.fantasy.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    public void shouldFindUserById() {
        User user = UserFixture.prepareUserWithTeam("abc@abc.com");
        user = userRepository.save(user);

        assertThat(userRepository.findById(user.getId()).orElseThrow(), is(notNullValue()));
    }

    @Test
    public void shouldFindNonBlockedUserByEmail() {
        User notBlockedUser = UserFixture.prepareUserWithTeam("abc@abc.com");
        userRepository.save(notBlockedUser);

        assertThat(userRepository.findNonBlockedByEmail("abc@abc.com").isPresent(), is(true));
    }

    @Test
    public void shouldFailToFindNonBlockedUserByEmail() {
        User notBlockedUser = UserFixture.prepareUserWithTeam("abc@abc.com", true);
        userRepository.save(notBlockedUser);

        assertThat(userRepository.findNonBlockedByEmail("abc@abc.com").isPresent(), is(false));
    }

    @Test
    public void shouldUpdateFailedLoginAttemptsAndBlockUser() {
        User user = UserFixture.prepareUserWithTeam("abc@abc.com");
        user = userRepository.save(user);

        userRepository.updateFailedLoginAttemptsCount(user.getId(), 150);

        assertThat(userRepository.findById(user.getId()).orElseThrow(), allOf(
                hasProperty("failedLoginAttempts", is(150)),
                hasProperty("blocked", is(true))
        ));
    }

    @Test
    public void shouldUpdateFailedLoginAttemptsAndNotBlockUser() {
        User user = UserFixture.prepareUserWithTeam("abc@abc.com");
        user = userRepository.save(user);

        userRepository.updateFailedLoginAttemptsCount(user.getId(), 2);

        assertThat(userRepository.findById(user.getId()).orElseThrow(), allOf(
                hasProperty("failedLoginAttempts", is(2)),
                hasProperty("blocked", is(false))
        ));
    }

}
