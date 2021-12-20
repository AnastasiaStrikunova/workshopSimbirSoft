package org.example.service.impl;

import org.example.dto.ReleaseRequestDto;
import org.example.entity.ReleaseEntity;
import org.example.exception.NotFoundException;
import org.example.repository.ReleaseRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
public class ReleaseServiceImplTest {
    private final ReleaseEntity releaseEntity1 = new ReleaseEntity((long) 1, "v1", new Date(), new Date());
    private final ReleaseEntity releaseEntity2 = new ReleaseEntity((long) 2, "v2", new Date(), new Date());

    @InjectMocks
    private ReleaseServiceImpl releaseService;

    @Mock
    private ReleaseRepository releaseRepository;

    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("myApp");

    @Test
    public void findAll() {
        Mockito.when(releaseRepository.findAll()).thenReturn(Stream.of(releaseEntity1, releaseEntity2).collect(Collectors.toList()));
        var actual = releaseService.findAll();
        Assert.assertEquals(releaseEntity1.getIdRelease(), actual.get(0).getIdRelease());
        Assert.assertEquals(releaseEntity1.getVersion(), actual.get(0).getVersion());
        Assert.assertEquals(releaseEntity1.getStartTime(), actual.get(0).getStartTime());
        Assert.assertEquals(releaseEntity1.getEndTime(), actual.get(0).getEndTime());
        Assert.assertEquals(releaseEntity2.getIdRelease(), actual.get(1).getIdRelease());
        Assert.assertEquals(releaseEntity2.getVersion(), actual.get(1).getVersion());
        Assert.assertEquals(releaseEntity2.getStartTime(), actual.get(1).getStartTime());
        Assert.assertEquals(releaseEntity2.getEndTime(), actual.get(1).getEndTime());
    }

    @Test
    public void findById() {
        Mockito.when(releaseRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(releaseEntity1));
        var actual = releaseService.findById(releaseEntity1.getIdRelease());
        Assert.assertEquals(releaseEntity1.getIdRelease(), actual.getIdRelease());
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNegative() {
        Mockito.when(releaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        releaseService.findById(releaseEntity2.getIdRelease());
    }

    @Test
    public void add() {
        Mockito.when(releaseRepository.save(Mockito.any())).thenReturn(releaseEntity1);
        var actual = releaseService.add(new ReleaseRequestDto(releaseEntity1.getVersion(), releaseEntity1.getStartTime(), releaseEntity1.getEndTime()));
        Assert.assertEquals(releaseEntity1.getIdRelease(), actual.getIdRelease());
        Assert.assertEquals(releaseEntity1.getVersion(), actual.getVersion());
        Assert.assertEquals(releaseEntity1.getStartTime(), actual.getStartTime());
        Assert.assertEquals(releaseEntity1.getEndTime(), actual.getEndTime());
    }

    @Test
    public void change() {
        Mockito.when(releaseRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(releaseEntity1));
        var actual = releaseService.change(releaseEntity1.getIdRelease(), new ReleaseRequestDto("v1.1", new Date(), new Date()));
        Assert.assertEquals(releaseEntity1.getIdRelease(), actual.getIdRelease());
        Assert.assertEquals("v1.1", actual.getVersion());
        Assert.assertEquals(releaseEntity1.getStartTime(), actual.getStartTime());
        Assert.assertEquals(releaseEntity1.getEndTime(), actual.getEndTime());
    }

    @Test
    public void delete() {
        Mockito.when(releaseRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(releaseEntity1));
        try {
            releaseService.delete(releaseEntity1.getIdRelease());
        } catch (NotFoundException e) {
            Assert.assertEquals(String.format(resourceBundle.getString("exceptionReleaseNotExist"), releaseEntity1.getIdRelease()), e.getMessage());
        }
    }

    @Test
    public void deleteNegative() {
        Mockito.when(releaseRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        try {
            releaseService.delete(releaseEntity2.getIdRelease());
        } catch (NotFoundException e) {
            Assert.assertEquals(String.format(resourceBundle.getString("exceptionReleaseNotExist"), releaseEntity2.getIdRelease()), e.getMessage());
        }
    }
}
