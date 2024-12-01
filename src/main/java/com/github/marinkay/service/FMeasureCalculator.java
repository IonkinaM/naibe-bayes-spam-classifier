package com.github.marinkay.service;

import com.github.marinkay.FMeasureMain;
import com.github.marinkay.dto.SpamClassifierEnum;

public class FMeasureCalculator {
    private int truePositive = 0;
    private int falsePositive = 0;
    private int falseNegative = 0;

    public void addResult(SpamClassifierEnum predicted, SpamClassifierEnum actual) {
        if (predicted == SpamClassifierEnum.SPAM && actual == SpamClassifierEnum.SPAM) {
            truePositive++;
        } else if (predicted == SpamClassifierEnum.SPAM && actual == SpamClassifierEnum.NOT_SPAM) {
            falsePositive++;
        } else if (predicted == SpamClassifierEnum.NOT_SPAM && actual == SpamClassifierEnum.SPAM) {
            falseNegative++;
        }
    }

    public double calculateFMeasure() {
        double precision = (truePositive + falsePositive) > 0 ? (double) truePositive / (truePositive + falsePositive) : 0;
        double recall = (truePositive + falseNegative) > 0 ? (double) truePositive / (truePositive + falseNegative) : 0;

        return (precision + recall > 0) ? 2 * (precision * recall) / (precision + recall) : 0;
    }
}
