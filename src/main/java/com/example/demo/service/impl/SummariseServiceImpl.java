package com.example.demo.service.impl;

import com.example.demo.algorithm.Algorithm;
import com.example.demo.model.enumeration.LimitFormat;
import com.example.demo.model.enumeration.Type;
import com.example.demo.service.SummariseService;
import com.example.demo.service.criteria.ExternalSummarizationDetails;
import com.example.demo.service.criteria.SummarizationCriteria;
import com.example.demo.util.PdfUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class SummariseServiceImpl implements SummariseService {

    private final Algorithm algorithm;
    private final RestTemplate restTemplate;

    public SummariseServiceImpl(Algorithm algorithm, RestTemplate restTemplate) {
        this.algorithm = algorithm;
        this.restTemplate = restTemplate;
    }

    @Override
    public String summariseText(SummarizationCriteria criteria) {
        String s = "";
        if (criteria.getType().equals(Type.PERCENT)) {
            s = summariseBasedOnPercent(criteria.getText(), criteria.getPercent());
        } else {
            s = summariseBasedOnWord(criteria.getText(), criteria.getPercent(), criteria.getWord());
        }
        return s;
    }

    @Override
    public String externalSummarization(ExternalSummarizationDetails details) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.set("key", "a557ed5ba1938e7eb2c933807419f9ac");
        map.set("of", "json");
        map.set("lang", "auto");
        switch (details.getInputFormat()) {
            case URL -> map.set("url", details.getUrl());
            case TXT -> map.set("txt", details.getText());
        }
        switch (details.getLimitFormat()) {
            case SENTENCE -> map.set("sentences", details.getSentenceLimit().toString());
            case LIMIT -> map.set("limit", details.getPercentLimit().toString());
        }
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        String response = restTemplate.postForEntity("https://api.meaningcloud.com/summarization-1.0", request, String.class).getBody();
        return response;
    }

    @Override
    public String externalSummarizationOfFile(MultipartFile multipartFile, LimitFormat limit, Integer size) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        map.set("key", "a557ed5ba1938e7eb2c933807419f9ac");
        map.set("of", "json");
        map.set("lang", "auto");
        map.set("txt", PdfUtils.PdfToString(multipartFile));
        switch (limit) {
            case SENTENCE -> map.set("sentences", size);
            case LIMIT -> map.set("limit", size);
        }

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
        String response = restTemplate.postForEntity("https://api.meaningcloud.com/summarization-1.0", request, String.class).getBody();
        return response;
    }

    private String summariseBasedOnPercent(String text, Integer percent) {
        if (!text.isEmpty()) {
            try {
                File file = new File("context.txt");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(text);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            algorithm.init();
            algorithm.extractSentenceFromContext();
            algorithm.groupSentencesIntoParagraphs();
            algorithm.createIntersectionMatrix();
            algorithm.createDictionary();
            algorithm.createSummary(algorithm.calculatePercentageLevel(percent));
            algorithm.printSummary();
        }
        return algorithm.getFinalSummery();
    }


    private String summariseBasedOnWord(String text, Integer percent, String word) {
        if (!text.isEmpty()) {
            try {
                File file = new File("context.txt");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(text);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            algorithm.init();
            algorithm.extractSentenceFromContext();
            algorithm.groupSentencesIntoParagraphs();
            algorithm.createnoOfKeyWordsArray(word);
            algorithm.createIntersectionMatrixBaseOnKeyWords();
            algorithm.createDictionaryBaseOnKeyWords();
            algorithm.createSummaryBaseOnKeyWords(algorithm.calculatePercentageLevel(percent));
        }
        return algorithm.getFinalSummeryBaseOnKeyWord();
    }

}
