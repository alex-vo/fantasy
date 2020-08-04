package com.example.fantasy;

import com.example.fantasy.dto.UpdateTeamDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TeamControllerTest extends E2ETest {

    @Override
    protected String getUserEmail() {
        return "test_user1@abc.com";
    }

    @Test
    @Order(1)
    public void shouldGetTeamInformation() throws Exception {
        mvc.perform(get("/v1/user/team").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.name", is("Barcelona")))
                .andExpect(jsonPath("$.country", is("Spain")))
                .andExpect(jsonPath("$.value", is(20_000_000.0)))
                .andExpect(jsonPath("$.balance", is(5_000_000.0)))
                .andExpect(jsonPath("$.players.length()", is(20)));
    }

    @Test
    @Order(2)
    public void shouldUpdateTeamInformation() throws Exception {
        mvc.perform(put("/v1/user/team").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prepareUpdateTeamDTO("Italy", "Milan"))))
                .andExpect(status().is(200));

        mvc.perform(get("/v1/user/team").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name", is("Milan")))
                .andExpect(jsonPath("$.country", is("Italy")));
    }

    private UpdateTeamDTO prepareUpdateTeamDTO(String country, String name) {
        UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO();
        updateTeamDTO.setCountry(country);
        updateTeamDTO.setName(name);
        return updateTeamDTO;
    }

}
