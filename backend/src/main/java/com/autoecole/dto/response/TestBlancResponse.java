package com.autoecole.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class TestBlancResponse {
    private Long id;
    private String titre;
    private String description;
    private Integer dureeMinutes;
    private Integer nombreQuestions;
    private Integer scoreMinimum;
    private List<QuestionResponse> questions;
}

@Data
class QuestionResponse {
    private Long id;
    private String enonce;
    private List<String> reponsesPossibles;
    private Integer points;
} 