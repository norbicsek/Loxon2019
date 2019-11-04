package team.executors.impl.nk;

import com.loxon.javachallenge.memory.api.communication.general.Command;
import com.loxon.javachallenge.memory.api.communication.general.Response;

import team.executors.impl.ExecutorsTeam;

public class NK extends ExecutorsTeam {

    @Override
    public Command nextRequest(int ri) {
        return allocate(0, 1);
    }

    @Override
    public void response(int ri, Response response) {
        
    }

}
