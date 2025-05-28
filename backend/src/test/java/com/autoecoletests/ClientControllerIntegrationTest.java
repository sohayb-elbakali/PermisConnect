package com.autoecoletests;

import com.autoecole.controllers.ClientController;
import com.autoecole.dto.request.ClientRequest;
import com.autoecole.dto.response.ClientResponse;
import com.autoecole.models.Client;
import com.autoecole.models.User;
import com.autoecole.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClientControllerIntegrationTest {
    private MockMvc mockMvc;
    @Mock
    private ClientService clientService;
    @InjectMocks
    private ClientController clientController;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();
    }

    private ClientRequest buildRequest() {
        ClientRequest req = new ClientRequest();
        req.setNom("Doe");
        req.setPrenom("John");
        req.setEmail("john.doe@example.com");
        req.setPassword("password123");
        req.setTelephone("+33612345678");
        req.setAdresse("123 rue de Paris");
        req.setDateNaissance("1990-01-01");
        req.setNumeroPermis("123456789");
        req.setTypePermis("B");
        return req;
    }

    private Client buildClient() {
        User user = new User("Doe", "John", "john.doe@example.com", "password123", "+33612345678", "123 rue de Paris");
        Client client = new Client(user, "1990-01-01", "123456789", "B", null);
        client.setId(1L);
        return client;
    }

    @Test
    void createClient_andGetById() throws Exception {
        Client client = buildClient();
        when(clientService.createClient(any(ClientRequest.class))).thenReturn(client);
        when(clientService.getAllClients()).thenReturn(List.of(client));
        when(clientService.getClientById(anyLong())).thenReturn(client);
        String json = objectMapper.writeValueAsString(buildRequest());
        mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void updateClient() throws Exception {
        Client client = buildClient();
        Client updatedClient = buildClient();
        updatedClient.getUser().setNom("Smith");
        updatedClient.getUser().setEmail("smith@example.com");
        when(clientService.updateClient(anyLong(), any(ClientRequest.class))).thenReturn(updatedClient);
        String updateJson = objectMapper.writeValueAsString(buildRequest());
        mockMvc.perform(put("/api/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Smith"))
                .andExpect(jsonPath("$.email").value("smith@example.com"));
    }

    @Test
    void deleteClient() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchClients() throws Exception {
        Client client = buildClient();
        when(clientService.searchClients(any())).thenReturn(Collections.singletonList(client));
        mockMvc.perform(get("/api/clients/search?query=Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Doe"));
    }

    @Test
    void getClientById_notFound_returns404() throws Exception {
        when(clientService.getClientById(anyLong())).thenThrow(new jakarta.persistence.EntityNotFoundException());
        mockMvc.perform(get("/api/clients/999"))
                .andExpect(status().isNotFound());
    }
} 
