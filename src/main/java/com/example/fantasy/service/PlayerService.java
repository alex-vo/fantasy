package com.example.fantasy.service;

import com.example.fantasy.dto.PlayerForUserDTO;
import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.User;
import com.example.fantasy.exception.BadRequestException;
import com.example.fantasy.exception.NotFoundException;
import com.example.fantasy.repository.admin.SecuredPlayerRepository;
import com.example.fantasy.repository.admin.SecuredTeamRepository;
import com.example.fantasy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final SecuredPlayerRepository securedPlayerRepository;
    private final UserRepository userRepository;
    private final SecuredTeamRepository securedTeamRepository;

    public void updatePlayer(Long ownerId, Long playerId, PlayerForUserDTO playerForUserDTO) {
        int updatedRows = securedPlayerRepository.updatePlayerInformation(playerId, ownerId, playerForUserDTO.getFirstName(),
                playerForUserDTO.getLastName(), playerForUserDTO.getCountry());
        if (updatedRows != 1) {
            throw new BadRequestException();
        }
    }

    public void placePlayerOnTransfer(Long ownerId, Long playerId, BigDecimal price) {
        int updatedRows = securedPlayerRepository.placePlayerOnTransfer(playerId, ownerId, price);
        if (updatedRows != 1) {
            throw new BadRequestException();
        }
    }

    @Transactional
    public void buyPlayer(Long buyerId, Long playerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(NotFoundException::new);
        Player player = securedPlayerRepository.findPlayerOnTransferById(playerId)
                .orElseThrow(NotFoundException::new);
        ensureBuyerHasEnoughBalance(buyer, player.getTransferPrice());
        securedTeamRepository.topUpBalance(player.getTeam().getId(), player.getTransferPrice());
        securedTeamRepository.reduceBalance(buyer.getTeam().getId(), player.getTransferPrice());
        securedPlayerRepository.performTransfer(playerId, buyer.getTeam(),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10, 101)).multiply(player.getValue()));
    }

    private void ensureBuyerHasEnoughBalance(User buyer, BigDecimal price) {
        if (buyer.getTeam().getBalance().compareTo(price) < 0) {
            throw new BadRequestException("not enough funds");
        }
    }

}
