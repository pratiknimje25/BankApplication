package com.javabook.BankApplication.service.impl;

import com.javabook.BankApplication.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

  @Value("${spring.mail.username}")
  private String sendEmail;

  @Autowired private JavaMailSender javaMailSender;

  public void sendEmailNotification(EmailDetails emailDetails) {

    try {
      SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom(sendEmail);
      mailMessage.setTo(emailDetails.getRecipient());
      mailMessage.setText(emailDetails.getMsgBody());
      mailMessage.setSubject(emailDetails.getSubject());
      javaMailSender.send(mailMessage);
      System.out.println("Mail sent successfully");
    } catch (MailException e) {
      throw new RuntimeException(e);
    }
  }
}
