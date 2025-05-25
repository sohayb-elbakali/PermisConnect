package com.autoecole.services;

import com.autoecole.models.*;
import com.autoecole.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TestBlancService {
    private final TestBlancRepository testBlancRepository;
    private final QuestionRepository questionRepository;
    private final ResultatTestRepository resultatTestRepository;
    private final ClientRepository clientRepository;

    public List<TestBlanc> getAvailableTests() {
        return testBlancRepository.findAll();
    }

    public TestBlanc getTestById(Long id) {
        return testBlancRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found with id: " + id));
    }

    public List<Question> getQuestionsForTest(Long testId) {
        if (!testBlancRepository.existsById(testId)) {
            throw new RuntimeException("Test not found with id: " + testId);
        }
        return questionRepository.findByTestBlancIdOrderByOrdreAsc(testId);
    }

    @Transactional
    public ResultatTest evaluateTest(Long testId, Long clientId, Map<Long, String> reponses) {
        // Validate test exists
        TestBlanc test = getTestById(testId);
        
        // Validate client exists
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        // Get and validate questions
        List<Question> questions = questionRepository.findByTestBlancIdOrderByOrdreAsc(testId);
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for test with id: " + testId);
        }

        // Validate responses
        if (reponses == null || reponses.isEmpty()) {
            throw new RuntimeException("No responses provided for evaluation");
        }

        // Calculate score
        int scoreTotal = 0;
        int questionsAnswered = 0;

        for (Question question : questions) {
            String reponseClient = reponses.get(question.getId());
            
            if (reponseClient != null) {
                questionsAnswered++;
                if (reponseClient.equals(question.getReponseCorrecte())) {
                    scoreTotal += question.getPoints();
                }
            }
        }

        // Validate minimum questions answered
        if (questionsAnswered < test.getNombreQuestions()) {
            throw new RuntimeException("Not all questions were answered. Required: " + 
                test.getNombreQuestions() + ", Answered: " + questionsAnswered);
        }

        // Create and save result
        ResultatTest resultat = new ResultatTest();
        resultat.setTestBlanc(test);
        resultat.setClient(client);
        resultat.setScore(scoreTotal);
        resultat.setDatePassage(LocalDateTime.now());
        resultat.setReussi(scoreTotal >= test.getScoreMinimum());
        resultat.setTempsUtilise(test.getDureeMinutes()); // Assuming full time was used

        return resultatTestRepository.save(resultat);
    }

    public List<ResultatTest> getClientResults(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client not found with id: " + clientId);
        }
        return resultatTestRepository.findByClientIdOrderByDatePassageDesc(clientId);
    }

    public List<ResultatTest> getTestResults(Long testId) {
        if (!testBlancRepository.existsById(testId)) {
            throw new RuntimeException("Test not found with id: " + testId);
        }
        return resultatTestRepository.findByTestBlancId(testId);
    }

    public List<ResultatTest> getClientTestResults(Long clientId, Long testId) {
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client not found with id: " + clientId);
        }
        if (!testBlancRepository.existsById(testId)) {
            throw new RuntimeException("Test not found with id: " + testId);
        }
        return resultatTestRepository.findByClientIdAndTestBlancId(clientId, testId);
    }

    @Transactional
    public TestBlanc createTest(TestBlanc test) {
        if (test.getTitre() == null || test.getTitre().trim().isEmpty()) {
            throw new RuntimeException("Test title cannot be empty");
        }
        if (test.getScoreMinimum() <= 0) {
            throw new RuntimeException("Minimum score must be greater than 0");
        }
        if (test.getDureeMinutes() <= 0) {
            throw new RuntimeException("Test duration must be greater than 0");
        }
        return testBlancRepository.save(test);
    }

    @Transactional
    public Question addQuestion(Long testId, Question question) {
        TestBlanc test = getTestById(testId);
        
        // Validate question
        if (question.getEnonce() == null || question.getEnonce().trim().isEmpty()) {
            throw new RuntimeException("Question statement cannot be empty");
        }
        if (question.getPoints() <= 0) {
            throw new RuntimeException("Question points must be greater than 0");
        }
        if (question.getReponseCorrecte() == null || question.getReponseCorrecte().trim().isEmpty()) {
            throw new RuntimeException("Correct answer cannot be empty");
        }
        
        // Set the order if not provided
        if (question.getOrdre() == null) {
            List<Question> existingQuestions = questionRepository.findByTestBlancId(testId);
            question.setOrdre(existingQuestions.size() + 1);
        }
        
        question.setTestBlanc(test);
        return questionRepository.save(question);
    }

    @Transactional
    public void deleteTest(Long id) {
        if (!testBlancRepository.existsById(id)) {
            throw new RuntimeException("Test not found with id: " + id);
        }
        // Delete associated questions first
        List<Question> questions = questionRepository.findByTestBlancId(id);
        questionRepository.deleteAll(questions);
        // Then delete the test
        testBlancRepository.deleteById(id);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }
} 