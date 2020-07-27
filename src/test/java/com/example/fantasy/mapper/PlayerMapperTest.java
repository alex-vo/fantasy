package com.example.fantasy.mapper;

import com.example.fantasy.dto.PlayerForUserDTO;
import com.example.fantasy.entity.Player;
import org.junit.jupiter.api.Test;

public class PlayerMapperTest {

    @Test
    public void tst() {
        PlayerMapper pm = new PlayerMapperImpl();
        PlayerForUserDTO playerForUserDTO = new PlayerForUserDTO();
        playerForUserDTO.setCountry("usa");
        playerForUserDTO.setFirstName("vasja");
        playerForUserDTO.setLastName("peti4kin");
        Player p = new Player();
        pm.map(p, playerForUserDTO);
        System.out.println(p);
    }

}
