package com.pismo.challenge.dto.request;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateTransactionRequest(Long accountId, int operationTypeId, BigDecimal amount) {}
