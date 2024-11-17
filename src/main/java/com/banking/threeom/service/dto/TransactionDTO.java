package com.banking.threeom.service.dto;

import com.banking.threeom.domain.enumeration.TransactionOperationType;
import com.banking.threeom.domain.enumeration.TransactionSourceType;
import com.banking.threeom.domain.enumeration.TransactionStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.banking.threeom.domain.Transaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private TransactionOperationType transactionType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private ZonedDateTime transactionDate;

    @NotNull
    private TransactionSourceType sourceType;

    @NotNull
    private TransactionSourceType destinationType;

    @NotNull
    private TransactionStatus status;

    private BankAccountDTO sourceBankAccount;

    private BankAccountDTO destinationBankAccount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionOperationType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionOperationType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(TransactionSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public TransactionSourceType getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(TransactionSourceType destinationType) {
        this.destinationType = destinationType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public BankAccountDTO getSourceBankAccount() {
        return sourceBankAccount;
    }

    public void setSourceBankAccount(BankAccountDTO sourceBankAccount) {
        this.sourceBankAccount = sourceBankAccount;
    }

    public BankAccountDTO getDestinationBankAccount() {
        return destinationBankAccount;
    }

    public void setDestinationBankAccount(BankAccountDTO destinationBankAccount) {
        this.destinationBankAccount = destinationBankAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", transactionType='" + getTransactionType() + "'" +
            ", amount=" + getAmount() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", destinationType='" + getDestinationType() + "'" +
            ", status='" + getStatus() + "'" +
            ", sourceBankAccount=" + getSourceBankAccount() +
            ", destinationBankAccount=" + getDestinationBankAccount() +
            "}";
    }
}
