package com.example.fantasy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminOperationsTest extends E2ETest {

    @Value("${admin.username}")
    String adminUsername;
    @Value("${admin.password}")
    String adminPassword;

    String adminAccessToken;

    @BeforeAll
    public void setUp() throws Exception {
        adminAccessToken = login(adminUsername, adminPassword).getAccessToken();
    }

    @Test
    public void shouldPerformUserCruds() throws Exception {
        Integer userId = extractCreatedId(mvc.perform(post("/v1/admin/users").header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.readString(Path.of("src/test/resources/new_user.json"))))
                .andExpect(status().is(201)).andReturn().getResponse());
        mvc.perform(get("/v1/admin/users/" + userId).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(userId)));
        Integer teamId = extractCreatedId(mvc.perform(post("/v1/admin/teams").header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .content(Files.readString(Path.of("src/test/resources/new_team.json"))))
                .andExpect(status().is(201)).andReturn().getResponse());
        mvc.perform(put("/v1/admin/teams/" + teamId + "/owner").header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .contentType("text/uri-list").content("/v1/admin/users/" + userId))
                .andExpect(status().is(204));
        mvc.perform(get("/v1/admin/users/" + userId + "/team").header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(teamId)));
        mvc.perform(patch("/v1/admin/users/" + userId).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Files.readString(Path.of("src/test/resources/patch_user.json"))))
                .andExpect(status().is(204));
        mvc.perform(get("/v1/admin/users/" + userId).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.blocked", is(true)))
                .andExpect(jsonPath("$.failedLoginAttempts", is(1)));
        mvc.perform(delete("/v1/admin/users/" + userId).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken))
                .andExpect(status().is(204));
        mvc.perform(get("/v1/admin/users/" + userId).header(HttpHeaders.AUTHORIZATION, "Bearer " + adminAccessToken))
                .andExpect(status().is(404));
    }

    private Integer extractCreatedId(MockHttpServletResponse response) {
        String location = response.getHeader(HttpHeaders.LOCATION);
        return Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));
    }

}
