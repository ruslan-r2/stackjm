package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.Role;
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
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/user/registration")
public class RegistrationController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private JavaMailSender mailSender;

    private User getUserFromDto(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setFullName(userRegistrationDto.getFirstName() + " " + userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(userRegistrationDto.getPassword());
        return user;
    }

    @PostMapping
    public void register(UserRegistrationDto userRegistrationDto) throws IOException, MessagingException {
        FileInputStream fis;
        Properties property = new Properties();
        User user = getUserFromDto(userRegistrationDto);
        Role roleUser = roleDao.getById(2L).get();
        user.setRole(roleUser);
        user.setIsEnabled(false);
        fis = new FileInputStream("src/main/resources/application.properties");
        property.load(fis);
        String toAddress = user.getEmail();
        String fromAddress = property.getProperty("spring.mail.username");
        String senderName = property.getProperty("sender.name");
        String subject = "Пожалуйста, подтвердите электронный адрес";
        String content = "Здравствуйте, [[name]]!<br>"
                + "Чтобы завершить регистрацию, подтвердите адрес электронной почты:<br>"
                + "<h3><a href=\"[[url]]\" target=\"_blank\">VERIFY</a></h3>"
                + "С уважением,<br>"
                + senderName;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", userRegistrationDto.getFirstName());
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userDao.persist(user);
        String verifyURL = "http://localhost:8080/api/user/registration/verify?token=" + token;
        content = content.replace("[[url]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);

    }

    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token) {
        User user = userDao.getUserByToken(token);
        LocalDateTime linkExpirationTime = user.getPersistDateTime().plusMinutes(1);
        if (LocalDateTime.now().isBefore(linkExpirationTime)) {
            user.setIsEnabled(true);
            userDao.persist(user);
            return "the user is registered";
        }
        return "failed registration";
    }

}
