package com.autoecole.controllers;

import com.autoecole.dto.response.TestBlancResponse;
import com.autoecole.models.*;
import com.autoecole.services.TestBlancService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200"})
public class TestBlancController {
    private final TestBlancService testBlancService;

    @GetMapping
    public ResponseEntity<List<TestBlanc>> getAvailableTests() {
        return ResponseEntity.ok(testBlancService.getAvailableTests());
    }

    @GetMapping("/{testId}")
    public ResponseEntity<TestBlancResponse> getTestById(@PathVariable Long testId) {
        TestBlanc test = testBlancService.getTestById(testId);
        TestBlancResponse response = convertToResponse(test);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{testId}/questions")
    public ResponseEntity<List<Question>> getTestQuestions(@PathVariable Long testId) {
        return ResponseEntity.ok(testBlancService.getQuestionsForTest(testId));
    }

    @PostMapping("/{testId}/evaluate")
    public ResponseEntity<ResultatTest> evaluateTest(
            @PathVariable Long testId,
            @RequestParam Long clientId,
            @RequestBody Map<Long, String> reponses) {
        return ResponseEntity.ok(testBlancService.evaluateTest(testId, clientId, reponses));
    }

    @GetMapping("/results/client/{clientId}")
    public ResponseEntity<List<ResultatTest>> getClientResults(@PathVariable Long clientId) {
        return ResponseEntity.ok(testBlancService.getClientResults(clientId));
    }

    @GetMapping("/results/test/{testId}")
    public ResponseEntity<List<ResultatTest>> getTestResults(@PathVariable Long testId) {
        return ResponseEntity.ok(testBlancService.getTestResults(testId));
    }

    @GetMapping("/results/client/{clientId}/test/{testId}")
    public ResponseEntity<List<ResultatTest>> getClientTestResults(
            @PathVariable Long clientId,
            @PathVariable Long testId) {
        return ResponseEntity.ok(testBlancService.getClientTestResults(clientId, testId));
    }

    @PostMapping
    public ResponseEntity<TestBlanc> createTest(@RequestBody TestBlanc test) {
        return ResponseEntity.ok(testBlancService.createTest(test));
    }

    @PostMapping("/{testId}/questions")
    public ResponseEntity<Question> addQuestion(
            @PathVariable Long testId,
            @RequestBody Question question) {
        return ResponseEntity.ok(testBlancService.addQuestion(testId, question));
    }

    @DeleteMapping("/{testId}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long testId) {
        testBlancService.deleteTest(testId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long questionId) {
        testBlancService.deleteQuestion(questionId);
        return ResponseEntity.ok().build();
    }

    private TestBlancResponse convertToResponse(TestBlanc test) {
        TestBlancResponse response = new TestBlancResponse();
        response.setId(test.getId());
        response.setTitre(test.getTitre());
        response.setDescription(test.getDescription());
        response.setDureeMinutes(test.getDureeMinutes());
        response.setNombreQuestions(test.getNombreQuestions());
        response.setScoreMinimum(test.getScoreMinimum());
        // Questions will be handled separately to avoid exposing correct answers
        return response;
    }
} 