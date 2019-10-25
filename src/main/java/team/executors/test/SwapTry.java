package team.executors.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;

import com.loxon.javachallenge.memory.GameImplementationFactory;
import com.loxon.javachallenge.memory.api.Game;
import com.loxon.javachallenge.memory.api.MemoryState;
import com.loxon.javachallenge.memory.api.Player;
import com.loxon.javachallenge.memory.api.communication.commands.CommandAllocate;
import com.loxon.javachallenge.memory.api.communication.commands.CommandFortify;
import com.loxon.javachallenge.memory.api.communication.commands.CommandFree;
import com.loxon.javachallenge.memory.api.communication.commands.CommandRecover;
import com.loxon.javachallenge.memory.api.communication.commands.CommandScan;
import com.loxon.javachallenge.memory.api.communication.commands.CommandSwap;
import com.loxon.javachallenge.memory.api.communication.general.Command;

public class SwapTry {

    public final static MemoryState F  = MemoryState.FREE;
    public final static MemoryState S  = MemoryState.SYSTEM;
    public final static MemoryState C  = MemoryState.CORRUPT;
    public final static MemoryState AX = MemoryState.ALLOCATED;
    public final static MemoryState AM = MemoryState.OWNED_ALLOCATED;
    public final static MemoryState FX = MemoryState.FORTIFIED;
    public final static MemoryState FM = MemoryState.OWNED_FORTIFIED;

    private final static int      GAME_ROUNDS = 10;

    public static List<Integer> asList( final Integer cell0, final Integer cell1 ) {
        return Arrays.asList(cell0, cell1);
    }

    public static Command allocate( final Player player, final Integer cell0, final Integer cell1 ) {
        return new CommandAllocate(player, asList(cell0, cell1));
    }

    public static Command free( final Player player, final Integer cell0, final Integer cell1 ) {
        return new CommandFree(player, asList(cell0, cell1));
    }

    public static Command recover( final Player player, final Integer cell0, final Integer cell1 ) {
        return new CommandRecover(player, asList(cell0, cell1));
    }

    public static Command fortify( final Player player, final Integer cell0, final Integer cell1 ) {
        return new CommandFortify(player, asList(cell0, cell1));
    }

    public static Command swap( final Player player, final Integer cell0, final Integer cell1 ) {
        return new CommandSwap(player, asList(cell0, cell1));
    }

    public static CommandScan scan( final Player player, final int cell ) {
        return new CommandScan(player, cell);
    }

    public static void main(String[] args) {
        Game game = GameImplementationFactory.get();
        Assert.assertNotNull("Game implementation is required.", game);
        Player pA = game.registerPlayer("a");
        Player pB = game.registerPlayer("b");
        game.startGame(Arrays.asList(
            F, F, F, F, F, S, S, S,
            F, F, F, F, F, F, F, F,
            F, F, F, F, F, F, F, F), GAME_ROUNDS);

        game.nextRound( // allocate cells
            allocate(pA, 0, 1),
            swap(pB, 0, 2)
        );
        System.out.println(game.visualize());
        System.out.println("\n");
    }

}
