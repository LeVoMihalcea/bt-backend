package leo.bachelorsthesis.backend.service.EmailService;

import leo.bachelorsthesis.backend.domain.Room;
import leo.bachelorsthesis.backend.domain.RoomCalendarEntry;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.UidGenerator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.GregorianCalendar;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendRegistrationMessage(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("webappbt@gmail.com");
        message.setTo(to);
        message.setSubject("Registration successful");
        message.setText("Hello " + name + "!\nYour registration was successful. \n\nbt-webapp");
        emailSender.send(message);
    }

    @Override
    public void sendCalendarInvite(String to, RoomCalendarEntry roomCalendarEntry, Room room) throws Exception {
        // Create a TimeZone
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
        TimeZone timezone = registry.getTimeZone("Europe/Bucharest");
        VTimeZone tz = timezone.getVTimeZone();

        String recurrencePattern = getRecurrencePattern(roomCalendarEntry);

        java.util.Calendar startDate = mapDate(timezone, roomCalendarEntry.getFirstDateTime());

        LocalDateTime endDateTime = roomCalendarEntry.getFirstDateTime().plusHours(roomCalendarEntry.getHours());

        java.util.Calendar endDate = mapDate(timezone, endDateTime);

        VEvent meeting = createVEvent(room, startDate, endDate);
        meeting.getProperties().add(tz.getTimeZoneId());

        setUniqueIdentifier(meeting);

        // Create a calendar
        Calendar icsCalendar = createCalendar();

        if(roomCalendarEntry.getRepeatEvery() != 0){
            meeting.getProperties().add(new RRule(recurrencePattern));
        }
        meeting.getProperties().add(new Description(room.getDescription() +
                "\nClick here to join the meeting:\nhttps://bt-webapp.sudo.rocks/room/" + room.getId()));


        // Add the event and print
        icsCalendar.getComponents().add(meeting);

        createCalendarFile(icsCalendar);

        sendEmail(to, room);
    }

    private Calendar createCalendar() {
        Calendar icsCalendar = new Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
        icsCalendar.getProperties().add(CalScale.GREGORIAN);
        icsCalendar.getProperties().add(Version.VERSION_2_0);
        return icsCalendar;
    }

    private void setUniqueIdentifier(VEvent meeting) throws SocketException {
        // generate unique identifier..
        UidGenerator ug = new UidGenerator("uidGen");
        Uid uid = ug.generateUid();
        meeting.getProperties().add(uid);
    }

    private void createCalendarFile(Calendar icsCalendar) {
        String filePath = "calendar.ics";
        FileOutputStream fout = null;
        try {

            fout = new FileOutputStream(filePath);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(icsCalendar, fout);

        } catch (IOException | ValidationException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendEmail(String to, Room room) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("webappbt@gmail.com");
        helper.setTo(to);
        helper.setSubject("Joined room " + room.getName());
        helper.setText("Click here to join the meeting:\nhttps://bt-webapp.sudo.rocks/room/" + room.getId());

        FileSystemResource file
                = new FileSystemResource(new File("calendar.ics"));
        helper.addAttachment("calendar.ics", file);

        emailSender.send(message);
    }

    private VEvent createVEvent(Room room, java.util.Calendar startDate, java.util.Calendar endDate) {
        String eventName = room.getName();
        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent meeting = new VEvent(start, end, eventName);
        return meeting;
    }

    private java.util.Calendar mapDate(TimeZone timezone, LocalDateTime firstDateTime) {
        java.util.Calendar startDate = new GregorianCalendar();
        startDate.setTimeZone(timezone);
        startDate.set(java.util.Calendar.MONTH, firstDateTime.getMonthValue() - 1);
        startDate.set(java.util.Calendar.DAY_OF_MONTH, firstDateTime.getDayOfMonth());
        startDate.set(java.util.Calendar.YEAR, firstDateTime.getYear());
        startDate.set(java.util.Calendar.HOUR_OF_DAY, firstDateTime.getHour());
        startDate.set(java.util.Calendar.MINUTE, firstDateTime.getMinute());
        startDate.set(java.util.Calendar.SECOND, 0);
        return startDate;
    }

    private String getRecurrencePattern(RoomCalendarEntry roomCalendarEntry) {
        return "FREQ=" + roomCalendarEntry.getTimeUnit()
                + ";INTERVAL=" + roomCalendarEntry.getRepeatEvery();
    }
}
