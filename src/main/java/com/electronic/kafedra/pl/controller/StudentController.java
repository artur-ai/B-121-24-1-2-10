package com.electronic.kafedra.pl.controller;

import com.electronic.kafedra.bll.dto.StudentDto;
import com.electronic.kafedra.bll.service.StudentService;
import com.electronic.kafedra.common.exception.EntityNotFoundException;
import com.electronic.kafedra.dal.entity.Student;
import com.electronic.kafedra.pl.mapper.UiMapper;
import com.electronic.kafedra.pl.model.request.CreateStudentRequest;
import com.electronic.kafedra.pl.model.request.UpdateStudentRequest;
import com.electronic.kafedra.pl.model.response.StudentResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final UiMapper uiMapper;

    public StudentController(StudentService studentService, UiMapper uiMapper) {
        this.studentService = studentService;
        this.uiMapper = uiMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<List<StudentResponse>> getAll() {
        return ResponseEntity.ok(
                uiMapper.toStudentResponseList(studentService.getAll())
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<StudentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                uiMapper.toResponse(studentService.getById(id))
        );
    }

    @GetMapping("/group/{groupId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<List<StudentResponse>> getByGroup(
            @PathVariable Long groupId) {
        return ResponseEntity.ok(
                uiMapper.toStudentResponseList(studentService.getByGroup(groupId))
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'TEACHER')")
    public ResponseEntity<List<StudentResponse>> search(
            @RequestParam String lastName) {
        return ResponseEntity.ok(
                uiMapper.toStudentResponseList(
                        studentService.searchByLastName(lastName))
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StudentResponse> create(
            @Valid @RequestBody CreateStudentRequest request) {
        StudentDto dto = uiMapper.toDto(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(uiMapper.toResponse(
                        studentService.create(dto, request.getUserId()))
                );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<StudentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStudentRequest request) {
        StudentDto dto = uiMapper.toDto(request);
        return ResponseEntity.ok(
                uiMapper.toResponse(studentService.update(id, dto))
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
