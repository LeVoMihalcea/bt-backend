package leo.bachelorsthesis.backend.service.RoomService;

import leo.bachelorsthesis.backend.domain.Room;
import leo.bachelorsthesis.backend.domain.requests.JoinRoomRequest;

import java.util.Optional;
import java.util.Set;

public interface RoomService {
    Optional<Room> generateRoom(Room room);

    Set<Room> getRoomsAdministrated();

    Set<Room> getJoinedRooms();

    boolean joinRoom(String id);

    Set<Room> getActiveRooms();
}
