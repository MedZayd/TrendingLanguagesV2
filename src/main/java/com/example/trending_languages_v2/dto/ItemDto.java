package com.example.trending_languages_v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDto {
    private long id;
    private String language;
    private String htmlUrl;
}
