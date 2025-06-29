package com.mintos.assignment.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.assignment.domain.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final UUID SOURCE_ACCOUNT = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-000000000002"); // Alice USD
    private static final UUID TARGET_ACCOUNT = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-000000000002"); // Bob USD

    @Test
    @DisplayName("Should return empty transaction list for new account")
    void shouldReturnEmptyListInitially() throws Exception {
        var result = mockMvc.perform(get("/api/accounts/" + SOURCE_ACCOUNT + "/transactions"))
            .andExpect(status().isOk())
            .andReturn();

        List<Transaction> transactions = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            new TypeReference<>() {}
        );

        assertThat(transactions).isEmpty();
    }

    @Test
    @DisplayName("Should return transactions after transfer")
    void shouldReturnTransactionsAfterTransfer() throws Exception {
        var request = new TransferController.TransferRequest(
            SOURCE_ACCOUNT, TARGET_ACCOUNT, java.math.BigDecimal.valueOf(50), "USD"
        );

        mockMvc.perform(
            post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk());

        var result = mockMvc.perform(get("/api/accounts/" + SOURCE_ACCOUNT + "/transactions"))
            .andExpect(status().isOk())
            .andReturn();

        List<Transaction> transactions = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            new TypeReference<>() {}
        );

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getSourceAccountId()).isEqualTo(SOURCE_ACCOUNT);
        assertThat(transactions.get(0).getTargetAccountId()).isEqualTo(TARGET_ACCOUNT);
        assertThat(transactions.get(0).getAmount()).isEqualByComparingTo("50.00");
    }

    @Test
    @DisplayName("Should return transactions sorted by timestamp descending")
    void shouldReturnTransactionsSortedDescending() throws Exception {
        // Perform two transfers to create two transactions
        var request1 = new TransferController.TransferRequest(
            SOURCE_ACCOUNT, TARGET_ACCOUNT, BigDecimal.valueOf(20), "USD"
        );

        var request2 = new TransferController.TransferRequest(
            SOURCE_ACCOUNT, TARGET_ACCOUNT, BigDecimal.valueOf(30), "USD"
        );

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isOk());

        Thread.sleep(10); // Ensure timestamp order

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
            .andExpect(status().isOk());

        // Fetch transactions
        var result = mockMvc.perform(get("/api/accounts/" + SOURCE_ACCOUNT + "/transactions"))
            .andExpect(status().isOk())
            .andReturn();

        List<Transaction> transactions = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            new TypeReference<>() {}
        );

        assertThat(transactions).hasSize(2);
        assertThat(transactions.get(0).getAmount()).isEqualByComparingTo("30.00"); // most recent
        assertThat(transactions.get(1).getAmount()).isEqualByComparingTo("20.00"); // older
    }


    @Test
    @DisplayName("Should reject invalid pagination values")
    void shouldRejectInvalidPaging() throws Exception {
        mockMvc.perform(get("/api/accounts/" + SOURCE_ACCOUNT + "/transactions")
                .param("offset", "-1")
                .param("limit", "9999"))
            .andExpect(status().isBadRequest());
    }
}
