package anonymous.automata.API;

import java.util.List;

import anonymous.automata.Models.Room;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by affan on 23/8/17.
 */

public interface Automata_API {
    @GET("room/")
    Call<List<Room>> getRoomList();
}
