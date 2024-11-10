package com.github.marinkay.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.marinkay.dto.Message;
import com.github.marinkay.dto.SpamClassifierEnum;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class NaiveBayesClassifierService {
    private static final Logger LOGGER = Logger.getLogger(NaiveBayesClassifierService.class.getName());
    private final Map<String, Integer> spamWordCount = new HashMap<>();
    private final Map<String, Integer> notSpamWordCount = new HashMap<>();
    private int spamCount = 0;
    private int notSpamCount = 0;

    public NaiveBayesClassifierService() throws IOException {
        LOGGER.info("Start training an algorithm");
        ObjectMapper objectMapper = new ObjectMapper();

        // Укажите путь к вашему JSON файлу
        File jsonFile = new File("src/main/resources/training_sample.json");

        // Чтение JSON файла и преобразование в List<Message>
        List<Message> messages = objectMapper.readValue(jsonFile, new TypeReference<List<Message>>() {
        });
        train(messages);
        LOGGER.info("Training end successfully");
    }

    private void train(List<Message> messages) {
        for (Message message : messages) {
            String[] words = message.getContent().toLowerCase().split("\\W+");
            if (SpamClassifierEnum.SPAM.equals(message.getSpamClassifier())) {
                spamCount++;
                for (String word : words) {
                    spamWordCount.put(word, spamWordCount.getOrDefault(word, 0) + 1);
                }
            } else {
                notSpamCount++;
                for (String word : words) {
                    notSpamWordCount.put(word, notSpamWordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

    }

    public Message classify(String message) {
        LOGGER.info("Got message: " + message + " Start classifying.");
        String[] words = message.trim().toLowerCase().split("\\W+");
        double spamProbability = Math.log(spamCount / (double) (spamCount + notSpamCount));
        double notSpamProbability = Math.log(notSpamCount / (double) (spamCount + notSpamCount));

        for (String word : words) {
            spamProbability += Math.log((spamWordCount.getOrDefault(word, 0) + 1) / (double) (spamCount + 2));
            notSpamProbability += Math.log((notSpamWordCount.getOrDefault(word, 0) + 1) / (double) (notSpamCount + 2));
        }
        if (spamProbability > notSpamProbability) {
            return new Message(message, SpamClassifierEnum.SPAM);
        } else {
            return new Message(message, SpamClassifierEnum.NOT_SPAM);
        }
    }


}
