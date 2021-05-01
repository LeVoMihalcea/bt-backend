package leo.bachelorsthesis.backend.service.EmailService;

import leo.bachelorsthesis.backend.domain.Room;
import leo.bachelorsthesis.backend.domain.RoomCalendarEntry;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.UidGenerator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
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


        String recurrencePattern = "FREQ=" + roomCalendarEntry.getTimeUnit()
                + ";INTERVAL=" + roomCalendarEntry.getRepeatEvery();

        java.util.Calendar startDate = new GregorianCalendar();
        startDate.setTimeZone(timezone);
        startDate.set(java.util.Calendar.MONTH, roomCalendarEntry.getFirstDateTime().getMonthValue() - 1);
        startDate.set(java.util.Calendar.DAY_OF_MONTH, roomCalendarEntry.getFirstDateTime().getDayOfMonth());
        startDate.set(java.util.Calendar.YEAR, roomCalendarEntry.getFirstDateTime().getYear());
        startDate.set(java.util.Calendar.HOUR_OF_DAY, roomCalendarEntry.getFirstDateTime().getHour());
        startDate.set(java.util.Calendar.MINUTE, roomCalendarEntry.getFirstDateTime().getMinute());
        startDate.set(java.util.Calendar.SECOND, 0);

        LocalDateTime endDateTime = roomCalendarEntry.getFirstDateTime().plusHours(roomCalendarEntry.getHours());

        java.util.Calendar endDate = new GregorianCalendar();
        endDate.setTimeZone(timezone);
        endDate.set(java.util.Calendar.MONTH, endDateTime.getMonthValue() - 1);
        endDate.set(java.util.Calendar.DAY_OF_MONTH, endDateTime.getDayOfMonth());
        endDate.set(java.util.Calendar.YEAR, endDateTime.getYear());
        endDate.set(java.util.Calendar.HOUR_OF_DAY, endDateTime.getHour());
        endDate.set(java.util.Calendar.MINUTE, endDateTime.getMinute());
        endDate.set(java.util.Calendar.SECOND, 0);

        // Create the event
        String eventName = room.getName();
        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent meeting = new VEvent(start, end, eventName);

        // add timezone info..
        meeting.getProperties().add(tz.getTimeZoneId());

        // generate unique identifier..
        UidGenerator ug = new UidGenerator("uidGen");
        Uid uid = ug.generateUid();
        meeting.getProperties().add(uid);

        // Create a calendar
        net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
        icsCalendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
        icsCalendar.getProperties().add(CalScale.GREGORIAN);
        icsCalendar.getProperties().add(Version.VERSION_2_0);

        if(roomCalendarEntry.getRepeatEvery() != 0){
            meeting.getProperties().add(new RRule(recurrencePattern));
        }
        meeting.getProperties().add(new Description(room.getDescription() +
                "\nClick here to join the meeting:\nhttps://bt-webapp.sudo.rocks/room/" + room.getId()));


        // Add the event and print
        icsCalendar.getComponents().add(meeting);

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
}
