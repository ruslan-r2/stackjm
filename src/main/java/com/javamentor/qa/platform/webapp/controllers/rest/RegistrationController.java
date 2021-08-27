package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.UserRegistrationDto;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.RoleService;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import com.javamentor.qa.platform.webapp.converters.UserConverter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@RequestMapping(path = "api/user/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserConverter userConverter;

    private User user;

    private final int EXPIRATION_TIME_IN_MINUTES = 1;

    private LocalDateTime sendMessageTime;

    private int code;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${sender.name}")
    private String senderName;

    @Value("${host}")
    private String host;

    @Tag(name = "Отправка сообщения", description = "Отправляет сообщение юзеру, содержащее ссылку с подтверждением почты")
    @PostMapping
    public void sendMessage(UserRegistrationDto userRegistrationDto) throws IOException, MessagingException {
        System.out.println("we are hear");
        user = userConverter.userRegistrationDtoToUser(userRegistrationDto);
        sendMessageTime = LocalDateTime.now();
        code = userRegistrationDto.getEmail().hashCode();
        String toAddress = userRegistrationDto.getEmail();
        String content = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/message.html")));
        content = content.replace("[[name]]", userRegistrationDto.getFirstName())
                .replace("[[host]]", host)
                .replace("[[code]]", code + "");
        System.out.println(content);
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setText(content, true);
        helper.setTo(toAddress);
        mailSender.send(message);

    }

    @Tag(name = "Регистрация юзера")
    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam("code") int code) {
        LocalDateTime linkExpirationTime = sendMessageTime.plusMinutes(EXPIRATION_TIME_IN_MINUTES);
        if ((this.code == code) &&
                (LocalDateTime.now().isBefore(linkExpirationTime))) {
            user.setRole(roleService.getByName("ROLE_USER").get());
            userService.persist(user);
            return new ResponseEntity<>("Вы успешно зарегистрировались!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Ссылка недействительна", HttpStatus.FORBIDDEN);
    }

}
