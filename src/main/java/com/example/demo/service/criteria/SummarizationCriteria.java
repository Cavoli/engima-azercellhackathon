package com.example.demo.service.criteria;

import com.example.demo.model.enumeration.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummarizationCriteria {

    private Type type;
    private Integer percent;
    private String word;
    private String text;
}
