package team.executors.Memory;

import com.loxon.javachallenge.memory.api.MemoryState;
import com.loxon.javachallenge.memory.api.Player;

public class MemoryCell {

    private boolean accessed = false;
    private Player owner = null;
    private MemoryState state;

    public MemoryCell(MemoryState initialState) {
        this.state = initialState;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public MemoryState getState() {
        return state;
    }

    public void setState(MemoryState state) {
        this.state = state;
    }

    public boolean isAccessed() {
        return accessed;
    }

    public void setAccessed(boolean accessed) {
        this.accessed = accessed;
    }

    public boolean allocate(Player player) {
        if (this.state.equals(MemoryState.SYSTEM) || this.state.equals(MemoryState.FORTIFIED) || this.state.equals(MemoryState.CORRUPT)) return false;
        if (this.accessed || (owner != null && this.state.equals(MemoryState.ALLOCATED))) {
            this.state = MemoryState.CORRUPT;
            this.owner = null;
            return false;
        }
        this.accessed = true;
        this.owner = player;
        this.state = MemoryState.ALLOCATED;
        return true;
    }

    public boolean fortify() {
        if (this.accessed || this.state.equals(MemoryState.SYSTEM) || this.owner == null || this.state.equals(MemoryState.FORTIFIED)) {
            this.accessed = true;
            return false;
        }
        this.state = MemoryState.FORTIFIED;
        this.accessed = true;
        return true;
    }

    public boolean free(Player player) {
        if (this.state.equals(MemoryState.SYSTEM) || this.state.equals(MemoryState.FORTIFIED)) return false;
        if (this.state.equals(MemoryState.FREE) && owner != null && !owner.equals(player)) {
            this.state = MemoryState.CORRUPT;
            this.owner = null;
            return false;
        }
        this.state = MemoryState.FREE;
        this.owner = player;
        return true;
    }

    public boolean recover (Player player) {
        if (this.state.equals(MemoryState.SYSTEM) || this.state.equals(MemoryState.FORTIFIED) ) return false;
        if (this.accessed || this.state.equals(MemoryState.ALLOCATED) || this.state.equals(MemoryState.FREE)) {
            this.state = MemoryState.CORRUPT;
            this.owner = null;
            this.accessed = true;
            return false;
        }
        if (this.state.equals(MemoryState.CORRUPT)) {
            this.state = MemoryState.ALLOCATED;
            this.owner = player;
            this.accessed = true;
            return true;
        }
        return false;
    }
}
