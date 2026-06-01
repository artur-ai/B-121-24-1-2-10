package com.electronic.kafedra.bll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeDto {
    private Long id;
    private Long studentId;
    private String studentFirstName;
    private String studentLastName;
    private Long disciplineId;
    private String disciplineName;
    private Long teacherId;
    private Integer value;
    private LocalDateTime gradedAt;
    private String comment;
}
