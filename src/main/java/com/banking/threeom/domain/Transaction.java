package com.banking.threeom.domain;

import com.banking.threeom.domain.enumeration.TransactionOperationType;
import com.banking.threeom.domain.enumeration.TransactionSourceType;
import com.banking.threeom.domain.enumeration.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionOperationType transactionType;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private ZonedDateTime transactionDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false)
    private TransactionSourceType sourceType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "destination_type", nullable = false)
    private TransactionSourceType destinationType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @ManyToOne
    @JsonIgnoreProperties(value = { "balance", "incomingTransactions", "outgoingTransactions" }, allowSetters = true)
    private BankAccount sourceBankAccount;

    @ManyToOne
    @JsonIgnoreProperties(value = { "balance", "incomingTransactions", "outgoingTransactions" }, allowSetters = true)
    private BankAccount destinationBankAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionOperationType getTransactionType() {
        return this.transactionType;
    }

    public Transaction transactionType(TransactionOperationType transactionType) {
        this.setTransactionType(transactionType);
        return this;
    }

    public void setTransactionType(TransactionOperationType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Transaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ZonedDateTime getTransactionDate() {
        return this.transactionDate;
    }

    public Transaction transactionDate(ZonedDateTime transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionSourceType getSourceType() {
        return this.sourceType;
    }

    public Transaction sourceType(TransactionSourceType sourceType) {
        this.setSourceType(sourceType);
        return this;
    }

    public void setSourceType(TransactionSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public TransactionSourceType getDestinationType() {
        return this.destinationType;
    }

    public Transaction destinationType(TransactionSourceType destinationType) {
        this.setDestinationType(destinationType);
        return this;
    }

    public void setDestinationType(TransactionSourceType destinationType) {
        this.destinationType = destinationType;
    }

    public TransactionStatus getStatus() {
        return this.status;
    }

    public Transaction status(TransactionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public BankAccount getSourceBankAccount() {
        return this.sourceBankAccount;
    }

    public void setSourceBankAccount(BankAccount bankAccount) {
        this.sourceBankAccount = bankAccount;
    }

    public Transaction sourceBankAccount(BankAccount bankAccount) {
        this.setSourceBankAccount(bankAccount);
        return this;
    }

    public BankAccount getDestinationBankAccount() {
        return this.destinationBankAccount;
    }

    public void setDestinationBankAccount(BankAccount bankAccount) {
        this.destinationBankAccount = bankAccount;
    }

    public Transaction destinationBankAccount(BankAccount bankAccount) {
        this.setDestinationBankAccount(bankAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", transactionType='" + getTransactionType() + "'" +
            ", amount=" + getAmount() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", sourceType='" + getSourceType() + "'" +
            ", destinationType='" + getDestinationType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
