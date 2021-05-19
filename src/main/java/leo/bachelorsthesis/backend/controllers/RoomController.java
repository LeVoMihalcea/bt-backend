package leo.bachelorsthesis.backend.controllers;

import leo.bachelorsthesis.backend.constants.RoomConstants;
import leo.bachelorsthesis.backend.domain.Room;
import leo.bachelorsthesis.backend.domain.responses.GeneralResponse;
import leo.bachelorsthesis.backend.domain.responses.GenerateRoomResponse;
import leo.bachelorsthesis.backend.service.RoomService.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/room")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private RoomService roomService;

    @PostMapping("/create")
    public GenerateRoomResponse generateRoom(@RequestBody @Valid Room room) {
        GenerateRoomResponse response = new GenerateRoomResponse();
        logger.info("received generate room request: {}", room);

        roomService.generateRoom(room);

        response.setMessage("Room added successfully!");
        logger.info("generation of room done: {}", response);
        return response;
    }

    @GetMapping("/mine")
    public Set<Room> getMyRooms() {
        Set<Room> response;
        logger.info("received get my rooms request");

        response = roomService.getRoomsAdministrated();

        logger.info("get my rooms done: {}", response);
        return response;
    }

    @GetMapping("/{id}")
    public Room getRoomById(@PathVariable String id) {
        Room response;
        logger.info("received get room details request");

        response = roomService.getRoomById(id);

        logger.info("get room details done: {}", response);
        return response;
    }

    @GetMapping("/joined")
    public Set<Room> getJoinedRooms() {
        Set<Room> response;
        logger.info("received get my rooms request");

        response = roomService.getJoinedRooms();

        logger.info("get my rooms done: {}", response);
        return response;
    }

    @GetMapping("/active")
    public List<Room> getActiveRooms() {
        List<Room> response;
        logger.info("received get my rooms request");

        response = roomService.getActiveRooms();

        logger.info("get my rooms done: {}", response);
        return response;
    }

    @PostMapping("/join/{id}")
    public GeneralResponse joinRoom(@PathVariable String id) {
        GeneralResponse response = new GeneralResponse();
        logger.info("received join room request: {}", id);

        response.setMessage(
                roomService.joinRoom(id) ?
                        RoomConstants.SUCCESSFUL_JOIN : RoomConstants.UNSUCCESSFUL_JOIN
        );

        logger.info("get my rooms done: {}", response);
        return response;
    }

    @DeleteMapping("/{id}")
    public GeneralResponse deleteRoom(@PathVariable String id){
        GeneralResponse response = new GeneralResponse();
        logger.info("received delete room request: {}", id);

        response.setMessage(
                roomService.deleteRoom(id) ?
                        RoomConstants.ROOM_DELETED : RoomConstants.ROOM_NOT_DELETED
        );

        logger.info("delete room done: {}", response);
        return response;
    }

    @DeleteMapping("/{id}/leave")
    public GeneralResponse leaveRoom(@PathVariable String id){
        GeneralResponse response = new GeneralResponse();
        logger.info("received leave room request: {}", id);

        response.setMessage(
                roomService.leaveRoom(id) ?
                        RoomConstants.ROOM_LEFT : RoomConstants.ROOM_NOT_LEFT
        );

        logger.info("leave room done: {}", response);
        return response;
    }
}
