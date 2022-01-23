package com.example.demo.controller;

import com.example.demo.model.enumeration.LimitFormat;
import com.example.demo.service.SummariseService;
import com.example.demo.service.criteria.ExternalSummarizationDetails;
import com.example.demo.service.criteria.SummarizationCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/summarize")
public class SummarizationController {

    private final SummariseService service;

    public SummarizationController(SummariseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> summariseText(@RequestBody SummarizationCriteria criteria) {
        String result = service.summariseText(criteria);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/external")
    public ResponseEntity<String> summariseText(@RequestBody ExternalSummarizationDetails details) throws IOException {
        String result = service.externalSummarization(details);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping(value = "/external/file", consumes = {"multipart/form-data"})
    public ResponseEntity<String> summariseTextFromFile(@RequestParam("file") MultipartFile multipartFile,
                                                        @RequestParam("limit") LimitFormat limit,
                                                        @RequestParam("size") Integer size
    ) throws IOException {
        String result = service.externalSummarizationOfFile(multipartFile, limit, size);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/char-count")
    public int countChars(@RequestBody String text) {
        return text.toCharArray().length;
    }

}
