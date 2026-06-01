package com.electronic.kafedra.pl.controller;

import com.electronic.kafedra.bll.dto.TeacherDto;
import com.electronic.kafedra.bll.service.TeacherService;
import com.electronic.kafedra.pl.mapper.UiMapper;
import com.electronic.kafedra.pl.model.request.CreateTeacherRequest;
import com.electronic.kafedra.pl.model.request.UpdateTeacherRequest;
import com.electronic.kafedra.pl.model.response.TeacherResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final UiMapper uiMapper;

    public TeacherController(TeacherService teacherService, UiMapper uiMapper) {
        this.teacherService = teacherService;
        this.uiMapper = uiMapper;
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponse>> getAll() {
        return ResponseEntity.ok(
                uiMapper.toTeacherResponseList(teacherService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                uiMapper.toResponse(teacherService.getById(id))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherResponse>> search(
            @RequestParam String lastName) {
        return ResponseEntity.ok(
                uiMapper.toTeacherResponseList(
                        teacherService.searchByLastName(lastName))
        );
    }

    @GetMapping("/discipline/{disciplineId}")
    public ResponseEntity<List<TeacherResponse>> getByDiscipline(
            @PathVariable Long disciplineId) {
        return ResponseEntity.ok(
                uiMapper.toTeacherResponseList(
                        teacherService.getByDiscipline(disciplineId))
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeacherResponse> create(
            @Valid @RequestBody CreateTeacherRequest request) {
        TeacherDto dto = uiMapper.toDto(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiMapper.toResponse(
                        teacherService.create(dto, request.getUserId()))
                );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeacherResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTeacherRequest request) {
        TeacherDto dto = uiMapper.toDto(request);
        return ResponseEntity.ok(
                uiMapper.toResponse(teacherService.update(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{teacherId}/disciplines/{disciplineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeacherResponse> assignDiscipline(
            @PathVariable Long teacherId,
            @PathVariable Long disciplineId) {
        return ResponseEntity.ok(
                uiMapper.toResponse(
                        teacherService.assignDiscipline(teacherId, disciplineId))
        );
    }

    @DeleteMapping("/{teacherId}/disciplines/{disciplineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TeacherResponse> removeDiscipline(
            @PathVariable Long teacherId,
            @PathVariable Long disciplineId) {
        return ResponseEntity.ok(
                uiMapper.toResponse(
                        teacherService.removeDiscipline(teacherId, disciplineId))
        );
    }
}
