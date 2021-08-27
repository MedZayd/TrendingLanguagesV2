package com.example.trending_languages_v2.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrendingLanguageDto {
    private String language;
    private int occurrences;
    private List<String> repositories;
}
