package com.github.marinkay;

import com.github.marinkay.dto.Message;
import com.github.marinkay.service.NaiveBayesClassifierService;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {
        // создание объекта класса байесовского классификатора
        NaiveBayesClassifierService classifier = new NaiveBayesClassifierService();

        String testMessage = "Hi, you won a prize 100$ for free and you can get the prize when you need. Just go through this link.";
        Message result = classifier.classify(testMessage);

        String testMessageSecond = "Hi, you parked in front of my car, please go out.";
        Message resultSecond = classifier.classify(testMessageSecond);
    }
}
