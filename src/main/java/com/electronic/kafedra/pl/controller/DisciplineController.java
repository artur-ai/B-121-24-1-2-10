package com.electronic.kafedra.pl.controller;


import com.electronic.kafedra.bll.dto.DisciplineDto;
import com.electronic.kafedra.bll.service.DisciplineService;
import com.electronic.kafedra.pl.mapper.UiMapper;
import com.electronic.kafedra.pl.model.request.CreateDisciplineRequest;
import com.electronic.kafedra.pl.model.response.DisciplineResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;
    private final UiMapper uiMapper;

    public DisciplineController(DisciplineService disciplineService,
                                UiMapper uiMapper) {
        this.disciplineService = disciplineService;
        this.uiMapper = uiMapper;
    }

    @GetMapping
    public ResponseEntity<List<DisciplineResponse>> getAll() {
        return ResponseEntity.ok(
                uiMapper.toDisciplineResponseList(disciplineService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplineResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                uiMapper.toResponse(disciplineService.getById(id))
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<DisciplineResponse>> search(
            @RequestParam String name) {
        return ResponseEntity.ok(
                uiMapper.toDisciplineResponseList(
                        disciplineService.searchByName(name))
        );
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<DisciplineResponse>> getByTeacher(
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(
                uiMapper.toDisciplineResponseList(
                        disciplineService.getByTeacher(teacherId))
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DisciplineResponse> create(
            @Valid @RequestBody CreateDisciplineRequest request) {
        DisciplineDto dto = uiMapper.toDto(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiMapper.toResponse(disciplineService.create(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<DisciplineResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateDisciplineRequest request) {
        DisciplineDto dto = uiMapper.toDto(request);
        return ResponseEntity.ok(
                uiMapper.toResponse(disciplineService.update(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        disciplineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
