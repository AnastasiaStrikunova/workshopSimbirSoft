package org.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ProjectRequestDto;
import org.example.entity.ProjectEntity;
import org.example.repository.ProjectRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ProjectControllerTest {
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLnJ1Iiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNjM5NzQ2MzI2LCJleHAiOjE2NDAzNTExMjZ9.Hce1WWfRslrQie9ndWAe1VRAf1Qj0DeV-XqLaJESUSQ";

    @Value(("${api-base-url}"))
    private String apiBaseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    private Long idStartProject;
    @Before
    public void resetDB() {
        projectRepository.deleteAll();
        idStartProject = projectRepository.save(new ProjectEntity(1L, "title1", false, null, null)).getIdProject();
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/project")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/project/" + idStartProject)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idProject").value(idStartProject));
    }

    @Test
    public void add() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post(apiBaseUrl + "/project")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new ProjectRequestDto("title2", false, null, null))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
        Assert.assertTrue(projectRepository.findById(idStartProject + 1).isPresent());
    }

    @Test
    public void change() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.put(apiBaseUrl + "/project/" + idStartProject)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new ProjectRequestDto("new_title", null, null, null))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("new_title"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(projectRepository.findById(idStartProject).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(apiBaseUrl + "/project/" + idStartProject)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.assertTrue(projectRepository.findById(idStartProject).isEmpty());
    }

    @Test
    public void completeProject() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.put(apiBaseUrl + "/project/complete/" + idStartProject)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.complete").value("true"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
    }

    //нужно запустить микросервис с оплатой и оплатить проект с id_project = idStartProject
    @Test
    public void startProject() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.put(apiBaseUrl + "/project/start/" + idStartProject)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
    }
}
