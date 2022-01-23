package com.example.demo.algorithm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Paragraph {

    private int number;
    private ArrayList<Sentence> sentences;

    public Paragraph(int number) {
        this.number = number;
        sentences = new ArrayList<Sentence>();
    }
}