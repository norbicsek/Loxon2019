package team.executors.main;

import com.loxon.javachallenge.memory.api.MemoryState;

import team.executors.impl.ExecutorsTeam;
import team.executors.impl.fg.FG;
import team.executors.impl.fl.FL;
import team.executors.impl.nk.NK;

public abstract class MainAbstract {

    protected static final MemoryState F  = MemoryState.FREE;
    protected static final MemoryState S  = MemoryState.SYSTEM;
    protected static final MemoryState C  = MemoryState.CORRUPT;
    protected static final MemoryState AX = MemoryState.ALLOCATED;
    protected static final MemoryState AM = MemoryState.OWNED_ALLOCATED;
    protected static final MemoryState FX = MemoryState.FORTIFIED;
    protected static final MemoryState FM = MemoryState.OWNED_FORTIFIED;

    protected static final ExecutorsTeam[] DEFAULT_TEAMS = new ExecutorsTeam[] {
        new FG()
        , new FL()
        , new NK()
    }
    ;

    protected MainAbstract() {
    }

}
