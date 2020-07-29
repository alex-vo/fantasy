package com.example.fantasy.controller.user;

import com.example.fantasy.dto.PlayerForUserDTO;
import com.example.fantasy.dto.TransferPlacementDTO;
import com.example.fantasy.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @PutMapping("/v1/player/{playerId}")
    public void updatePlayer(@PathVariable Long playerId,
                             @Valid @RequestBody PlayerForUserDTO playerForUserDTO) {
        playerService.updatePlayer(1L, playerId, playerForUserDTO);
    }

    @PostMapping("/v1/player/{playerId}/place-on-transfer")
    public void placePlayerOnTransfer(@PathVariable Long playerId,
                                      @Valid @RequestBody TransferPlacementDTO transferPlacementDTO) {
        playerService.placePlayerOnTransfer(1L, playerId, transferPlacementDTO.getPrice());
    }

    @PostMapping("/v1/player/{playerId}/buy")
    public void buyPlayer(@PathVariable Long playerId) {
        playerService.buyPlayer(23L, playerId);
    }

}
