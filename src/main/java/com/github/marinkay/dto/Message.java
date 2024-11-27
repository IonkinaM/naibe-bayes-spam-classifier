package com.github.marinkay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// Класс-сообщение, включающий определние спам или не спам и само сообщение
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private String content;
    private SpamClassifierEnum spamClassifier;
}
