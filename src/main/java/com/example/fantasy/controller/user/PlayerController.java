package com.example.fantasy.controller.user;

import com.example.fantasy.config.FantasyAuthToken;
import com.example.fantasy.dto.PlayerDTO;
import com.example.fantasy.dto.TransferPlacementDTO;
import com.example.fantasy.dto.UpdatePlayerDTO;
import com.example.fantasy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/v1/player/{playerId}")
    public PlayerDTO getPlayer(@PathVariable Long playerId) {
        return playerService.getPlayer(playerId);
    }

    @PutMapping("/v1/player/{playerId}")
    public void updatePlayer(FantasyAuthToken auth,
                             @PathVariable Long playerId,
                             @Valid @RequestBody UpdatePlayerDTO updatePlayerDTO) {
        playerService.updatePlayer(auth.getUserId(), playerId, updatePlayerDTO);
    }

    @GetMapping("/v1/player/list-transfer")
    public Page<PlayerDTO> listPlayersOnTransfer(@RequestParam(required = false) String country,
                                                 @RequestParam(required = false) String teamName,
                                                 @RequestParam(required = false) String playerLastName,
                                                 @RequestParam(required = false) BigDecimal valueMin,
                                                 @RequestParam(required = false) BigDecimal valueMax,
                                                 @PageableDefault(page = 0, size = 5)
                                                 @SortDefault.SortDefaults({
                                                         @SortDefault(sort = "lastName")
                                                 }) Pageable pageable) {
        return playerService.listPlayersOnTransfer(country, teamName, playerLastName, valueMin, valueMax, pageable);
    }

    @PostMapping("/v1/player/{playerId}/place-on-transfer")
    public void placePlayerOnTransfer(FantasyAuthToken auth,
                                      @PathVariable Long playerId,
                                      @Valid @RequestBody TransferPlacementDTO transferPlacementDTO) {
        playerService.placePlayerOnTransfer(auth.getUserId(), playerId, transferPlacementDTO.getPrice());
    }

    @PostMapping("/v1/player/{playerId}/buy")
    public void buyPlayer(FantasyAuthToken auth,
                          @PathVariable Long playerId) {
        playerService.buyPlayer(auth.getUserId(), playerId);
    }

}
