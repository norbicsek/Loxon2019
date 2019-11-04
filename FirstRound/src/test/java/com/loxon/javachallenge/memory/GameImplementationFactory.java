package com.loxon.javachallenge.memory;

import com.loxon.javachallenge.memory.api.Game;
import team.executors.ExecutorsGame;

public class GameImplementationFactory {
    public static Game get() {
        return new ExecutorsGame();
    }
}
