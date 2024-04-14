package io.github.wilsoncastro.tucrevision.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.wilsoncastro.tucrevision.IntegrationTest;
import io.github.wilsoncastro.tucrevision.core.pojo.CreateUserDTO;
import io.github.wilsoncastro.tucrevision.core.pojo.UpdateUserDTO;
import io.github.wilsoncastro.tucrevision.core.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@IntegrationTest
@Sql(scripts = "classpath:sql/mocked-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class UserControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @AfterEach
    void cleanGroups() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create group when valid data")
    void shouldCreateGroupWhenValidData() throws Exception {
        var createReqMock = new CreateUserDTO(
            "Default name", "Default description", "123");

        var request = post("/api/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(createReqMock));

        ResultActions response = mvc.perform(request);

        response.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should get user by id")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldGetUserById() throws Exception {
        Long userId = 1L; // Assuming this id exists in mocked data
        var request = get("/api/users/{id}", userId);

        ResultActions response = mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    @DisplayName("Should update user")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldUpdateUser() throws Exception {
        Long userId = 1L; // Assuming this id exists in mocked data
        var updateReqMock = new UpdateUserDTO("Updated name", "updated@example.com", "updatedPassword");

        var request = put("/api/users/{id}", userId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateReqMock));

        ResultActions response = mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateReqMock.name()))
                .andExpect(jsonPath("$.email").value(updateReqMock.email()));
    }

    @Test
    @DisplayName("Should delete user")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldDeleteUser() throws Exception {
        Long userId = 2L; // Assuming this id exists in mocked data
        var request = delete("/api/users/{id}", userId);

        ResultActions response = mvc.perform(request);

        response.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should get all users")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldGetAllUsers() throws Exception {
        var request = get("/api/users")
                .param("page", "0")
                .param("size", "10");

        ResultActions response = mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    @DisplayName("Should check if user exists by email")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldCheckIfUserExistsByEmail() throws Exception {
        String email = "email"; // Assuming this email exists in mocked data
        var request = get("/api/users/exists/{email}", email);

        ResultActions response = mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("Should check if user exists by email ignoring id")
    @WithUserDetails(value = "email", userDetailsServiceBeanName = "userDetailsServiceImpl")
    void shouldCheckIfUserExistsByEmailIgnoringId() throws Exception {
        String email = "email"; // Assuming this email exists in mocked data
        Long id = 1L; // Assuming this id exists in mocked data
        var request = get("/api/users/exists/{email}?id={id}", email, id);

        ResultActions response = mvc.perform(request);

        response.andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

}