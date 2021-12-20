package org.example.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.TaskRequestDto;
import org.example.dto.TaskResponseDto;
import org.example.entity.ReleaseEntity;
import org.example.entity.StatusEntity;
import org.example.entity.TaskEntity;
import org.example.enums.Status;
import org.example.repository.ReleaseRepository;
import org.example.repository.StatusRepository;
import org.example.repository.TaskRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileInputStream;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TaskControllerTest {
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLnJ1Iiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNjM5NzQ2MzI2LCJleHAiOjE2NDAzNTExMjZ9.Hce1WWfRslrQie9ndWAe1VRAf1Qj0DeV-XqLaJESUSQ";

    @Value(("${api-base-url}"))
    private String apiBaseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ReleaseRepository releaseRepository;

    private Long idStartTask;
    private Long idStatus;
    private Long idRelease;
    @Before
    public void resetDB() {
        taskRepository.deleteAll();
        idStartTask = taskRepository.save(new TaskEntity(1L, "title1", "priority1", null, null, new Date(), new Date(), null, null, null)).getIdTask();
        statusRepository.deleteAll();
        idStatus = statusRepository.save(new StatusEntity(1L, Status.BACKLOG.name())).getIdStatus();
        releaseRepository.deleteAll();
        idRelease = releaseRepository.save(new ReleaseEntity(1L, "v1", new Date(), new Date())).getIdRelease();
    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/task")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    public void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/task/" + idStartTask)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idTask").value(idStartTask));
    }

    @Test
    public void add() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post(apiBaseUrl + "/task")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new TaskRequestDto())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idTask").value(idStartTask + 1))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
        Assert.assertTrue(taskRepository.findById(idStartTask + 1).isPresent());
    }

    @Test
    public void change() throws Exception {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setTitle("new_title");
        String body = mockMvc.perform(MockMvcRequestBuilders.put(apiBaseUrl + "/task/" + idStartTask)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("new_title"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
    }

    @Test
    public void delete() throws Exception {
        Assert.assertTrue(taskRepository.findById(idStartTask).isPresent());
        mockMvc.perform(MockMvcRequestBuilders.delete(apiBaseUrl + "/task/" + idStartTask)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Assert.assertTrue(taskRepository.findById(idStartTask).isEmpty());
    }

    @Test
    public void changeStatus() throws Exception {
        TaskRequestDto taskRequestDto = new TaskRequestDto();
        taskRequestDto.setIdStatus(idStatus);
        String body = mockMvc.perform(MockMvcRequestBuilders.put(apiBaseUrl + "/task/" + idStartTask + "/status")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idStatus").value(idStatus))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
    }

    @Test
    public void countAfterDateRelease() throws Exception {
        Date now=new Date();
        Date date=new Date(now.getTime() + 8*24*60*60*1000);
        taskRepository.save(new TaskEntity(2L, "title2", "priority2", null, null, date, date, null, null, null));
        String body = mockMvc.perform(MockMvcRequestBuilders.get(apiBaseUrl + "/task/release/" + idRelease)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        Assert.assertEquals("1", body);
    }

    @Test
    public void findByFilter() throws Exception {
        taskRepository.save(new TaskEntity(2L, "title2", "priority2", null, null, null, null, null, null, null));
        String body = mockMvc.perform(MockMvcRequestBuilders.post(apiBaseUrl + "/task/filter")
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token)
                        .content(objectMapper.writeValueAsString(new TaskRequestDto("title2", null, null, null, null, null, null, null, null))))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        var expected = "[" + objectMapper.writeValueAsString(new TaskResponseDto(idStartTask + 1, "title2", "priority2", null, null, null, null, null, null, null)) + "]";
        Assert.assertEquals(expected, body);
    }

    @Test
    public void addViaFile() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.multipart(apiBaseUrl + "/task/file")
                        .file(new MockMultipartFile("multipartFile", new FileInputStream("src/testIntegration/java/org/example/new.csv")))
                        .contentType(MediaType.MULTIPART_FORM_DATA).header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idTask").value(idStartTask + 1))
                .andReturn().getResponse().getContentAsString();
        System.out.println(body);
    }
}
