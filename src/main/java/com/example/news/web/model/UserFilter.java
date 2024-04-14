package com.example.news.web.model;

import com.example.news.validation.UserFilterValid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@UserFilterValid
public class UserFilter {

    private Integer pageSize;

    private Integer pageNumber;
}
