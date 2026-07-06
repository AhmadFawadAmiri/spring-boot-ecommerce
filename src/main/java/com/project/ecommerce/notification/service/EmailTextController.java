package com.project.ecommerce.notification.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class EmailTextController {
    private final EmailService emailService;

    public EmailTextController(EmailService emailService) {
        this.emailService = emailService;
    }

//    @PostMapping("/email")
//    public String sendEmail(){
//        emailService.send("ahmadfawad.amiri1@gmail.com",
//                "Spring Boot Test",
//                "Congratulations! Your email service works.");
//        return "Email sent!";
//    }
}
