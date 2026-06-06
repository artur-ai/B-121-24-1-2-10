package com.electronic.kafedra.pl.controller;


import com.electronic.kafedra.bll.dto.GroupDto;
import com.electronic.kafedra.bll.service.GroupService;
import com.electronic.kafedra.pl.mapper.UiMapper;
import com.electronic.kafedra.pl.model.request.CreateGroupRequest;
import com.electronic.kafedra.pl.model.response.GroupResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final UiMapper uiMapper;

    public GroupController(GroupService groupService, UiMapper uiMapper) {
        this.groupService = groupService;
        this.uiMapper = uiMapper;
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAll() {
        return ResponseEntity.ok(
                uiMapper.toGroupResponseList(groupService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                uiMapper.toResponse(groupService.getById(id))
        );
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<GroupResponse>> getByYear(
            @PathVariable Integer year) {
        return ResponseEntity.ok(
                uiMapper.toGroupResponseList(groupService.getByYear(year))
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<GroupResponse> create(
            @Valid @RequestBody CreateGroupRequest request) {
        GroupDto dto = uiMapper.toDto(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiMapper.toResponse(groupService.create(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<GroupResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateGroupRequest request) {
        GroupDto dto = uiMapper.toDto(request);
        return ResponseEntity.ok(
                uiMapper.toResponse(groupService.update(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        groupService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
