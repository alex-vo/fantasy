package com.example.fantasy.service;

import com.example.fantasy.dto.PlayerForUserDTO;
import com.example.fantasy.exception.BadRequestException;
import com.example.fantasy.repository.admin.PlayerForAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerForAdminRepository playerForAdminRepository;

    public void updatePlayer(Long ownerId, Long playerId, PlayerForUserDTO playerForUserDTO) {
        int updatedRows = playerForAdminRepository.updatePlayerInformation(playerId, ownerId, playerForUserDTO.getFirstName(),
                playerForUserDTO.getLastName(), playerForUserDTO.getCountry());
        if (updatedRows != 1) {
            throw new BadRequestException();
        }
    }

}
