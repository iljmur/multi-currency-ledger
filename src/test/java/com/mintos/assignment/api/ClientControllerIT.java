package com.mintos.assignment.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.assignment.domain.model.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UUID ALICE_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID UNKNOWN_ID = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    @Test
    @DisplayName("Should return client details for known ID")
    void shouldReturnClientForKnownId() throws Exception {
        var result = mockMvc.perform(get("/api/clients/" + ALICE_ID))
            .andExpect(status().isOk())
            .andReturn();

        Client client = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(client.getId()).isEqualTo(ALICE_ID);
        assertThat(client.getName()).isEqualTo("Alice");
        assertThat(client.getEmail()).isEqualTo("alice@mintos.com");
    }

    @Test
    @DisplayName("Should return 404 for unknown client ID")
    void shouldReturn404ForUnknownId() throws Exception {
        mockMvc.perform(get("/api/clients/" + UNKNOWN_ID)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
