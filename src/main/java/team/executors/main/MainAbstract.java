package team.executors.main;

import com.loxon.javachallenge.memory.api.MemoryState;

import team.executors.ExecutorsTeam;
import team.executors.impl.FG;
import team.executors.impl.FL;
import team.executors.impl.NK;

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
