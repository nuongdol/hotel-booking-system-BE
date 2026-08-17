package com.example.BookingHotel.service;

import com.example.BookingHotel.request.MailBody;
import io.netty.handler.codec.MessageAggregationException;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendSimpleMessage(MailBody mailBody) throws MessageAggregationException, MessagingException;
}
