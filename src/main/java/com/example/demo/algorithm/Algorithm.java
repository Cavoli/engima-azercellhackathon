package com.example.demo.algorithm;

import com.example.demo.algorithm.entity.Paragraph;
import com.example.demo.algorithm.entity.Sentence;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

@Getter
@Setter
@Component
public class Algorithm {

    private FileInputStream in;
    private FileOutputStream out;

    private ArrayList<Sentence> sentences;

    private int noOfSentences;

    private ArrayList<Paragraph> paragraphs;

    private int noOfParagraphs;

    private ArrayList<Sentence> contentSummary;

    private String finalSummery;

    private int[] noOfKeyWords;

    private ArrayList<Sentence> sentencesWithKeyWords;

    private ArrayList<Sentence> contentSummaryBaseOnKeyWord;

    private String finalSummeryBaseOnKeyWord;

    private double[][] intersectionMatrix;

    private LinkedHashMap<Sentence, Double> dictionary;

    private double Commpression;

    public Algorithm() {
        this.in = null;
        this.out = null;
        this.noOfSentences = 0;
        this.noOfParagraphs = 0;

        try {

        } catch (Exception e) {

        }

    }

    public void init() {
        setSentences(new ArrayList<Sentence>());
        setSentencesWithKeyWords(new ArrayList<Sentence>());
        setParagraphs(new ArrayList<Paragraph>());
        setContentSummary(new ArrayList<Sentence>());
        setContentSummaryBaseOnKeyWord(new ArrayList<Sentence>());
        setDictionary(new LinkedHashMap<Sentence, Double>());
        setNoOfSentences(0);
        setNoOfParagraphs(0);
        try {
            setIn(new FileInputStream("context.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void extractSentenceFromContext() {
        int nextChar, j = 0;
        int prevChar = -1;
        try {
            while ((nextChar = getIn().read()) != -1) {
                j = 0;
                char[] temp = new char[100000];
                while ((char) nextChar != '.') {
                    temp[j] = (char) nextChar;

                    if ((nextChar = getIn().read()) == -1) {
                        break;
                    }
                    if ((char) nextChar == '\n' && (char) prevChar == '\n') {
                        setNoOfParagraphs(getNoOfParagraphs() + 1);
                    }

                    j++;
                    prevChar = nextChar;
                }
                getSentences().add(new Sentence(getNoOfSentences(), (new String(temp)).trim(), (new String(temp)).trim().length(), getNoOfParagraphs()));//here noOfSentences=sentence No
                setNoOfSentences(getNoOfSentences() + 1);
                prevChar = nextChar;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void groupSentencesIntoParagraphs() {
        int paraNum = 0;
        Paragraph paragraph = new Paragraph(0);
        for (int i = 0; i < getNoOfSentences(); i++) {
            if (getSentences().get(i).getParagraphNumber() == paraNum) {

                //continue
            } else {
                getParagraphs().add(paragraph);
                paraNum++;
                paragraph = new Paragraph(paraNum);

            }

            paragraph.getSentences().add(getSentences().get(i));//if selected sentence paragraph number similar to paraNum insert that selected sentence in to that paragraph sentnce array list.

        }

        getParagraphs().add(paragraph);

    }

    public double noOfCommonWords(Sentence str1, Sentence str2) {
        double commonCount = 0;

        for (String str1Word : str1.getValue().split("\\s+")) {

            for (String str2Word : str2.getValue().split("\\s+")) {

                if (str1Word.compareToIgnoreCase(str2Word) == 0) {
                    commonCount++;
                } else {
                }
            }
        }
        return commonCount;
    }

    public void createIntersectionMatrix() {
        setIntersectionMatrix(new double[getNoOfSentences()][getNoOfSentences()]);//arr[i][j]
        for (int i = 0; i < getNoOfSentences(); i++) {
            for (int j = 0; j < getNoOfSentences(); j++) {

                if (i <= j) {
                    Sentence str1 = getSentences().get(i);
                    Sentence str2 = getSentences().get(j);
                    getIntersectionMatrix()[i][j] = noOfCommonWords(str1, str2) / ((double) (str1.getNoOfWords() + str2.getNoOfWords()) / 2);
                } else {
                    getIntersectionMatrix()[i][j] = getIntersectionMatrix()[j][i];
                }

            }
        }

    }

    public int noOfKeyWords(Sentence str1, String keyWord) {
        int keyWordCount = 0;

        for (String str1Word : str1.getValue().split("\\s+")) {

            if (str1Word.compareToIgnoreCase(keyWord) == 0) {
                keyWordCount++;
            }

        }

        return keyWordCount;
    }

    public void createnoOfKeyWordsArray(String keyWord) {
        setNoOfKeyWords(new int[getNoOfSentences()]);
        for (int i = 0; i < getNoOfSentences(); i++) {

            Sentence str1 = getSentences().get(i);
            getNoOfKeyWords()[i] = noOfKeyWords(str1, keyWord);
            if (getNoOfKeyWords()[i] > 0) {
                getSentencesWithKeyWords().add(str1);

            }
        }
        System.out.println(getSentencesWithKeyWords().toString());
    }

    public void createIntersectionMatrixBaseOnKeyWords() {
        setIntersectionMatrix(new double[getSentencesWithKeyWords().size()][getSentencesWithKeyWords().size()]);//arr[i][j]
        for (int i = 0; i < getSentencesWithKeyWords().size(); i++) {
            for (int j = 0; j < getSentencesWithKeyWords().size(); j++) {

                if (i <= j) {
                    Sentence str1 = getSentences().get(i);
                    Sentence str2 = getSentences().get(j);
                    getIntersectionMatrix()[i][j] = noOfCommonWords(str1, str2) / ((double) (str1.getNoOfWords() + str2.getNoOfWords()) / 2);
                } else {
                    getIntersectionMatrix()[i][j] = getIntersectionMatrix()[j][i];
                }

            }
        }

    }
    public void createDictionary() {
        for (int i = 0; i < getNoOfSentences(); i++) {
            double score = 0.0;
            if (sentences.get(i).getNumber()<=getNoOfSentences()/2 ) {

                score += 1.0/sentences.get(i).getNumber();
            }
            for (int j = 0; j < getNoOfSentences(); j++) {
                score += getIntersectionMatrix()[i][j];//score=intersectionMatrix[i][j]+score;


            }
            getDictionary().put(getSentences().get(i), score);
            sentences.get(i).setScore(score);
        }
    }



    public void createDictionaryBaseOnKeyWords() {
        for (int i = 0; i < getSentencesWithKeyWords().size(); i++) {
            double score = 0;
            for (int j = 0; j < getSentencesWithKeyWords().size(); j++) {
                score += getIntersectionMatrix()[i][j];

            }

            getDictionary().put(getSentencesWithKeyWords().get(i), score);
            getSentencesWithKeyWords().get(i).setScore(score);
        }
    }

    public void createSummary(int summeryLevel) {
        for (int j = 0; j <= getNoOfParagraphs(); j++) {
            int primary_set = getParagraphs().get(j).getSentences().size() / summeryLevel;
            System.out.println(primary_set);
            Collections.sort(getParagraphs().get(j).getSentences(), new SentenceComparatorOnScore());
            for (int i = 1; i <= primary_set; i++) {
                getContentSummary().add(getParagraphs().get(j).getSentences().get(i));

            }
        }

        Collections.sort(getContentSummary(), new SentenceComparatorForSummary());//sort here according to the sentence no(SentenceComparatorForSummary has a compare methode to comapare and collection has sort method to sort)

    }


    public void createSummaryBaseOnKeyWords(int summeryLevel) {
        int primary_set = getSentencesWithKeyWords().size() / summeryLevel;//find no of group of 5 sentences.ex:if has 17 sentences in a paragraph we select  4 group of sentences for summery
        getSentencesWithKeyWords().sort(new SentenceComparatorOnScore());//sort here according to the score of a sentence(SentenceComparatorOnScore has a compare to do this)
        for (int i = 0; i <= primary_set; i++) {//from a one group select 2 sentences
            getContentSummaryBaseOnKeyWord().add(getSentencesWithKeyWords().get(i));
        }
        Collections.sort(getContentSummaryBaseOnKeyWord(), new SentenceComparatorForSummary());//sort here according to the sentence no(SentenceComparatorForSummary has a compare methode to do this)

    }

    public void printSummary() {
        StringBuilder sb = new StringBuilder();
        for (Sentence sentence : getContentSummary()) {
            sb.append(sentence.getValue());
            sb.append("\n");

        }
        setFinalSummery(sb.toString());
    }


    public void printSummaryBaseOnKeyWords() {

        StringBuilder sb = new StringBuilder();
        for (Sentence sentence : getContentSummaryBaseOnKeyWord()) {
            sb.append(sentence.getValue());
            sb.append("\n");

        }
        setFinalSummeryBaseOnKeyWord(sb.toString());
    }

    public double getWordCount(ArrayList<Sentence> sentenceList) {
        double wordCount = 0.0;
        for (Sentence sentence : sentenceList) {
            wordCount += sentence.getNoOfWords();
        }
        return wordCount;
    }

    public int calculatePercentageLevel(int currentPercentageLevel) {
        return currentPercentageLevel / 10;
    }
}
