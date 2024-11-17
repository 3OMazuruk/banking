package com.banking.threeom.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.banking.threeom.domain.Balance} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BalanceDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal initialBalance;

    @NotNull
    private BigDecimal currentBalance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BalanceDTO)) {
            return false;
        }

        BalanceDTO balanceDTO = (BalanceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, balanceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BalanceDTO{" +
            "id=" + getId() +
            ", initialBalance=" + getInitialBalance() +
            ", currentBalance=" + getCurrentBalance() +
            "}";
    }
}
