package org.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ReleaseRequestDto;
import org.example.dto.ReleaseResponseDto;
import org.example.entity.ReleaseEntity;
import org.example.repository.ReleaseRepository;
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

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ReleaseControllerTest {
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLnJ1Iiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNjM5NzQ2MzI2LCJleHAiOjE2NDAzNTExMjZ9.Hce1WWfRslrQie9ndWAe1VRAf1Qj0DeV-XqLaJESUSQ";

    @Value(("${api-base-url}"))
    private String apiBaseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReleaseRepository releaseRepository;

    private Long idStartRelease;
    @Before
    public void resetDB() {
        releaseRepository.deleteAll();
        idStartRelease = releaseRepository.save(new ReleaseEntity(1L, "v1", new Date(), new Date())).getIdRelease();
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/release")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/release/" + idStartRelease.toString())
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idRelease").value(idStartRelease));
    }

    @Test
    public void add() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post(apiBaseUrl + "/release")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new ReleaseRequestDto("v2", null, null))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(new ReleaseResponseDto(idStartRelease + 1, "v2", null, null))))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
        Assert.assertTrue(releaseRepository.findById(idStartRelease + 1).isPresent());
    }

    @Test
    public void change() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.put(apiBaseUrl + "/release/" + idStartRelease)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new ReleaseRequestDto("new_version", null, null))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.version").value("new_version"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(releaseRepository.findById(idStartRelease).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(apiBaseUrl + "/release/" + idStartRelease)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.assertTrue(releaseRepository.findById(idStartRelease).isEmpty());
    }
}
