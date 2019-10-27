package team.executors.util;

import com.loxon.javachallenge.memory.api.communication.general.Command;
import com.loxon.javachallenge.memory.api.communication.general.Response;

public class GameUtils {

    private GameUtils() {
    }

    public static final String requestToString(Command request) {
        if(request == null) {
            return "null";
        }
        return request.getClass().getSimpleName();
    }

    public static final String responseToString(Response response) {
        if(response == null) {
            return "null";
        }
        return response.getClass().getSimpleName();
    }

}
