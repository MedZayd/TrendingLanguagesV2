package com.example.trending_languages_v2.restController;

import com.example.trending_languages_v2.dto.ItemDto;
import com.example.trending_languages_v2.dto.SearchRepoResponseDto;
import com.example.trending_languages_v2.dto.TrendingLanguageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class TrendingLanguages {

    private static final Logger log = LoggerFactory.getLogger(TrendingLanguages.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/trendingLanguages")
    public ResponseEntity<List<TrendingLanguageDto>> getTrendingLanguages() {
        LocalDate date = LocalDate.now().minusDays(30);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        String formattedDate = formatter.format(date);
        log.info("Requesting trending languages starting @ {} from Github.", formattedDate);

        String url = "https://api.github.com/search/repositories";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("q", "created:>".concat(formattedDate))
                .queryParam("sort", "stars")
                .queryParam("per_page", "100")
                .queryParam("order", "desc");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<SearchRepoResponseDto> response = restTemplate.exchange(
                builder.build().toString(),
                HttpMethod.GET,
                entity,
                SearchRepoResponseDto.class);

        if (response.hasBody()) {
            Map<String, List<ItemDto>> map = Objects.requireNonNull(response.getBody())
                    .getItems()
                    .stream()
                    .filter(itemDto -> itemDto.getLanguage() != null)
                    .collect(Collectors.groupingBy(ItemDto::getLanguage));

            List<TrendingLanguageDto> trendingLanguageList = map.keySet().stream().map(key -> {
                TrendingLanguageDto dto = new TrendingLanguageDto();
                dto.setLanguage(key);
                dto.setOccurrences(map.get(key).size());
                dto.setRepositories(map.get(key).stream().map(ItemDto::getHtmlUrl).collect(Collectors.toList()));
                return dto;
            }).collect(Collectors.toList());

            return new ResponseEntity<>(trendingLanguageList, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
