package com.monbattle.system;

import com.monbattle.enums.Environment;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.monbattle.monster.Monster;
import com.monbattle.player.Player;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SaveState implements Serializable {
    private Player player;
    private Environment currEnv;
    private Monster leadMon;
}
