package com.example.demo.algorithm.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Sentence {
    private int paragraphNumber;
    private int number;
    private int stringLength;
    private double score;
    private int noOfWords;
    public String value;

    public Sentence(int number, String value, int stringLength, int paragraphNumber) {
        this.number = number;
        this.value = new String(value);
        this.stringLength = stringLength;
        noOfWords = value.split("\\s+").length;
        score = 0.0;
        this.paragraphNumber = paragraphNumber;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "paragraphNumber=" + paragraphNumber +
                ", number=" + number +
                ", stringLength=" + stringLength +
                ", score=" + score +
                ", noOfWords=" + noOfWords +
                ", value='" + value + '\'' +
                '}';
    }
}