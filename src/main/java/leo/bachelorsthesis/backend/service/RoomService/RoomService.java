package leo.bachelorsthesis.backend.service.RoomService;

import leo.bachelorsthesis.backend.domain.Room;
import leo.bachelorsthesis.backend.domain.requests.JoinRoomRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomService {
    Optional<Room> generateRoom(Room room);

    Set<Room> getRoomsAdministrated();

    Set<Room> getJoinedRooms();

    boolean joinRoom(String id);

    List<Room> getActiveRooms();

    boolean deleteRoom(String id);

    boolean leaveRoom(String id);

    Room getRoomById(String id);
}
