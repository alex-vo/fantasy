package com.example.fantasy;

import com.example.fantasy.dto.TokenDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends E2ETest {

    @Test
    public void shouldRegisterAndLogin() throws Exception {
        mvc.perform(post("/v1/user/account/signup")
                .content(objectMapper.writeValueAsString(prepareNewUserDTO("test_user2@abc.com", "Qwerty123", "Spain", "Barcelona")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        TokenDTO tokenDTO = getResponse(mvc.perform(post("/v1/user/account/login")
                .content(objectMapper.writeValueAsString(prepareUserDTO("test_user2@abc.com", "Qwerty123")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString(), TokenDTO.class);

        mvc.perform(get("/v1/user/team"))
                .andExpect(status().is(403));
        mvc.perform(get("/v1/user/team").header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getAccessToken()))
                .andExpect(status().is(200));
    }

}
