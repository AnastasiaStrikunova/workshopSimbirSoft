package org.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.App;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.junit.Assert;
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
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = App.class)
@AutoConfigureMockMvc
public class UserControllerTest {
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLnJ1Iiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNjM5NzQ2MzI2LCJleHAiOjE2NDAzNTExMjZ9.Hce1WWfRslrQie9ndWAe1VRAf1Qj0DeV-XqLaJESUSQ";

    @Value(("${api-base-url}"))
    private String apiBaseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private Long idStartUser;
    @Before
    public void resetDB() {
        userRepository.deleteAll();
        idStartUser = userRepository.save(new UserEntity(1L, "name1", "priority1")).getIdUser();
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/user")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/user/" + idStartUser)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void add() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post(apiBaseUrl + "/user")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new UserRequestDto("name2", "priority2"))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
        Assert.assertTrue(userRepository.findById(idStartUser + 1).isPresent());
    }

    @Test
    public void change() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.put(apiBaseUrl + "/user/" + idStartUser.toString())
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new UserRequestDto("new_name", null))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("new_name"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
        Assert.assertEquals(objectMapper.writeValueAsString(new UserResponseDto(idStartUser, "new_name", "priority1")), body);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(userRepository.findById(idStartUser).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(apiBaseUrl + "/user/" + idStartUser)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.assertTrue(userRepository.findById(idStartUser).isEmpty());
    }

}
