package com.loxon.javachallenge.memory;

import com.loxon.javachallenge.memory.api.Player;

import java.util.List;

public class TempResult {

    private Player player;
    private List<Integer> cells;

    public TempResult(Player player, List<Integer> cells) {
        this.player = player;
        this.cells = cells;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Integer> getCells() {
        return cells;
    }

    public void setCells(List<Integer> cells) {
        this.cells = cells;
    }
}
