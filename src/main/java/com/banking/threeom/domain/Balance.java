package com.banking.threeom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Balance.
 */
@Entity
@Table(name = "balance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "initial_balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal initialBalance;

    @NotNull
    @Column(name = "current_balance", precision = 21, scale = 2, nullable = false)
    private BigDecimal currentBalance;

    @JsonIgnoreProperties(value = { "balance", "incomingTransactions", "outgoingTransactions" }, allowSetters = true)
    @OneToOne(mappedBy = "balance")
    private BankAccount bankAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Balance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getInitialBalance() {
        return this.initialBalance;
    }

    public Balance initialBalance(BigDecimal initialBalance) {
        this.setInitialBalance(initialBalance);
        return this;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getCurrentBalance() {
        return this.currentBalance;
    }

    public Balance currentBalance(BigDecimal currentBalance) {
        this.setCurrentBalance(currentBalance);
        return this;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BankAccount getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        if (this.bankAccount != null) {
            this.bankAccount.setBalance(null);
        }
        if (bankAccount != null) {
            bankAccount.setBalance(this);
        }
        this.bankAccount = bankAccount;
    }

    public Balance bankAccount(BankAccount bankAccount) {
        this.setBankAccount(bankAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Balance)) {
            return false;
        }
        return id != null && id.equals(((Balance) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Balance{" +
            "id=" + getId() +
            ", initialBalance=" + getInitialBalance() +
            ", currentBalance=" + getCurrentBalance() +
            "}";
    }
}
