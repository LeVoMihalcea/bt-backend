package leo.bachelorsthesis.backend.service.RoomService;

import leo.bachelorsthesis.backend.constants.RoomConstants;
import leo.bachelorsthesis.backend.constants.UserConstants;
import leo.bachelorsthesis.backend.domain.Room;
import leo.bachelorsthesis.backend.domain.User;
import leo.bachelorsthesis.backend.domain.requests.JoinRoomRequest;
import leo.bachelorsthesis.backend.error.exceptions.RoomNotFoundException;
import leo.bachelorsthesis.backend.error.exceptions.UserNotFoundException;
import leo.bachelorsthesis.backend.repository.RoomRepository;
import leo.bachelorsthesis.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomServiceImpl(RoomRepository roomRepository, UserRepository userRepository) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Room> generateRoom(Room room) {
        User user = getUser();
        room.setHost(user);

        return Optional.of(roomRepository.save(room));
    }

    @Override
    public Set<Room> getRoomsAdministrated() {
        User user = getUser();

        return user.getRoomsAdministrated();
    }

    @Override
    public Set<Room> getJoinedRooms() {
        User user = getUser();

        return user.getRoomsJoined();
    }

    @Override
    public boolean joinRoom(String id) {
        User user = getUser();

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
                });

        room.addUser(user);
        roomRepository.saveAndFlush(room);
        return true;
    }

    @Override
    public Set<Room> getActiveRooms() {
        User user = getUser();

        Set<Room> rooms = user.getRoomsAdministrated();
        rooms.addAll(user.getRoomsJoined());

        return rooms;
    }

    @Override
    public boolean deleteRoom(String id) {
        User user = getUser();

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
                });

        if (room.getHost().equals(user)) {
            roomRepository.delete(room);
            return true;
        }
        return false;
    }

    @Override
    public boolean leaveRoom(String id) {
        User user = getUser();

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RoomNotFoundException(RoomConstants.ROOM_NOT_FOUND);
                });

        if (!room.getHost().equals(user)) {
            room.setUsers(room.getUsers().stream()
                    .filter(roomUser -> !roomUser.equals(user))
                    .collect(Collectors.toSet()));
            roomRepository.save(room);
            return true;
        }
        return false;
    }

    private User getUser() {
        Authentication loggedInUserDetails = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(loggedInUserDetails.getName())
                .orElseThrow(() -> {
                    throw new UserNotFoundException(UserConstants.USER_NOT_FOUND);
                });
    }
}
