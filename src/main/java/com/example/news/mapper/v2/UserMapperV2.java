package com.example.news.mapper.v2;

import com.example.news.model.Role;
import com.example.news.model.RoleType;
import com.example.news.model.User;
import com.example.news.web.model.UpsertUserRequest;
import com.example.news.web.model.UserListResponse;
import com.example.news.web.model.UserResponse;
import com.example.news.web.model.UserResponseCountComments;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {NewsMapperV2.class,
        CommentNewsMapperV2.class})
public interface UserMapperV2 {

    User requestToUser(UpsertUserRequest request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, UpsertUserRequest request);

    UserResponse userToResponse(User user);

    UserResponseCountComments userToResponseCountComment(User user);

    default List<Role> roleTypeListToRoleList(List<RoleType> rolesType) {
        List<Role> roles = new ArrayList<>(rolesType.size());
        for (RoleType roleType : rolesType) {
            roles.add(roleTypeToRole(roleType));
        }
        return roles;
    }

    default Role roleTypeToRole(RoleType roleType) {
        Role role = new Role();
        role.setAuthority(roleType);

        return role;
    }

    default List<RoleType> roleListToRoleTypeList(List<Role> roles) {
        List<RoleType> rolesType = new ArrayList<>(roles.size());
        for (Role role : roles) {
            rolesType.add(roleToRoleType(role));
        }
        return rolesType;
    }

    default RoleType roleToRoleType(Role role) {
        return role.getAuthority();
    }

   default UserListResponse userListToUserListResponse(List<User> users) {
       UserListResponse userListResponse = new UserListResponse();

       userListResponse.setUsers(users.stream()
       .map(this::userToResponseCountComment)
       .collect(Collectors.toList()));

       return userListResponse;
   }
}
