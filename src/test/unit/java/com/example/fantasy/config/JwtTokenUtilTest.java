package com.example.fantasy.config;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class JwtTokenUtilTest {

    JwtTokenUtil jwtTokenUtil = new JwtTokenUtil("abc123", 10000);

    @Test
    public void shouldGenerateJwtToken() {
        String token = jwtTokenUtil.generateJwtToken("abc@abc.com");

        assertThat(token, is(notNullValue()));
    }

    @Test
    public void shouldGetEmailFromJwtToken() {
        String email = jwtTokenUtil.getEmailFromJwtToken(jwtTokenUtil.generateJwtToken("abc@abc.com"));

        assertThat(email, is("abc@abc.com"));
    }

    @Test
    public void shouldValidateJwtToken() {
        boolean valid = jwtTokenUtil.validateJwtToken(jwtTokenUtil.generateJwtToken("abc@abc.com"));

        assertThat(valid, is(true));
    }

    @Test
    public void shouldFailToValidateJwtToken() {
        boolean valid = jwtTokenUtil.validateJwtToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmNAYWJjLmNvbSIsImlhdCI6MTU5NjUyNDU2MSwiZXhwIjoxNTk2NTI0NTcxfQ.Wn_K9qaZ9_ejfqCMM21Rw_RzSTiDygEGrhfFA9rOjilc95ZPYibO13iJ6Si92w82hF5XxxZqDdnUE5O46L-lcQ");

        assertThat(valid, is(false));
    }

}
