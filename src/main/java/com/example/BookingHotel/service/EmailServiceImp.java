package com.example.BookingHotel.service;

import com.example.BookingHotel.request.MailBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements IEmailService{
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    @Override
    public void sendSimpleMessage(MailBody mailBody) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        Context context = new Context();
        context.setVariable("subject", mailBody.subject());
        context.setVariable("message", mailBody.text());
        String htmlBody = templateEngine.process("emailOtp", context);
        helper.setSubject(mailBody.subject());
        helper.setTo(mailBody.to());
        helper.setText(htmlBody, true);
        mailSender.send(mimeMessage);
    }
}
