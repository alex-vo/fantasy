package com.example.fantasy.service;

import com.example.fantasy.dto.PlayerDTO;
import com.example.fantasy.dto.UpdatePlayerDTO;
import com.example.fantasy.entity.Player;
import com.example.fantasy.entity.User;
import com.example.fantasy.exception.BadRequestException;
import com.example.fantasy.exception.NotFoundException;
import com.example.fantasy.mapper.TeamDTOMapper;
import com.example.fantasy.repository.admin.SecuredTeamRepository;
import com.example.fantasy.repository.user.PlayerRepository;
import com.example.fantasy.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final SecuredTeamRepository securedTeamRepository;
    private final TeamDTOMapper teamDTOMapper;

    public PlayerDTO getPlayer(Long playerId) {
        return playerRepository.findPlayerById(playerId)
                .map(teamDTOMapper::toPlayerDTO)
                .orElseThrow(NotFoundException::new);
    }

    public Page<PlayerDTO> listPlayersOnTransfer(String country, String teamName, String playerLastName,
                                                 BigDecimal valueMin, BigDecimal valueMax, Pageable pageable) {
        return playerRepository.findPlayersOnTransfer(country, teamName, playerLastName, valueMin, valueMax, pageable)
                .map(teamDTOMapper::toPlayerDTO);
    }

    public void updatePlayer(Long ownerId, Long playerId, UpdatePlayerDTO updatePlayerDTO) {
        int updatedRows = playerRepository.updatePlayerInformation(playerId, ownerId, updatePlayerDTO.getFirstName(),
                updatePlayerDTO.getLastName(), updatePlayerDTO.getCountry());
        if (updatedRows != 1) {
            throw new BadRequestException();
        }
    }

    public void placePlayerOnTransfer(Long ownerId, Long playerId, BigDecimal price) {
        int updatedRows = playerRepository.placePlayerOnTransfer(playerId, ownerId, price);
        if (updatedRows != 1) {
            throw new BadRequestException();
        }
    }

    @Transactional
    public void buyPlayer(Long buyerId, Long playerId) {
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(NotFoundException::new);
        Player player = playerRepository.findPlayerOnTransferById(playerId)
                .orElseThrow(NotFoundException::new);
        ensureBuyerHasEnoughBalance(buyer, player.getTransferPrice());
        securedTeamRepository.topUpBalance(player.getTeam().getId(), player.getTransferPrice());
        securedTeamRepository.reduceBalance(buyer.getTeam().getId(), player.getTransferPrice());
        playerRepository.performTransfer(playerId, buyer.getTeam(),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextInt(10, 101)).multiply(player.getValue()));
    }

    private void ensureBuyerHasEnoughBalance(User buyer, BigDecimal price) {
        if (buyer.getTeam().getBalance().compareTo(price) < 0) {
            throw new BadRequestException("not enough funds");
        }
    }

}
