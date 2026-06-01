package com.electronic.kafedra.pl.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGradeRequest {

    @NotNull(message = "Value is required")
    @Min(value = 1, message = "Grade must be at least 1")
    @Max(value = 100, message = "Grade must be at most 100")
    private Integer value;

    private String comment;
}

