package com.example.news.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentNewsResponse {

    private Long id;

    private String comment;

    private String user;

    private Long newsId;
}
