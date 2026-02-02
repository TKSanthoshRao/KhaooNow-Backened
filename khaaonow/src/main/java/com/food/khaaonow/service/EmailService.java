package com.food.khaaonow.service;

import com.food.khaaonow.repo.EmailVerificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    private JavaMailSender mailSender;
    private EmailVerificationTokenRepo emailVerificationTokenRepo;

    @Autowired
    public EmailService(JavaMailSender mailSender, EmailVerificationTokenRepo emailVerificationTokenRepo) {
        this.mailSender = mailSender;
        this.emailVerificationTokenRepo = emailVerificationTokenRepo;
    }

    public void sendSimpleEmail(String to, String subject, String body,String OTP) throws MailException {


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
        }catch (MailException mailException){
            throw mailException;
        }
    }
}
