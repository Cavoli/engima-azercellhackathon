package com.example.demo.service;

import com.example.demo.model.enumeration.LimitFormat;
import com.example.demo.service.criteria.ExternalSummarizationDetails;
import com.example.demo.service.criteria.SummarizationCriteria;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SummariseService {

    String summariseText(SummarizationCriteria criteria);

    String externalSummarization(ExternalSummarizationDetails details) throws IOException;

    String externalSummarizationOfFile(MultipartFile multipartFile, LimitFormat limit, Integer size) throws IOException;
}
