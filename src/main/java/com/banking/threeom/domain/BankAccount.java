package com.banking.threeom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BankAccount.
 */
@Entity
@Table(name = "bank_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "additional_info")
    private String additionalInfo;

    @JsonIgnoreProperties(value = { "bankAccount" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Balance balance;

    @OneToMany(mappedBy = "sourceBankAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sourceBankAccount", "destinationBankAccount" }, allowSetters = true)
    private Set<Transaction> incomingTransactions = new HashSet<>();

    @OneToMany(mappedBy = "destinationBankAccount")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sourceBankAccount", "destinationBankAccount" }, allowSetters = true)
    private Set<Transaction> outgoingTransactions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BankAccount id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public BankAccount firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public BankAccount lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public BankAccount email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public BankAccount phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public BankAccount address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

    public BankAccount additionalInfo(String additionalInfo) {
        this.setAdditionalInfo(additionalInfo);
        return this;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Balance getBalance() {
        return this.balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public BankAccount balance(Balance balance) {
        this.setBalance(balance);
        return this;
    }

    public Set<Transaction> getIncomingTransactions() {
        return this.incomingTransactions;
    }

    public void setIncomingTransactions(Set<Transaction> transactions) {
        if (this.incomingTransactions != null) {
            this.incomingTransactions.forEach(i -> i.setSourceBankAccount(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setSourceBankAccount(this));
        }
        this.incomingTransactions = transactions;
    }

    public BankAccount incomingTransactions(Set<Transaction> transactions) {
        this.setIncomingTransactions(transactions);
        return this;
    }

    public BankAccount addIncomingTransactions(Transaction transaction) {
        this.incomingTransactions.add(transaction);
        transaction.setSourceBankAccount(this);
        return this;
    }

    public BankAccount removeIncomingTransactions(Transaction transaction) {
        this.incomingTransactions.remove(transaction);
        transaction.setSourceBankAccount(null);
        return this;
    }

    public Set<Transaction> getOutgoingTransactions() {
        return this.outgoingTransactions;
    }

    public void setOutgoingTransactions(Set<Transaction> transactions) {
        if (this.outgoingTransactions != null) {
            this.outgoingTransactions.forEach(i -> i.setDestinationBankAccount(null));
        }
        if (transactions != null) {
            transactions.forEach(i -> i.setDestinationBankAccount(this));
        }
        this.outgoingTransactions = transactions;
    }

    public BankAccount outgoingTransactions(Set<Transaction> transactions) {
        this.setOutgoingTransactions(transactions);
        return this;
    }

    public BankAccount addOutgoingTransactions(Transaction transaction) {
        this.outgoingTransactions.add(transaction);
        transaction.setDestinationBankAccount(this);
        return this;
    }

    public BankAccount removeOutgoingTransactions(Transaction transaction) {
        this.outgoingTransactions.remove(transaction);
        transaction.setDestinationBankAccount(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankAccount)) {
            return false;
        }
        return id != null && id.equals(((BankAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankAccount{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", additionalInfo='" + getAdditionalInfo() + "'" +
            "}";
    }
}
