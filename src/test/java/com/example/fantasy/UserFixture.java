package com.example.fantasy;

import com.example.fantasy.entity.Role;
import com.example.fantasy.entity.Team;
import com.example.fantasy.entity.User;

import java.math.BigDecimal;

public class UserFixture {

    public static User prepareUserWithTeam(String email) {
        return prepareUserWithTeam(email, false);
    }

    public static User prepareUserWithTeam(String email, boolean blocked) {
        User user = prepareUser(email, blocked);
        Team team = TeamFixture.prepareTeam("Bayern", "Germany", BigDecimal.valueOf(6_000_000));
        user.setTeam(team);
        return user;
    }

    public static User prepareUser(String email) {
        return prepareUser(email, false);
    }

    public static User prepareUser(String email, boolean blocked) {
        User user = new User();
        user.setRole(Role.ROLE_USER);
        user.setEmail(email);
        user.setPasswordHash("2$B$asdkamsdkasmdka23423ojmk23j4n2m3");
        user.setBlocked(blocked);
        user.setFailedLoginAttempts(0);
        return user;
    }

}
