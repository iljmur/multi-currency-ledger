package com.mintos.assignment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintos.assignment.config.StubExchangeRateConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(StubExchangeRateConfig.class)
class TransferControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void transfer_shouldSucceed_withSameCurrency() throws Exception {
        var request = new TransferController.TransferRequest(
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-000000000002"), // Alice USD
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-000000000002"), // Bob USD
            new BigDecimal("100.00"),
            "USD"
        );

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void transfer_shouldSucceed_withCurrencyConversion() throws Exception {
        var request = new TransferController.TransferRequest(
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-000000000002"), // Alice USD
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-000000000001"), // Bob EUR
            new BigDecimal("50.00"),
            "USD"
        );

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void transfer_shouldFail_whenCurrencyDoesNotMatchSource() throws Exception {
        var request = new TransferController.TransferRequest(
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-000000000001"), // Alice EUR
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-000000000002"), // Bob USD
            new BigDecimal("50.00"),
            "USD" // Mismatch: source is EUR
        );

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldFail_whenAmountIsZero() throws Exception {
        var request = new TransferController.TransferRequest(
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-000000000002"),
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-000000000002"),
            new BigDecimal("0.00"),
            "USD"
        );

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void transfer_shouldFail_whenInsufficientFunds() throws Exception {
        var request = new TransferController.TransferRequest(
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-000000000003"), // Bob CHF (0.00)
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-000000000003"), // Alice CHF
            new BigDecimal("50.00"),
            "CHF"
        );

        mockMvc.perform(post("/api/transfers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
