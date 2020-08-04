package com.example.fantasy;

import com.example.fantasy.dto.TransferPlacementDTO;
import com.example.fantasy.dto.UpdatePlayerDTO;
import com.example.fantasy.entity.PlayerPosition;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlayerControllerTest extends E2ETest {

    @Override
    protected String getUserEmail() {
        return "test_user3@abc.com";
    }

    @Test
    @Order(1)
    public void shouldGetPlayerInformation() throws Exception {
        Integer playerId = getFirstPlayerId(accessToken);

        mvc.perform(get("/v1/user/player/" + playerId).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(playerId)))
                .andExpect(jsonPath("$.firstName", is(notNullValue())))
                .andExpect(jsonPath("$.lastName", is(notNullValue())))
                .andExpect(jsonPath("$.country", is("Spain")))
                .andExpect(jsonPath("$.age", allOf(lessThan(41), greaterThan(17))))
                .andExpect(jsonPath("$.value", is(1000000.0)))
                .andExpect(jsonPath("$.isOnTransfer", is(false)))
                .andExpect(jsonPath("$.transferPrice", is(nullValue())))
                .andExpect(jsonPath("$.position", anyOf(is(PlayerPosition.GOALKEEPER.name()), is(PlayerPosition.DEFENDER.name()),
                        is(PlayerPosition.MIDFIELDER.name()), is(PlayerPosition.ATTACKER.name()))))
        ;
    }

    @Test
    @Order(2)
    public void shouldUpdatePlayerInformation() throws Exception {
        Integer playerId = getFirstPlayerId(accessToken);
        UpdatePlayerDTO updatePlayerDTO = new UpdatePlayerDTO();
        updatePlayerDTO.setFirstName("Franz");
        updatePlayerDTO.setLastName("Beckenbauer");
        updatePlayerDTO.setCountry("Germany");

        mvc.perform(put("/v1/user/player/" + playerId).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(updatePlayerDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        mvc.perform(get("/v1/user/player/" + playerId).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.firstName", is("Franz")))
                .andExpect(jsonPath("$.lastName", is("Beckenbauer")))
                .andExpect(jsonPath("$.country", is("Germany")));
    }

    @Test
    public void shouldListPlayersOnTransfer() throws Exception {
        Integer playerId = getFirstPlayerId(accessToken);

        mvc.perform(post("/v1/user/player/" + playerId + "/place-on-transfer").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(prepareTransferPlacementDTO(BigDecimal.TEN)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        mvc.perform(get("/v1/user/player/" + playerId).header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.isOnTransfer", is(true)));

        mvc.perform(get("/v1/user/player/list-transfer").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.content[0].id", is(playerId)));
    }

    private TransferPlacementDTO prepareTransferPlacementDTO(BigDecimal price) {
        TransferPlacementDTO transferPlacementDTO = new TransferPlacementDTO();
        transferPlacementDTO.setPrice(price);
        return transferPlacementDTO;
    }

    private Integer getFirstPlayerId(String token) throws Exception {
        return JsonPath.read(mvc.perform(get("/v1/user/team").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(200)).andReturn().getResponse().getContentAsString(), "$.players[0].id");
    }

    @Test
    public void shouldBuyPlayer() throws Exception {
        mvc.perform(get("/v1/user/team").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.value", is(20_000_000.0)))
                .andExpect(jsonPath("$.balance", is(5_000_000.0)))
                .andExpect(jsonPath("$.players.length()", is(20)));
        String otherUserToken = signUpAndLogin("test_user4@abc.com", "Qwerty123").getAccessToken();
        Integer playerId = getFirstPlayerId(otherUserToken);
        mvc.perform(post("/v1/user/player/" + playerId + "/place-on-transfer").header(HttpHeaders.AUTHORIZATION, "Bearer " + otherUserToken)
                .content(objectMapper.writeValueAsString(prepareTransferPlacementDTO(BigDecimal.valueOf(100))))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        mvc.perform(post("/v1/user/player/" + playerId + "/buy").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200));
        mvc.perform(get("/v1/user/team").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.value", allOf(greaterThan(21_000_000.0), lessThan(22_000_001.0))))
                .andExpect(jsonPath("$.balance", is(4_999_900.0)))
                .andExpect(jsonPath("$.players.length()", is(21)));
    }

}
