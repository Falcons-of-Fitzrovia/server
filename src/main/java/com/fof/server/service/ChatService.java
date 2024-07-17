package com.fof.server.service;

import com.fof.server.enumeration.Chat;
import com.fof.server.exception.CustomErrorException;
import com.fof.server.model.entity.ChatDTO;
import com.fof.server.model.normal.PayLoadDTO;
import com.fof.server.repository.ChatRepository;
import com.fof.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public Boolean saveMessage(PayLoadDTO payLoadDTO) {

        var sender = userRepository.findById(Integer.parseInt(payLoadDTO.getSender())).orElseThrow(() -> new CustomErrorException("Sender not found"));
        var receiver = userRepository.findById(Integer.parseInt(payLoadDTO.getReceiver())).orElseThrow(() -> new CustomErrorException("Receiver not found"));
        Timestamp timestamp = getTimestamp(payLoadDTO.getTime());

        var message = ChatDTO.builder()
                .sender(sender)
                .receiver(receiver)
                .message(payLoadDTO.getMessage())
                .timestamp(timestamp)
                .type(Chat.MESSAGE)
                .status(Chat.SENT)
                .build();

        chatRepository.save(message);

        return true;
    }

    public Timestamp getTimestamp(String time) {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date parsedDate = dateFormat.parse(time);

            return new Timestamp(parsedDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
