package team.executors.impl.fl;

import com.loxon.javachallenge.memory.api.communication.general.Command;
import com.loxon.javachallenge.memory.api.communication.general.Response;

import team.executors.impl.ExecutorsTeam;

public class FL extends ExecutorsTeam {

    @Override
    public Command nextRequest(int ri) {
        return allocate(0, 1);
    }

    @Override
    public void response(int ri, Response response) {
        
    }

}
