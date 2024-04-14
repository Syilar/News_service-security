package com.example.news.controller.v1;

import com.example.news.AbstractTestController;
import com.example.news.model.RoleType;
import com.example.news.web.model.*;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;

public class UserControllerTest extends AbstractTestController {

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void whenFindAll_thenReturnAllUsers() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/v2/user?pageNumber=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserFilter filter = new UserFilter(10, 0);
        String expectedResponse = objectMapper.writeValueAsString(
                userMapper.userListToUserListResponse(userService.findAll(filter)));

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "User",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenGetUserById_thenReturnUserById() throws Exception {
        String actualResponse = mockMvc.perform(get("/api/v2/user/{id}", USER_ID_WITH_USER_ROLE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(
                userMapper.userToResponse(userService.findById(USER_ID_WITH_USER_ROLE)));

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }

    @Test
    public void whenCreateUser_thenReturnNewUser() throws Exception {
        assertEquals(2, userRepository.count());

        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("new User");
        request.setSurname("new Surname");
        request.setEmail("new@gmail.com");
        request.setPassword("12345");
        request.setRoles(Collections.singletonList(RoleType.ROLE_USER));

        String actualResponse = mockMvc.perform(post("/api/v2/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(
                new UserResponse(3L, "new User", "new Surname", "new@gmail.com","12345",
                        Collections.singletonList(RoleType.ROLE_USER), new ArrayList<>(), new ArrayList<>()));

                assertEquals(3, userRepository.count());
                JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("password"));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "User",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        UpsertUserRequest request = new UpsertUserRequest();
        request.setName("update User");
        request.setSurname("update Surname");
        request.setPassword("321");
        request.setRoles(Collections.singletonList(RoleType.ROLE_USER));

        String actualResponse = mockMvc.perform(put("/api/v2/user/{id}", USER_ID_WITH_USER_ROLE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(
                new UserResponse(2L, "update User", "update Surname", "u@gmail.com","321",
                        Collections.singletonList(RoleType.ROLE_USER), new ArrayList<>(), new ArrayList<>()));

        assertEquals(2, userRepository.count());
        JsonAssert.assertJsonEquals(expectedResponse, actualResponse, JsonAssert.whenIgnoringPaths("password"));
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "Admin",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenDeleteClientById_thenReturnStatusNoContent() throws Exception {
        assertEquals(2, userRepository.count());
        mockMvc.perform(delete("/api/v2/user/{id}", USER_ID_WITH_USER_ROLE))
                .andExpect(status().isNoContent());

        assertEquals(1, userRepository.count());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "User",
            setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void whenGetUserWithNotCorrectRole_thenReturnNok() throws Exception{
        var response = mockMvc.perform(get("/api/v2/user/{id}", ADMIN_ID_WITH_ADMIN_ROLE))
                .andExpect(status().isForbidden())
                .andReturn()
                .getResponse();

        response.setCharacterEncoding("UTF-8");
        String actualResponse = response.getContentAsString();
        String expectedResponse = objectMapper.writeValueAsString(new ErrorResponse(
                "Вы не можете просматривать, изменять, удалять информацию о другом пользователе!"));

        JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
    }
}
