package com.example.fantasy;

import com.example.fantasy.dto.NewUserDTO;
import com.example.fantasy.dto.TokenDTO;
import com.example.fantasy.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class E2ETest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    String accessToken;

    protected String getUserEmail() {
        return null;
    }

    @BeforeAll
    public void setUp() throws Exception {
        if (getUserEmail() == null) {
            return;
        }

        TokenDTO tokenDTO = signUpAndLogin(getUserEmail(), "Qwerty123");
        accessToken = tokenDTO.getAccessToken();
    }

    protected TokenDTO signUpAndLogin(String email, String password) throws Exception {
        mvc.perform(post("/v1/user/account/signup")
                .content(objectMapper.writeValueAsString(prepareNewUserDTO(email, password, "Spain", "Barcelona")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        return login(email, password);
    }

    protected TokenDTO login(String email, String password) throws Exception {
        return getResponse(mvc.perform(post("/v1/user/account/login")
                .content(objectMapper.writeValueAsString(prepareUserDTO(email, password)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(), TokenDTO.class);
    }

    protected UserDTO prepareUserDTO(String email, String password) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        return userDTO;
    }

    protected NewUserDTO prepareNewUserDTO(String email, String password, String country, String teamName) {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail(email);
        newUserDTO.setPassword(password);
        newUserDTO.setCountry(country);
        newUserDTO.setTeamName(teamName);
        return newUserDTO;
    }

    protected <T> T getResponse(String raw, Class<T> clazz) {
        try {
            return objectMapper.readValue(raw, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
