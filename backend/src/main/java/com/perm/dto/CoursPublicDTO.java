package com.perm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CoursPublicDTO extends CoursDTO {
    private Integer nombreVues;
}
