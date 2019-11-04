package team.executors.Memory;

import com.loxon.javachallenge.memory.api.MemoryState;
import com.loxon.javachallenge.memory.api.Player;

import java.util.*;

public class Memory {

    private Map<Integer, MemoryBlock> memoryBlocks = new HashMap<>();
    private int size;

    public Memory (List<MemoryState> initialCellStates) {
        this.size = initialCellStates.size();
        MemoryBlock block = null;
        for (int i = 0; i < initialCellStates.size(); i++) {
            if (i % 4 == 0) {
                block = new MemoryBlock(i);
                memoryBlocks.put(i / 4, block);
            }
            block.addCell(initialCellStates.get(i));
        }
    }

    public Collection<MemoryBlock> getMemoryBlocks() {
        return this.memoryBlocks.values();
    }

    public MemoryBlock getBlockByCell(int cellIdx) {
        return memoryBlocks.get((calculateBlockIndex(cellIdx)));
    }

    public int size() {
        return this.size;
    }

    public boolean allocate(Player player, int cellIdx) {
        return getBlockByCell(cellIdx).allocateCell(player, calculateCellIndex(cellIdx));
    }

    public boolean free(Player player, int cellIdx) {
        return getBlockByCell(cellIdx).freeCell(player, calculateCellIndex(cellIdx));
    }

    public boolean recover(Player player, int cellIdx) {
        return getBlockByCell(cellIdx).recoverCell(player, calculateCellIndex(cellIdx));
    }

    public boolean fortify(int cellIdx) {
        return getBlockByCell(cellIdx).fortifyCell(calculateCellIndex(cellIdx));
    }

    public boolean swap(List<Integer> cells) {
        MemoryCell cellA = getBlockByCell(cells.get(0)).getCell(calculateCellIndex(cells.get(0)));
        MemoryCell cellB = getBlockByCell(cells.get(1)).getCell(calculateCellIndex(cells.get(0)));

        if (!cellA.isAccessed() && !cellB.isAccessed()) {
            MemoryState storeState = cellA.getState();
            Player storePlayer = cellA.getOwner();

            cellA.setState(cellB.getState());
            cellA.setOwner(cellB.getOwner());

            cellB.setState(storeState);
            cellB.setOwner(storePlayer);

            cellA.setAccessed(true);
            cellB.setAccessed(true);

            return true;
        } else {
            cellA.setState(MemoryState.CORRUPT);
            cellB.setState(MemoryState.CORRUPT);
            return false;
        }
    }

    public void setCellState(int cellIdx, MemoryState state) {
        getBlockByCell(cellIdx).getCell(calculateCellIndex(cellIdx)).setState(state);
    }

    public MemoryState getCellState(int cellIdx) {
        return getBlockByCell(cellIdx).getCellState(calculateCellIndex(cellIdx));
    }

    /*
        Cell index divided by 4 tells the block index
     */
    private int calculateBlockIndex(int absoluteCellIndex) {
        return absoluteCellIndex / 4;
    }

    /*
        Modulo 4 of the cell index tells the relative index in the block
     */
    private int calculateCellIndex(int absoluteCellIndex) {
        return  absoluteCellIndex % 4;
    }
}
