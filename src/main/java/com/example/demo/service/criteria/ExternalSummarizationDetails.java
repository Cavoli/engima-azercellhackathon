package com.example.demo.service.criteria;

import com.example.demo.model.enumeration.InputFormat;
import com.example.demo.model.enumeration.LimitFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ExternalSummarizationDetails {
    private InputFormat inputFormat;
    private LimitFormat limitFormat;
    private Integer sentenceLimit;
    private Integer percentLimit;
    private String text;
    private String url;
}
