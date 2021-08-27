package com.example.trending_languages_v2.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRepoResponseDto {
    private List<ItemDto> items;
}
