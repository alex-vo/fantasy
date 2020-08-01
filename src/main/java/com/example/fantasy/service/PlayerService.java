package com.example.fantasy.service;

import com.example.fantasy.dto.PlayerForUserDTO;
import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.User;
import com.example.fantasy.exception.BadRequestException;
import com.example.fantasy.exception.NotFoundException;
import com.example.fantasy.repository.admin.PlayerForAdminRepository;
import com.example.fantasy.repository.admin.TeamForAdminRepository;
import com.example.fantasy.repository.admin.UserForAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerForAdminRepository playerForAdminRepository;
    private final UserForAdminRepository userForAdminRepository;
    private final TeamForAdminRepository teamForAdminRepository;

    public void updatePlayer(Long ownerId, Long playerId, PlayerForUserDTO playerForUserDTO) {
        int updatedRows = playerForAdminRepository.updatePlayerInformation(playerId, ownerId, playerForUserDTO.getFirstName(),
                playerForUserDTO.getLastName(), playerForUserDTO.getCountry());
        if (updatedRows != 1) {
            throw new BadRequestException();
        }
    }

    public void placePlayerOnTransfer(Long ownerId, Long playerId, BigDecimal price) {
        int updatedRows = playerForAdminRepository.placePlayerOnTransfer(playerId, ownerId, price);
        if (updatedRows != 1) {
            throw new BadRequestException();
        }
    }

    @Transactional
    public void buyPlayer(Long buyerId, Long playerId) {
        User buyer = userForAdminRepository.findById(buyerId)
                .orElseThrow(NotFoundException::new);
        Player player = playerForAdminRepository.findPlayerOnTransferById(playerId)
                .orElseThrow(NotFoundException::new);
        ensureBuyerHasEnoughBalance(buyer, player.getTransferPrice());
        teamForAdminRepository.topUpBalance(player.getTeam().getId(), player.getTransferPrice());
        teamForAdminRepository.reduceBalance(buyer.getTeam().getId(), player.getTransferPrice());
        playerForAdminRepository.performTransfer(playerId, buyer.getTeam(),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10, 101)).multiply(player.getValue()));
    }

    private void ensureBuyerHasEnoughBalance(User buyer, BigDecimal price) {
        if (buyer.getTeam().getBalance().compareTo(price) < 0) {
            throw new BadRequestException("not enough funds");
        }
    }

}
