package com.mintos.assignment.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.assignment.domain.model.Account;
import com.mintos.assignment.domain.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID aliceId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private final UUID bobChfAccountId = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-000000000003");

    @Test
    @DisplayName("Should return accounts for valid client")
    void shouldReturnAccountsForClient() throws Exception {
        var result = mockMvc.perform(get("/api/accounts/client/" + aliceId))
            .andExpect(status().isOk())
            .andReturn();

        List<Account> accounts = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            new TypeReference<>() {}
        );

        assertThat(accounts).hasSize(3);
    }

    @Test
    @DisplayName("Should return 404 for unknown client")
    void shouldFailForUnknownClient() throws Exception {
        mockMvc.perform(get("/api/accounts/client/" + UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return paginated transaction list")
    void shouldReturnTransactionHistory() throws Exception {
        var result = mockMvc.perform(get("/api/accounts/" + bobChfAccountId + "/transactions")
                .param("offset", "0")
                .param("limit", "50"))
            .andExpect(status().isOk())
            .andReturn();

        List<Transaction> transactions = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            new TypeReference<>() {}
        );

        assertThat(transactions).isNotNull();
    }

    @Test
    @DisplayName("Should reject invalid pagination values")
    void shouldRejectInvalidPaging() throws Exception {
        mockMvc.perform(get("/api/accounts/" + bobChfAccountId + "/transactions")
                .param("offset", "-1")
                .param("limit", "9999"))
            .andExpect(status().isBadRequest());
    }
}
