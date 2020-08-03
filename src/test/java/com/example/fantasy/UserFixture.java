package com.example.fantasy;

import com.example.fantasy.entity.Role;
import com.example.fantasy.entity.User;

public class UserFixture {

    public static User prepareUser(String email) {
        User user = new User();
        user.setRole(Role.ROLE_USER);
        user.setEmail(email);
        user.setPasswordHash("2$B$asdkamsdkasmdka23423ojmk23j4n2m3");
        user.setBlocked(false);
        user.setFailedLoginAttempts(0);
        return user;
    }

}
