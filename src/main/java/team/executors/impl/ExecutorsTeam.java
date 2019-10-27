package team.executors.impl;

import java.util.Arrays;
import java.util.List;

import com.loxon.javachallenge.memory.api.MemoryState;
import com.loxon.javachallenge.memory.api.Player;
import com.loxon.javachallenge.memory.api.communication.commands.CommandAllocate;
import com.loxon.javachallenge.memory.api.communication.commands.CommandFortify;
import com.loxon.javachallenge.memory.api.communication.commands.CommandFree;
import com.loxon.javachallenge.memory.api.communication.commands.CommandRecover;
import com.loxon.javachallenge.memory.api.communication.commands.CommandScan;
import com.loxon.javachallenge.memory.api.communication.commands.CommandSwap;
import com.loxon.javachallenge.memory.api.communication.general.Command;
import com.loxon.javachallenge.memory.api.communication.general.Response;

public abstract class ExecutorsTeam {

    protected Player player;

    protected int rounds;

    protected List<MemoryState> initialMemory;

    public abstract Command nextRequest(int ri);

    public abstract void response(int ri, Response response);

    public void init(
        Player player
        , int rounds
        , List<MemoryState> initialMemory
    ) {
        this.player = player;
        this.rounds = rounds;
        this.initialMemory = initialMemory;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return getName();
    }

    protected List<Integer> asList(final Integer cell0, final Integer cell1 ) {
        return Arrays.asList(cell0, cell1);
    }

    protected Command allocate(final Integer cell0, final Integer cell1 ) {
        return new CommandAllocate(player, asList(cell0, cell1));
    }

    protected Command free(final Integer cell0, final Integer cell1 ) {
        return new CommandFree(player, asList(cell0, cell1));
    }

    protected Command recover(final Integer cell0, final Integer cell1 ) {
        return new CommandRecover(player, asList(cell0, cell1));
    }

    protected Command fortify(final Integer cell0, final Integer cell1 ) {
        return new CommandFortify(player, asList(cell0, cell1));
    }

    protected Command swap(final Integer cell0, final Integer cell1 ) {
        return new CommandSwap(player, asList(cell0, cell1));
    }

    protected CommandScan scan(final int cell ) {
        return new CommandScan(player, cell);
    }

}
