package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Properties;

@RestController
@RequestMapping(path = "api/user/registration")
public class RegistrationController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private JavaMailSender mailSender;

    private User user;

    private final int EXPIRATION_TIME_IN_MINUTES = 10;

    private LocalDateTime sendMessageTime;

    @PostMapping
    public void sendMessage(UserRegistrationDto userRegistrationDto) throws IOException, MessagingException {
        user = getUserFromDto(userRegistrationDto);
        sendMessageTime = LocalDateTime.now();
        Properties property = new Properties();
        property.load(new FileInputStream("src/main/resources/application.properties"));
        String fromAddress = property.getProperty("spring.mail.username");
        String senderName = property.getProperty("sender.name");
        String content = "Здравствуйте, " + userRegistrationDto.getFirstName() + "!<br>" +
                "Чтобы завершить регистрацию, подтвердите адрес электронной почты:<br>" +
                "<h3><a href=\"" + "http://localhost:8080/api/user/registration/verify?code=" +
                userRegistrationDto.getEmail().hashCode() + "\" target=\"_blank\">VERIFY</a></h3>";
        String toAddress = userRegistrationDto.getEmail();
        MimeMessage message = createMessage(fromAddress, senderName, content, toAddress);
        mailSender.send(message);

    }

    @GetMapping("/verify")
    public String verify(@RequestParam("code") int code) {
        LocalDateTime linkExpirationTime = sendMessageTime.plusMinutes(EXPIRATION_TIME_IN_MINUTES);
        if ((code == user.getEmail().hashCode()) &&
                (LocalDateTime.now().isBefore(linkExpirationTime))) {
            user.setRole(roleDao.getById(2L).get());

            System.out.println(user);
            userDao.persist(user);
            return "the user is registered";
        }
        return "failed registration";
    }

    private MimeMessage createMessage(String fromAddress,
                                      String senderName,
                                      String content,
                                      String toAddress
    ) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setText(content, true);
        helper.setTo(toAddress);
        return message;
    }

    private User getUserFromDto(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setFullName(userRegistrationDto.getFirstName() + " " + userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(userRegistrationDto.getPassword());
        user.setIsEnabled(true);
        return user;
    }

}
