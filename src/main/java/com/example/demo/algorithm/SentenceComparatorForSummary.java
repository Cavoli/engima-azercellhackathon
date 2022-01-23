package com.example.demo.algorithm;

import com.example.demo.algorithm.entity.Sentence;

import java.util.Comparator;

class SentenceComparatorForSummary implements Comparator<Sentence> {

    @Override
    public int compare(Sentence obj1, Sentence obj2) {
        if (obj1.getNumber() > obj2.getNumber()) {
            return 1;
        } else if (obj1.getNumber() < obj2.getNumber()) {
            return -1;
        } else {
            return 0;
        }
    }
}