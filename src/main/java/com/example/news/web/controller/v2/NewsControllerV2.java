package com.example.news.web.controller.v2;

import com.example.news.aop.CheckOwner;
import com.example.news.mapper.v2.NewsMapperV2;
import com.example.news.model.News;
import com.example.news.service.NewsService;
import com.example.news.web.model.NewsFilter;
import com.example.news.web.model.NewsListResponse;
import com.example.news.web.model.NewsResponse;
import com.example.news.web.model.UpsertNewsRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/news")
@RequiredArgsConstructor
public class NewsControllerV2 {

    private final NewsService databaseNewsService;

    private final NewsMapperV2 newsMapper;

    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(@Valid NewsFilter filter) {
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponse(databaseNewsService.filterBy(filter)));
    }

    @GetMapping
    public ResponseEntity<NewsListResponse> findAll(@Valid NewsFilter filter) {
        return ResponseEntity.ok(newsMapper.newsListToNewsListResponse(databaseNewsService.findAll(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(newsMapper.newsToResponse(databaseNewsService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<NewsResponse> create(@AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid UpsertNewsRequest request) {
        News news = databaseNewsService.createNews(newsMapper.requestToNews(request), userDetails.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(newsMapper.newsToResponse(news));
    }

    @PutMapping("/{id}")
    @CheckOwner
    public ResponseEntity<NewsResponse> update(@PathVariable("id") Long newsId, @RequestBody @Valid UpsertNewsRequest request) {
        News updatedNews = databaseNewsService.updateNews(newsMapper.requestToNews(newsId, request));

        return ResponseEntity.ok(newsMapper.newsToResponse(updatedNews));
    }

    @DeleteMapping("/{id}")
    @CheckOwner
    public ResponseEntity<NewsResponse> deleteById(@PathVariable Long id) {
        databaseNewsService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
