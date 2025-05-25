package com.autoecole.services;

import com.autoecole.models.Client;
import com.autoecole.models.Diagnostic;
import com.autoecole.models.ResultatTest;
import com.autoecole.repositories.ClientRepository;
import com.autoecole.repositories.DiagnosticRepository;
import com.autoecole.repositories.ResultatTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosticService {
    private final DiagnosticRepository diagnosticRepository;
    private final ResultatTestRepository resultatTestRepository;
    private final ClientRepository clientRepository;

    @Transactional
    public Diagnostic generateDiagnostic(Long clientId) {
        // Validate client exists
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'id: " + clientId));

        // Get all test results for the client
        List<ResultatTest> resultats = resultatTestRepository.findByClientIdOrderByDatePassageDesc(clientId);
        
        if (resultats.isEmpty()) {
            throw new RuntimeException("Aucun test trouvé pour le client avec l'id: " + clientId);
        }

        // Create new diagnostic
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setClient(client);
        diagnostic.setDateDiagnostic(LocalDateTime.now());
        diagnostic.setNombreTestsPasses(resultats.size());
        
        // Calculate average score
        double moyenne = resultats.stream()
                .mapToInt(ResultatTest::getScore)
                .average()
                .orElse(0.0);
        diagnostic.setMoyenneTests(moyenne);
        
        // Calculate driving hours (assuming each test takes 1 hour)
        diagnostic.setNombreHeuresConduite(resultats.size());
        
        // Determine general level
        String niveau = determineNiveau(moyenne, resultats.size());
        diagnostic.setNiveauGeneral(niveau);
        
        // Generate comment based on results
        String commentaire = generateCommentaire(moyenne, niveau, resultats.size());
        diagnostic.setCommentaire(commentaire);
        
        return diagnosticRepository.save(diagnostic);
    }

    public List<Diagnostic> getClientDiagnostics(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new RuntimeException("Client non trouvé avec l'id: " + clientId);
        }
        return diagnosticRepository.findByClientIdOrderByDateDiagnosticDesc(clientId);
    }

    public Diagnostic getLatestDiagnostic(Long clientId) {
        List<Diagnostic> diagnostics = getClientDiagnostics(clientId);
        if (diagnostics.isEmpty()) {
            throw new RuntimeException("Aucun diagnostic trouvé pour le client avec l'id: " + clientId);
        }
        return diagnostics.get(0);
    }

    private String determineNiveau(double moyenne, int nombreTests) {
        if (nombreTests < 3) {
            return "Débutant";
        }
        if (moyenne >= 80) {
            return "Avancé";
        } else if (moyenne >= 60) {
            return "Intermédiaire";
        } else {
            return "Débutant";
        }
    }

    private String generateCommentaire(double moyenne, String niveau, int nombreTests) {
        StringBuilder commentaire = new StringBuilder();
        commentaire.append("Niveau actuel : ").append(niveau).append("\n");
        commentaire.append("Nombre de tests passés : ").append(nombreTests).append("\n");
        commentaire.append("Moyenne des tests : ").append(String.format("%.1f", moyenne)).append("\n");
        
        if (moyenne >= 80) {
            commentaire.append("Excellent niveau ! Continuez à vous entraîner pour maintenir ce niveau.");
        } else if (moyenne >= 60) {
            commentaire.append("Bon niveau. Quelques points à améliorer, mais vous êtes sur la bonne voie.");
        } else {
            commentaire.append("Niveau à améliorer. Nous vous conseillons de revoir les bases et de faire plus d'exercices.");
        }
        
        return commentaire.toString();
    }
} 