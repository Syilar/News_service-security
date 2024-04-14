package com.example.news.mapper.v2;

import com.example.news.model.User;
import com.example.news.web.model.UpsertUserRequest;
import com.example.news.web.model.UserResponse;
import com.example.news.web.model.UserResponseCountComments;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class UserMapperDelegate implements UserMapperV2 {

    @Autowired
    private NewsMapperV2 newsMapperV2;

    @Autowired
    private CommentNewsMapperV2 commentNewsMapperV2;

    @Override
    public User requestToUser(UpsertUserRequest request) {
        User user = new User();

        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRoles(roleTypeListToRoleList(request.getRoles()));
        return user;
    }

    @Override
    public UserResponseCountComments userToResponseCountComment(User user) {
        UserResponseCountComments userResponseCountComments = new UserResponseCountComments();

        userResponseCountComments.setId(user.getId());
        userResponseCountComments.setName(user.getName());
        userResponseCountComments.setSurname(user.getSurname());
        userResponseCountComments.setEmail(user.getEmail());
        userResponseCountComments.setPassword(user.getPassword());
        userResponseCountComments.setRoles(roleListToRoleTypeList(user.getRoles()));
        userResponseCountComments.setListNews(newsMapperV2.newsListToResponseListCountComments(user.getListNews()));
        userResponseCountComments.setListComments(commentNewsMapperV2.commentNewsListToResponseList(user.getListComments()));
        return userResponseCountComments;
    }

    @Override
    public UserResponse userToResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        userResponse.setSurname(user.getSurname());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setRoles(roleListToRoleTypeList(user.getRoles()));
        userResponse.setListNews(newsMapperV2.newsListToResponseList(user.getListNews()));
        userResponse.setListComments(commentNewsMapperV2.commentNewsListToResponseList(user.getListComments()));
        return userResponse;
    }
}
