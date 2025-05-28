package com.autoecoletests;

import com.autoecole.dto.request.ClientRequest;
import com.autoecole.models.Client;
import com.autoecole.models.User;
import com.autoecole.repositories.ClientRepository;
import com.autoecole.services.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createClient_success() {
        ClientRequest request = new ClientRequest();
        request.setNom("Doe");
        request.setPrenom("John");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setTelephone("+33612345678");
        request.setAdresse("123 rue de Paris");
        request.setDateNaissance("1990-01-01");
        request.setNumeroPermis("123456789");
        request.setTypePermis("B");

        when(clientRepository.existsByUserEmail(request.getEmail())).thenReturn(false);
        when(clientRepository.existsByUserTelephone(request.getTelephone())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);

        Client client = clientService.createClient(request);
        assertEquals(request.getEmail(), client.getUser().getEmail());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void createClient_duplicateEmail_throwsException() {
        ClientRequest request = new ClientRequest();
        request.setEmail("john.doe@example.com");
        request.setTelephone("+33612345678");
        when(clientRepository.existsByUserEmail(request.getEmail())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(request));
    }

    @Test
    void getClientById_found() {
        User user = new User("Doe", "John", "john.doe@example.com", "password123", "+33612345678", "123 rue de Paris");
        Client client = new Client(user, "1990-01-01", "123456789", "B", null);
        client.setId(1L);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        Client found = clientService.getClientById(1L);
        assertEquals(1L, found.getId());
    }

    @Test
    void getClientById_notFound() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clientService.getClientById(1L));
    }

    @Test
    void updateClient_success() {
        User user = new User("Doe", "John", "john.doe@example.com", "password123", "+33612345678", "123 rue de Paris");
        Client client = new Client(user, "1990-01-01", "123456789", "B", null);
        client.setId(1L);
        ClientRequest request = new ClientRequest();
        request.setNom("Smith");
        request.setPrenom("Jane");
        request.setEmail("jane.smith@example.com");
        request.setPassword("newpass");
        request.setTelephone("+33687654321");
        request.setAdresse("456 rue de Lyon");
        request.setDateNaissance("1992-02-02");
        request.setNumeroPermis("987654321");
        request.setTypePermis("A");
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientRepository.existsByUserEmail(request.getEmail())).thenReturn(false);
        when(clientRepository.existsByUserTelephone(request.getTelephone())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);
        Client updated = clientService.updateClient(1L, request);
        assertEquals("jane.smith@example.com", updated.getUser().getEmail());
        assertEquals("Smith", updated.getUser().getNom());
    }

    @Test
    void deleteClient_success() {
        when(clientRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clientRepository).deleteById(1L);
        assertDoesNotThrow(() -> clientService.deleteClient(1L));
        verify(clientRepository).deleteById(1L);
    }

    @Test
    void deleteClient_notFound() {
        when(clientRepository.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> clientService.deleteClient(1L));
    }

    @Test
    void searchClients_returnsResults() {
        List<Client> clients = new ArrayList<>();
        when(clientRepository.findByUserNomContainingIgnoreCase(anyString())).thenReturn(clients);
        when(clientRepository.findByUserEmailContainingIgnoreCase(anyString())).thenReturn(clients);
        when(clientRepository.findByUserTelephoneContaining(anyString())).thenReturn(clients);
        List<Client> result = clientService.searchClients("query");
        assertNotNull(result);
    }
} 
