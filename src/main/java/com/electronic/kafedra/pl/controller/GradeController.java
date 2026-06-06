package com.electronic.kafedra.pl.controller;


import com.electronic.kafedra.bll.dto.GradeDto;
import com.electronic.kafedra.bll.service.GradeService;
import com.electronic.kafedra.pl.mapper.UiMapper;
import com.electronic.kafedra.pl.model.request.CreateGradeRequest;
import com.electronic.kafedra.pl.model.request.UpdateGradeRequest;
import com.electronic.kafedra.pl.model.response.GradeResponse;
import com.electronic.kafedra.pl.security.JwtCurrentUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;
    private final UiMapper uiMapper;
    private final JwtCurrentUserService currentUserService;

    public GradeController(GradeService gradeService,
                           UiMapper uiMapper,
                           JwtCurrentUserService currentUserService) {
        this.gradeService = gradeService;
        this.uiMapper = uiMapper;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER', 'STUDENT')")
    public ResponseEntity<List<GradeResponse>> getByStudent(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(
                uiMapper.toGradeResponseList(gradeService.getByStudent(studentId))
        );
    }

    @GetMapping("/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<List<GradeResponse>> getByTeacher(
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(
                uiMapper.toGradeResponseList(gradeService.getByTeacher(teacherId))
        );
    }

    @GetMapping("/discipline/{disciplineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<List<GradeResponse>> getByDiscipline(
            @PathVariable Long disciplineId) {
        return ResponseEntity.ok(
                uiMapper.toGradeResponseList(
                        gradeService.getByDiscipline(disciplineId))
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<GradeResponse> addGrade(
            @Valid @RequestBody CreateGradeRequest request,
            HttpServletRequest httpRequest) {
        Long currentUserId = currentUserService.getCurrentUserId(httpRequest);
        GradeDto dto = uiMapper.toDto(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiMapper.toResponse(
                        gradeService.addGrade(dto, currentUserId))
                );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<GradeResponse> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGradeRequest request,
            HttpServletRequest httpRequest) {
        Long currentUserId = currentUserService.getCurrentUserId(httpRequest);
        GradeDto dto = uiMapper.toDto(request);
        return ResponseEntity.ok(
                uiMapper.toResponse(
                        gradeService.updateGrade(id, dto, currentUserId))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<Void> deleteGrade(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {
        Long currentUserId = currentUserService.getCurrentUserId(httpRequest);
        gradeService.deleteGrade(id, currentUserId);
        return ResponseEntity.noContent().build();
    }
}
