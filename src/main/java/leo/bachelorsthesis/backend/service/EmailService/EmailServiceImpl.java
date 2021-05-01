package leo.bachelorsthesis.backend.service.EmailService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

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
}
