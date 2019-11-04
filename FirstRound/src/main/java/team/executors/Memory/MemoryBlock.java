package team.executors.Memory;

import com.loxon.javachallenge.memory.api.MemoryState;
import com.loxon.javachallenge.memory.api.Player;

import java.util.ArrayList;
import java.util.List;

public class MemoryBlock {

    private final int startIndex;
    private List<MemoryCell> cells;

    public MemoryBlock(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public void addCell(MemoryState state) {
        if (cells == null) {
            this.cells = new ArrayList<>();
        }
        this.cells.add(new MemoryCell(state));
    }

    public List<MemoryCell> getCells() {
        return this.cells;
    }

    public MemoryCell getCell(int cellIdx) {
        return this.cells.get(cellIdx);
    }

    public List<MemoryState> getCellStates(Player player) {
        List<MemoryState> ret = new ArrayList<>();
        for (MemoryCell cell : cells) {
            boolean owned = cell.getOwner() == player;
            if (cell.getState().equals(MemoryState.ALLOCATED) && owned) {
                ret.add(MemoryState.OWNED_ALLOCATED);
            } else if (cell.getState().equals(MemoryState.FORTIFIED) && owned) {
                ret.add(MemoryState.OWNED_FORTIFIED);
            } else {
                ret.add(cell.getState());
            }
        }
        return ret;
    }

    public MemoryState getCellState(int cellIdx) {
        return this.cells.get(cellIdx).getState();
    }

    public boolean allocateCell(Player player, int cellIdx) {
        return this.cells.get(cellIdx).allocate(player);
    }

    public boolean freeCell(Player player, int cellIdx) {
        return this.cells.get(cellIdx).free(player);
    }

    public boolean recoverCell(Player player, int cellIdx) {
        return this.cells.get(cellIdx).recover(player);
    }

    public boolean fortifyCell(int cellIdx) {
        return this.cells.get(cellIdx).fortify();
    }
}
