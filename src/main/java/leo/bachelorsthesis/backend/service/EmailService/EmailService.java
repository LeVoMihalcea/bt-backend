package leo.bachelorsthesis.backend.service.EmailService;

import leo.bachelorsthesis.backend.domain.Room;
import leo.bachelorsthesis.backend.domain.RoomCalendarEntry;
import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    void sendRegistrationMessage(String to, String name);

    void sendCalendarInvite(String to, RoomCalendarEntry roomCalendarEntry, Room room) throws Exception;
}
