package com.electronic.kafedra.service;

import com.electronic.kafedra.bll.dto.GroupDto;
import com.electronic.kafedra.bll.mapper.GroupMapper;
import com.electronic.kafedra.bll.service.impl.GroupServiceImpl;
import com.electronic.kafedra.common.exception.AlreadyExistsException;
import com.electronic.kafedra.dal.entity.*;
import com.electronic.kafedra.dal.repository.*;
import com.electronic.kafedra.dal.uow.UnitOfWork;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private UnitOfWork uow;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        when(uow.groups()).thenReturn(groupRepository);
    }

    @Test
    void create_whenGroupNameUnique_shouldCreateGroup() {
        GroupDto dto = new GroupDto();
        dto.setName("PI-121");
        dto.setYear(2024);

        Group group = new Group();
        group.setId(1L);
        group.setName("PI-121");
        group.setYear(2024);

        GroupDto resultDto = new GroupDto();
        resultDto.setId(1L);
        resultDto.setName("PI-121");

        when(groupRepository.findByName("PI-121"))
                .thenReturn(Optional.empty());
        when(groupMapper.toEntity(dto)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);
        when(groupMapper.toDto(group)).thenReturn(resultDto);

        GroupDto result = groupService.create(dto);

        assertNotNull(result);
        assertEquals("PI-121", result.getName());
        verify(groupRepository).save(group);
    }

    @Test
    void create_whenGroupNameExists_shouldThrowAlreadyExistsException() {
        GroupDto dto = new GroupDto();
        dto.setName("PI-121");
        dto.setYear(2024);

        Group existing = new Group();
        existing.setName("PI-121");

        when(groupRepository.findByName("PI-121"))
                .thenReturn(Optional.of(existing));

        assertThrows(AlreadyExistsException.class,
                () -> groupService.create(dto)
        );
        verify(groupRepository, never()).save(any());
    }

    @Test
    void getById_whenExists_shouldReturnGroup() {
        Group group = new Group();
        group.setId(1L);
        group.setName("PI-121");

        GroupDto dto = new GroupDto();
        dto.setId(1L);
        dto.setName("PI-121");

        when(groupRepository.findById(1L)).thenReturn(group);
        when(groupMapper.toDto(group)).thenReturn(dto);

        GroupDto result = groupService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void delete_whenExists_shouldDelete() {
        Group group = new Group();
        group.setId(1L);

        when(groupRepository.findById(1L)).thenReturn(group);

        groupService.delete(1L);

        verify(groupRepository).delete(group);
    }
}
