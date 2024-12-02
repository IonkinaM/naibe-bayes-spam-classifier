package com.github.marinkay.service;

import com.github.marinkay.FMeasureMain;
import com.github.marinkay.dto.SpamClassifierEnum;

public class FMeasureCalculator {
    private int truePositive = 0;
    private int falsePositive = 0;
    private int falseNegative = 0;
    private int trueNegative = 0;

    public void addResult(SpamClassifierEnum predicted, SpamClassifierEnum actual) {
        if (predicted == SpamClassifierEnum.SPAM && actual == SpamClassifierEnum.SPAM) {
            truePositive++;
        } else if (predicted == SpamClassifierEnum.SPAM && actual == SpamClassifierEnum.NOT_SPAM) {
            falsePositive++;
        } else if (predicted == SpamClassifierEnum.NOT_SPAM && actual == SpamClassifierEnum.SPAM) {
            falseNegative++;
        } else if (predicted == SpamClassifierEnum.NOT_SPAM && actual == SpamClassifierEnum.NOT_SPAM) {
            trueNegative++;
        }
    }

    public double calculateFMeasure() {
        double precision = (truePositive + falsePositive) > 0 ? (double) truePositive / (truePositive + falsePositive) : 0;
        System.out.println("Precision - " + precision);
        double recall = (truePositive + falseNegative) > 0 ? (double) truePositive / (truePositive + falseNegative) : 0;
        System.out.println("Recall - " + recall);
        double fallout = (falsePositive + trueNegative) > 0 ? (double) falsePositive / (falsePositive + trueNegative) : 0;
        System.out.println("Fallout - " + fallout);
        return (precision + recall > 0) ? 2 * (precision * recall) / (precision + recall) : 0;
    }
}
