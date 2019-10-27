package team.executors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loxon.javachallenge.memory.api.Game;
import com.loxon.javachallenge.memory.api.MemoryState;
import com.loxon.javachallenge.memory.api.Player;
import com.loxon.javachallenge.memory.api.communication.general.Command;
import com.loxon.javachallenge.memory.api.communication.general.Response;

import team.executors.util.GameUtils;

public class GameRunner {

    private final ExecutorsTeam[] teams;

    private final List<MemoryState> initialMemory;

    private final int rounds;

    private final Game game;

    private final Map<Player, ExecutorsTeam> teamByPlayer = new HashMap<>();

    public GameRunner(
        final ExecutorsTeam[] teams
        , final List<MemoryState> initialMemory
        , final int rounds
    ) {
        this.teams = teams;
        this.initialMemory = initialMemory;
        this.rounds = rounds;
        this.game = new ExecutorsGame();
    }

    private final void nextRound(final int ri) {
        List<Command> requests = new ArrayList<>(teams.length);
        for(ExecutorsTeam team : teams) {
            Command request = team.nextRequest(ri);
            SysOut.println("    Team " + team + " request: " + GameUtils.requestToString(request));
            if(request != null) {
                requests.add(request);
            }
        }
        List<Response> responses = game.nextRound(requests.toArray(new Command[requests.size()]));
        for(Response response : responses) {
            ExecutorsTeam team = teamByPlayer.get(response.getPlayer());
            SysOut.println("    Team " + team + " response: " + GameUtils.responseToString(response));
            team.response(ri, response);
        }
    }

    public void runGame() {
        for(int ti = 0; ti < teams.length; ti++) {
            ExecutorsTeam team = teams[ti];
            Player player = game.registerPlayer(team.getName());
            team.init(player, rounds, initialMemory);
            teamByPlayer.put(player, team);
        }
        game.startGame(initialMemory, rounds);
        SysOut.println("Initial:");
        SysOut.println(game.visualize());
        for(int ri = 0; ri < rounds; ri++) {
            SysOut.println();
            SysOut.println();
            SysOut.println("Starting round " + ri + "...");
            nextRound(ri);
            SysOut.println("After round " + ri + ":");
            SysOut.println(game.visualize());
        }
        SysOut.println("Game Over.");
    }

}
