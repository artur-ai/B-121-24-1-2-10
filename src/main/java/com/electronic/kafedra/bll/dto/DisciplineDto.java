package com.electronic.kafedra.bll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineDto {
    private Long id;
    private String name;
    private String description;
    private List<Long> teacherIds;
}
