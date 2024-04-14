package com.example.news.web.model;

import com.example.news.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String password;

    private List<RoleType> roles = new ArrayList<>();

    private List<NewsResponse> listNews = new ArrayList<>();

    private List<CommentNewsResponse> listComments = new ArrayList<>();
}
