package com.loxon.javachallenge.memory;

import com.loxon.javachallenge.memory.api.*;
import com.loxon.javachallenge.memory.api.communication.commands.*;
import com.loxon.javachallenge.memory.api.communication.general.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecutorsGame implements Game {

    private List<Player> players = new ArrayList<>();

    private Memory memory;
    private int rounds;

    @Override
    public Player registerPlayer(String name) {
        Player p = new Player(name);
        this.players.add(p);
        return p;
    }

    @Override
    public void startGame(List<MemoryState> initialMemory, int rounds) {
        this.memory = new Memory(initialMemory);
        this.rounds = rounds;
    }

    @Override
    public List<Response> nextRound(Command... requests) {
        rounds -= 1;
        List<Response> ret = new ArrayList<>();
        List<TempResult> tempResults = new ArrayList<>();
        List<CommandScan> processAtEnd = new ArrayList<>();
        List<Player> processedPlayer = new ArrayList<>();
        List<SwapPair> swapPairs = new ArrayList<>();

        for (Command request : requests) {
            if (!players.contains(request.getPlayer())) {
                continue;
            }
            if (processedPlayer.contains(request.getPlayer())) {
                continue;
            }
            // Scan commands store them for later execution
            if (request instanceof CommandScan) {
                CommandScan scan = (CommandScan) request;
                processAtEnd.add(scan);
            }

            // Allocate commands
            if (request instanceof CommandAllocate) {
                CommandAllocate allocate = (CommandAllocate) request;
                List<Integer> allocatedCells = new ArrayList<>();
                boolean doNotAllocate = false;

                if (!anyOutOfBound(allocate.getCells()) && indicesInSameBlock(allocate.getCells())) {
                    for (Integer cell : allocate.getCells()) {
                        if (cell == null) {
                            continue;
                        }
                        if (cell > memory.size() -1 || cell < 0) {
                            continue;
                        }
                        if (memory.allocate(allocate.getPlayer(), cell)) {
                            allocatedCells.add(cell);
                        }
                    }
                }
                tempResults.add(new TempResult(allocate.getPlayer(), doNotAllocate || allocatedCells.isEmpty() ? Collections.emptyList() : allocatedCells));
            }

            // Free command
            if (request instanceof CommandFree) {
                CommandFree free = (CommandFree) request;
                List<Integer> freedCells = new ArrayList<>();
                if (free.getCells() != null && free.getCells().size() == 2) {
                    for (Integer cell : free.getCells()) {
                        if (cell != null) {
                            if (cell > memory.size() - 1 || cell < 0) {
                                continue;
                            }
                            if (memory.free(free.getPlayer(), cell)) {
                                freedCells.add(cell);
                            }
                        }
                    }
                }
                tempResults.add(new TempResult(free.getPlayer(), freedCells.isEmpty() ? Collections.emptyList() : freedCells));
            }
            // Recover command
            if (request instanceof CommandRecover) {
                CommandRecover recover = (CommandRecover) request;
                List<Integer> recoveredCells = new ArrayList<>();
                for (Integer cell : recover.getCells()) {
                    if (cell == null) {
                        continue;
                    }
                    if (cell > memory.size() - 1 || cell < 0) {
                        continue;
                    }
                    if (memory.recover(recover.getPlayer(), cell)) {
                        recoveredCells.add(cell);
                    }
                }
                ret.add(new ResponseSuccessList(recover.getPlayer(), recoveredCells.isEmpty() ? Collections.emptyList() : recoveredCells));
            }
            // Fortify command
            if (request instanceof CommandFortify) {
                CommandFortify fortify = (CommandFortify) request;
                List<Integer> fortifiedCells = new ArrayList<>();
                for (Integer cell : fortify.getCells()) {
                    if (cell == null) {
                        continue;
                    }
                    if (cell > memory.size() - 1 || cell < 0) {
                        continue;
                    }
                    if (memory.fortify(cell)) {
                        fortifiedCells.add(cell);
                    }
                }
                ret.add(new ResponseSuccessList(fortify.getPlayer(), fortifiedCells.isEmpty() ? Collections.emptyList() : fortifiedCells));
            }
            // Swap Command
            if (request instanceof CommandSwap) {
                CommandSwap swap = (CommandSwap) request;
                boolean canSwap = true;
                if (swap.getCells() != null && swap.getCells().size() == 2) {
                    for (SwapPair swapPair : swapPairs) {
                        if ((swap.getCells().get(0) != null && swapPair.hasCell(swap.getCells().get(0)))
                                || (swap.getCells().get(1) != null && swapPair.hasCell(swap.getCells().get(1)))) {
                            canSwap = false;
                            if (swap.getCells().get(0) != null) {
                                memory.setCellState(swap.getCells().get(0), MemoryState.CORRUPT);
                            }
                            if (swap.getCells().get(1) != null) {
                                memory.setCellState(swap.getCells().get(1), MemoryState.CORRUPT);
                            }
                            memory.setCellState(swapPair.getCellA(), MemoryState.CORRUPT);
                            memory.setCellState(swapPair.getCellB(), MemoryState.CORRUPT);
                        }
                    }
                    if (swap.getCells().get(0) != null && swap.getCells().get(1) != null && (swap.getCells().get(0).equals(swap.getCells().get(1)))) {
                        canSwap = false;
                        memory.setCellState(swap.getCells().get(0), MemoryState.CORRUPT);
                    }
                    if (canSwap) {
                        for (Integer cell : swap.getCells()) {
                            if (cell == null) {
                                canSwap = false;
                                break;
                            }
                            if (cell > memory.size() - 1 || cell < 0) {
                                canSwap = false;
                                break;
                            }
                            MemoryState currentState = memory.getCellState(cell);
                            if (currentState.equals(MemoryState.SYSTEM) || currentState.equals(MemoryState.FORTIFIED)) {
                                canSwap = false;
                                break;
                            }
                        }
                    }
                } else {
                    canSwap = false;
                }
                if (canSwap && memory.swap(swap.getCells())) {
                    tempResults.add(new TempResult(swap.getPlayer(), swap.getCells()));
                    swapPairs.add(new SwapPair(swap.getCells().get(0), swap.getCells().get(1)));
                } else {
                    ret.add((new ResponseSuccessList(swap.getPlayer(), Collections.emptyList())));
                }
            }
            // Stats command
            if (request instanceof CommandStats) {
                CommandStats stats = (CommandStats) request;
                ResponseStats responseStats = new ResponseStats(stats.getPlayer());
                responseStats.setCellCount(memory.size());
                responseStats.setRemainingRounds(rounds);

                for (MemoryBlock memoryBlock : memory.getMemoryBlocks()) {
                    for (MemoryCell cell : memoryBlock.getCells()) {
                        switch (cell.getState()) {
                            case ALLOCATED:
                                responseStats.setAllocatedCells(responseStats.getAllocatedCells() + 1);
                                if (cell.getOwner() != null && cell.getOwner().equals(stats.getPlayer())) {
                                    responseStats.setOwnedCells(responseStats.getOwnedCells() + 1);
                                }
                                break;
                            case CORRUPT:
                                responseStats.setCorruptCells(responseStats.getCorruptCells() + 1);
                                break;
                            case FORTIFIED:
                                responseStats.setFortifiedCells(responseStats.getFortifiedCells() + 1);
                                if (cell.getOwner() != null && cell.getOwner().equals(stats.getPlayer())) {
                                    responseStats.setOwnedCells(responseStats.getOwnedCells() + 1);
                                }
                                break;
                            case SYSTEM:
                                responseStats.setSystemCells(responseStats.getSystemCells() + 1);
                                break;
                            case FREE:
                                responseStats.setFreeCells(responseStats.getFreeCells() + 1);
                                break;
                        }
                    }
                }
                ret.add(responseStats);
            }

            processedPlayer.add(request.getPlayer());
        }


        if (!processAtEnd.isEmpty()) {
            for (CommandScan scan : processAtEnd) {
                if (scan.getCell() > memory.size() - 1 || scan.getCell() < 0) {
                    ret.add(new ResponseScan(scan.getPlayer(), -1, Collections.emptyList()));
                    continue;
                }
                MemoryBlock block = memory.getBlockByCell(scan.getCell());
                ret.add(new ResponseScan(scan.getPlayer(), block.getStartIndex(), block.getCellStates(scan.getPlayer())));
            }
        }

        //Post-process

        for (TempResult tempResult : tempResults) {
            List<Integer> successful = new ArrayList<>();
            for (Integer integer : tempResult.getCells()) {
                if (!memory.getCellState(integer).equals(MemoryState.CORRUPT)) {
                    successful.add(integer);
                }
            }
            ret.add(new ResponseSuccessList(tempResult.getPlayer(), successful.isEmpty() ? Collections.emptyList() : successful));
        }

        for (MemoryBlock memoryBlock : memory.getMemoryBlocks()) {
            for (MemoryCell cell : memoryBlock.getCells()) {
                cell.setAccessed(false);
                if (cell.getState().equals(MemoryState.FREE)) {
                    cell.setOwner(null);
                }
            }
        }
        return ret;
    }

    @Override
    public List<PlayerScore> getScores() {
        List<PlayerScore> ret = new ArrayList<>();
        for (Player player : players) {
            PlayerScore score = new PlayerScore(player);
            for (MemoryBlock memoryBlock : memory.getMemoryBlocks()) {
                int ownedNCells = 0;
                for (MemoryCell cell : memoryBlock.getCells()) {
                    if (cell.getOwner() != null && cell.getOwner().equals(player)) {
                        switch (cell.getState()) {
                            case ALLOCATED:
                                 score.setOwnedCells(score.getOwnedCells() + 1);
                                 break;
                            case FORTIFIED:
                                score.setOwnedCells(score.getOwnedCells() + 1);
                                score.setFortifiedCells(score.getFortifiedCells() +1);
                                break;
                        }
                        ownedNCells +=1;
                    }
                }
                if (ownedNCells == 4) {
                    score.setOwnedBlocks(score.getOwnedBlocks() + 1);
                }
            }
            score.setTotalScore(score.getOwnedCells() + (4 * score.getOwnedBlocks()));
            ret.add(score);
        }
        return ret;
    }

    @Override
    public String visualize() {
        return "";
    }

    private boolean anyOutOfBound(List<Integer> cells) {
        boolean ret = false;
        for (Integer cell : cells) {
            if (cell != null) {
                ret =  cell < 0 || cell > memory.size() - 1;
            }
        }
        return ret;
    }

    private boolean indicesInSameBlock(List<Integer> cells) {
        boolean ret = false;
        if (cells.size() == 2) {
            if (cells.get(0) == null || cells.get(1) == null) {
                ret = true;
            } else {
                ret = (cells.get(0) / 4) == (cells.get(1) / 4);
            }
        }
        return ret;
    }
}
