package com.github.marinkay;

import java.util.HashMap;
import java.util.Map;

public class NaiveBayesClassifier {
    private Map<String, Integer> spamWordCount = new HashMap<>();
    private Map<String, Integer> notSpamWordCount = new HashMap<>();
    private int spamCount = 0;
    private int notSpamCount = 0;

    public NaiveBayesClassifier() {
        // Тренировочная выборка
        train("Hi, how are you?", "Not spam");
        train("Congratulations, you won a prize!", "Spam");
        train("Buy the product now and get a discount!", "Spam");
        train("Let's walk this evening", "Not spam");
    }

    private void train(String message, String label) {
        String[] words = message.toLowerCase().split("\\W+");
        if (label.equals("Spam")) {
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

    public String classify(String message) {
        String[] words = message.toLowerCase().split("\\W+");
        double spamProbability = Math.log(spamCount / (double) (spamCount + notSpamCount));
        double notSpamProbability = Math.log(notSpamCount / (double) (spamCount + notSpamCount));

        for (String word : words) {
            spamProbability += Math.log((spamWordCount.getOrDefault(word, 0) + 1) / (double) (spamCount + 2));
            notSpamProbability += Math.log((notSpamWordCount.getOrDefault(word, 0) + 1) / (double) (notSpamCount + 2));
        }

        return spamProbability > notSpamProbability ? "Spam" : "Not spam";
    }

    public static void main(String[] args) {
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();

        String testMessage = "Hi, you won a discount and you can get the prize this evening.";
        String result = classifier.classify(testMessage);

        System.out.println("The message is classified as: " + result);
    }
}
