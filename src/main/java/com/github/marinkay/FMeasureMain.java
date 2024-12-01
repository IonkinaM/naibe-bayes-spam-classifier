package com.github.marinkay;

import com.github.marinkay.dto.Message;
import com.github.marinkay.dto.SpamClassifierEnum;
import com.github.marinkay.service.FMeasureCalculator;
import com.github.marinkay.service.NaiveBayesClassifierService;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FMeasureMain {
    private static final Logger LOGGER = Logger.getLogger(FMeasureMain.class.getName());


    @SneakyThrows
    public static void main(String[] args) {
        NaiveBayesClassifierService classifier = new NaiveBayesClassifierService();
        FMeasureCalculator fMeasureCalculator = new FMeasureCalculator();

        // Пример входных сообщений с реальными метками
        List<Message> testMessages = new ArrayList<>();
        testMessages.add(new Message("Congratulations! You've won a prize!", SpamClassifierEnum.SPAM));
        testMessages.add(new Message("Your invoice is attached.", SpamClassifierEnum.NOT_SPAM)); // Сообщение которое не СПАМ, но алгоритм определяет как СПАМ из-за слабой выборки
        testMessages.add(new Message("Get rich quick schemes!", SpamClassifierEnum.SPAM));
        testMessages.add(new Message("Meeting at noon tomorrow.", SpamClassifierEnum.NOT_SPAM));// Сообщение которое не СПАМ, но алгоритм определяет как СПАМ из-за слабой выборки
        testMessages.add(new Message("Limited time offer just for you!", SpamClassifierEnum.SPAM));
        testMessages.add(new Message("You parked car badly!", SpamClassifierEnum.NOT_SPAM));
        testMessages.add(new Message("Go throw this link and get the money!", SpamClassifierEnum.SPAM));

        // Классификация и подсчет результатов
        for (Message message : testMessages) {
            Message predicted = classifier.classify(message.getContent());
            fMeasureCalculator.addResult(predicted.getSpamClassifier(), message.getSpamClassifier());
            LOGGER.info("Message: " + predicted.getContent() + ", Predicted: " + predicted.getSpamClassifier() + ", Actual: " + message.getSpamClassifier());
        }

        // Вычисление F-measure
        double fMeasure = fMeasureCalculator.calculateFMeasure();
        System.out.printf("F-measure: %.2f%n", fMeasure);
    }


}
