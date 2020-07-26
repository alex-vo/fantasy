package com.example.fantasy.controller.user;

import com.example.fantasy.dto.TransferPlacementDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PlayerController {

    @PostMapping("/v1/player/:playerId/place-on-transfer")
    public void placePlayerOnTransfer(@PathVariable Long playerId,
                                      @Valid @RequestBody TransferPlacementDTO transferPlacementDTO) {

    }

    @PostMapping("/v1/player/:playerId/buy")
    public void buyPlayer(@PathVariable Long playerId) {

    }

}
