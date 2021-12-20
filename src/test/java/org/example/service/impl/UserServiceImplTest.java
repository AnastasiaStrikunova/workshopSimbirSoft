package org.example.service.impl;

import org.example.dto.UserRequestDto;
import org.example.entity.UserEntity;
import org.example.exception.NotFoundException;
import org.example.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private final UserEntity userEntity1 = new UserEntity((long) 1, "first test user", "main");
    private final UserEntity userEntity2 = new UserEntity((long) 2, "second test user", "main");

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("myApp");

    @Test
    public void findAll() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.singletonList(userEntity1));
        var actual = userService.findAll();
        Assert.assertEquals(1, actual.get(0).getIdUser().longValue());
        Assert.assertEquals("first test user", actual.get(0).getName());
        Assert.assertEquals("main", actual.get(0).getPriority());
    }

    @Test
    public void findById() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(userEntity1));
        var actual = userService.findById(userEntity1.getIdUser());
        Assert.assertEquals(userEntity1.getIdUser(), actual.getIdUser());
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNegative() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        userService.findById(userEntity2.getIdUser());
    }

    @Test
    public void add() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(userEntity1);
        var actual = userService.add(new UserRequestDto(userEntity1.getName(), userEntity1.getPriority()));
        Assert.assertEquals(userEntity1.getIdUser(), actual.getIdUser());
        Assert.assertEquals(userEntity1.getName(), actual.getName());
        Assert.assertEquals(userEntity1.getPriority(), actual.getPriority());
    }

    @Test
    public void change() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(userEntity1));
        var actual = userService.change(userEntity1.getIdUser(), new UserRequestDto("updated user", "main1"));
        Assert.assertEquals(userEntity1.getIdUser(), actual.getIdUser());
        Assert.assertEquals("updated user", actual.getName());
        Assert.assertEquals("main1", actual.getPriority());
    }

    @Test
    public void delete() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(userEntity1));
        try {
            userService.delete(userEntity1.getIdUser());
        } catch (NotFoundException e) {
            Assert.assertEquals(String.format(resourceBundle.getString("exceptionUserNotExist"), userEntity1.getIdUser()), e.getMessage());
        }
    }

    @Test
    public void deleteNegative() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        try {
            userService.delete(userEntity2.getIdUser());
        } catch (NotFoundException e) {
            Assert.assertEquals(String.format(resourceBundle.getString("exceptionUserNotExist"), userEntity2.getIdUser()), e.getMessage());
        }
    }

}
