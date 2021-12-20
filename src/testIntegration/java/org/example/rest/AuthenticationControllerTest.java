package org.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.AuthenticationRequestDto;
import org.example.repository.UsersRepository;
import org.example.security.entity.User;
import org.example.security.enums.Role;
import org.example.security.enums.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Value(("${api-base-url}"))
    private String apiBaseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsersRepository usersRepository;

    @Before
    public void resetDB() {
        usersRepository.deleteAll();
        User admin = new User(1L, "admin@mail.ru", "admin", "adminov", "$2a$12$yHDN5IIfwJ2vBDhPEs9gLO5dbZ826Vfav9rfVgR5AhNRaYxt0KwU2", Role.ADMIN, UserStatus.ACTIVE);
        User user = new User(2L, "user@mail.ru", "user", "userov", "$2a$12$i5NYGX5DdUtmycBqG.kRneEkB/rG3Rzn3vk57GwJ/i3ZH9WobrGnu", Role.USER, UserStatus.ACTIVE);
        usersRepository.save(admin);
        usersRepository.save(user);
    }

    @Test
    public void authenticateAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(apiBaseUrl + "/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthenticationRequestDto("admin@mail.ru","admin"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void authenticateUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(apiBaseUrl + "/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AuthenticationRequestDto("user@mail.ru","user"))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void logout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(apiBaseUrl + "/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
