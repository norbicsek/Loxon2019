package team.executors.main;

import java.util.Arrays;
import java.util.List;

import com.loxon.javachallenge.memory.api.MemoryState;

import team.executors.GameRunner;

public class Main1 extends MainAbstract {

    private static final int ROUNDS = 100;

    private static final List<MemoryState> BOARD =
        Arrays.asList(
            F, F, F, F, F, S, S, S,
            F, F, F, F, F, F, F, F,
            F, F, F, F, F, F, F, F
        )
    ;

    public static void main(String[] args) {
        new GameRunner(
            DEFAULT_TEAMS
            , BOARD
            , ROUNDS
        ).runGame()
        ;
    }

}
