package com.example.trending_languages_v2.dto;

import java.util.List;

public class SearchRepoResponseDto {
    private List<ItemDto> items;

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }
}
