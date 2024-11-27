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

    // Вычитка из файла и преобразование в массив объектов
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

    // Обучение на основе выборки
    // На вход получает выборку в формате коллекции объектов
    private void train(List<Message> messages) {
        for (Message message : messages) {
            String[] words = message.getContent().toLowerCase().split("\\W+");
            if (SpamClassifierEnum.SPAM.equals(message.getSpamClassifier())) {
                spamCount++;
                for (String word : words) {
                    spamWordCount.put(word, spamWordCount.getOrDefault(word, 0) + 1); // количество вхождений слова в спам или не спам
                }
            } else {
                notSpamCount++;
                for (String word : words) {
                    notSpamWordCount.put(word, notSpamWordCount.getOrDefault(word, 0) + 1);
                }
            }
        }

    }

    // Метод классификации
    public Message classify(String message) {
        LOGGER.info("Got message '" + message + "' Start classifying.");
        String[] words = message.trim().toLowerCase().split("\\W+");//удаление лишних пробелов, перевод в нижний регистр, разделение слов
        double spamProbability = Math.log(spamCount / (double) (spamCount + notSpamCount)); //вероятность спама на основе выборки
        double notSpamProbability = Math.log(notSpamCount / (double) (spamCount + notSpamCount));// вероятность неспама на основе выборки

        for (String word : words) {
            spamProbability += Math.log((spamWordCount.getOrDefault(word, 0) + 1) / (double) (spamCount + 2));
            notSpamProbability += Math.log((notSpamWordCount.getOrDefault(word, 0) + 1) / (double) (notSpamCount + 2));
        }
        LOGGER.info("Relative spam probability = " + spamProbability + " , relative not spam probability = " + notSpamProbability);
        if (spamProbability > notSpamProbability) {
            LOGGER.info("Message '" + message + "' classified as SPAM");
            return new Message(message, SpamClassifierEnum.SPAM);
        } else {
            LOGGER.info("Message  '" + message + "'  classified as NOT_SPAM ");
            return new Message(message, SpamClassifierEnum.NOT_SPAM);
        }
    }


}
