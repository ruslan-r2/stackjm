package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.dao.abstracts.model.RoleDao;
import com.javamentor.qa.platform.dao.abstracts.model.UserDao;
import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.webapp.converters.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final int EXPIRATION_TIME_IN_MINUTES = 1;

    private LocalDateTime sendMessageTime;

    @Tag(name = "Отправка сообщения", description = "Отправляет сообщение юзеру, содержащее ссылку с подтверждением почты")
    @PostMapping
    public void sendMessage(UserRegistrationDto userRegistrationDto) throws IOException, MessagingException {
        user = UserMapper.userMapper.toUser(userRegistrationDto);
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

    @Tag(name = "Регистрация юзера")
    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("code") int code) {
        LocalDateTime linkExpirationTime = sendMessageTime.plusMinutes(EXPIRATION_TIME_IN_MINUTES);
        if ((code == user.getEmail().hashCode()) &&
                (LocalDateTime.now().isBefore(linkExpirationTime))) {
            System.out.println(user);
            user.setRole(roleDao.getById(2L).get());
            userDao.persist(user);
            return new ResponseEntity<>("Вы успешно зарегистрировались!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Ссылка недействительна", HttpStatus.FORBIDDEN);
    }

    @Operation(
            summary = "Создание сообщения",
            description = "Создает сообщение, содержащее ссылку"
    )
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

}
