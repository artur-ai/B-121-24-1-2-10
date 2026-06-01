package com.electronic.kafedra.bll.service;

import com.electronic.kafedra.bll.dto.GroupDto;

import java.util.List;

public interface GroupService {
    List<GroupDto> getAll();

    GroupDto getById(Long id);

    List<GroupDto> getByYear(Integer year);

    GroupDto create(GroupDto dto);

    GroupDto update(Long id, GroupDto dto);

    void delete(Long id);
}
