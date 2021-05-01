package leo.bachelorsthesis.backend.service.EmailService;

import leo.bachelorsthesis.backend.domain.Room;
import leo.bachelorsthesis.backend.domain.RoomCalendarEntry;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendRegistrationMessage(String to, String name);

    @Async
    void sendCalendarInvite(String to, RoomCalendarEntry roomCalendarEntry, Room room) throws Exception;
}
